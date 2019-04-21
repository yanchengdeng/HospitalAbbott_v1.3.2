package com.comvee.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.comvee.hospitalabbott.bean.RefreshHistoryModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REFRESH_HISTORY_MODEL".
*/
public class RefreshHistoryModelDao extends AbstractDao<RefreshHistoryModel, Long> {

    public static final String TABLENAME = "REFRESH_HISTORY_MODEL";

    /**
     * Properties of entity RefreshHistoryModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MemberId = new Property(1, String.class, "memberId", false, "MEMBER_ID");
        public final static Property IsInit = new Property(2, boolean.class, "isInit", false, "IS_INIT");
        public final static Property RefreshTime = new Property(3, String.class, "refreshTime", false, "REFRESH_TIME");
    }


    public RefreshHistoryModelDao(DaoConfig config) {
        super(config);
    }
    
    public RefreshHistoryModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REFRESH_HISTORY_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"MEMBER_ID\" TEXT," + // 1: memberId
                "\"IS_INIT\" INTEGER NOT NULL ," + // 2: isInit
                "\"REFRESH_TIME\" TEXT);"); // 3: refreshTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REFRESH_HISTORY_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RefreshHistoryModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(2, memberId);
        }
        stmt.bindLong(3, entity.getIsInit() ? 1L: 0L);
 
        String refreshTime = entity.getRefreshTime();
        if (refreshTime != null) {
            stmt.bindString(4, refreshTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RefreshHistoryModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(2, memberId);
        }
        stmt.bindLong(3, entity.getIsInit() ? 1L: 0L);
 
        String refreshTime = entity.getRefreshTime();
        if (refreshTime != null) {
            stmt.bindString(4, refreshTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RefreshHistoryModel readEntity(Cursor cursor, int offset) {
        RefreshHistoryModel entity = new RefreshHistoryModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // memberId
            cursor.getShort(offset + 2) != 0, // isInit
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // refreshTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RefreshHistoryModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMemberId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIsInit(cursor.getShort(offset + 2) != 0);
        entity.setRefreshTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RefreshHistoryModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RefreshHistoryModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RefreshHistoryModel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}