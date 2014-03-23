package br.com.alepmendonca.mail.bradesco;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;

import android.util.Log;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.properties.BradescoMailProperties;

public class BradescoMailExtractStrategy {

	private Store store = null;
	protected BradescoMailProperties properties = null;
	protected DaoSession dao = null;

	public BradescoMailExtractStrategy(Store s, BradescoMailProperties p, DaoSession d) throws IOException, MessagingException {
		store = s;
		properties = p;
		dao = d;
	}

	protected String getPdfPassword() {
		return properties.getBradescoPdfPassword();
	}

	public List<Object> extractMessages() throws IOException, MessagingException, ParseException {
		Message messages[] = getMessages();
    	Log.d(BradescoMailProperties.TAG, "Found all these messages: " + messages.length);

    	List result = new ArrayList();
    	for(Message message:messages) {
    		Multipart mp = (Multipart)message.getContent();
    		InputStream pdf = mp.getBodyPart(1).getInputStream();
    		BradescoTransactionMailReader mailReader = new BradescoTransactionMailReader(this);
    		if (mailReader.canExtract(mp)) {
    			result.add(mailReader.extractMessage(mp, pdf));
    		}
		}
    	return result;
	}

	private Message[] getMessages() throws MessagingException {
		Folder labelFolder = store.getFolder(properties.getGmailLabel());
		labelFolder.open(Folder.READ_ONLY);
    	SearchTerm ft = new AndTerm(
    			new FlagTerm(new Flags(Flags.Flag.SEEN), false),
    			new FromTerm(properties.getBradescoMailSender()));
		return labelFolder.search(ft);
	}

}
