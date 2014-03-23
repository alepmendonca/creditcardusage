package br.com.alepmendonca.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAccountManager;
import org.robolectric.shadows.ShadowApplication;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gm.contentprovider.GmailContract;
import com.honorables.beckfowler.CreditCardUsageApplication;

@RunWith(RobolectricTestRunner.class)
public class StartupGmailLabelObserverBroadcastReceiverTest {

	private ShadowApplication app;
	private Context c;
	private final String accountName = "test@gmail.com";
	private final Uri defaultUri = GmailContract.Labels.getLabelUri(accountName, GmailContract.Labels.getInboxLabelId());

	@Before
	public void setup() {
		app = Robolectric.getShadowApplication();
		c = app.getApplicationContext();		
		Robolectric.shadowOf(app.getContentResolver()).clearContentObservers();
	}

	@After
	public void tearDown() {
		StartupGmailLabelObserverBroadcastReceiver r = (StartupGmailLabelObserverBroadcastReceiver) app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		r.setGmailContract(new GmailContract());
	}

	@Test
	public void verifyBroadcastReceiverRegisteredAfterBootCompleted() {
		assertTrue(app.hasReceiverForIntent(new Intent(Intent.ACTION_BOOT_COMPLETED)));
		assertEquals(1, app.getRegisteredReceivers().size());
		BroadcastReceiver r = app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		assertEquals(StartupGmailLabelObserverBroadcastReceiver.class, r.getClass());
	}

	@Test
	public void onReceive_failWhenGmailNotInstalled() throws Exception {
		BroadcastReceiver r = app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		r.onReceive(c, null);
		assertNull(Robolectric.shadowOf(app.getContentResolver()).getContentObserver(defaultUri));
	}

	private void makeReadingGmailLabelsAvailable() {
		GmailContract gc = Mockito.mock(GmailContract.class);
		Mockito.when(gc.canReadLabels(Mockito.any(Context.class))).thenReturn(true);
		StartupGmailLabelObserverBroadcastReceiver r = (StartupGmailLabelObserverBroadcastReceiver) app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		r.setGmailContract(gc);	
	}

	@Test
	public void onReceive_failWhenThereIsNoGmailAccountOnDevice() {
		BroadcastReceiver r = app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		makeReadingGmailLabelsAvailable();
		r.onReceive(c, null);
		assertNull(Robolectric.shadowOf(app.getContentResolver()).getContentObserver(defaultUri));
	}
	
	@Test
	public void onReceive_verifyContentObserverRegisteredWithInboxLabelWhenLabelNotDefined() {
		BroadcastReceiver r = app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		makeReadingGmailLabelsAvailable();
		Account account = new Account(accountName, "com.google");
		Robolectric.shadowOf(ShadowAccountManager.get(c)).addAccount(account);

		r.onReceive(c, null);
		assertNotNull(Robolectric.shadowOf(app.getContentResolver()).getContentObserver(defaultUri));
	}
	
	@Test
	public void onReceive_verifyContentObserverRegisteredWithSpecifiedLabelWhenLabelNotDefined() {
		BroadcastReceiver r = app.getRegisteredReceivers().get(0).getBroadcastReceiver();
		makeReadingGmailLabelsAvailable();
		Account account = new Account(accountName, "com.google");
		Robolectric.shadowOf(ShadowAccountManager.get(c)).addAccount(account);
		((CreditCardUsageApplication) Robolectric.application).getProperties().setGmailLabelId(134);
		
		r.onReceive(c, null);
		Uri specifiedUri = GmailContract.Labels.getLabelUri(accountName, 134);
		assertNotNull(Robolectric.shadowOf(app.getContentResolver()).getContentObserver(specifiedUri));
	}

}
