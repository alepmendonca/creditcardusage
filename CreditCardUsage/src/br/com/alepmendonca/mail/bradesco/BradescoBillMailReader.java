package br.com.alepmendonca.mail.bradesco;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.Multipart;

public class BradescoBillMailReader {

	private BradescoMailExtractStrategy strategy = null;

	protected boolean canExtractMessage(Multipart message) throws IOException, MessagingException {
		return message.getBodyPart(0).getContent().toString().contains("Extrato");
	}

	public BradescoBillMailReader(BradescoMailExtractStrategy str) {
		strategy = str;
	}

	public BradescoMailNotifiable extractMessage(Multipart message, InputStream pdf) throws IOException, MessagingException {
		if (canExtractMessage(message)) {
			//#TODO
			System.out.println("Still have something to do");
		} else {
			new UnsupportedOperationException("Message cannot be extracted: " + message.getContentType());
		}
		return null;
	}

}
