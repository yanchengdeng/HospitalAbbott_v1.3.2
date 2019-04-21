package com.comvee.hospitalabbott.helper;

import com.comvee.greendao.MigrationHelper;
import com.comvee.greendao.gen.BloodRangeBeanDao;
import com.comvee.greendao.gen.DaoSession;
import com.comvee.greendao.gen.DepartmentModelDao;
import com.comvee.greendao.gen.MemberHistoryBeanDao;
import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.greendao.gen.ParamLogListBeanDao;
import com.comvee.greendao.gen.QualityBeanDao;
import com.comvee.greendao.gen.RefreshHistoryModelDao;
import com.comvee.greendao.gen.TestPaperModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.ui.download.DownloadPresenter;

import org.greenrobot.greendao.internal.DaoConfig;

/**
 * Created by F011512088 on 2018/1/25.
 */

public class OutHelper {

    public static void outLogin(){
        UserHelper.putBoolean(DownloadPresenter.APK_ISDOWNLOAD, false);
        UserHelper.outLogin();
        delectAllSQL();
    }
    /**
     * 删除除本地血糖记录表
     */
    private static void delectAllSQL() {
        DaoSession daoSession = XTYApplication.getInstance().getDaoSession();

        DaoConfig daoConfig = new DaoConfig(daoSession.getDatabase(), DepartmentModelDao.class);
        String tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getDepartmentModelDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), BloodRangeBeanDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getBloodRangeBeanDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), MemberHistoryBeanDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getMemberHistoryBeanDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), MemberModelDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getMemberModelDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), ParamLogListBeanDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getParamLogListBeanDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), QualityBeanDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getQualityBeanDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), RefreshHistoryModelDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getRefreshHistoryModelDao().deleteAll();

        daoConfig = new DaoConfig(daoSession.getDatabase(), TestPaperModelDao.class);
        tableName = daoConfig.tablename;
        if (MigrationHelper.checkTable(daoSession.getDatabase(), tableName))
            daoSession.getTestPaperModelDao().deleteAll();
    }

}
