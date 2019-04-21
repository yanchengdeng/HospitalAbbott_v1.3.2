package com.comvee.hospitalabbott.helper;

import android.text.TextUtils;

import com.comvee.greendao.gen.MemberHistoryBeanDao;
import com.comvee.greendao.gen.ParamLogListBeanDao;
import com.comvee.greendao.gen.RefreshHistoryModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.HistoryCountModel;
import com.comvee.hospitalabbott.bean.HistoryModel;
import com.comvee.hospitalabbott.bean.MemberHistoryBean;
import com.comvee.hospitalabbott.bean.MemberHistoryModel;
import com.comvee.hospitalabbott.bean.ParamLogListBean;
import com.comvee.hospitalabbott.bean.RefreshHistoryModel;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by F011512088 on 2018/1/11.
 */

public class HistoryHelper {

    /**
     * 添加本地血糖记录，添加刷新数据
     *
     * @param memberId
     * @param historyModel
     */
    public synchronized static void setLocalHistory(final String memberId, HistoryModel historyModel) {
//        LogUtils.e("resultModel == null --" + (historyModel == null));
        if (historyModel == null) return;
        String refreshTime = historyModel.getRefreshTime();
        if (TextUtils.isEmpty(refreshTime))//刷新时间获取失败，赋值为当前系统时间
            refreshTime = DateUtil.getCurDateStr();
//        LogUtils.e("MemberHistory == null --" + (historyModel.getMemberHistory() == null));
        if (historyModel.getMemberHistory() == null) return;
        MemberHistoryBeanDao historyDao = XTYApplication.getInstance().getDaoSession().getMemberHistoryBeanDao();
        ParamLogListBeanDao paramDao = XTYApplication.getInstance().getDaoSession().getParamLogListBeanDao();
        for (MemberHistoryBean historyBean : historyModel.getMemberHistory()) {
            MemberHistoryBean localHistoryBean = historyDao.queryBuilder()
                    .where(MemberHistoryBeanDao.Properties.MemberId.eq(memberId),
                            MemberHistoryBeanDao.Properties.Date.eq(historyBean.getDate()))
                    .unique();
            if (localHistoryBean == null) {
                historyBean.setMemberId(memberId);
                historyDao.insertInTx(historyBean);
            }
            for (ParamLogListBean paramBean : historyBean.getParamLogList()) {
                ParamLogListBean localParamBean = paramDao.queryBuilder()
                        .where(ParamLogListBeanDao.Properties.MemberId.eq(paramBean.getMemberId()),
                                ParamLogListBeanDao.Properties.RecordTime.eq(paramBean.getRecordTime()),
                                ParamLogListBeanDao.Properties.ParamCode.eq(paramBean.getParamCode()))
                        .unique();
                if (localParamBean != null) {
                    paramDao.delete(localParamBean);
                }
                paramBean.setRecordDt(historyBean.getDate());
                paramDao.insertInTx(paramBean);
            }

        }

        //更新本地改患者刷新时间
        setLocalRefreshHistory(memberId, refreshTime, true);
    }

    /**
     * 获取本地患者血糖记录历史、血糖统计值
     *
     * @param memberId
     * @return
     */
    public static HistoryCountModel getLocalHistory(final String memberId) {
        MemberHistoryBeanDao historyDao = XTYApplication.getInstance().getDaoSession().getMemberHistoryBeanDao();
        ParamLogListBeanDao paramDao = XTYApplication.getInstance().getDaoSession().getParamLogListBeanDao();

        HistoryCountModel historyCountModel = new HistoryCountModel();
        List<MemberHistoryModel> historyModelList = new ArrayList<>();
        final List<MemberHistoryBean> localHistoryList = historyDao.queryBuilder()
                .where(MemberHistoryBeanDao.Properties.MemberId.eq(memberId))
                .orderDesc(MemberHistoryBeanDao.Properties.Date)//降序 orderDesc 升序 orderAsc
                .list();
        if (localHistoryList != null) {
            for (MemberHistoryBean historyBean : localHistoryList) {
//                Log.d("MemberHistoryBean :", historyBean.toString());
                MemberHistoryModel historyModel = getMemberHistoryData(historyCountModel, historyBean, paramDao);
                historyModelList.add(historyModel);

            }
        }

        historyCountModel.setHistoryModels(historyModelList);
        return historyCountModel;
    }

    /**
     * 根据时间段获取相对应血糖数据历史并进行血糖数据统计
     *
     * @param historyCountModel
     * @param historyBean
     * @param paramDao
     * @return
     */
    private static MemberHistoryModel getMemberHistoryData(HistoryCountModel historyCountModel, MemberHistoryBean historyBean, ParamLogListBeanDao paramDao) {

        MemberHistoryModel historyModel = new MemberHistoryModel(historyBean.getMemberId(), historyBean.getDate());
        historyModel.setBeforeDawnList(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_0AM));
        historyCountModel.setCountMap(getCountLevel(historyModel.getBeforeDawnList()));

