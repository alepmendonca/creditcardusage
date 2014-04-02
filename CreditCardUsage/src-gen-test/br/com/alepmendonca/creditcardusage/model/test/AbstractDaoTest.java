package br.com.alepmendonca.creditcardusage.model.test;

/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.alepmendonca.creditcardusage.dao.DaoMaster;

import com.honorables.beckfowler.CreditCardUsageApplication;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoLog;
import de.greenrobot.dao.DbUtils;
import de.greenrobot.dao.InternalUnitTestDaoAccess;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.internal.SqlUtils;

/**
 * Base class for DAOs having a long/Long as a PK, which is quite common.
 * 
 * @author Markus
 * 
 * @param <D>
 *            DAO class
 * @param <T>
 *            Entity type of the DAO
 */
public abstract class AbstractDaoTest<D extends AbstractDao<T, Long>, T> {

	protected final Class<D> daoClass;
	protected D dao;
	protected SQLiteDatabase db;
	protected InternalUnitTestDaoAccess<T, Long> daoAccess;
	protected IdentityScope<Long, T> identityScopeForDao;
	protected Set<Long> usedPks;
	protected final Random random;
	private Property pkColumn;

	public AbstractDaoTest(Class<D> daoClass) {
		random = new Random();
		this.daoClass = daoClass;
		usedPks = new HashSet<Long>();
	}

	public void setIdentityScopeBeforeSetUp(IdentityScope<Long, T> identityScope) {
		this.identityScopeForDao = identityScope;
	}

	protected void clearIdentityScopeIfAny() {
		if (identityScopeForDao != null) {
			identityScopeForDao.clear();
			DaoLog.d("Identity scope cleared");
		} else {
			DaoLog.d("No identity scope to clear");
		}
	}

	protected void logTableDump() {
		DbUtils.logTableDump(db, dao.getTablename());
	}

	@Before
	public void setUp() throws Exception {
		try {
			CreditCardUsageApplication application = (CreditCardUsageApplication) Robolectric.application;
			application.onCreate();
			db = application.getOpenHelper().getWritableDatabase();
			DaoMaster.dropAllTables(db, false);
			DaoMaster.createAllTables(db, false);
			daoAccess = new InternalUnitTestDaoAccess<T, Long>(db, (Class<AbstractDao<T, Long>>) daoClass, identityScopeForDao);
			dao = (D) daoAccess.getDao();
		} catch (Exception e) {
			throw new RuntimeException("Could not prepare DAO Test", e);
		}
		Property[] columns = daoAccess.getProperties();
		for (Property column : columns) {
			if (column.primaryKey) {
				if (pkColumn != null) {
					throw new RuntimeException("Test does not work with multiple PK columns");
				}
				pkColumn = column;
			}
		}
		if (pkColumn == null) {
			throw new RuntimeException("Test does not work without a PK column");
		}
	}

	@After
	public void tearDown() throws Exception {
		Robolectric.application.onTerminate();
	}

	@Test
	public void testInsertAndLoad() {
		Long pk = nextPk();
		T entity = createEntity(pk);
		dao.insert(entity);
		assertEquals(pk, daoAccess.getKey(entity));
		T entity2 = dao.load(pk);
		assertNotNull(entity2);
		assertEquals(daoAccess.getKey(entity), daoAccess.getKey(entity2));
	}

