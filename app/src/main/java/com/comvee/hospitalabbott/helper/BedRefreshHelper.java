package com.comvee.hospitalabbott.helper;

import android.text.TextUtils;

import com.comvee.greendao.gen.DepartmentModelDao;
import com.comvee.greendao.gen.MemberHistoryBeanDao;
import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.DepartmentModel;
import com.comvee.hospitalabbott.bean.MemberHistoryBean;
import com.comvee.hospitalabbott.bean.MemberModel;
import com.comvee.hospitalabbott.bean.ParamCodeBean;
import com.comvee.hospitalabbott.bean.RefreshBedModel;
import com.comvee.hospitalabbott.bean.Rows;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.LocalLogTool;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by F011512088 on 2018/1/12.
 */

public class BedRefreshHelper {

    public synchronized static void outLocalMember(List<RefreshBedModel.OutHospitalListBean> outMemberList, String refreshTime) {
        StringBuilder builder = new StringBuilder();
        if (outMemberList == null || outMemberList.size() == 0) {
            builder.append("本次刷新时间：" + refreshTime);
            builder.append("\r\n");
            builder.append("未有出院数据。");
            builder.append("\r\n");
            builder.append("\r\n");
            LocalLogTool.saveLog(builder.toString());
            return;
        }
        MemberModelDao memberDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
        MemberHistoryBeanDao historyDao = XTYApplication.getInstance().getDaoSession().getMemberHistoryBeanDao();
        builder.append("本次刷新时间：" + refreshTime);
        builder.append("\r\n");
        builder.append("收到出院患者：");
        for (RefreshBedModel.OutHospitalListBean bean : outMemberList) {
            builder.append(bean.getMemberId() + "  ,  ");
            MemberModel memberModel = memberDao.queryBuilder()
                    .where(MemberModelDao.Properties.MemberId.eq(bean.getMemberId())).unique();
            if (memberModel != null) {
                memberDao.delete(memberModel);
            }

            List<MemberHistoryBean> historyList = historyDao.queryBuilder()
                    .where(MemberHistoryBeanDao.Properties.MemberId.eq(bean.getMemberId())).list();
            if (historyList != null && historyList.size() > 0) {
                historyDao.deleteInTx(historyList);
            }
        }
        builder.append("\r\n");
        builder.append("\r\n");
        LocalLogTool.saveLog(builder.toString());
    }

    public synchronized static void upDateMember(List<Rows> rows) {
        if (rows == null || rows.size() == 0) return;
        MemberModelDao bedModelDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
        for (Rows bean : rows) {
            MemberModel bedModel = new MemberModel(bean);
            //查询是否存在 存在则进行替换
            //查询是否存在 存在则进行替换
            MemberModel oldBedModel = bedModelDao.queryBuilder()
                    .where(MemberModelDao.Properties.MemberId.eq(bedModel.getMemberId())).unique();
            if (oldBedModel != null) {
                bedModel.setId(oldBedModel.getId());
                bedModelDao.saveInTx(bedModel);
            } else
                bedModelDao.insertInTx(bedModel);
        }
    }

    public static void upDataDepartment(List<RefreshBedModel.DepartmentListBean> list) {
        DepartmentModelDao dao = XTYApplication.getInstance().getDaoSession().getDepartmentModelDao();
        for (RefreshBedModel.DepartmentListBean departmentBean : list) {
            DepartmentModel oldModel = dao.queryBuilder()
                    .where(DepartmentModelDao.Properties.DepartmentId.eq(departmentBean.getDepartmentId())).unique();
            if (oldModel != null) {
                oldModel.setDepartmentName(departmentBean.getDepartmentName());
                dao.saveInTx(oldModel);
            } else {
                DepartmentModel model = new DepartmentModel();
                model.setDepartmentId(departmentBean.getDepartmentId());
                model.setDepartmentName(departmentBean.getDepartmentName());
                dao.insertInTx(model);
            }
        }
    }

    /**
     * 根据对应时间段更新血糖记录数据
     *
     * @param info
     * @param level
     * @param isNetWork
     */
    public static void upDataFilterParamBean(TestInfo info, int level, boolean isNetWork) {
        MemberModelDao dao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
        String paramCode = info.getParamCode();//时间段
        String recordTime = info.getRecordTime(); //记录时间
        MemberModel bedModel = dao.queryBuilder().where(MemberModelDao.Properties.MemberId.eq(info.getMemberId())).unique();
        if (bedModel == null) return;
        ParamCodeBean paramCodeBean = new ParamCodeBean(info);
        paramCodeBean.setNetWork(isNetWork);
        paramCodeBean.setLevel(level + "");
        String paramCodeStr = new Gson().toJson(paramCodeBean, ParamCodeBean.class);
        if (paramCode.equals(TestResultDataUtil.PARAM_CODE_0AM)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getBeforedawn(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setBeforedawn(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_3AM)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getThreeclock(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setThreeclock(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getBeforeBreakfast(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setBeforeBreakfast(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getAfterBreakfast(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setAfterBreakfast(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFORELUNCH)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getBeforeLunch(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setBeforeLunch(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERLUNCH)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getAfterLunch(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setAfterLunch(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFOREDINNER)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getBeforeDinner(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setBeforeDinner(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERDINNER)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getAfterDinner(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setAfterDinner(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFORESLEEP)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getBeforeSleep(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setBeforeSleep(paramCodeStr);
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_RANDOMTIME)) {
            ParamCodeBean localParamCodeBean = new Gson().fromJson(bedModel.getRandomtime(), ParamCodeBean.class);
            if (TextUtils.isEmpty(localParamCodeBean.getRecordTime()) || DateUtil.judgeIsUpdateNewBed(localParamCodeBean.getRecordTime(), recordTime))
                bedModel.setRandomtime(paramCodeStr);
        }
        dao.update(bedModel);
    }

    /**
     * 比较两个时间的大小,compareTo方法讲解:
     * 如果参数 Date 等于此 Date，则返回值 0；
     * 如果此 Date 在 Date 参数之前，则返回小于 0 的值；
     * 如果此 Date 在 Date 参数之后，则返回大于 0 的值。
     *
     * @param oldRecordTime
     * @param newRecordTime
     * @return
     */
    public static boolean judgeIsUpdate(String oldRecordTime, String newRecordTime) {
        if (TextUtils.isEmpty(oldRecordTime)) {
            return true;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime1 = dateFormat.parse(newRecordTime);
            Date dateTime2 = dateFormat.parse(oldRecordTime);
            int isSize = dateTime1.compareTo(dateTime2);
            if (isSize >= 0) {//根据位置来判断时间的先后
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
