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

	public Multipart createMessage(String fileName) throws MessagingException, IOException {
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

	@Test
	public void extractMessage() throws MessagingException, IOException, ParseException {
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

}