	@Test
	public void testInsertInTx() {
		dao.deleteAll();
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < 20; i++) {
			list.add(createEntityWithRandomPk());
		}
		dao.insertInTx(list);
		assertEquals(list.size(), dao.count());
	}

	@Test
	public void testCount() {
		dao.deleteAll();
		assertEquals(0, dao.count());
		dao.insert(createEntityWithRandomPk());
		assertEquals(1, dao.count());
		dao.insert(createEntityWithRandomPk());
		assertEquals(2, dao.count());
	}

	@Test
	public void testInsertTwice() {
		Long pk = nextPk();
		T entity = createEntity(pk);
		dao.insert(entity);
		try {
			dao.insert(entity);
			fail("Inserting twice should not work");
		} catch (SQLException expected) {
			// OK
		}
	}

	@Test
	public void testInsertOrReplaceTwice() {
		T entity = createEntityWithRandomPk();
		long rowId1 = dao.insert(entity);
		long rowId2 = dao.insertOrReplace(entity);
		if (dao.getPkProperty().type == Long.class) {
			assertEquals(rowId1, rowId2);
		}
	}

	@Test
	public void testInsertOrReplaceInTx() {
		dao.deleteAll();
		List<T> listPartial = new ArrayList<T>();
		List<T> listAll = new ArrayList<T>();
		for (int i = 0; i < 20; i++) {
			T entity = createEntityWithRandomPk();
			if (i % 2 == 0) {
				listPartial.add(entity);
			}
			listAll.add(entity);
		}
		dao.insertOrReplaceInTx(listPartial);
		dao.insertOrReplaceInTx(listAll);
		assertEquals(listAll.size(), dao.count());
	}

	@Test
	public void testDelete() {
		Long pk = nextPk();
		dao.deleteByKey(pk);
		T entity = createEntity(pk);
		dao.insert(entity);
		assertNotNull(dao.load(pk));
		dao.deleteByKey(pk);
		assertNull(dao.load(pk));
	}

	@Test
	public void testDeleteAll() {
		List<T> entityList = new ArrayList<T>();
		for (int i = 0; i < 10; i++) {
			T entity = createEntityWithRandomPk();
			entityList.add(entity);
		}
		dao.insertInTx(entityList);
		dao.deleteAll();
		assertEquals(0, dao.count());
		for (T entity : entityList) {
			Long key = daoAccess.getKey(entity);
			assertNotNull(key);
			assertNull(dao.load(key));
		}
	}

	@Test
	public void testDeleteInTx() {
		List<T> entityList = new ArrayList<T>();
		for (int i = 0; i < 10; i++) {
			T entity = createEntityWithRandomPk();
			entityList.add(entity);
		}
		dao.insertInTx(entityList);
		List<T> entitiesToDelete = new ArrayList<T>();
		entitiesToDelete.add(entityList.get(0));
		entitiesToDelete.add(entityList.get(3));
		entitiesToDelete.add(entityList.get(4));
		entitiesToDelete.add(entityList.get(8));
		dao.deleteInTx(entitiesToDelete);
		assertEquals(entityList.size() - entitiesToDelete.size(), dao.count());
		for (T deletedEntity : entitiesToDelete) {
			Long key = daoAccess.getKey(deletedEntity);
			assertNotNull(key);
			assertNull(dao.load(key));
		}
	}

	@Test
	public void testDeleteByKeyInTx() {
		List<T> entityList = new ArrayList<T>();
		for (int i = 0; i < 10; i++) {
			T entity = createEntityWithRandomPk();
			entityList.add(entity);
		}
		dao.insertInTx(entityList);
		List<Long> keysToDelete = new ArrayList<Long>();
		keysToDelete.add(daoAccess.getKey(entityList.get(0)));
		keysToDelete.add(daoAccess.getKey(entityList.get(3)));
		keysToDelete.add(daoAccess.getKey(entityList.get(4)));
		keysToDelete.add(daoAccess.getKey(entityList.get(8)));
		dao.deleteByKeyInTx(keysToDelete);
		assertEquals(entityList.size() - keysToDelete.size(), dao.count());
		for (Long key : keysToDelete) {
			assertNotNull(key);
			assertNull(dao.load(key));
		}
	}

	@Test
	public void testRowId() {
		T entity1 = createEntityWithRandomPk();
		T entity2 = createEntityWithRandomPk();
		long rowId1 = dao.insert(entity1);
		long rowId2 = dao.insert(entity2);
		assertTrue(rowId1 != rowId2);
	}

	@Test
	public void testLoadAll() {
		dao.deleteAll();
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < 15; i++) {
			T entity = createEntity(nextPk());
			list.add(entity);
		}
		dao.insertInTx(list);
		List<T> loaded = dao.loadAll();
		assertEquals(list.size(), loaded.size());
	}

	@Test
	public void testQuery() {
		dao.insert(createEntityWithRandomPk());
		Long pkForQuery = nextPk();
		dao.insert(createEntity(pkForQuery));
		dao.insert(createEntityWithRandomPk());

		String where = "WHERE " + dao.getPkColumns()[0] + "=?";
		List<T> list = dao.queryRaw(where, pkForQuery.toString());
		assertEquals(1, list.size());
		assertEquals(pkForQuery, daoAccess.getKey(list.get(0)));
	}

	@Test
	public void testUpdate() {
		dao.deleteAll();
		T entity = createEntityWithRandomPk();
		dao.insert(entity);
		dao.update(entity);
		assertEquals(1, dao.count());
	}

	@Test
	public void testReadWithOffset() {
		Long pk = nextPk();
		T entity = createEntity(pk);
		dao.insert(entity);

		Cursor cursor = queryWithDummyColumnsInFront(5, "42", pk);
		try {
			T entity2 = daoAccess.readEntity(cursor, 5);
			assertEquals(pk, daoAccess.getKey(entity2));
		} finally {
			cursor.close();
		}
	}

	@Test
	public void testLoadPkWithOffset() {
		runLoadPkTest(10);
	}

	@Test
	public void testLoadPk() {
		runLoadPkTest(0);
	}

	@Test
	public void testAssignPk() {
		if (daoAccess.isEntityUpdateable()) {
			T entity1 = createEntity(null);
			if (entity1 != null) {
				T entity2 = createEntity(null);

				dao.insert(entity1);
				dao.insert(entity2);

				Long pk1 = daoAccess.getKey(entity1);
				assertNotNull(pk1);
				Long pk2 = daoAccess.getKey(entity2);
				assertNotNull(pk2);

				assertFalse(pk1.equals(pk2));

				assertNotNull(dao.load(pk1));
				assertNotNull(dao.load(pk2));
			} else {
				DaoLog.d("Skipping testAssignPk for " + daoClass + " (createEntity returned null for null key)");
			}
		} else {
			DaoLog.d("Skipping testAssignPk for not updateable " + daoClass);
		}
	}

	protected void runLoadPkTest(int offset) {
		Long pk = nextPk();
		T entity = createEntity(pk);
		dao.insert(entity);

		Cursor cursor = queryWithDummyColumnsInFront(offset, "42", pk);
		try {
			Long pk2 = daoAccess.readKey(cursor, offset);
			assertEquals(pk, pk2);
		} finally {
			cursor.close();
		}
	}

	protected Cursor queryWithDummyColumnsInFront(int dummyCount, String valueForColumn, Long pk) {
		StringBuilder builder = new StringBuilder("SELECT ");
		for (int i = 0; i < dummyCount; i++) {
			builder.append(valueForColumn).append(",");
		}
		SqlUtils.appendColumns(builder, "T", dao.getAllColumns()).append(" FROM ");
		builder.append(dao.getTablename()).append(" T");
		if (pk != null) {
			builder.append(" WHERE ");

			assertEquals(1, dao.getPkColumns().length);
			builder.append(dao.getPkColumns()[0]).append("=");
			DatabaseUtils.appendValueToSql(builder, pk);
		}

		String select = builder.toString();
		Cursor cursor = db.rawQuery(select, null);
		assertTrue(cursor.moveToFirst());
		try {
			for (int i = 0; i < dummyCount; i++) {
				assertEquals(valueForColumn, cursor.getString(i));
			}
			if (pk != null) {
				assertEquals(1, cursor.getCount());
			}
		} catch (RuntimeException ex) {
			cursor.close();
			throw ex;
		}
		return cursor;
	}

	/** Provides a collision free PK () not returned before in the current test. */
	protected Long nextPk() {
		for (int i = 0; i < 100000; i++) {
			Long pk = createRandomPk();
			if (usedPks.add(pk)) {
				return pk;
			}
		}
		throw new IllegalStateException("Could not find a new PK");
	}

	protected T createEntityWithRandomPk() {
		return createEntity(nextPk());
	}

	/**
	 * Creates an insertable entity. If the given key is null, but the entity's PK is not null the method must return
	 * null.
	 */
	protected abstract T createEntity(Long key);

	protected Long createRandomPk() {
		return random.nextLong();
	}

}