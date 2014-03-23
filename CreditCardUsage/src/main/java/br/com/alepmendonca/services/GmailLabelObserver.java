package br.com.alepmendonca.services;

import java.util.Calendar;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.honorables.beckfowler.CreditCardUsageApplication;

public class GmailLabelObserver extends ContentObserver {

	private Calendar lastCheck = null;
	private String accountName = null;
	private int timeoutSeconds = 0;
	private CreditCardUsageApplication context;

	public GmailLabelObserver(String account, int timeout, CreditCardUsageApplication aContext, Handler handler) {
		this(handler);
		accountName = account;
		context = aContext;
		timeoutSeconds = timeout;
	}

	public GmailLabelObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		if (! selfChange && hasPassedEnoughTimeSinceLastMailCheck()) {
			lastCheck = Calendar.getInstance();
			Intent anIntent = new Intent(context, GmailReaderService.class);
			anIntent.putExtra(GmailReaderService.GMAIL_ACCOUNT, accountName);
			context.startService(anIntent);
		}
	}

	private boolean hasPassedEnoughTimeSinceLastMailCheck() {
		return lastCheck == null || 
				(Calendar.getInstance().getTimeInMillis() - lastCheck.getTimeInMillis() > timeoutSeconds * 1000);
	}

	@Override
	public void onChange(boolean selfChange) {
		this.onChange(selfChange, null);
	}

}
