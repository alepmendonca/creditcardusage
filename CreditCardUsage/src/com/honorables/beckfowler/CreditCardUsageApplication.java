package com.honorables.beckfowler;

import br.com.alepmendonca.creditcardusage.dao.DaoMaster;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.dao.DaoMaster.DevOpenHelper;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class CreditCardUsageApplication extends Application {

	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession dao;

	@Override
	public void onCreate() {
		super.onCreate();
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ccusage-db", null);
		db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        dao = daoMaster.newSession();
	}

	public DaoSession getDAO() {
		return dao;
	}

}
