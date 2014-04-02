package br.com.alepmendonca.mail.bradesco;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.Multipart;

import br.com.alepmendonca.creditcardusage.dao.CreditCardDao;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.dao.StoreDao;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.CreditCard;
import br.com.alepmendonca.creditcardusage.model.Store;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class BradescoTransactionMailReader {

	private BradescoMailExtractStrategy strategy = null;

	protected boolean canExtract(Multipart message) throws IOException, MessagingException {
		return message.getBodyPart(0).getContent().toString().contains("Compras");
	}

	public BradescoTransactionMailReader(BradescoMailExtractStrategy str) {
		strategy = str;
	}

	public CardReceipt extractMessage(Multipart message, InputStream pdf) throws IOException, ParseException, MessagingException {
		if (canExtract(message)) {
			PdfReader reader = new PdfReader(pdf, strategy.getPdfPassword().getBytes());
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy pdfStrategy = parser.processContent(1, new SimpleTextExtractionStrategy());
			CardReceipt receipt = createCardReceipt(pdfStrategy.getResultantText());
			System.out.println(receipt);
			return receipt;
		} else {
			throw new UnsupportedOperationException("Bradesco Mail not about card receipt");
		}
	}

	private CardReceipt createCardReceipt(String receipt) throws ParseException {
		StringTokenizer st = new StringTokenizer(receipt, "\n\r\f");
		Calendar data = null, hora = null;
		final HashMap<String, Object> receiptItems = new HashMap<String, Object>();
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			if (line.contains("XXXX")) {
				receiptItems.put("finalCartao", Integer.valueOf(line.substring(line.lastIndexOf('.')+1)));
			} else if (line.startsWith("ESTABELECIMENTO")) {
				receiptItems.put("estabelecimento", line.substring(18));
			} else if (line.startsWith("VALOR")) {
				receiptItems.put("moeda", NumberFormat.getCurrencyInstance().getCurrency()); // #TODO descobrir moeda a partir de string
				receiptItems.put("valor", new BigDecimal(NumberFormat.getCurrencyInstance().parse(line.substring(8)).floatValue()));
			} else if (line.contains("DATA")) {
				data = Calendar.getInstance();
				data.setTime(DateFormat.getDateInstance().parse(line.substring(7)));
			} else if (line.contains("HORA")) {
				hora = Calendar.getInstance();
				hora.setTime(DateFormat.getTimeInstance().parse(line.substring(7)));
			} else if (line.contains("AUTENTICA")) {
				receiptItems.put("autenticacao", Long.valueOf(line.substring(18)));
			}
		}
		
		final Calendar dataHoraAutorizacao;
		final Integer finalCartao = (Integer) receiptItems.get("finalCartao");
		final String estabelecimento = (String) receiptItems.get("estabelecimento");
		final Currency moeda = (Currency) receiptItems.get("moeda");
		final BigDecimal valor = (BigDecimal) receiptItems.get("valor");
		final long autenticacao = (Long) receiptItems.get("autenticacao");
		if (data != null && hora != null) {
			dataHoraAutorizacao = data;
			dataHoraAutorizacao.set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY));
			dataHoraAutorizacao.set(Calendar.MINUTE, hora.get(Calendar.MINUTE));
			dataHoraAutorizacao.set(Calendar.SECOND, hora.get(Calendar.SECOND));
			dataHoraAutorizacao.set(Calendar.MILLISECOND, hora.get(Calendar.MILLISECOND));
		} else {
			dataHoraAutorizacao = null;
		}

		final DaoSession daoSession = strategy.dao;
		daoSession.runInTx(new Runnable() {

			public void run() {
				CreditCardDao ccDao = daoSession.getCreditCardDao();
				CreditCard cc = ccDao.queryBuilder().where(CreditCardDao.Properties.FinalNumbers.eq(finalCartao)).unique();
				if (cc == null) {
					cc = new CreditCard(null, finalCartao, null, null);
					ccDao.insert(cc);
				}
				StoreDao sDao = daoSession.getStoreDao();
				Store s = sDao.queryBuilder().where(StoreDao.Properties.OriginalName.eq(estabelecimento)).unique();
				if (s == null) {
					s = new Store(null, estabelecimento, null, null);
					sDao.insert(s);
				}
				CardReceipt cr = new CardReceipt(cc, s, valor, moeda, dataHoraAutorizacao, autenticacao);
				Long id = daoSession.getCardReceiptDao().insert(cr);
				receiptItems.put("id", id);
			}
			
		});
		return daoSession.getCardReceiptDao().load((Long) receiptItems.get("id")) ;
	}
}
