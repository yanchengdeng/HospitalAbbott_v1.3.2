package com.comvee.hospitalabbott.helper;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.greendao.gen.DepartmentModelDao;
import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.Branch;
import com.comvee.hospitalabbott.bean.DepartmentModel;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.bean.MemberModel;
import com.comvee.hospitalabbott.bean.ParamCodeBean;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by F011512088 on 2018/1/3.
 */

public class BedHelper {

    /**
     * 添加科室+病床本地数据
     *
     * @param jsonObject
     * @throws JSONException
     */
    public String setLocalRowsBeanJSONObject(JSONObject jsonObject, String isAll) throws JSONException {//synchronized
        JSONArray jsonDepartmentArray = jsonObject.optJSONArray("departmentList");
        if (jsonDepartmentArray.length() == 0) return isAll;
        DepartmentModelDao departmentBeanDao = XTYApplication.getInstance().getDaoSession().getDepartmentModelDao();
        for (int i = 0; i < jsonDepartmentArray.length(); i++) {
            JSONObject departmentJSON = jsonDepartmentArray.optJSONObject(i);
            DepartmentModel departmentBean = new DepartmentModel();
            departmentBean.setDepartmentId(departmentJSON.optString("departmentId"));
            departmentBean.setDepartmentName(departmentJSON.optString("departmentName"));
            //查询是否存在 存在则进行替换
            DepartmentModel oldDepartmentModel = departmentBeanDao.queryBuilder().where(DepartmentModelDao.Properties.DepartmentId.eq(departmentBean.getDepartmentId())).unique();
            if (oldDepartmentModel != null) {
                departmentBean.setId(oldDepartmentModel.getId());
                departmentBeanDao.save(departmentBean);
            } else {
                departmentBeanDao.saveInTx(departmentBean);
            }
        }
        try {
            upDataMember(jsonObject.optJSONArray("rows"), isAll);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAll;
    }

    /**
     * 新增、更新患者床位信息
     *
     * @param jsonRowsArray
     * @param isAll
     * @return
     */
    public static synchronized String upDataMember(JSONArray jsonRowsArray, String isAll) {
        if (jsonRowsArray == null || jsonRowsArray.length() == 0) return isAll;
        MemberModelDao bedModelDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
        for (int i = 0; i < jsonRowsArray.length(); i++) {
            JSONObject department = jsonRowsArray.optJSONObject(i);
            MemberModel bedModel = new MemberModel();
            bedModel.setBedId(department.optString("bi"));
            bedModel.setBedNo(department.optString("bn"));
            bedModel.setConcernStatus(department.optString("cs"));
            bedModel.setDepartmentId(department.optString("di"));
            bedModel.setDepartmentName(department.optString("dn"));
            bedModel.setMemberId(department.optString("mi"));
            bedModel.setMemberName(department.optString("mn"));
            bedModel.setSmbgScheme(department.optString("ss").equals("null") ? "" : department.optString("ss"));
            bedModel.setSex(department.optString("sex"));
            bedModel.setPatPatientId(department.optString("patPatientId"));
            bedModel.setPlanType(department.optInt("planType", 0)); // 监测类型    1 长期  2临时 0 没有
            bedModel.setBtTxt(department.optString("btTxt"));
            bedModel.setDiabetesTxt(department.optString("diabetesTxt"));


//            Log.d("upDataMember :", "planType = " + department.optInt("planType", 0));
            JSONObject newSugarMap = department.optJSONObject("newSugarMap");
//                    LogUtils.e("memberName [" + department.optString("memberName") + "], newSugarMap[" + department.optString("newSugarMap") + "]");
            String paramCodeBean = new Gson().toJson(new ParamCodeBean(), ParamCodeBean.class);
            String ab = newSugarMap.optString("ab");
            String ad = newSugarMap.optString("ad");
            String al = newSugarMap.optString("al");
            String bb = newSugarMap.optString("bb");
            String bd = newSugarMap.optString("bd");
            String beforedawn = newSugarMap.optString("beforedawn");
            String threeeclock = newSugarMap.optString("3am");
            String bl = newSugarMap.optString("bl");
            String bs = newSugarMap.optString("bs");
            String randomtime = newSugarMap.optString("randomtime");
            if (newSugarMap != null) {
                bedModel.setAfterBreakfast(ab.equals("") ? paramCodeBean : ab);
                bedModel.setAfterDinner(ad.equals("") ? paramCodeBean : ad);
                bedModel.setAfterLunch(al.equals("") ? paramCodeBean : al);
                bedModel.setBeforeBreakfast(bb.equals("") ? paramCodeBean : bb);
                bedModel.setBeforeDinner(bd.equals("") ? paramCodeBean : bd);
                bedModel.setBeforedawn(beforedawn.equals("") ? paramCodeBean : beforedawn);
                bedModel.setThreeclock(threeeclock.equals("")?paramCodeBean:threeeclock);
                bedModel.setBeforeLunch(bl.equals("") ? paramCodeBean : bl);
                bedModel.setBeforeSleep(bs.equals("") ? paramCodeBean : bs);
                bedModel.setRandomtime(randomtime.equals("") ? paramCodeBean : randomtime);
            } else {
                bedModel.setAfterBreakfast(paramCodeBean);
                bedModel.setAfterDinner(paramCodeBean);
                bedModel.setAfterLunch(paramCodeBean);
                bedModel.setBeforeBreakfast(paramCodeBean);
                bedModel.setBeforeDinner(paramCodeBean);
                bedModel.setBeforeLunch(paramCodeBean);
                bedModel.setBeforeSleep(paramCodeBean);
                bedModel.setBeforedawn(paramCodeBean);
                bedModel.setThreeclock(paramCodeBean);
                bedModel.setRandomtime(paramCodeBean);
            }

            //查询是否存在 存在则进行替换
            MemberModel oldBedModel = bedModelDao.queryBuilder()
                    .where(MemberModelDao.Properties.MemberId.eq(bedModel.getMemberId())).unique();
            if (oldBedModel != null) {
                bedModelDao.deleteByKey(oldBedModel.getId());
            }
            bedModelDao.insertInTx(bedModel);
        }
        return isAll;
    }

    /**
     * 获取本地科室+病床信息
     *
     * @param paramCode 时间段 不为空则为今日任务界面调用,否则为病床界面调用
     * @param status    筛选条件: 全部、关注、已监测、未监测、拒绝监测
     * @return
     */
    public Observable<List<MultiItemEntity>> getBedList(final String paramCode, final int status) {
        return Observable.create(new ObservableOnSubscribe<List<MultiItemEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MultiItemEntity>> e) throws Exception {
                e.onNext(localBedList(paramCode, status));
                e.onComplete();
            }
        }).compose(RxJavaUtil.<List<MultiItemEntity>>toComputation());
    }

    private List<MultiItemEntity> localBedList(String code, int status) {
        synchronized (BedHelper.this) {
            final String paramCode = code;
            final int concernStatus = status;
            DepartmentModelDao departmentDao = XTYApplication.getInstance().getDaoSession().getDepartmentModelDao();
            final List<DepartmentModel> departmentList = departmentDao.queryBuilder().build()
                    .forCurrentThread()
                    .list();
            String dateStr = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);//当前日期 yyyy-MM-dd
            ArrayList<MultiItemEntity> res = new ArrayList<>();

            MemberCountBean countBean = new MemberCountBean();
            Map<String, Integer> countMap = initCountMap();
            for (DepartmentModel model : departmentList) {
                Branch branch = new Branch(model.getDepartmentId(), model.getDepartmentName());
                MemberModelDao memberDao;
                memberDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
                final List<MemberModel> memberModels = memberDao.queryBuilder()
                        .where(MemberModelDao.Properties.DepartmentId.eq(model.getDepartmentId()))
                        .orderAsc(MemberModelDao.Properties.BedNo)
                        .list();

                for (MemberModel memberMode : memberModels) {
                    HospitalBed hospitalBed = new HospitalBed(memberMode);
                    ParamCodeBean paramCodeModel;
                    if (TextUtils.isEmpty(paramCode)) {
                        paramCodeModel = nowParamCode(memberMode);
                    } else {
                        String paramCodeJson = filterParamBean(paramCode, memberMode);
                        if (TextUtils.isEmpty(paramCodeJson)) {
                            paramCodeModel = new ParamCodeBean();
                        } else {
                            paramCodeModel = new Gson().fromJson(paramCodeJson, ParamCodeBean.class);
                        }
                        //判断记录时间日期是否等于今天日期
                        if (paramCodeModel != null &&
                                !TextUtils.isEmpty(paramCodeModel.getRecordTime())) {
                            SimpleDateFormat df = new SimpleDateFormat(DateUtil.FORMAT_YMD);
                            Date date = DateUtil.parse(paramCodeModel.getRecordTime(), DateUtil.FORMAT_YMD);
                            String oldDate = df.format(date);
                            if (!oldDate.equals(dateStr))
                                paramCodeModel = new ParamCodeBean();
                        }
                    }

                    hospitalBed.setParamCodeModel(paramCodeModel);
                    switch (concernStatus) {
                        case 0:
                            if (!TextUtils.isEmpty(paramCode)) {
                                //今日任务筛选
                                if (isMonitoringBed(paramCodeModel.getValue(),
                                        hospitalBed.getSmbgScheme(),
                                        DateUtil.getWeekOfDate(new Date()),
                                        paramCode,
                                        hospitalBed.getPlanType())) {
                                    branch.addSubItem(hospitalBed);
                                }
                            } else {
                                branch.addSubItem(hospitalBed);
                            }
                            break;
                        case 1://关注
                            if (memberMode.getConcernStatus().equals("1")) {
                                branch.addSubItem(hospitalBed);
                            }
                            break;
                        case 2://关注+已检测
                            if (paramCodeModel.getStatus().equals("1") &&
                                    !TextUtils.isEmpty(paramCodeModel.getParamCode())) {
                                branch.addSubItem(hospitalBed);
                            }
                            break;
                        case 3://未监测
                            String value = paramCodeModel.getValue();
                            if (TextUtils.isEmpty(value)) { // &&!TextUtils.isEmpty(oldParamCode)
                                branch.addSubItem(hospitalBed);
                            }
                            break;
                        case 4://拒绝监测
                            if (paramCodeModel.getStatus().equals("2") && !TextUtils.isEmpty(paramCodeModel.getParamCode())) {
                                branch.addSubItem(hospitalBed);
                            }
                            break;
                    }
                }
                if (branch.hasSubItem()) {
                    res.add(branch);
                }
            }
            countBean.setCountList(new MemberCountBean.CountListBean(countMap));
            countBean.setTotal(getCountTotal(countMap));
            return res;
        }
    }

    /**
     * 根据科室筛选
     * @param departmentId
     * @param status
     * @return
     */
    public Observable<List<HospitalBed>> getBedListByDepartmentId(final String departmentId, final int status) {
        return Observable.create(new ObservableOnSubscribe<List<HospitalBed>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HospitalBed>> e) throws Exception {
                e.onNext(localDepartmentBedList(departmentId, status));
                e.onComplete();
            }
        }).compose(RxJavaUtil.<List<HospitalBed>>toComputation());
    }

    private List<HospitalBed> localDepartmentBedList(String id, int status) {
        synchronized (BedHelper.this) {
            final int concernStatus = status;
            String paramCode = "";
            DepartmentModelDao departmentDao = XTYApplication.getInstance().getDaoSession().getDepartmentModelDao();
            DepartmentModel model = departmentDao.queryBuilder().where(DepartmentModelDao.Properties.DepartmentId.eq(id)).unique();
            ArrayList<HospitalBed> res = new ArrayList<>();

            MemberCountBean countBean = new MemberCountBean();
            Map<String, Integer> countMap = initCountMap();

            MemberModelDao memberDao;
            memberDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
            final List<MemberModel> memberModels = memberDao.queryBuilder()
                    .where(MemberModelDao.Properties.DepartmentId.eq(model.getDepartmentId()))
                    .orderAsc(MemberModelDao.Properties.BedNoNew)
                    .list();

            for (MemberModel memberMode : memberModels) {
                HospitalBed hospitalBed = new HospitalBed(memberMode);
                ParamCodeBean paramCodeModel = nowParamCode(memberMode);
                hospitalBed.setParamCodeModel(paramCodeModel);
                switch (concernStatus) {
                    case 0:
                        if (!TextUtils.isEmpty(paramCode)) {
                            //今日任务筛选
                            if (isMonitoringBed(paramCodeModel.getValue(),
                                    hospitalBed.getSmbgScheme(),
                                    DateUtil.getWeekOfDate(new Date()),
                                    paramCode,
                                    hospitalBed.getPlanType())) {
                                res.add(hospitalBed);
                            }
                        } else {
                            res.add(hospitalBed);
                        }
                        break;
                    case 1://关注
                        if (memberMode.getConcernStatus().equals("1")) {
                            res.add(hospitalBed);
                        }
                        break;
                    case 2://关注+已检测
                        if (paramCodeModel.getStatus().equals("1") &&
                                !TextUtils.isEmpty(paramCodeModel.getParamCode())) {
                            res.add(hospitalBed);
                        }
                        break;
                    case 3://未监测
                        String value = paramCodeModel.getValue();
                        if (TextUtils.isEmpty(value)) { // &&!TextUtils.isEmpty(oldParamCode)
                            res.add(hospitalBed);
                        }
                        break;
                    case 4://拒绝监测
                        if (paramCodeModel.getStatus().equals("2") && !TextUtils.isEmpty(paramCodeModel.getParamCode())) {
                            res.add(hospitalBed);
                        }
                        break;
                }
            }

            countBean.setCountList(new MemberCountBean.CountListBean(countMap));
            countBean.setTotal(getCountTotal(countMap));
            return res;
        }
    }

    /**
     * 获取所有科室
     * @return
     */
    public static List<DepartmentModel> getDepartmentList(){
        DepartmentModelDao departmentDao = XTYApplication.getInstance().getDaoSession().getDepartmentModelDao();
        return departmentDao.queryBuilder().build().forCurrentThread().list();
    }


    public static Map<String, Integer> getCountBean(@NonNull MemberModel bedMode) {
        Map<String, Integer> countMap = initCountMap();
        String smbgScheme = bedMode.getSmbgScheme();
        int planType = bedMode.getPlanType();
        boolean isMonitor = false;
        switch (planType) {
            case 2://20180117-0;20180117-1
                String ymdStr = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
                for (String paramCode : TestResultDataUtil.getPeriod()) {
                    isMonitor = isMonitorDay(smbgScheme, ymdStr, paramCode);
                    if (isMonitor) {//需要监测
                        String paramCodeBean = filterParamBean(paramCode, bedMode);
                        countSms(countMap, paramCode, paramCodeBean);
                    }
                }
                break;
            case 1://1-0;1-1;1-2...
                int weekDay = DateUtil.getWeekOfDate(new Date());
                for (String paramCode : TestResultDataUtil.getPeriod()) {
                    isMonitor = isMonitorWeek(smbgScheme, weekDay, paramCode);
                    if (isMonitor) {//需要监测
                        String paramCodeBean = filterParamBean(paramCode, bedMode);
                        countSms(countMap, paramCode, paramCodeBean);
                    }
                }
                break;
        }
        return countMap;
    }

    /**
     * 先判断这个时间段是否有血糖值，有就显示，没有再进行周监测方案判断.
     * 周监测方案 然后获取当天周几以及时间段 判断是否显示(病床界面获取全面数据)
     * (随机、有血糖值或者没有监测方案都显示)
     *
     * @param smbgScheme
     * @param weekDay    "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" 0~6
     * @param paramCode
     * @param planType   监测类型    1 长期  2临时 0 没有
     * @return
     */
    private boolean isMonitoringBed(String value, String smbgScheme, int weekDay, String paramCode,
                                    int planType) {
        if (paramCode.equals(TestResultDataUtil.PARAM_CODE_RANDOMTIME) || !TextUtils.isEmpty(value)) {
            //随机、有血糖值或者没有监测方案都显示 || TextUtils.isEmpty(smbgScheme)
            return true;
        }
        if (TextUtils.isEmpty(smbgScheme))
            return false;
        boolean isMonitor = false;
        switch (planType) {
            case 2://20180117-0;20180117-1
                isMonitor = isMonitorDay(smbgScheme, DateUtil.getCurDateStr(DateUtil.FORMAT_YMD), paramCode);
                break;
            case 1://1-0;1-1;1-2...
                isMonitor = isMonitorWeek(smbgScheme, weekDay, paramCode);
                break;
            default:
                isMonitor = false;
        }
        return isMonitor;
    }

    /**
     * @param smbgScheme  20180117-0;20180117-1;20180117-2
     * @param yearDateStr 2018-01-17
     * @param paramCode
     * @return
     */
    public static boolean isMonitorDay(String smbgScheme, String yearDateStr, String paramCode) {
        int paramCodePosition = TestResultDataUtil.getParamCodePosition(paramCode);
        if (paramCodePosition == -1) return false;
        yearDateStr = yearDateStr.replace("-", "");
        if (smbgScheme.contains(";")) {
            String[] date = smbgScheme.split(";");
            for (int i = 0; i < date.length; i++) {
                String[] times = date[i].split("-");
                int timeSection = Integer.parseInt(times[1]);
                if (times[0].equals(yearDateStr) &&
                        timeSection == paramCodePosition) {//日期且时间段相等
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMonitorWeek(String smbgScheme, int weekDay, String paramCode) {
        boolean[][] allId = new boolean[7][9];//第一个标识周几 。第二个标识时段
        //1-0 凌晨 1-1 3am时辰 1-2 空腹 1-3 早晨后 1-4 午餐前 1-5 午餐后 1-6 晚餐前 1-7 晚餐后 1-8 睡前 1-9 随机....
//            LogUtils.e("smbgScheme =" + smbgScheme);
        if (smbgScheme.contains(";")) {
            String[] date = smbgScheme.split(";");
            for (int i = 0; i < date.length; i++) {
                String[] times = date[i].split("-");
                int day = Integer.parseInt(times[0]);
                int timeSection = Integer.parseInt(times[1]);
                if (day < 8 && timeSection < 9) {
//                        LogUtils.e("day"+ (day -1)+", timeSection ="+timeSection);
                    allId[day - 1][timeSection] = true;
                }
            }
        }

        int paramCodePosition = TestResultDataUtil.getParamCodePosition(paramCode);
        if (paramCodePosition == -1) return false;
//        if (allId[weekDay][paramCodePosition])
//            LogUtils.e("smbgScheme =" + smbgScheme);
//        LogUtils.e("星期" + (weekDay + 1) + ", paramCodePosition =" + paramCodePosition +
//                ", is: " + (allId[weekDay][paramCodePosition]));
        return allId[weekDay][paramCodePosition];
    }

    /**
     * 按时间段返回最新记录血糖数据，只取当天血糖记录
     * 睡觉前-> 晚餐后->  晚餐前-> 午餐后-> 午餐前-> 早餐后-> 空腹-> 凌晨-> 随机
     *
     * @param bedModel
     * @return
     */
    public static ParamCodeBean nowParamCode(MemberModel bedModel) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DateUtil.FORMAT_YMD);
            String dateStr = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);//当前日期 yyyy-MM-dd
            Gson gson = new Gson();
            ParamCodeBean paramCode = null;
            if (!TextUtils.isEmpty(bedModel.getBeforeSleep())) {
                paramCode = gson.fromJson(bedModel.getBeforeSleep(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getAfterDinner())) {
                paramCode = gson.fromJson(bedModel.getAfterDinner(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getBeforeDinner())) {
                paramCode = gson.fromJson(bedModel.getBeforeDinner(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getAfterLunch())) {
                paramCode = gson.fromJson(bedModel.getAfterLunch(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getBeforeLunch())) {
                paramCode = gson.fromJson(bedModel.getBeforeLunch(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getAfterBreakfast())) {
                paramCode = gson.fromJson(bedModel.getAfterBreakfast(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getBeforeBreakfast())) {
                paramCode = gson.fromJson(bedModel.getBeforeBreakfast(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getBeforedawn())) {
                paramCode = gson.fromJson(bedModel.getBeforedawn(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

            if (!TextUtils.isEmpty(bedModel.getRandomtime())) {
                paramCode = gson.fromJson(bedModel.getRandomtime(), ParamCodeBean.class);
                if (!TextUtils.isEmpty(paramCode.getValue())) {
                    Date date = DateUtil.parse(paramCode.getRecordTime(), DateUtil.FORMAT_YMD);
                    String oldDate = df.format(date);
                    if (oldDate.equals(dateStr))
                        return paramCode;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ParamCodeBean();
    }

    /**
     * 根据对应时间段获取血糖记录数据
     *
     * @param paramCode
     * @param bedModel
     * @return
     */
    private static String filterParamBean(String paramCode, MemberModel bedModel) {
        if (paramCode.equals(TestResultDataUtil.PARAM_CODE_0AM)) {
            return bedModel.getBeforedawn();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_3AM)) {
            return bedModel.getThreeclock();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST)) {
            return bedModel.getBeforeBreakfast();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST)) {
            return bedModel.getAfterBreakfast();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFORELUNCH)) {
            return bedModel.getBeforeLunch();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERLUNCH)) {
            return bedModel.getAfterLunch();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFOREDINNER)) {
            return bedModel.getBeforeDinner();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERDINNER)) {
            return bedModel.getAfterDinner();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFORESLEEP)) {
            return bedModel.getBeforeSleep();
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_RANDOMTIME)) {
            return bedModel.getRandomtime();
        }
        return "";
    }

    private static Map<String, Integer> countSms(Map<String, Integer> countMap, String paramCode, String paramCodeBean) {
        if (paramCode.equals(TestResultDataUtil.PARAM_CODE_0AM)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_0AM);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_0AM, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_3AM)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_3AM);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_3AM, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFORELUNCH)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_BEFORELUNCH);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_BEFORELUNCH, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERLUNCH)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_AFTERLUNCH);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_AFTERLUNCH, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFOREDINNER)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_BEFOREDINNER);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_BEFOREDINNER, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_AFTERDINNER)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_AFTERDINNER);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_AFTERDINNER, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_BEFORESLEEP)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_BEFORESLEEP);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_BEFORESLEEP, countNumber);
            }
        } else if (paramCode.equals(TestResultDataUtil.PARAM_CODE_RANDOMTIME)) {
            int countNumber = countMap.get(TestResultDataUtil.PARAM_CODE_RANDOMTIME);
            if (TextUtils.isEmpty(paramCodeBean)) {
                countNumber++;
                countMap.put(TestResultDataUtil.PARAM_CODE_RANDOMTIME, countNumber);
            }
        }
        return countMap;
    }

    private static Map<String, Integer> initCountMap() {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put(TestResultDataUtil.PARAM_CODE_0AM, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_3AM, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_BEFORELUNCH, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_AFTERLUNCH, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_BEFOREDINNER, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_AFTERDINNER, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_BEFORESLEEP, 0);
        countMap.put(TestResultDataUtil.PARAM_CODE_RANDOMTIME, 0);
        return countMap;
    }

    private static int getCountTotal(Map<String, Integer> map) {
        int total = map.get(TestResultDataUtil.PARAM_CODE_0AM) +
                map.get(TestResultDataUtil.PARAM_CODE_3AM)+
                map.get(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST) +
                map.get(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST) +
                map.get(TestResultDataUtil.PARAM_CODE_BEFORELUNCH) +
                map.get(TestResultDataUtil.PARAM_CODE_AFTERLUNCH) +
                map.get(TestResultDataUtil.PARAM_CODE_BEFOREDINNER) +
                map.get(TestResultDataUtil.PARAM_CODE_AFTERDINNER) +
                map.get(TestResultDataUtil.PARAM_CODE_BEFORESLEEP) +
                map.get(TestResultDataUtil.PARAM_CODE_RANDOMTIME);
        return total;
    }

}
