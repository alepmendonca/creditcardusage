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

import android.content.Context;
import br.com.alepmendonca.properties.BradescoMailProperties;

public class BradescoMailExtractStrategy {

	private Store store = null;
	protected Context context = null;
	Folder labelFolder = null;

	public BradescoMailExtractStrategy(Store s, Context c) throws IOException, MessagingException {
		store = s;
		context = c;
    	labelFolder = store.getFolder(getLabelName());
	}

	protected String getLabelName() {
		return "Inbox";
	}

	protected String getPdfPassword() {
		return "didamendes";
	}

	public List<Object> extractMessages() throws IOException, MessagingException, ParseException {
		labelFolder.open(Folder.READ_ONLY);
    	SearchTerm ft = new AndTerm(
    			new FlagTerm(new Flags(Flags.Flag.SEEN), false),
    			new FromTerm(BradescoMailProperties.getBradescoMailSender()));
		Message messages[] = labelFolder.search(ft);
    	System.out.println("Found all these messages: " + messages.length);
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

}
