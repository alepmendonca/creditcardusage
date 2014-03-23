package br.com.alepmendonca.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.content.Context;
import android.content.Intent;
import br.com.alepmendonca.mail.bradesco.BradescoMailExtractStrategy;
import br.com.alepmendonca.notification.CreditCardUsageNotificationFactory;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.honorables.beckfowler.CreditCardUsageApplication;

@RunWith(RobolectricTestRunner.class)
public class GmailReaderServiceTest {

	GmailReaderService service;
	Context context;
	Intent anIntent;
	Store store;
	CreditCardUsageNotificationFactory factory;
	BradescoMailExtractStrategy strategy;

	private abstract class MockStore extends Store {
		protected MockStore(Session session, URLName urlname) {
			super(session, urlname);
		}
	}

	@Before
	public void setUp() throws Exception {
		store = mock(MockStore.class);
		context = Robolectric.application;
		anIntent = new Intent(context, GmailReaderService.class);
		anIntent.putExtra(GmailReaderService.GMAIL_ACCOUNT, "test@gmail.com");
		factory = mock(CreditCardUsageNotificationFactory.class);
		strategy = mock(BradescoMailExtractStrategy.class);
		service = new GmailReaderService();
		service.setStore(store);
		service.setFactory(factory);
		service.setStrategy(strategy);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void onHandleIntent_happyDayNoMessages() throws MessagingException, IOException, ParseException {
	    BradescoMailProperties properties = ((CreditCardUsageApplication) context).getProperties();
	    when(strategy.extractMessages()).thenReturn(new ArrayList<Object>());

		service.onCreate();
	    service.onHandleIntent(anIntent);

	    verify(store).connect(properties.getIMAPServer(), "test@gmail.com", properties.getMailPassword());
	    verify(factory).createNotifications(eq(new ArrayList<Object>()), eq(service));
	}

	@Test
	public void onHandleIntent_happyDayReadXMessagesCreateXNotifications() throws MessagingException, IOException, ParseException {
	    BradescoMailProperties properties = ((CreditCardUsageApplication) context).getProperties();
	    ArrayList<Object> list = new ArrayList<Object>();
	    list.add(1);
	    list.add(2);
	    when(strategy.extractMessages()).thenReturn(list);

		service.onCreate();
	    service.onHandleIntent(anIntent);

	    verify(store).connect(properties.getIMAPServer(), "test@gmail.com", properties.getMailPassword());
	    verify(factory, times(1)).createNotifications(eq(list), eq(service));
	}

}
