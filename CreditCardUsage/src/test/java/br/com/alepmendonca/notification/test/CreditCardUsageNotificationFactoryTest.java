package br.com.alepmendonca.notification.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.shadowOf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.Multipart;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.StoreType;
import br.com.alepmendonca.mail.bradesco.BradescoMailExtractStrategy;
import br.com.alepmendonca.mail.bradesco.BradescoTransactionMailReader;
import br.com.alepmendonca.mail.bradesco.BradescoTransactionMailReaderTest;
import br.com.alepmendonca.notification.CreditCardUsageNotificationFactory;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.honorables.beckfowler.CreditCardUsageApplication;
import com.honorables.beckfowler.R;

@RunWith(RobolectricTestRunner.class)
public class CreditCardUsageNotificationFactoryTest {

	NotificationManager notificationManager;
	CreditCardUsageNotificationFactory factory;
	BradescoTransactionMailReader reader;
	DaoSession dao;
	CardReceipt c;

	@Before
	public void startUp() throws IOException, MessagingException, ParseException {
		BradescoMailProperties p = ((CreditCardUsageApplication) Robolectric.application).getProperties();
		dao = ((CreditCardUsageApplication) Robolectric.application).getDAO();
		p.setBradescoPdfPassword("didamendes");
		reader = new BradescoTransactionMailReader(new BradescoMailExtractStrategy(null, p, dao));
		Multipart message = BradescoTransactionMailReaderTest.createMessage("src/test/resources/mailTransaction1.msg");
		InputStream pdf = new FileInputStream("src/test/resources/transaction1.pdf");
		c = reader.extractMessage(message, pdf);
		
		notificationManager = (NotificationManager) Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);
		factory = new CreditCardUsageNotificationFactory();
	}

	@Test
	public void createOneNotificationForCompleteCardReceipt() throws MessagingException, IOException, ParseException {
		c.getCreditCard().setIssuer("VISA");
		c.getCreditCard().setOwner("Test user");
		c.getCreditCard().update();
		c.getStore().setUserDefinedName("Lojinha lala");
		StoreType st = new StoreType(null, "Alimentacao", null);
		dao.getStoreTypeDao().insert(st);
		c.getStore().setStoreType(st);
		c.getStore().update();
		
		ArrayList<Object> receipts = new ArrayList<Object>();
		receipts.add(c);
		factory.createNotifications(receipts, Robolectric.application);
		
		assertEquals(1, shadowOf(notificationManager).size());
		Notification n = shadowOf(notificationManager).getNotification(null, c.getCreditCard().getFinalNumbers());
		assertNotNull(n);
		assertEquals("Alimentacao em Lojinha lala - R$ 65,76", shadowOf(n).getContentText());
		assertEquals("Nova compra", shadowOf(n).getContentTitle());
		assertEquals(R.drawable.ic_card_receipt, shadowOf(n).getSmallIcon());
	}

	@Test
	public void createOneNotificationForCardReceiptWithoutStoreDefined() throws MessagingException, IOException, ParseException {
		c.getCreditCard().setIssuer("VISA");
		c.getCreditCard().setOwner("Test user");
		c.getCreditCard().update();
		
		ArrayList<Object> receipts = new ArrayList<Object>();
		receipts.add(c);
		factory.createNotifications(receipts, Robolectric.application);
		
		assertEquals(1, shadowOf(notificationManager).size());
		Notification n = shadowOf(notificationManager).getNotification(null, c.getCreditCard().getFinalNumbers());
		assertNotNull(n);
		assertEquals("KTS COMERCIO             SAO PAULO - R$ 65,76", shadowOf(n).getContentText());
		assertEquals("Nova compra", shadowOf(n).getContentTitle());
		assertEquals(R.drawable.ic_card_receipt, shadowOf(n).getSmallIcon());
	}

}
