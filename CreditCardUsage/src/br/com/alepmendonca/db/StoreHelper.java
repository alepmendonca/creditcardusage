package br.com.alepmendonca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StoreHelper extends CreditCardUsageAbstractHelper {

	public static final String COLUMN_ORIGINAL_NAME = "ORIGINAL_NAME";
	public static final String COLUMN_USER_DEFINED_NAME = "USER_DEFINED_NAME";
	public static final String COLUMN_ID_STORE_TYPE = "ID_TYPE";

    public StoreHelper(Context context) {
        super(context);
    }

	public static String getTableName() {
		return "STORE";
	}

	@Override
	protected String getAdditionalColumns() {
		return COLUMN_ORIGINAL_NAME + " TEXT," +
				COLUMN_USER_DEFINED_NAME + " TEXT," +
				COLUMN_ID_STORE_TYPE + " INTEGER," +
				"FOREIGN KEY(" + COLUMN_ID_STORE_TYPE + ") REFERENCES " + StoreTypeHelper.getTableName() + "(" + StoreTypeHelper.getIdColumn() + "))";
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
