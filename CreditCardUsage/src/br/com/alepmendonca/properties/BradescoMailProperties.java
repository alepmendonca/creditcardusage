package br.com.alepmendonca.properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gm.contentprovider.GmailContract;

public class BradescoMailProperties {

	public static final String TAG = "CreditCardUsage";

	//TODO todas as properties devem ser guardadas em BD
	public static InternetAddress getBradescoMailSender() {
		try {
			return new InternetAddress("infoemail@infobradesco.com.br");
		} catch (AddressException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getGmailLabel(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt("gmail_label", GmailContract.Labels.getInboxLabelId());
	}

	public static void setGmailAccount(String account, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gmail_account", account).commit();
	}

	public static String getMailPassword() {
		// TODO Usar XOAuth2 ao invés de senha
		return "inzilxgmryojkhty";
	}

	public static String getBradescoPdfPassword(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString("bradesco_pdf_password", null);
	}

	public static String getIMAPServer() {
		return "imap.gmail.com";
	}

}
