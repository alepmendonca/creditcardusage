package br.com.alepmendonca.notification.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.CreditCard;
import br.com.alepmendonca.creditcardusage.model.Store;
import br.com.alepmendonca.notification.CreditCardUsageNotificationFactory;
import android.test.AndroidTestCase;

public class CreditCardUsageNotificationFactoryTest extends AndroidTestCase {

	CardReceipt cr;
	ArrayList receipts;

	protected void setUp() throws Exception {
		super.setUp();
		cr = new CardReceipt(
				new CreditCard(100L, 3048, "Mastercard", "Alexandre Mendonça"), 
				new Store(200L, "LOJA ABC", "Boteco", null), 
				new BigDecimal(34), Currency.getInstance("986"), Calendar.getInstance(), 387744L);
		receipts = new ArrayList<CardReceipt>();
		receipts.add(cr);
	}

	public void testCreateSingleNotificationForOneCardReceipt() {
		CreditCardUsageNotificationFactory.createNotifications(receipts, mContext);
	}

}
