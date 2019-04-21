package com.comvee.greendao.gen;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.comvee.hospitalabbott.bean.MemberModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEMBER_MODEL".
*/
public class MemberModelDao extends AbstractDao<MemberModel, Long> {

    public static final String TABLENAME = "MEMBER_MODEL";

    /**
     * Properties of entity MemberModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BedId = new Property(1, String.class, "bedId", false, "BED_ID");
        public final static Property BedNo = new Property(2, String.class, "bedNo", false, "BED_NO");
        public final static Property BedNoNew = new Property(3, float.class, "bedNoNew", false, "BED_NO_NEW");
        public final static Property ConcernStatus = new Property(4, String.class, "concernStatus", false, "CONCERN_STATUS");
        public final static Property DepartmentId = new Property(5, String.class, "departmentId", false, "DEPARTMENT_ID");
        public final static Property DepartmentName = new Property(6, String.class, "departmentName", false, "DEPARTMENT_NAME");
        public final static Property MemberId = new Property(7, String.class, "memberId", false, "MEMBER_ID");
        public final static Property MemberName = new Property(8, String.class, "memberName", false, "MEMBER_NAME");
        public final static Property AfterBreakfast = new Property(9, String.class, "afterBreakfast", false, "AFTER_BREAKFAST");
        public final static Property AfterDinner = new Property(10, String.class, "afterDinner", false, "AFTER_DINNER");
        public final static Property AfterLunch = new Property(11, String.class, "afterLunch", false, "AFTER_LUNCH");
        public final static Property BeforeBreakfast = new Property(12, String.class, "beforeBreakfast", false, "BEFORE_BREAKFAST");
        public final static Property BeforeDinner = new Property(13, String.class, "beforeDinner", false, "BEFORE_DINNER");
        public final static Property BeforeLunch = new Property(14, String.class, "beforeLunch", false, "BEFORE_LUNCH");
        public final static Property BeforeSleep = new Property(15, String.class, "beforeSleep", false, "BEFORE_SLEEP");
        public final static Property Beforedawn = new Property(16, String.class, "beforedawn", false, "BEFOREDAWN");
        public final static Property Threeclock = new Property(17, String.class, "threeclock", false, "THREECLOCK");
        public final static Property Randomtime = new Property(18, String.class, "randomtime", false, "RANDOMTIME");
        public final static Property PlanType = new Property(19, int.class, "planType", false, "PLAN_TYPE");
        public final static Property SmbgScheme = new Property(20, String.class, "smbgScheme", false, "SMBG_SCHEME");
        public final static Property Sex = new Property(21, String.class, "sex", false, "SEX");
        public final static Property PatPatientId = new Property(22, String.class, "patPatientId", false, "PAT_PATIENT_ID");
    }

    private Query<MemberModel> departmentModel_BedModelListQuery;

    public MemberModelDao(DaoConfig config) {
        super(config);
    }
    
    public MemberModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEMBER_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"BED_ID\" TEXT," + // 1: bedId
                "\"BED_NO\" TEXT," + // 2: bedNo
                "\"BED_NO_NEW\" REAL NOT NULL ," + // 3: bedNoNew
                "\"CONCERN_STATUS\" TEXT," + // 4: concernStatus
                "\"DEPARTMENT_ID\" TEXT," + // 5: departmentId
                "\"DEPARTMENT_NAME\" TEXT," + // 6: departmentName
                "\"MEMBER_ID\" TEXT," + // 7: memberId
                "\"MEMBER_NAME\" TEXT," + // 8: memberName
                "\"AFTER_BREAKFAST\" TEXT," + // 9: afterBreakfast
                "\"AFTER_DINNER\" TEXT," + // 10: afterDinner
                "\"AFTER_LUNCH\" TEXT," + // 11: afterLunch
                "\"BEFORE_BREAKFAST\" TEXT," + // 12: beforeBreakfast
                "\"BEFORE_DINNER\" TEXT," + // 13: beforeDinner
                "\"BEFORE_LUNCH\" TEXT," + // 14: beforeLunch
                "\"BEFORE_SLEEP\" TEXT," + // 15: beforeSleep
                "\"BEFOREDAWN\" TEXT," + // 16: beforedawn
                "\"THREECLOCK\" TEXT," + // 17: threeclock
                "\"RANDOMTIME\" TEXT," + // 18: randomtime
                "\"PLAN_TYPE\" INTEGER NOT NULL ," + // 19: planType
                "\"SMBG_SCHEME\" TEXT," + // 20: smbgScheme
                "\"SEX\" TEXT," + // 21: sex
                "\"PAT_PATIENT_ID\" TEXT);"); // 22: patPatientId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEMBER_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MemberModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bedId = entity.getBedId();
        if (bedId != null) {
            stmt.bindString(2, bedId);
        }
 
        String bedNo = entity.getBedNo();
        if (bedNo != null) {
            stmt.bindString(3, bedNo);
        }
        stmt.bindDouble(4, entity.getBedNoNew());
 
        String concernStatus = entity.getConcernStatus();
        if (concernStatus != null) {
            stmt.bindString(5, concernStatus);
        }
 
        String departmentId = entity.getDepartmentId();
        if (departmentId != null) {
            stmt.bindString(6, departmentId);
        }
 
        String departmentName = entity.getDepartmentName();
        if (departmentName != null) {
            stmt.bindString(7, departmentName);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(8, memberId);
        }
 
        String memberName = entity.getMemberName();
        if (memberName != null) {
            stmt.bindString(9, memberName);
        }
 
        String afterBreakfast = entity.getAfterBreakfast();
        if (afterBreakfast != null) {
            stmt.bindString(10, afterBreakfast);
        }
 
        String afterDinner = entity.getAfterDinner();
        if (afterDinner != null) {
            stmt.bindString(11, afterDinner);
        }
 
        String afterLunch = entity.getAfterLunch();
        if (afterLunch != null) {
            stmt.bindString(12, afterLunch);
        }
 
        String beforeBreakfast = entity.getBeforeBreakfast();
        if (beforeBreakfast != null) {
            stmt.bindString(13, beforeBreakfast);
        }
 
        String beforeDinner = entity.getBeforeDinner();
        if (beforeDinner != null) {
            stmt.bindString(14, beforeDinner);
        }
 
        String beforeLunch = entity.getBeforeLunch();
        if (beforeLunch != null) {
            stmt.bindString(15, beforeLunch);
        }
 
        String beforeSleep = entity.getBeforeSleep();
        if (beforeSleep != null) {
            stmt.bindString(16, beforeSleep);
        }
 
        String beforedawn = entity.getBeforedawn();
        if (beforedawn != null) {
            stmt.bindString(17, beforedawn);
        }
 
        String threeclock = entity.getThreeclock();
        if (threeclock != null) {
            stmt.bindString(18, threeclock);
        }
 
        String randomtime = entity.getRandomtime();
        if (randomtime != null) {
            stmt.bindString(19, randomtime);
        }
        stmt.bindLong(20, entity.getPlanType());
 
        String smbgScheme = entity.getSmbgScheme();
        if (smbgScheme != null) {
            stmt.bindString(21, smbgScheme);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(22, sex);
        }
 
        String patPatientId = entity.getPatPatientId();
        if (patPatientId != null) {
            stmt.bindString(23, patPatientId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MemberModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bedId = entity.getBedId();
        if (bedId != null) {
            stmt.bindString(2, bedId);
        }
 
        String bedNo = entity.getBedNo();
        if (bedNo != null) {
            stmt.bindString(3, bedNo);
        }
        stmt.bindDouble(4, entity.getBedNoNew());
 
        String concernStatus = entity.getConcernStatus();
        if (concernStatus != null) {
            stmt.bindString(5, concernStatus);
        }
 
        String departmentId = entity.getDepartmentId();
        if (departmentId != null) {
            stmt.bindString(6, departmentId);
        }
 
        String departmentName = entity.getDepartmentName();
        if (departmentName != null) {
            stmt.bindString(7, departmentName);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(8, memberId);
        }
 
        String memberName = entity.getMemberName();
        if (memberName != null) {
            stmt.bindString(9, memberName);
        }
 
        String afterBreakfast = entity.getAfterBreakfast();
        if (afterBreakfast != null) {
            stmt.bindString(10, afterBreakfast);
        }
 
        String afterDinner = entity.getAfterDinner();
        if (afterDinner != null) {
            stmt.bindString(11, afterDinner);
        }
 
        String afterLunch = entity.getAfterLunch();
        if (afterLunch != null) {
            stmt.bindString(12, afterLunch);
        }
 
        String beforeBreakfast = entity.getBeforeBreakfast();
        if (beforeBreakfast != null) {
            stmt.bindString(13, beforeBreakfast);
        }
 
        String beforeDinner = entity.getBeforeDinner();
        if (beforeDinner != null) {
            stmt.bindString(14, beforeDinner);
        }
 
        String beforeLunch = entity.getBeforeLunch();
        if (beforeLunch != null) {
            stmt.bindString(15, beforeLunch);
        }
 
        String beforeSleep = entity.getBeforeSleep();
        if (beforeSleep != null) {
            stmt.bindString(16, beforeSleep);
        }
 
        String beforedawn = entity.getBeforedawn();
        if (beforedawn != null) {
            stmt.bindString(17, beforedawn);
        }
 
        String threeclock = entity.getThreeclock();
        if (threeclock != null) {
            stmt.bindString(18, threeclock);
        }
 
        String randomtime = entity.getRandomtime();
        if (randomtime != null) {
            stmt.bindString(19, randomtime);
        }
        stmt.bindLong(20, entity.getPlanType());
 
        String smbgScheme = entity.getSmbgScheme();
        if (smbgScheme != null) {
            stmt.bindString(21, smbgScheme);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(22, sex);
        }
 
        String patPatientId = entity.getPatPatientId();
        if (patPatientId != null) {
            stmt.bindString(23, patPatientId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MemberModel readEntity(Cursor cursor, int offset) {
        MemberModel entity = new MemberModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bedId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // bedNo
            cursor.getFloat(offset + 3), // bedNoNew
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // concernStatus
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // departmentId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // departmentName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // memberId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // memberName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // afterBreakfast
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // afterDinner
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // afterLunch
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // beforeBreakfast
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // beforeDinner
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // beforeLunch
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // beforeSleep
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // beforedawn
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // threeclock
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // randomtime
            cursor.getInt(offset + 19), // planType
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // smbgScheme
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // sex
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22) // patPatientId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MemberModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBedId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBedNo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBedNoNew(cursor.getFloat(offset + 3));
        entity.setConcernStatus(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDepartmentId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDepartmentName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMemberId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMemberName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAfterBreakfast(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAfterDinner(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAfterLunch(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setBeforeBreakfast(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBeforeDinner(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setBeforeLunch(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setBeforeSleep(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setBeforedawn(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setThreeclock(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setRandomtime(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setPlanType(cursor.getInt(offset + 19));
        entity.setSmbgScheme(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setSex(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setPatPatientId(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MemberModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MemberModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MemberModel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "bedModelList" to-many relationship of DepartmentModel. */
    public List<MemberModel> _queryDepartmentModel_BedModelList(String departmentId) {
        synchronized (this) {
            if (departmentModel_BedModelListQuery == null) {
                QueryBuilder<MemberModel> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.DepartmentId.eq(null));
                departmentModel_BedModelListQuery = queryBuilder.build();
            }
        }
        Query<MemberModel> query = departmentModel_BedModelListQuery.forCurrentThread();
        query.setParameter(0, departmentId);
        return query.list();
    }

}