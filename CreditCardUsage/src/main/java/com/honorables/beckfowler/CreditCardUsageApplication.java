package com.honorables.beckfowler;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import br.com.alepmendonca.creditcardusage.dao.DaoMaster;
import br.com.alepmendonca.creditcardusage.dao.DaoMaster.OpenHelper;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.properties.BradescoMailProperties;

public class CreditCardUsageApplication extends Application {

	private SQLiteDatabase db;
	private OpenHelper helper;
	private DaoMaster daoMaster;
	private DaoSession dao;
	private BradescoMailProperties properties;

	@Override
	public void onCreate() {
		super.onCreate();
		db = getOpenHelper().getWritableDatabase();
        daoMaster = new DaoMaster(db);
        dao = daoMaster.newSession();
        properties = new BradescoMailProperties(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		helper.close();
	}

	public OpenHelper getOpenHelper() {
		if (helper == null) {
			helper = new OpenHelper(this, "ccusage-db", null) {				
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
					// TODO Criar classe separada para tratar upgrades
				}
			};
		}
		return helper;
	}

	public void setOpenHelper(OpenHelper h) {
		helper = h;
	}

	public DaoSession getDAO() {
		return dao;
	}

	public BradescoMailProperties getProperties() {
		return properties;
	}

}
