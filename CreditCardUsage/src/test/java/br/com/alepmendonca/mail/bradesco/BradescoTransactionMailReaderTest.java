package br.com.alepmendonca.mail.bradesco;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.CreditCard;
import br.com.alepmendonca.creditcardusage.model.Store;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.honorables.beckfowler.CreditCardUsageApplication;

@RunWith(RobolectricTestRunner.class)
public class BradescoTransactionMailReaderTest {

	Message mail;
	BradescoTransactionMailReader reader;

	@Before
	public void setUp() throws Exception {
		BradescoMailProperties p = ((CreditCardUsageApplication) Robolectric.application).getProperties();
		DaoSession d = ((CreditCardUsageApplication) Robolectric.application).getDAO();

		p.setBradescoPdfPassword("didamendes");
		BradescoMailExtractStrategy str = new BradescoMailExtractStrategy(null, p, d);
		reader = new BradescoTransactionMailReader(str);
	}

	public static Multipart createMessage(String fileName) throws MessagingException, IOException {
		InputStream mailFileInputStream = new FileInputStream(fileName);
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		return (Multipart) new MimeMessage(session, mailFileInputStream).getContent();
	}

	@Test
	public void canExtract_verifyTransactionMail() throws MessagingException, IOException {
		Multipart message = createMessage("src/test/resources/mailTransaction1.msg");
		assertTrue(reader.canExtract(message));
	}

	@Test
	public void canExtract_verifyBillMail() throws MessagingException, IOException {
		Multipart message = createMessage("src/test/resources/mailBill1.msg");
		assertFalse(reader.canExtract(message));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void extractMessage_tryToExtractBillMail() throws MessagingException, IOException, ParseException {
		Multipart message = createMessage("src/test/resources/mailBill1.msg");
		InputStream pdf = new FileInputStream("src/test/resources/bill1.pdf");
		reader.extractMessage(message, pdf);
	}

	@Test
	public void extractMessageCreateEveryObject() throws MessagingException, IOException, ParseException {
		Multipart message = createMessage("src/test/resources/mailTransaction1.msg");
		InputStream pdf = new FileInputStream("src/test/resources/transaction1.pdf");
		CardReceipt c = reader.extractMessage(message, pdf);
		assertEquals(65.76d, c.getValue(), 0.01);
		assertEquals(Currency.getInstance("BRL"), c.getMoeda());
		assertEquals(6372, c.getCreditCard().getFinalNumbers());
		assertEquals("KTS COMERCIO             SAO PAULO", c.getStore().getOriginalName());
		assertEquals(new Date(2014 - 1900, 2, 21, 20, 27, 49), c.getAuthorizationDate());
		assertEquals(674965L, c.getTransaction());
	}

	@Test
	public void extractMessageDontCreateCreditCard() throws MessagingException, IOException, ParseException {
		DaoSession dao = ((CreditCardUsageApplication) Robolectric.application).getDAO();
		CreditCard cc = new CreditCard(null, 6372, "VISA", "Test User");
		long ccid = dao.getCreditCardDao().insert(cc);

		Multipart message = createMessage("src/test/resources/mailTransaction1.msg");
		InputStream pdf = new FileInputStream("src/test/resources/transaction1.pdf");
		CardReceipt c = reader.extractMessage(message, pdf);
		assertEquals(6372, c.getCreditCard().getFinalNumbers());
		assertEquals(ccid, c.getCreditCardId());
	}

	@Test
	public void extractMessageDontCreateStore() throws MessagingException, IOException, ParseException {
		DaoSession dao = ((CreditCardUsageApplication) Robolectric.application).getDAO();
		Store s = new Store(null, "KTS COMERCIO             SAO PAULO", "Casa de Tolerancia", null);
		long sid = dao.getStoreDao().insert(s);

		Multipart message = createMessage("src/test/resources/mailTransaction1.msg");
		InputStream pdf = new FileInputStream("src/test/resources/transaction1.pdf");
		CardReceipt c = reader.extractMessage(message, pdf);
		assertEquals("KTS COMERCIO             SAO PAULO", c.getStore().getOriginalName());
		assertEquals("Casa de Tolerancia", c.getStore().getUserDefinedName());
		assertEquals(sid, c.getStoreId());
	}

	@Test
	public void extractMessage_2TimesSameMessageCreateOneCardReceipt() throws MessagingException, IOException, ParseException {
		Multipart message = createMessage("src/test/resources/mailTransaction1.msg");
		InputStream pdf = new FileInputStream("src/test/resources/transaction1.pdf");
		CardReceipt c1 = reader.extractMessage(message, pdf);
		
		pdf = new FileInputStream("src/test/resources/transaction1.pdf");
		CardReceipt c2 = reader.extractMessage(message, pdf);
		
		assertEquals(c1, c2);
		assertEquals(65.76d, c2.getValue(), 0.01);
		assertEquals(Currency.getInstance("BRL"), c2.getMoeda());
		assertEquals(6372, c2.getCreditCard().getFinalNumbers());
		assertEquals("KTS COMERCIO             SAO PAULO", c2.getStore().getOriginalName());
		assertEquals(new Date(2014 - 1900, 2, 21, 20, 27, 49), c2.getAuthorizationDate());
		assertEquals(674965L, c2.getTransaction());
	}

}
