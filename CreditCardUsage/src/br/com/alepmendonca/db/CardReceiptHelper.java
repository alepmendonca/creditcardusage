package br.com.alepmendonca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CardReceiptHelper extends CreditCardUsageAbstractHelper {

	public static final String COLUMN_ID_CARD = "ID_CARD";
	public static final String COLUMN_ID_STORE = "ID_STORE";
	public static final String COLUMN_VALUE = "VALUE";
	public static final String COLUMN_CURRENCY = "CURRENCY";
	public static final String COLUMN_AUTHORIZED_DATE = "AUTHORIZED_DATE";
	public static final String COLUMN_AUTENTICATION = "AUTENTICATION";

    public CardReceiptHelper(Context context) {
        super(context);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		db.execSQL("CREATE UNIQUE INDEX CARD_RECEIPT_TRANSACTION ON " + getTableName() +
				"(" + COLUMN_ID_CARD + "," + COLUMN_AUTENTICATION + ")");
	}

	public static String getTableName() {
		return "CREDIT_CARD_RECEIPT";
	}

	@Override
	protected String getAdditionalColumns() {
		return COLUMN_ID_CARD + " INTEGER," +
				COLUMN_ID_STORE + " INTEGER," +
				COLUMN_VALUE + " REAL," +
				COLUMN_CURRENCY + " TEXT," +
				COLUMN_AUTHORIZED_DATE + " TEXT," +
				COLUMN_AUTENTICATION + " INTEGER," +
				"FOREIGN KEY(" + COLUMN_ID_CARD + ") REFERENCES " + CreditCardHelper.getTableName() + "(" + CreditCardHelper.getIdColumn() + ")," +
				"FOREIGN KEY(" + COLUMN_ID_STORE + ") REFERENCES " + StoreHelper.getTableName() + "(" + StoreHelper.getIdColumn() + "))";
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
