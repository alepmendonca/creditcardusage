package br.com.alepmendonca.services;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.google.android.gm.contentprovider.GmailContract;

public class StartupGmailLabelObserverBroadcastReceiver extends BroadcastReceiver {

    private static final String ACCOUNT_TYPE_GOOGLE = "com.google";
    private static final String[] FEATURES_MAIL = {"service_mail"};
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
                            Log.e(BradescoMailProperties.TAG, "Got OperationCanceledException", oce);
                        } catch (IOException ioe) {
                            Log.e(BradescoMailProperties.TAG, "Got OperationCanceledException", ioe);
                        } catch (AuthenticatorException ae) {
                            Log.e(BradescoMailProperties.TAG, "Got OperationCanceledException", ae);
                        }
                        onAccountResults(accounts, context);
                    }
                }, null /* handler */);
	}
	
	private void onAccountResults(Account[] accounts, Context context) {
        if (accounts != null && accounts.length > 0) { //TODO pegando a primeira conta do gmail disponivel.. deveria perguntar pela conta no preferences
            final String account = accounts[0].name;
            BradescoMailProperties.setGmailAccount(account, context);
            final int labelId = BradescoMailProperties.getGmailLabel(context);
            Uri labelUri = GmailContract.Labels.getLabelUri(account, labelId);
            context.getContentResolver().registerContentObserver(labelUri, false, new GmailLabelObserver(account, context, new Handler()));
        } else {
        	Log.wtf(BradescoMailProperties.TAG, "Nao encontrei conta de GMail. Nada sera feito...");
        }
    }
}
