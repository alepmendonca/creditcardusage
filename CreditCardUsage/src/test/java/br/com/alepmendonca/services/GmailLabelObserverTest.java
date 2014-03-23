package br.com.alepmendonca.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import android.content.Intent;

import com.honorables.beckfowler.CreditCardUsageApplication;

@RunWith(RobolectricTestRunner.class)
public class GmailLabelObserverTest {

	GmailLabelObserver obs;
	CreditCardUsageApplication application;
	int minTimeCheckSeconds = 2;

	@Before
	public void setUp() throws Exception {
		application = mock(CreditCardUsageApplication.class);
		obs = new GmailLabelObserver("test@gmail.com", minTimeCheckSeconds, application, null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void onChange_selfChangeDoesntCallStartGmailService() {
		obs.onChange(true);
		verifyZeroInteractions(application);
	}

	@Test
	public void onChange_callOnceStartGmailService() {
		obs.onChange(false);
		ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
		verify(application, times(1)).startService(intentCaptor.capture());
		Intent i = intentCaptor.getValue();
		assertEquals(i.getStringExtra(GmailReaderService.GMAIL_ACCOUNT), "test@gmail.com");
		assertEquals(i.getComponent().getClassName(), GmailReaderService.class.getName());
	}

	@Test
	public void onChange_callSequenciallyTwiceStartGmailServiceOnce() {
		obs.onChange(false);
		obs.onChange(false);
		ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
		verify(application, times(1)).startService(intentCaptor.capture());
		Intent i = intentCaptor.getValue();
		assertEquals(i.getStringExtra(GmailReaderService.GMAIL_ACCOUNT), "test@gmail.com");
		assertEquals(i.getComponent().getClassName(), GmailReaderService.class.getName());
	}

	@Test
	public void onChange_callTwiceBeforeParameteredTimeStartGmailServiceOnce() throws InterruptedException {
		obs.onChange(false);
		Thread.sleep((minTimeCheckSeconds - 1) * 1000);
		obs.onChange(false);
		ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
		verify(application, times(1)).startService(intentCaptor.capture());
		Intent i = intentCaptor.getValue();
		assertEquals(i.getStringExtra(GmailReaderService.GMAIL_ACCOUNT), "test@gmail.com");
		assertEquals(i.getComponent().getClassName(), GmailReaderService.class.getName());
	}

	@Test
	public void onChange_callTwiceAfterParameteredTimeStartGmailServiceTwice() throws InterruptedException {
		obs.onChange(false);
		Thread.sleep((minTimeCheckSeconds + 1) * 1000);
		obs.onChange(false);
		ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
		verify(application, times(2)).startService(intentCaptor.capture());
		List<Intent> intents = intentCaptor.getAllValues();
		assertEquals(intents.get(0).getStringExtra(GmailReaderService.GMAIL_ACCOUNT), "test@gmail.com");
		assertEquals(intents.get(0).getComponent().getClassName(), GmailReaderService.class.getName());
		assertEquals(intents.get(1).getStringExtra(GmailReaderService.GMAIL_ACCOUNT), "test@gmail.com");
		assertEquals(intents.get(1).getComponent().getClassName(), GmailReaderService.class.getName());
	}

}
