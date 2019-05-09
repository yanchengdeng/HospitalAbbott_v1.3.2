package com.comvee.hospitalabbott.tool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.comvee.greendao.gen.BloodRangeBeanDao;
import com.comvee.greendao.gen.DaoMaster;
import com.comvee.greendao.gen.DepartmentModelDao;
import com.comvee.greendao.gen.MemberHistoryBeanDao;
import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.greendao.gen.NewBloodItemDao;
import com.comvee.greendao.gen.ParamLogListBeanDao;
import com.comvee.greendao.gen.QualityBeanDao;
import com.comvee.greendao.gen.QualityResultBeanDao;
import com.comvee.greendao.gen.RecordBeanDao;
import com.comvee.greendao.gen.RefreshHistoryModelDao;
import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.greendao.gen.TestPaperModelDao;

import org.greenrobot.greendao.database.Database;

public class MyDaoMaster extends DaoMaster.OpenHelper {
    private static final String TAG = "MyDaoMaster";
    public MyDaoMaster(Context context, String name) {
        super(context, name);
    }

    public MyDaoMaster(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, BloodRangeBeanDao.class, DepartmentModelDao.class, MemberHistoryBeanDao.class, MemberModelDao.class,
                NewBloodItemDao.class, ParamLogListBeanDao.class, QualityBeanDao.class, QualityResultBeanDao.class,
                RecordBeanDao.class, RefreshHistoryModelDao.class, TestInfoDao.class, TestPaperModelDao.class);
        Log.e(TAG, "onUpgrade: " + oldVersion + " newVersion = " + newVersion);
    }
}