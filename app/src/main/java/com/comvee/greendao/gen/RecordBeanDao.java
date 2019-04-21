package com.comvee.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.comvee.hospitalabbott.bean.RecordBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECORD_BEAN".
*/
public class RecordBeanDao extends AbstractDao<RecordBean, Long> {

    public static final String TABLENAME = "RECORD_BEAN";

    /**
     * Properties of entity RecordBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Time = new Property(1, String.class, "time", false, "TIME");
        public final static Property MemberId = new Property(2, String.class, "memberId", false, "MEMBER_ID");
    }


    public RecordBeanDao(DaoConfig config) {
        super(config);
    }
    
    public RecordBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECORD_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TIME\" TEXT," + // 1: time
                "\"MEMBER_ID\" TEXT);"); // 2: memberId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECORD_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RecordBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(2, time);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(3, memberId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RecordBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(2, time);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(3, memberId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RecordBean readEntity(Cursor cursor, int offset) {
        RecordBean entity = new RecordBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // time
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // memberId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RecordBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMemberId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RecordBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RecordBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RecordBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
