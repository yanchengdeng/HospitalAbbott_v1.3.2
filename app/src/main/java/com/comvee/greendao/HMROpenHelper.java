package com.comvee.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.comvee.greendao.gen.BloodRangeBeanDao;
import com.comvee.greendao.gen.DaoMaster;
import com.comvee.greendao.gen.DepartmentModelDao;
import com.comvee.greendao.gen.MemberHistoryBeanDao;
import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.greendao.gen.ParamLogListBeanDao;
import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.greendao.gen.TestPaperModelDao;

import org.greenrobot.greendao.database.Database;

public class HMROpenHelper extends DaoMaster.OpenHelper {

    public HMROpenHelper(Context context, String name, CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新
        MigrationHelper.migrate(db, BloodRangeBeanDao.class, MemberHistoryBeanDao.class,
                DepartmentModelDao.class, MemberModelDao.class, ParamLogListBeanDao.class, TestInfoDao.class,
                TestPaperModelDao.class);
    }

}