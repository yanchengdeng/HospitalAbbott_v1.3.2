package com.comvee.hospitalabbott.helper;

import com.comvee.greendao.gen.BloodRangeBeanDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.BloodRangeBean;

import org.greenrobot.greendao.DaoException;

/**
 * Created by F011512088 on 2018/1/30.
 */

public class RangeHelper {

    /**
     * 设置血糖范围
     *
     * @param memberId
     * @param bloodRangeBean
     */
    public static void setBloodRangeBean(String memberId, BloodRangeBean bloodRangeBean) {
        BloodRangeBeanDao bloodRangeBeanDao = XTYApplication.getInstance().getDaoSession().getBloodRangeBeanDao();
        BloodRangeBean oldBloodRangeBean = getBloodRangeBean(memberId);
        if (oldBloodRangeBean != null)
            bloodRangeBean.setId(oldBloodRangeBean.getId());
        bloodRangeBeanDao.saveInTx(bloodRangeBean);
    }

    /**
     * 根据患者ID获取本地患者血糖范围
     *
     * @param memberId
     * @return
     */
    public static BloodRangeBean getBloodRangeBean(String memberId) {
        BloodRangeBeanDao bloodRangeBeanDao = XTYApplication.getInstance().getDaoSession().getBloodRangeBeanDao();
        try {
            BloodRangeBean bean = bloodRangeBeanDao.queryBuilder().where(BloodRangeBeanDao.Properties.MemberId.eq(memberId)).unique();
            return bean == null ? new BloodRangeBean() : bean;
        } catch (DaoException daoException) {
            return new BloodRangeBean();
        }
    }

}
