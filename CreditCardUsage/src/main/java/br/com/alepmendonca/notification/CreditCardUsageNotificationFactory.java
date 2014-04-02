package br.com.alepmendonca.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import br.com.alepmendonca.creditcardusage.model.CardExtract;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.CreditCard;

import com.honorables.beckfowler.CardReceiptActivity;
import com.honorables.beckfowler.R;

public class CreditCardUsageNotificationFactory {

	public void createNotifications(List<Object> messages, Context context) {
		List<CardReceipt> cardReceipts = new ArrayList<CardReceipt>();
		List<CardExtract> cardExtracts = new ArrayList<CardExtract>();
		for (Object m : messages) {
			if (m instanceof CardReceipt) {
				cardReceipts.add((CardReceipt) m);
			} else if (m instanceof CardExtract) {
				cardExtracts.add((CardExtract) m);
			}
		}
		createCardReceiptNotification(cardReceipts, context);
		createCardExtractNotification(cardExtracts, context);
	}

	private void createCardReceiptNotification(List<CardReceipt> receipts, Context context) {
		HashMap<CreditCard, CardReceipt> cardsToReceipts = new HashMap<CreditCard, CardReceipt>();
		for (CardReceipt r : receipts) {
			cardsToReceipts.put(r.getCreditCard(), r);
		}
		for (CreditCard cc : cardsToReceipts.keySet()) {
			List<CardReceipt> receiptsFromACC = new ArrayList<CardReceipt>(cardsToReceipts.values());
			Collections.sort(receiptsFromACC, new CardReceipt.CardReceiptComparator());
			Notification.Builder mBuilder = 
					new Notification.Builder(context)
					.setSmallIcon(R.drawable.ic_card_receipt)
					.setContentText(receiptsFromACC.get(0).toString());
			if (receiptsFromACC.size() < 2) {
				mBuilder.setContentTitle("Nova compra");
				if (receiptsFromACC.get(0).getCreditCard().getOwner() == null)
					mBuilder.addAction(android.R.drawable.btn_dialog, "Config CC", PendingIntent.getActivity(context, 0, new Intent(context, CardReceiptActivity.class), 0));
				if (receiptsFromACC.get(0).getStore().getUserDefinedName() == null)
					mBuilder.addAction(android.R.drawable.ic_dialog_alert, "Config Loja", PendingIntent.getActivity(context, 0, new Intent(context, CardReceiptActivity.class), 0));
			} else {
				mBuilder.setContentTitle(receiptsFromACC.size() + " novas compras");
				Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
				for (CardReceipt cr : receiptsFromACC) {
				    inboxStyle.addLine(cr.toString());
				}
				mBuilder.setStyle(inboxStyle);
			}
			mBuilder.setAutoCancel(true);
			mBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
			//TODO definir cor das notificacoes
			//mBuilder.setLights(Notification, LEDON, LEDOFF);
			displayNotification(mBuilder, cc.getFinalNumbers(), context);
		}

	}

	private void displayNotification(Notification.Builder mBuilder, int notificationId, Context context) {
		//TODO tentar adicionar intent nas notificacoes
/*		Intent resultIntent = new Intent(context, CardReceiptActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		
		stackBuilder.addParentStack(CardReceiptActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);*/
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notificationId, mBuilder.build());
	}

	private void createCardExtractNotification(List<CardExtract> extracts, Context context) {
		
	}

}
