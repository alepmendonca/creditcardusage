package br.com.alepmendonca.services;

import java.util.Calendar;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class GmailLabelObserver extends ContentObserver {

	//TODO Mover parametro para um local central de properties
	private static final int TIME_INTERVAL_BETWEEN_CHECKS_SECONDS = 10;
	private Calendar lastCheck = Calendar.getInstance();
	private String accountName = null;
	private Context context;

	public GmailLabelObserver(String account, Context aContext, Handler handler) {
		this(null);
		accountName = account;
		context = aContext;
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
		return Calendar.getInstance().getTimeInMillis() - lastCheck.getTimeInMillis() > 
			TIME_INTERVAL_BETWEEN_CHECKS_SECONDS * 1000;
	}

	@Override
	public void onChange(boolean selfChange) {
		this.onChange(selfChange, null);
	}

}
