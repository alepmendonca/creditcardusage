package br.com.alepmendonca.services;

import java.io.IOException;

import com.google.android.gm.contentprovider.GmailContract;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class StartupGmailLabelObserverBroadcastReceiver extends BroadcastReceiver {

    private static final String ACCOUNT_TYPE_GOOGLE = "com.google";
    private static final String[] FEATURES_MAIL = {"service_mail"};
    static final String TAG = "KentBeck";
    ContentObserver labelObserver = null;

	@Override
	public void onReceive(final Context context, Intent intent) {
		if (!GmailContract.canReadLabels(context)) return;
		
        AccountManager.get(context).getAccountsByTypeAndFeatures(ACCOUNT_TYPE_GOOGLE, FEATURES_MAIL,
                new AccountManagerCallback<Account[]>() {
                    @Override
                    public void run(AccountManagerFuture<Account[]> future) {
                        Account[] accounts = null;
                        try {
                            accounts = future.getResult();
                        } catch (OperationCanceledException oce) {
                            Log.e(TAG, "Got OperationCanceledException", oce);
                        } catch (IOException ioe) {
                            Log.e(TAG, "Got OperationCanceledException", ioe);
                        } catch (AuthenticatorException ae) {
                            Log.e(TAG, "Got OperationCanceledException", ae);
                        }
                        onAccountResults(accounts, context);
                    }
                }, null /* handler */);
	}
	
	private void onAccountResults(Account[] accounts, Context context) {
        if (accounts != null && accounts.length > 0) {
            final String account = accounts[0].name;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("gmail_account", account);
            editor.commit();
            Uri labelUri = GmailContract.Labels.getLabelUri(account, 3);            
            context.getContentResolver().registerContentObserver(labelUri, false, new GmailLabelObserver(account, context, null));
        }
    }
}
