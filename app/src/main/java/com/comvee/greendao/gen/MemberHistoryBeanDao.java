package com.comvee.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.comvee.hospitalabbott.bean.MemberHistoryBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEMBER_HISTORY_BEAN".
*/
public class MemberHistoryBeanDao extends AbstractDao<MemberHistoryBean, Long> {

    public static final String TABLENAME = "MEMBER_HISTORY_BEAN";

    /**
     * Properties of entity MemberHistoryBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MemberId = new Property(1, String.class, "memberId", false, "MEMBER_ID");
        public final static Property Date = new Property(2, String.class, "date", false, "DATE");
    }

    private DaoSession daoSession;


    public MemberHistoryBeanDao(DaoConfig config) {
        super(config);
    }
    
    public MemberHistoryBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEMBER_HISTORY_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"MEMBER_ID\" TEXT," + // 1: memberId
                "\"DATE\" TEXT);"); // 2: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEMBER_HISTORY_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MemberHistoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(2, memberId);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MemberHistoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(2, memberId);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
    }

    @Override
    protected final void attachEntity(MemberHistoryBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MemberHistoryBean readEntity(Cursor cursor, int offset) {
        MemberHistoryBean entity = new MemberHistoryBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // memberId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MemberHistoryBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMemberId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MemberHistoryBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MemberHistoryBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MemberHistoryBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
