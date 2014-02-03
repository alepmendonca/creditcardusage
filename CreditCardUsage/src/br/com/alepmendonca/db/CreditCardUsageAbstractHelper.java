package br.com.alepmendonca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class CreditCardUsageAbstractHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "creditcardusage.db";
	private static final int DATABASE_VERSION = 1;

    public CreditCardUsageAbstractHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String tableCreate = 
				"CREATE TABLE " + getTableName() + " (" +
						getIdColumn() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						getAdditionalColumns() + ")";
		db.execSQL(tableCreate);
	}

	public static String getTableName() {
		throw new UnsupportedOperationException("You must implement getTableName");
	}

	protected abstract String getAdditionalColumns();

	public static final String getIdColumn() {
		return "_ID";
	}

}
