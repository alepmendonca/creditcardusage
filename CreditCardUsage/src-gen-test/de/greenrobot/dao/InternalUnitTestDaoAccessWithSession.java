package de.greenrobot.dao;

import java.lang.reflect.Constructor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.internal.DaoConfig;

/** Reserved for internal unit tests that want to access some non-public methods. Don't use for anything else. */
public class InternalUnitTestDaoAccessWithSession<T, K> {
	private final AbstractDao<T, K> dao;

	public InternalUnitTestDaoAccessWithSession(SQLiteDatabase db, Class<AbstractDao<T, K>> daoClass, DaoSession session, IdentityScope<?, ?> identityScope)
			throws Exception {
		DaoConfig daoConfig = new DaoConfig(db, daoClass);
		daoConfig.setIdentityScope(identityScope);
		Constructor<AbstractDao<T, K>> constructor = daoClass.getConstructor(DaoConfig.class, DaoSession.class);
		dao = constructor.newInstance(daoConfig, session);
	}

	public K getKey(T entity) {
		return dao.getKey(entity);
	}

	public Property[] getProperties() {
		return dao.getProperties();
	}

	public boolean isEntityUpdateable() {
		return dao.isEntityUpdateable();
	}

	public T readEntity(Cursor cursor, int offset) {
		return dao.readEntity(cursor, offset);
	}

	public K readKey(Cursor cursor, int offset) {
		return dao.readKey(cursor, offset);
	}

	public AbstractDao<T, K> getDao() {
		return dao;
	}

}