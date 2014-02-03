package br.com.alepmendonca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CreditCardHelper extends CreditCardUsageAbstractHelper {

	public static final String COLUMN_FINAL_NUMBERS = "FINAL_NUMBERS";
	public static final String COLUMN_ISSUER = "ISSUER";
	public static final String COLUMN_HOLDER = "HOLDER";

	public CreditCardHelper(Context context) {
		super(context);
	}

	public static String getTableName() {
		return "CREDIT_CARD";
	}

	@Override
	protected String getAdditionalColumns() {
		return COLUMN_FINAL_NUMBERS + " INTEGER," +
				COLUMN_ISSUER + " TEXT NOT NULL," +
				COLUMN_HOLDER + " TEXT NOT NULL";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
