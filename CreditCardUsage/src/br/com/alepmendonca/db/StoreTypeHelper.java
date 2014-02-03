package br.com.alepmendonca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StoreTypeHelper extends CreditCardUsageAbstractHelper {

	public static final String COLUMN_TYPE = "TYPE_NAME";
	public static final String COLUMN_ID_PARENT = "ID_PARENT";

    public StoreTypeHelper(Context context) {
        super(context);
    }

	public static String getTableName() {
		return "STORE_TYPE";
	}

	@Override
	protected String getAdditionalColumns() {
		return COLUMN_TYPE + " TEXT," +
				COLUMN_ID_PARENT + " INTEGER," +
				"FOREIGN KEY(" + COLUMN_ID_PARENT + ") REFERENCES " + StoreTypeHelper.getTableName() + "(" + StoreTypeHelper.getIdColumn() + "))";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
