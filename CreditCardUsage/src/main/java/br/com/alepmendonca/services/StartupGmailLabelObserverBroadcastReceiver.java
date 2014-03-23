package br.com.alepmendonca.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import br.com.alepmendonca.properties.BradescoMailProperties;

import com.google.android.gm.contentprovider.GmailContract;
import com.honorables.beckfowler.CreditCardUsageApplication;

public class StartupGmailLabelObserverBroadcastReceiver extends BroadcastReceiver {

    private static final String ACCOUNT_TYPE_GOOGLE = "com.google";
    private static final String[] FEATURES_MAIL = {"service_mail"};
    ContentObserver labelObserver = null;
    GmailContract gc = new GmailContract();


	@Override
	public void onReceive(final Context context, Intent intent) {
		if (!gc.canReadLabels(context)) return;
	
		Account[] accounts = AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE_GOOGLE);
		onAccountResults(accounts, context);
		//TODO verificar impacto de não solicitar features
//        AccountManager.get(context).getAccountsByTypeAndFeatures(ACCOUNT_TYPE_GOOGLE, FEATURES_MAIL,
//                new AccountManagerCallback<Account[]>() {
//
//                    public void run(AccountManagerFuture<Account[]> future) {
//                        Account[] accounts = null;
//                        try {
//                            accounts = future.getResult();
//                        } catch (OperationCanceledException oce) {
//                            Log.e(BradescoMailProperties.TAG, "Problems getting Google account", oce);
//                        } catch (IOException ioe) {
//                            Log.e(BradescoMailProperties.TAG, "Problems getting Google account", ioe);
//                        } catch (AuthenticatorException ae) {
//                            Log.e(BradescoMailProperties.TAG, "Problems getting Google account", ae);
//                        }
//                        onAccountResults(accounts, context);
//                    }
//                }, null /* handler */);
//                
	}
	
	private void onAccountResults(Account[] accounts, Context context) {
        if (accounts != null && accounts.length > 0) { //TODO pegando a primeira conta do gmail disponivel.. deveria perguntar pela conta no preferences
            final String account = accounts[0].name;
            BradescoMailProperties p = ((CreditCardUsageApplication) context).getProperties();
            p.setGmailAccount(account);
            final int labelId = p.getGmailLabelId();
            Uri labelUri = GmailContract.Labels.getLabelUri(account, labelId);
            context.getContentResolver().registerContentObserver(labelUri, false, new GmailLabelObserver(account, p.getMinTimeBetweenMailChecks(), (CreditCardUsageApplication) context, new Handler()));
        } else {
        	Log.wtf(BradescoMailProperties.TAG, "Nao encontrei conta de GMail. Nada sera feito...");
        }
    }

    void setGmailContract(GmailContract c) {
    	gc = c;
    }
}
