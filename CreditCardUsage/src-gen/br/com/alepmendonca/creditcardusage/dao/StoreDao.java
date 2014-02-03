package br.com.alepmendonca.creditcardusage.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import br.com.alepmendonca.creditcardusage.StoreType;

import br.com.alepmendonca.creditcardusage.Store;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table STORE.
*/
public class StoreDao extends AbstractDao<Store, Long> {

    public static final String TABLENAME = "STORE";

    /**
     * Properties of entity Store.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property OriginalName = new Property(1, String.class, "originalName", false, "ORIGINAL_NAME");
        public final static Property UserDefinedName = new Property(2, String.class, "userDefinedName", false, "USER_DEFINED_NAME");
        public final static Property StoreTypeId = new Property(3, Long.class, "storeTypeId", false, "STORE_TYPE_ID");
    };

    private DaoSession daoSession;


    public StoreDao(DaoConfig config) {
        super(config);
    }
    
    public StoreDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'STORE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ORIGINAL_NAME' TEXT NOT NULL ," + // 1: originalName
                "'USER_DEFINED_NAME' TEXT," + // 2: userDefinedName
                "'STORE_TYPE_ID' INTEGER);"); // 3: storeTypeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'STORE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Store entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getOriginalName());
 
        String userDefinedName = entity.getUserDefinedName();
        if (userDefinedName != null) {
            stmt.bindString(3, userDefinedName);
        }
 
        Long storeTypeId = entity.getStoreTypeId();
        if (storeTypeId != null) {
            stmt.bindLong(4, storeTypeId);
        }
    }

    @Override
    protected void attachEntity(Store entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Store readEntity(Cursor cursor, int offset) {
        Store entity = new Store( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // originalName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userDefinedName
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // storeTypeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Store entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOriginalName(cursor.getString(offset + 1));
        entity.setUserDefinedName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStoreTypeId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Store entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Store entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getStoreTypeDao().getAllColumns());
            builder.append(" FROM STORE T");
            builder.append(" LEFT JOIN STORE_TYPE T0 ON T.'STORE_TYPE_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Store loadCurrentDeep(Cursor cursor, boolean lock) {
        Store entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        StoreType storeType = loadCurrentOther(daoSession.getStoreTypeDao(), cursor, offset);
        entity.setStoreType(storeType);

        return entity;    
    }

    public Store loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Store> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Store> list = new ArrayList<Store>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Store> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Store> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}