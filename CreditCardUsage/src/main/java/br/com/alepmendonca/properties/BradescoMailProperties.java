package br.com.alepmendonca.properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gm.contentprovider.GmailContract;

public class BradescoMailProperties {

	public static final String TAG = "CreditCardUsage";
	private final Context context;

	public BradescoMailProperties(Context applicationContext) {
		context = applicationContext;
	}

	public InternetAddress getBradescoMailSender() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		try {
			return new InternetAddress(preferences.getString("bradesco_mail_sender", "infoemail@infobradesco.com.br"));
		} catch (AddressException e) {
			Log.wtf(TAG, e);
			try {
				return new InternetAddress("infoemail@infobradesco.com.br");
			} catch (AddressException e1) {
				return null;
			}
		}
	}

	public int getMinTimeBetweenMailChecks() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt("gmail_min_time_between_checks_seconds", 10);
	}

	public int getGmailLabelId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt("gmail_label_id", GmailContract.Labels.getInboxLabelId());
	}

	public void setGmailLabelId(int labelId) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gmail_label_id", labelId).commit();
	}

	public String getGmailLabel() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString("gmail_label_canonical_name", GmailContract.Labels.LabelCanonicalNames.CANONICAL_NAME_INBOX);
	}

	public void setGmailLabel(String label) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gmail_label_canonical_name", label).commit();
	}

	public void setGmailAccount(String account) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gmail_account", account).commit();
	}

	public String getMailPassword() {
		// TODO Usar XOAuth2 ao inv�s de senha
		return "inzilxgmryojkhty";
	}

	public String getBradescoPdfPassword() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString("bradesco_pdf_password", "");
	}

	public void setBradescoPdfPassword(String pass) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("bradesco_pdf_password", pass).commit();
	}

	public String getIMAPServer() {
		return "imap.gmail.com";
	}

}