        historyModel.setThreeclockList(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_3AM));
        historyCountModel.setCountMap(getCountLevel(historyModel.getThreeclockList()));

        historyModel.setBeforeBreakfast(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST));
        historyCountModel.setCountMap(getCountLevel(historyModel.getBeforeBreakfast()));

        historyModel.setAfterBreakfast(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST));
        historyCountModel.setCountMap(getCountLevel(historyModel.getAfterBreakfast()));

        historyModel.setBeforeLunch(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_BEFORELUNCH));
        historyCountModel.setCountMap(getCountLevel(historyModel.getBeforeLunch()));

        historyModel.setAfterLunch(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_AFTERLUNCH));
        historyCountModel.setCountMap(getCountLevel(historyModel.getAfterLunch()));

        historyModel.setBeforeDinner(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_BEFOREDINNER));
        historyCountModel.setCountMap(getCountLevel(historyModel.getBeforeDinner()));

        historyModel.setAfterDinner(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_AFTERDINNER));
        historyCountModel.setCountMap(getCountLevel(historyModel.getAfterDinner()));

        historyModel.setBeforeSleep(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_BEFORESLEEP));
        historyCountModel.setCountMap(getCountLevel(historyModel.getBeforeSleep()));

        historyModel.setRandomtime(getParamLogListData(historyBean, paramDao, TestResultDataUtil.PARAM_CODE_RANDOMTIME));
        historyCountModel.setCountMap(getCountLevel(historyModel.getRandomtime()));

        return historyModel;
    }

    public static final String COUNT_NORMAL = "normal";
    public static final String COUNT_HIGH = "high";
    public static final String COUNT_LOW = "low";

    private static Map<String, Integer> getCountLevel(List<ParamLogListBean> list) {
        Map<String, Integer> map = new HashMap<>();
        int high = 0;
        int normal = 0;
        int low = 0;
        for (ParamLogListBean paramLogBean : list) {
            if (!paramLogBean.getStatus().equals("2")) {//不等于拒绝监测
                int level = Integer.valueOf(paramLogBean.getLevel());
                if (level == 3) {
                    normal++;
                } else if (level > 3) {
                    high++;
                } else if (level < 3) {
                    low++;
                }
            }
        }
        map.put(COUNT_NORMAL, normal);
        map.put(COUNT_HIGH, high);
        map.put(COUNT_LOW, low);
        return map;
    }

    private static List<ParamLogListBean> getParamLogListData(MemberHistoryBean historyBean, ParamLogListBeanDao paramDao, String paramCode) {
        return paramDao.queryBuilder()
                .where(ParamLogListBeanDao.Properties.MemberId.eq(historyBean.getMemberId()),
                        ParamLogListBeanDao.Properties.RecordDt.eq(historyBean.getDate()),
                        ParamLogListBeanDao.Properties.ParamCode.eq(paramCode))
                .orderDesc(ParamLogListBeanDao.Properties.RecordTime)
                .list();
    }

    /**
     * 更新本地该患者刷新时间
     *
     * @param memberId    患者id
     * @param refreshTime 刷新时间
     * @param isInit      是否初始化成功
     */
    public synchronized static void setLocalRefreshHistory(String memberId, String refreshTime, boolean isInit) {
        RefreshHistoryModelDao dao = XTYApplication.getInstance().getDaoSession().getRefreshHistoryModelDao();
        RefreshHistoryModel refreshModel = dao.queryBuilder()
                .where(RefreshHistoryModelDao.Properties.MemberId.eq(memberId))
                .build().unique();
        if (refreshModel != null) { //判断本地是否存在患者刷新信息，有删除。
            dao.delete(refreshModel);
        }
        dao.saveInTx(new RefreshHistoryModel(memberId, isInit, refreshTime));
    }

    /**
     * 判断本地MemberHistoryBean是否有存在本日期，无添加。
     * 判断本地ParamLogListBean集合是否存在患者这个的记录时间，有删除添加最新数据，无添加.
     *
     * @param testInfo
     * @param dateStr
     * @param level
     */
    public static void upDataHistoryMember(TestInfo testInfo, String dateStr, String level) {
        MemberHistoryBeanDao historyDao = XTYApplication.getInstance().getDaoSession().getMemberHistoryBeanDao();
        MemberHistoryBean localHistoryBean = historyDao.queryBuilder()
                .where(MemberHistoryBeanDao.Properties.MemberId.eq(testInfo.getMemberId()),
                        MemberHistoryBeanDao.Properties.Date.eq(dateStr))
                .unique();
        if (localHistoryBean == null) {
            MemberHistoryBean bean = new MemberHistoryBean();
            bean.setMemberId(testInfo.getMemberId());
            bean.setDate(dateStr);
            historyDao.insertInTx(bean);
        }

        ParamLogListBeanDao paramDao = XTYApplication.getInstance().getDaoSession().getParamLogListBeanDao();
        ParamLogListBean localParamBean = paramDao.queryBuilder()
                .where(ParamLogListBeanDao.Properties.MemberId.eq(testInfo.getMemberId()),
                        ParamLogListBeanDao.Properties.Value.eq(testInfo.getValue()),
                        ParamLogListBeanDao.Properties.RecordTime.eq(testInfo.getRecordTime()))
                .unique();
        if (localParamBean != null) {
            paramDao.delete(localParamBean);
        }
        ParamLogListBean bean = new ParamLogListBean(testInfo, dateStr, level);
        paramDao.insertInTx(bean);

    }

}
