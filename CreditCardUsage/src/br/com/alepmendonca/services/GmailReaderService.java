package br.com.alepmendonca.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import br.com.alepmendonca.mail.bradesco.BradescoMailExtractStrategy;
import br.com.alepmendonca.notification.CreditCardUsageNotificationFactory;
import br.com.alepmendonca.properties.BradescoMailProperties;

public class GmailReaderService extends IntentService {

	public static final String GMAIL_ACCOUNT = "GMAIL_ACCOUNT";

	private String gmailUser = null;
	private static final String gmailPassword = BradescoMailProperties.getMailPassword();

	public GmailReaderService() {
		super("GmailReaderService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		gmailUser = intent.getStringExtra(GMAIL_ACCOUNT);
		readLabelMail();
	}
	
	public void readLabelMail() {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
        	Session session = Session.getDefaultInstance(props, null);
        	Store store = session.getStore("imaps");
        	store.connect(BradescoMailProperties.getIMAPServer(), gmailUser, gmailPassword);
        	System.out.println(store);

        	BradescoMailExtractStrategy strategy = new BradescoMailExtractStrategy(store, this);
        	List<Object> messages = strategy.extractMessages();
        	CreditCardUsageNotificationFactory.createNotifications(messages, this);
        } catch (NoSuchProviderException e) {
        	e.printStackTrace();
        	System.exit(1);
        } catch (MessagingException e) {
        	e.printStackTrace();
        	System.exit(2);
        } catch (ParseException e) {
			e.printStackTrace();
			System.exit(3);
        } catch (IOException e) {
        	e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
