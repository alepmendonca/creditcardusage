package br.com.alepmendonca.properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class BradescoMailProperties {

	//TODO todas as properties devem ser guardadas em BD
	public static InternetAddress getBradescoMailSender() {
		try {
			return new InternetAddress("infoemail@infobradesco.com.br");
		} catch (AddressException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getIMAPServer() {
		return "imap.gmail.com";
	}

	public static String getMailPassword() {
		// TODO Usar XOAuth2 ao invés de senha
		return "inzilxgmryojkhty";
	}
}
