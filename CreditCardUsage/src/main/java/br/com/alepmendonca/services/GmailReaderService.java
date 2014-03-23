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
import android.util.Log;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.mail.bradesco.BradescoMailExtractStrategy;
import br.com.alepmendonca.notification.CreditCardUsageNotificationFactory;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.honorables.beckfowler.CreditCardUsageApplication;

public class GmailReaderService extends IntentService {

	public static final String GMAIL_ACCOUNT = "GMAIL_ACCOUNT";

	private String gmailUser;
	private String gmailPassword;
	private Store store;
	private BradescoMailExtractStrategy strategy;
	private CreditCardUsageNotificationFactory factory;

	public GmailReaderService() {
		super("GmailReaderService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		gmailUser = intent.getStringExtra(GMAIL_ACCOUNT);
		readLabelMail();
	}
	
	void setStore(Store s) {
		store = s;
	}

	private Store getStore() {
		if (store == null) {
	        Properties props = System.getProperties();
	        props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			try {
				store = session.getStore("imaps");
			} catch (NoSuchProviderException e) {
				Log.e(BradescoMailProperties.TAG, "IMAP store not found on system", e);
				System.exit(1);
			}
		}
		return store;
	}

	void setStrategy(BradescoMailExtractStrategy s) {
		strategy = s;
	}

	private BradescoMailExtractStrategy getStrategy(BradescoMailProperties properties, DaoSession dao) throws IOException, MessagingException {
		if (strategy == null) {
			strategy = new BradescoMailExtractStrategy(getStore(), properties, dao);
		}
		return strategy;
	}

	void setFactory(CreditCardUsageNotificationFactory f) {
		factory = f;
	}

	private CreditCardUsageNotificationFactory getFactory() {
		if (factory == null) {
			factory = new CreditCardUsageNotificationFactory();
		}
		return factory;
	}

	public void readLabelMail() {
        try {
        	CreditCardUsageApplication app = (CreditCardUsageApplication) this.getApplication();
        	BradescoMailProperties p = app.getProperties();
        	gmailPassword = p.getMailPassword();
        	getStore().connect(p.getIMAPServer(), gmailUser, gmailPassword);
        	BradescoMailExtractStrategy strategy = getStrategy(p, app.getDAO());
        	List<Object> messages = strategy.extractMessages();
        	getFactory().createNotifications(messages, this);
        } catch (MessagingException e) {
        	Log.e(BradescoMailProperties.TAG, "Problems getting messages", e);
        	System.exit(2);
        } catch (ParseException e) {
			Log.e(BradescoMailProperties.TAG, "Problems parsing PDF", e);
			System.exit(3);
        } catch (IOException e) {
        	Log.e(BradescoMailProperties.TAG, "Problems with connection and/or files", e);
        	System.exit(4);
		} finally {
			try {
				getStore().close();
			} catch (MessagingException e) {
				Log.e(BradescoMailProperties.TAG, "Couldnt close IMAP connection well", e);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
