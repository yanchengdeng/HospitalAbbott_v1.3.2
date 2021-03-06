package com.comvee.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.comvee.hospitalabbott.bean.BloodRangeBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BLOOD_RANGE_BEAN".
*/
public class BloodRangeBeanDao extends AbstractDao<BloodRangeBean, Long> {

    public static final String TABLENAME = "BLOOD_RANGE_BEAN";

    /**
     * Properties of entity BloodRangeBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HighAfterMeal = new Property(1, String.class, "highAfterMeal", false, "HIGH_AFTER_MEAL");
        public final static Property HighBeforeBreakfast = new Property(2, String.class, "highBeforeBreakfast", false, "HIGH_BEFORE_BREAKFAST");
        public final static Property HighBeforeMeal = new Property(3, String.class, "highBeforeMeal", false, "HIGH_BEFORE_MEAL");
        public final static Property HighBeforeSleep = new Property(4, String.class, "highBeforeSleep", false, "HIGH_BEFORE_SLEEP");
        public final static Property HighBeforedawn = new Property(5, String.class, "highBeforedawn", false, "HIGH_BEFOREDAWN");
        public final static Property InsertDt = new Property(6, String.class, "insertDt", false, "INSERT_DT");
        public final static Property IsValid = new Property(7, String.class, "isValid", false, "IS_VALID");
        public final static Property LowAfterMeal = new Property(8, String.class, "lowAfterMeal", false, "LOW_AFTER_MEAL");
        public final static Property LowBeforeBreakfast = new Property(9, String.class, "lowBeforeBreakfast", false, "LOW_BEFORE_BREAKFAST");
        public final static Property LowBeforeMeal = new Property(10, String.class, "lowBeforeMeal", false, "LOW_BEFORE_MEAL");
        public final static Property LowBeforeSleep = new Property(11, String.class, "lowBeforeSleep", false, "LOW_BEFORE_SLEEP");
        public final static Property LowBeforedawn = new Property(12, String.class, "lowBeforedawn", false, "LOW_BEFOREDAWN");
        public final static Property MemberId = new Property(13, String.class, "memberId", false, "MEMBER_ID");
        public final static Property ModifyDt = new Property(14, String.class, "modifyDt", false, "MODIFY_DT");
        public final static Property RangeId = new Property(15, String.class, "rangeId", false, "RANGE_ID");
    }


    public BloodRangeBeanDao(DaoConfig config) {
        super(config);
    }
    
    public BloodRangeBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BLOOD_RANGE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"HIGH_AFTER_MEAL\" TEXT," + // 1: highAfterMeal
                "\"HIGH_BEFORE_BREAKFAST\" TEXT," + // 2: highBeforeBreakfast
                "\"HIGH_BEFORE_MEAL\" TEXT," + // 3: highBeforeMeal
                "\"HIGH_BEFORE_SLEEP\" TEXT," + // 4: highBeforeSleep
                "\"HIGH_BEFOREDAWN\" TEXT," + // 5: highBeforedawn
                "\"INSERT_DT\" TEXT," + // 6: insertDt
                "\"IS_VALID\" TEXT," + // 7: isValid
                "\"LOW_AFTER_MEAL\" TEXT," + // 8: lowAfterMeal
                "\"LOW_BEFORE_BREAKFAST\" TEXT," + // 9: lowBeforeBreakfast
                "\"LOW_BEFORE_MEAL\" TEXT," + // 10: lowBeforeMeal
                "\"LOW_BEFORE_SLEEP\" TEXT," + // 11: lowBeforeSleep
                "\"LOW_BEFOREDAWN\" TEXT," + // 12: lowBeforedawn
                "\"MEMBER_ID\" TEXT," + // 13: memberId
                "\"MODIFY_DT\" TEXT," + // 14: modifyDt
                "\"RANGE_ID\" TEXT);"); // 15: rangeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BLOOD_RANGE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BloodRangeBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String highAfterMeal = entity.getHighAfterMeal();
        if (highAfterMeal != null) {
            stmt.bindString(2, highAfterMeal);
        }
 
        String highBeforeBreakfast = entity.getHighBeforeBreakfast();
        if (highBeforeBreakfast != null) {
            stmt.bindString(3, highBeforeBreakfast);
        }
 
        String highBeforeMeal = entity.getHighBeforeMeal();
        if (highBeforeMeal != null) {
            stmt.bindString(4, highBeforeMeal);
        }
 
        String highBeforeSleep = entity.getHighBeforeSleep();
        if (highBeforeSleep != null) {
            stmt.bindString(5, highBeforeSleep);
        }
 
        String highBeforedawn = entity.getHighBeforedawn();
        if (highBeforedawn != null) {
            stmt.bindString(6, highBeforedawn);
        }
 
        String insertDt = entity.getInsertDt();
        if (insertDt != null) {
            stmt.bindString(7, insertDt);
        }
 
        String isValid = entity.getIsValid();
        if (isValid != null) {
            stmt.bindString(8, isValid);
        }
 
        String lowAfterMeal = entity.getLowAfterMeal();
        if (lowAfterMeal != null) {
            stmt.bindString(9, lowAfterMeal);
        }
 
        String lowBeforeBreakfast = entity.getLowBeforeBreakfast();
        if (lowBeforeBreakfast != null) {
            stmt.bindString(10, lowBeforeBreakfast);
        }
 
        String lowBeforeMeal = entity.getLowBeforeMeal();
        if (lowBeforeMeal != null) {
            stmt.bindString(11, lowBeforeMeal);
        }
 
        String lowBeforeSleep = entity.getLowBeforeSleep();
        if (lowBeforeSleep != null) {
            stmt.bindString(12, lowBeforeSleep);
        }
 
        String lowBeforedawn = entity.getLowBeforedawn();
        if (lowBeforedawn != null) {
            stmt.bindString(13, lowBeforedawn);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(14, memberId);
        }
 
        String modifyDt = entity.getModifyDt();
        if (modifyDt != null) {
            stmt.bindString(15, modifyDt);
        }
 
        String rangeId = entity.getRangeId();
        if (rangeId != null) {
            stmt.bindString(16, rangeId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BloodRangeBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String highAfterMeal = entity.getHighAfterMeal();
        if (highAfterMeal != null) {
            stmt.bindString(2, highAfterMeal);
        }
 
        String highBeforeBreakfast = entity.getHighBeforeBreakfast();
        if (highBeforeBreakfast != null) {
            stmt.bindString(3, highBeforeBreakfast);
        }
 
        String highBeforeMeal = entity.getHighBeforeMeal();
        if (highBeforeMeal != null) {
            stmt.bindString(4, highBeforeMeal);
        }
 
        String highBeforeSleep = entity.getHighBeforeSleep();
        if (highBeforeSleep != null) {
            stmt.bindString(5, highBeforeSleep);
        }
 
        String highBeforedawn = entity.getHighBeforedawn();
        if (highBeforedawn != null) {
            stmt.bindString(6, highBeforedawn);
        }
 
        String insertDt = entity.getInsertDt();
        if (insertDt != null) {
            stmt.bindString(7, insertDt);
        }
 
        String isValid = entity.getIsValid();
        if (isValid != null) {
            stmt.bindString(8, isValid);
        }
 
        String lowAfterMeal = entity.getLowAfterMeal();
        if (lowAfterMeal != null) {
            stmt.bindString(9, lowAfterMeal);
        }
 
        String lowBeforeBreakfast = entity.getLowBeforeBreakfast();
        if (lowBeforeBreakfast != null) {
            stmt.bindString(10, lowBeforeBreakfast);
        }
 
        String lowBeforeMeal = entity.getLowBeforeMeal();
        if (lowBeforeMeal != null) {
            stmt.bindString(11, lowBeforeMeal);
        }
 
        String lowBeforeSleep = entity.getLowBeforeSleep();
        if (lowBeforeSleep != null) {
            stmt.bindString(12, lowBeforeSleep);
        }
 
        String lowBeforedawn = entity.getLowBeforedawn();
        if (lowBeforedawn != null) {
            stmt.bindString(13, lowBeforedawn);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(14, memberId);
        }
 
        String modifyDt = entity.getModifyDt();
        if (modifyDt != null) {
            stmt.bindString(15, modifyDt);
        }
 
        String rangeId = entity.getRangeId();
        if (rangeId != null) {
            stmt.bindString(16, rangeId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BloodRangeBean readEntity(Cursor cursor, int offset) {
        BloodRangeBean entity = new BloodRangeBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // highAfterMeal
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // highBeforeBreakfast
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // highBeforeMeal
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // highBeforeSleep
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // highBeforedawn
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // insertDt
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // isValid
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // lowAfterMeal
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // lowBeforeBreakfast
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // lowBeforeMeal
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // lowBeforeSleep
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // lowBeforedawn
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // memberId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // modifyDt
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // rangeId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BloodRangeBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHighAfterMeal(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHighBeforeBreakfast(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHighBeforeMeal(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHighBeforeSleep(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHighBeforedawn(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setInsertDt(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsValid(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLowAfterMeal(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLowBeforeBreakfast(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setLowBeforeMeal(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLowBeforeSleep(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setLowBeforedawn(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setMemberId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setModifyDt(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setRangeId(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BloodRangeBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BloodRangeBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BloodRangeBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
