package br.com.alepmendonca.mail.bradesco;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.google.android.gm.contentprovider.GmailContract;
import com.honorables.beckfowler.CreditCardUsageApplication;
import com.itextpdf.text.exceptions.BadPasswordException;

@RunWith(RobolectricTestRunner.class)
public class BradescoMailExtractStrategyTest {

	BradescoMailExtractStrategy str;
	Store store;
	
	private abstract class MockStore extends Store {
		protected MockStore(Session session, URLName urlname) {
			super(session, urlname);
		}
	}
	private abstract class MockFolder extends Folder {
		protected MockFolder(Store store) {
			super(store);
		}		
	}
	
	@Before
	public void setUp() throws Exception {
		DaoSession dao = ((CreditCardUsageApplication) Robolectric.application).getDAO();
		BradescoMailProperties p = ((CreditCardUsageApplication) Robolectric.application).getProperties();
		p.setBradescoPdfPassword("didamendes");
		store = mock(MockStore.class);
		str = new BradescoMailExtractStrategy(store, p, dao);
	}

	public Message createMessage(String fileName) throws MessagingException, IOException {
		InputStream mailFileInputStream = new FileInputStream(fileName);
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		return new MimeMessage(session, mailFileInputStream);
	}

	@Test
	public void extractMessagesDefaultInboxLabel() throws IOException, MessagingException, ParseException {
		Folder mockFolder = mock(MockFolder.class);
		when(store.getFolder(anyString())).thenReturn(mockFolder);
		Message[] msgs = new Message[1];
		msgs[0] = createMessage("src/test/resources/mailTransaction1.msg");
		when(mockFolder.search(any(SearchTerm.class))).thenReturn(msgs);

		List<Object> cardReceipts = str.extractMessages();

		verify(mockFolder).open(eq(Folder.READ_ONLY));
		verify(store).getFolder(GmailContract.Labels.LabelCanonicalNames.CANONICAL_NAME_INBOX);
		assertEquals(1, cardReceipts.size());
		assertEquals(CardReceipt.class, cardReceipts.get(0).getClass());
		CardReceipt c = (CardReceipt) cardReceipts.get(0);
		assertEquals(65.76d, c.getValue(), 0.01);
		assertEquals(Currency.getInstance("BRL"), c.getMoeda());
		assertEquals(6372, c.getCreditCard().getFinalNumbers());
		assertEquals("KTS COMERCIO             SAO PAULO", c.getStore().getOriginalName());
		assertEquals(new Date(2014 - 1900, 2, 21, 20, 27, 49), c.getAuthorizationDate());
		assertEquals(674965L, c.getTransaction());
	}

	@Test
	public void extractMessagesSpecifiedLabel() throws IOException, MessagingException, ParseException {
		Folder mockFolder = mock(MockFolder.class);
		when(store.getFolder(anyString())).thenReturn(mockFolder);
		Message[] msgs = new Message[1];
		msgs[0] = createMessage("src/test/resources/mailTransaction1.msg");
		when(mockFolder.search(any(SearchTerm.class))).thenReturn(msgs);
		str.properties.setGmailLabel("Bradesco");

		List<Object> cardReceipts = str.extractMessages();

		verify(mockFolder).open(eq(Folder.READ_ONLY));
		verify(store).getFolder("Bradesco");
		assertEquals(1, cardReceipts.size());
		assertEquals(CardReceipt.class, cardReceipts.get(0).getClass());
	}

	@Test(expected=BadPasswordException.class)
	public void extractMessagesDefaultPdfPassword() throws IOException, MessagingException, ParseException {
		Folder mockFolder = mock(MockFolder.class);
		when(store.getFolder(anyString())).thenReturn(mockFolder);
		Message[] msgs = new Message[1];
		msgs[0] = createMessage("src/test/resources/mailTransaction1.msg");
		when(mockFolder.search(any(SearchTerm.class))).thenReturn(msgs);
		str.properties.setBradescoPdfPassword(null);

		assertEquals("", str.getPdfPassword());
		List<Object> cardReceipts = str.extractMessages();
	}

	@Test(expected=BadPasswordException.class)
	public void extractMessagesWrongPdfPassword() throws IOException, MessagingException, ParseException {
		Folder mockFolder = mock(MockFolder.class);
		when(store.getFolder(anyString())).thenReturn(mockFolder);
		Message[] msgs = new Message[1];
		msgs[0] = createMessage("src/test/resources/mailTransaction1.msg");
		when(mockFolder.search(any(SearchTerm.class))).thenReturn(msgs);
		str.properties.setBradescoPdfPassword("abc");

		List<Object> cardReceipts = str.extractMessages();
	}

}
