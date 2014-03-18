package br.com.alepmendonca.services.test;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;
import android.util.Log;
import br.com.alepmendonca.services.StartupGmailLabelObserverBroadcastReceiver;

import com.google.android.gm.contentprovider.GmailContract;

public class StartupGmailLabelObserverBroadcastReceiverTest extends AndroidTestCase {

	Context mockContext;
	PackageManager mockPackageManager;

	protected void setUp() throws Exception {
		super.setUp();
		mockContext = getContext();
	}

	public void testOnReceiveContextIntent() throws Exception {
		GmailContract mockContract = mock(GmailContract.class);
		when(mockContract.canReadLabels(mockContext)).thenReturn(true);

		Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mockContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		try {
		    pendingIntent.send(0);
		} catch (CanceledException e) {
		    Log.e(StartupGmailLabelObserverBroadcastReceiverTest.class.getSimpleName(), Log.getStackTraceString(e));
		}
		Thread.sleep(2000);

	}

	public void testOnDeviceWithoutGmailApp() {
		GmailContract mockContract = mock(GmailContract.class);
		when(mockContract.canReadLabels(mockContext)).thenReturn(false);
		Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
		//gmailObserver.onReceive(mockContext, intent);

	}
}
