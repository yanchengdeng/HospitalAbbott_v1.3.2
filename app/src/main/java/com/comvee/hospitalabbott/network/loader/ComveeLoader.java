package com.comvee.hospitalabbott.network.loader;

import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.greendao.gen.TestPaperModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.BloodSugarChatDynmicInfo;
import com.comvee.hospitalabbott.bean.BloodSugarChatInfo;
import com.comvee.hospitalabbott.bean.HistoryCountModel;
import com.comvee.hospitalabbott.bean.HistoryModel;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.LoginModel;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.bean.NewBloodItem;
import com.comvee.hospitalabbott.bean.RefreshBedModel;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.bean.TestPaperBean;
import com.comvee.hospitalabbott.bean.TestPaperModel;
import com.comvee.hospitalabbott.bean.UncheckinBean;
import com.comvee.hospitalabbott.bean.VersionBean;
import com.comvee.hospitalabbott.helper.BedHelper;
import com.comvee.hospitalabbott.helper.BedRefreshHelper;
import com.comvee.hospitalabbott.helper.HistoryHelper;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.BaseResponse;
import com.comvee.hospitalabbott.network.NetWorkManger;
import com.comvee.hospitalabbott.network.api.ComveeApi;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.config.HttpFaultException;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.widget.calendar.TimeUtil;
import com.google.gson.Gson;

import org.apache.commons.lang.text.StrBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by F011512088 on 2017/12/27.
 */

public class ComveeLoader extends ObjectLoader {

    public static final int ROWS = 200;
    private ComveeApi mComveeApi;
    private static ComveeLoader comveeLoader;

    /**
     * 血糖仪自动上传
     */
    public final static String RECORD_ORIGIN_XTY_AUTO = "XTY_ORIGIN";
    /**
     * 血糖仪手动录入
     */
    public final static String RECORD_ORIGIN_XTY_MANUAL = "XTY_ORIGIN_MANUAL";

    public static ComveeLoader getInstance() {
        if (comveeLoader == null)
            comveeLoader = new ComveeLoader();
        comveeLoader.setComveeApi(NetWorkManger.getInstance().create(ComveeApi.class));
        return comveeLoader;
    }

    public static void reset(){
        NetWorkManger.reset();
        comveeLoader = new ComveeLoader();
        comveeLoader.setComveeApi(NetWorkManger.getInstance().create(ComveeApi.class));
    }

    public void setComveeApi(ComveeApi mComveeApi) {
        this.mComveeApi = mComveeApi;
    }

    public ComveeLoader() {
        mComveeApi = NetWorkManger.getInstance().create(ComveeApi.class);
    }

    public Observable<LoginModel> login(String userNo, String pass) {
        Map<String, String> map = new HashMap<>();
        map.put("userNo", userNo);
        map.put("password", pass);
        return mComveeApi.login(map)
                .compose(ObjectLoader.<LoginModel>applySchedulersMap());
    }

    /**
     * 全部病床数据
     *
     * @param page
     * @param rows
     * @return
     */
    public Observable<Boolean> getBedListByPage(int page, final int rows) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("rows", rows + "");
        map.put("concernStatus", "");//关注状态 0-未关注 1-有关注 不传全部
        return mComveeApi.getBedListByPage(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<BaseResponse<Object>, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull BaseResponse<Object> objectBaseResponse) throws Exception {
                        if (!objectBaseResponse.getCode().equals("0")) {
                            throw new HttpFaultException(Integer.valueOf(objectBaseResponse.getCode()),
                                    objectBaseResponse.getMsg());
                        }
                        BedHelper helper = new BedHelper();
                        /**
                         * 判断是否最后一页，否则请求下一页数据
                         */
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(objectBaseResponse.getObj()));
                        UserHelper.putString(UserHelper.refreshBedTime,
                                jsonObject.optString("returnTime", DateUtil.getCurDateStr(DateUtil.FORMAT_YMDHMS)));
                        helper.setLocalRowsBeanJSONObject(jsonObject, "");
                        int currentPage = jsonObject.optInt("pageNum");
                        int total = jsonObject.optInt("totalPages");
                        if (currentPage < total) {
                            return false;
                        } else {
                            UserHelper.setInitBed(true);
                            return true;
                        }
                    }
                }).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 刷新接口
     *
     * @param refreshTime   刷新时间
     * @param page          页数
     * @param rows          行数
     * @param concernStatus 关注状态 0-未关注 1-有关注 不传全部
     * @param machineId     机器id
     */
    public Observable<Boolean> refreshBedList(String refreshTime, String concernStatus,
                                              String machineId, int page, int rows) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("rows", rows + "");
        refreshTime = TimeUtil.formatTimeToHours(refreshTime, "yyyy-MM-dd HH:mm:ss"); //减去二十分钟
        map.put("refreshTime", refreshTime);
        map.put("concernStatus", concernStatus);
        map.put("machineNo", machineId);
        final String finalRefreshTime = refreshTime;
        return mComveeApi.refreshBedList(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<BaseResponse<RefreshBedModel>, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull BaseResponse<RefreshBedModel> response) throws Exception {
                        if (!response.getCode().equals("0")) {
                            throw new HttpFaultException(Integer.valueOf(response.getCode()), response.getMsg());
                        }
                        RefreshBedModel refreshBedModel = response.getObj();
                        if (refreshBedModel != null) {
                            BedRefreshHelper.outLocalMember(refreshBedModel.getOutHospitalList(), finalRefreshTime);
                            String gson = new Gson().toJson(refreshBedModel);
                            JSONObject jsonObject = new JSONObject(gson);
//                            Log.d("refreshBedList", gson);
                            try {
                                JSONArray jsonRowsArray = jsonObject.getJSONArray("rows");
                                BedHelper.upDataMember(jsonRowsArray, "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BedRefreshHelper.upDataDepartment(refreshBedModel.getDepartmentList());
                            int currentPage = refreshBedModel.getPageNum();
                            int total = refreshBedModel.getTotalPages();
                            if (currentPage >= total) {
                                UserHelper.putString(UserHelper.refreshBedTime, refreshBedModel.getReturnTime());
                                return true;
                            }
                        } else {
                            UserHelper.putString(UserHelper.refreshBedTime, refreshBedModel.getReturnTime());
                            return true;
                        }
                        return false;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 添加用户血糖记录(自动记录)
     *
     * @param userId     用户id
     * @param value      血糖值
     * @param paramCode  血糖时间段code
     * @param recordTime 记录时间
     * @param memberId   成员id
     * @param remark     备注
     * @param status     1是正常 2是拒绝
     * @param isManual   XTY_ORIGIN  自动上传     XTY_ORIGIN_MANUAL 手动录入
     */
    public void addMemberParamLog(String userId, String value, String paramCode,
                                  String recordTime, String memberId,
                                  String remark, String status, boolean isManual) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("value", value);
        map.put("paramCode", paramCode);
        map.put("recordTime", recordTime);
        map.put("memberId", memberId);
        map.put("remark", remark);
        map.put("status", status);
        map.put("recordOrigin", isManual ? RECORD_ORIGIN_XTY_AUTO : RECORD_ORIGIN_XTY_MANUAL);

        Intent intent = new Intent();
        intent.putExtra("memberId", memberId);
        intent.putExtra("recordTime", recordTime);
        intent.putExtra("value", value);
        intent.putExtra("paramCode", paramCode);
        mComveeApi.addMemberParamLog(map)
                .compose(ObjectLoader.<String>applySchedulersMap())
                .subscribe(new HttpCall<String>(null, intent) {
                    @Override
                    public void onNext(String s) {
                        //greendao数据库
                        TestInfoDao testInfoDao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
                        List<TestInfo> oldTestList = testInfoDao.queryBuilder()
                                .where(TestInfoDao.Properties.MemberId.eq(getIntent().getStringExtra("memberId")),
                                        TestInfoDao.Properties.Value.eq(getIntent().getStringExtra("value")),
                                        TestInfoDao.Properties.ParamCode.eq(getIntent().getStringExtra("paramCode")),
                                        TestInfoDao.Properties.RecordTime.eq(getIntent().getStringExtra("recordTime")))
                                .build().list();
                        if (oldTestList.size() > 0)
                            testInfoDao.deleteInTx(oldTestList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                });
    }

    public Observable<String> addMemberParamLog(TestInfo testInfo) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", testInfo.getUserId());
        map.put("value", testInfo.getValue());
        map.put("paramCode", testInfo.getParamCode());
        map.put("recordTime", testInfo.getRecordTime());
        map.put("memberId", testInfo.getMemberId());
        map.put("remark", testInfo.getRemark());
        map.put("status", testInfo.getStateInte() + "");
        map.put("recordOrigin", testInfo.isManual() ? RECORD_ORIGIN_XTY_AUTO : RECORD_ORIGIN_XTY_MANUAL);
        return mComveeApi.addMemberParamLog(map)
                .compose(ObjectLoader.<String>applySchedulersMap());
    }


    /**
     * 批量添加用户血糖
     */

    public Observable<String> batchList(HospitalBed hospitalBed, List<NewBloodItem> databaseAll) {
//        NewBloodItemDao newBloodItemDao = XTYApplication.getInstance().getDaoSession().getNewBloodItemDao();
//        List<NewBloodItem> databaseAll = newBloodItemDao.loadAll();
        JSONArray jsonArray = new JSONArray();
        if (databaseAll!=null && databaseAll.size()>0){
            for (int i = 0;i< databaseAll.size();i++) {
                JSONObject item = new JSONObject();
                try {
                    item.put("batchId", databaseAll.get(i).getHistory_id());
                    item.put("value",  databaseAll.get(i).getSugar());
                    item.put("recordDt", getDate(databaseAll.get(i)));
                    item.put("recordTime", getDate(databaseAll.get(i))+getTime(databaseAll.get(i)));
                    item.put("paramType", "1");
                    item.put("remark","-----");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(item);
            }
        }



        Map<String, String> map = new HashMap<>();
        map.put("memberId", hospitalBed.getMemberId());
        map.put("paramLogArrayJson",jsonArray.toString());
        return mComveeApi.batchUploadMemberData(map)
                .compose(ObjectLoader.<String>applySchedulersMap());
    }

    private String getDate(NewBloodItem newBloodItem) {
        StrBuilder strBuilder = new StrBuilder();
        strBuilder.append("20"+newBloodItem.getYear())
                .append("-")
                .append(newBloodItem.getMouth()>9?newBloodItem.getMouth():"0"+newBloodItem.getMouth())
                .append("-")
                .append(newBloodItem.getDay()>9?newBloodItem.getDay():"0"+newBloodItem.getDay());

        return strBuilder.toString();
    }

    private String getTime(NewBloodItem newBloodItem){
        StrBuilder strBuilder = new StrBuilder();
        strBuilder.append(" ").append(newBloodItem.getHour()>9?newBloodItem.getHour():"0"+newBloodItem.getHour())
                .append(":")
                .append(newBloodItem.getMinus()>9?newBloodItem.getMinus():"0"+newBloodItem.getMinus())
                .append(":")
                .append("00");

        return strBuilder.toString();

    }

    /**
     * 获取用户血糖记录列表
     *
     * @param memberId     成员id
     * @param startDt      开始时间
     * @param endDt        结束时间
     * @param type 需要查询的血糖时间段code  传空字符串获取全部
     */
    public Observable<BloodSugarChatInfo> getBloodChartInfo(final String memberId, String startDt,
                                                           String endDt, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("startDt", startDt);
        map.put("endDt", endDt);
        map.put("type", type);//图表数据类型 1 血糖标准天图 (默认） 2 趋势图
        return mComveeApi.loadMemberBloodSugarChar(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<BaseResponse<BloodSugarChatInfo>, BloodSugarChatInfo>() {
                    @Override
                    public BloodSugarChatInfo apply(@NonNull BaseResponse<BloodSugarChatInfo> response) throws Exception {
//                        if (!response.isSuccess()) {
                        if (!response.getCode().equals("0")) {
                            throw new HttpFaultException(Integer.valueOf(response.getCode()), response.getMsg());
                        }
                        BloodSugarChatInfo historyModel = response.getObj();
                        LogUtils.w("dyc",historyModel);
//                        HistoryHelper.setLocalHistory(memberId, historyModel);
//                        HistoryCountModel countModel = HistoryHelper.getLocalHistory(memberId);
                        return historyModel;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户血糖记录列表
     *
     * @param memberId     成员id
     * @param startDt      开始时间
     * @param endDt        结束时间
     * @param type 需要查询的血糖时间段code  传空字符串获取全部
     */
    public Observable<BloodSugarChatDynmicInfo> getBloodChartInfoSuper(final String memberId, String startDt,
                                                                       String endDt, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("startDt", startDt);
        map.put("endDt", endDt);
        map.put("type", type);//图表数据类型 1 血糖标准天图 (默认） 2 趋势图
        return mComveeApi.loadMemberBloodSugarCharDyanmic(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<BaseResponse<BloodSugarChatDynmicInfo>, BloodSugarChatDynmicInfo>() {
                    @Override
                    public BloodSugarChatDynmicInfo apply(@NonNull BaseResponse<BloodSugarChatDynmicInfo> response) throws Exception {
//                        if (!response.isSuccess()) {
                        if (!response.getCode().equals("0")) {
                            throw new HttpFaultException(Integer.valueOf(response.getCode()), response.getMsg());
                        }
                        BloodSugarChatDynmicInfo historyModel = response.getObj();
                        LogUtils.w("dyc",historyModel);
//                        HistoryHelper.setLocalHistory(memberId, historyModel);
//                        HistoryCountModel countModel = HistoryHelper.getLocalHistory(memberId);
                        return historyModel;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    /**
     * 获取用户血糖记录列表
     *
     * @param memberId     成员id
     * @param startDt      开始时间
     * @param endDt        结束时间
     * @param paramCodeStr 需要查询的血糖时间段code  传空字符串获取全部
     */
    public Observable<HistoryCountModel> getMemberParamLog(final String memberId, String startDt,
                                                           String endDt, String paramCodeStr) {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("startDt", startDt);
        map.put("endDt", endDt);
        map.put("paramCodeStr", paramCodeStr);
        return mComveeApi.getMemberParamLog(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<BaseResponse<HistoryModel>, HistoryCountModel>() {
                    @Override
                    public HistoryCountModel apply(@NonNull BaseResponse<HistoryModel> response) throws Exception {
//                        if (!response.isSuccess()) {
                        if (!response.getCode().equals("0")) {
                            throw new HttpFaultException(Integer.valueOf(response.getCode()), response.getMsg());
                        }
                        HistoryModel historyModel = response.getObj();
                        HistoryHelper.setLocalHistory(memberId, historyModel);
                        HistoryCountModel countModel = HistoryHelper.getLocalHistory(memberId);
                        return countModel;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 刷新用户血糖记录列表
     *
     * @param refreshTime  刷新时间
     * @param memberId     用户id
     * @param paramCodeStr 记录时间code串, 传空刷新所有时段
     * @param status       记录状态  传空刷新全部 1是正常 2是拒绝
     */
    public Observable<HistoryModel> refreshParamLogList(String refreshTime, final String memberId,
                                                        String paramCodeStr, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("refreshTime", refreshTime);
        map.put("memberId", memberId);
        map.put("paramCodeStr", paramCodeStr);
        map.put("status", status);
        return mComveeApi.refreshParamLogList(map).subscribeOn(Schedulers.io())
                .map(new Function<BaseResponse<HistoryModel>, HistoryModel>() {
                    @Override
                    public HistoryModel apply(@NonNull BaseResponse<HistoryModel> response) throws Exception {
//                        if (!response.isSuccess()) {
                        if (!response.getCode().equals("0")) {
                            throw new HttpFaultException(Integer.valueOf(response.getCode()), response.getMsg());
                        }
                        HistoryModel historyModel = response.getObj();
                        return historyModel;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询患者信息
     *
     * @param departmentName 科室名称
     * @param memberName     患者名称
     * @param roomNo         房间号
     * @param bedNo          病床号
     */
    public Observable<List<SearchBean>> searchMemberInfo(String departmentName,
                                                         String memberName, String roomNo,
                                                         String bedNo) {
        Map<String, String> map = new HashMap<>();
        map.put("departmentName", departmentName);
        map.put("memberName", memberName);
        map.put("roomNo", roomNo);
        map.put("bedNo", bedNo);
        return mComveeApi.searchMemberInfo(map)
                .compose(ObjectLoader.<List<SearchBean>>applySchedulersMap());
    }

    /**
     * 获取最新版本号
     * <p>
     * 上传默认参数即可(版本号、设备号emei码)
     */
    public Observable<VersionBean> getMachineVersionModel() {
        Map<String, String> map = new HashMap<>();
        map.put("appType","3");
        return mComveeApi.getMachineVersionModel(map)
                .compose(ObjectLoader.<VersionBean>applySchedulersMap());
    }

    /**
     * 添加质控模式
     *
     * @param qcPaperNo     质控试纸编号
     * @param qcLiquidNo    质控液编号
     * @param qcLiquidLevel 质控液等级
     * @param value         测试数据
     * @param passStatus    通过状态 0-不通过 1-通过
     * @param qcLiquidLow
     * @param qcLiquidHigh
     * @param recordDt      记录时间
     * @param machineId     机器编号
     */
    public Observable<String> addQualityControlLog(String qcPaperNo,
                                                   String qcLiquidNo, String qcLiquidLevel, String value, String passStatus,
                                                   float qcLiquidLow, float qcLiquidHigh, String recordDt, String machineId) {
        Map<String, String> map = new HashMap<>();
        map.put("qcPaperNo", qcPaperNo);
        map.put("qcLiquidNo", qcLiquidNo);
        map.put("qcLiquidLevel", qcLiquidLevel);
        map.put("value", value);
        map.put("passStatus", passStatus);
        map.put("qcLiquidLow", qcLiquidLow + "");
        map.put("qcLiquidHigh", qcLiquidHigh + "");
        map.put("recordDt", recordDt);
        map.put("machineId", machineId);
        return mComveeApi.addQualityControlLog(map)
                .compose(ObjectLoader.<String>applySchedulersMap());
    }

    /**
     * 获取患者血糖控制目标
     *
     * @param memberId 患者id
     */
    public Observable<BloodRangeBean> getMemberRange(String memberId) {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        return mComveeApi.getMemberRange(map)
                .compose(ObjectLoader.<BloodRangeBean>applySchedulersMap());
    }

    /**
     * 获取质控等级
     */
    public Observable<String> getQualityControlLevel() {
        Map<String, String> map = new HashMap<>();
        return mComveeApi.getQualityControlLevel(map)
                .compose(ObjectLoader.<String>applySchedulersMap());
    }

    /**
     * 添加非住院患者血糖记录
     *
     * @param userId     用户id
     * @param recordTime 记录时间
     * @param value      血糖记录值
     * @param status     记录状态
     * @param remark     备注 1是正常 2是拒绝
     */
    public Observable<String> addUncheckinMemberParamLog(String userId, String recordTime,
                                                         String value, String status,
                                                         String remark) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("recordTime", recordTime);
        map.put("value", value);
        map.put("status", status);
        map.put("remark", remark);
        return mComveeApi.addUncheckinMemberParamLog(map)
                .compose(ObjectLoader.<String>applySchedulersMap());
    }

    /**
     * 获取非住院患者血糖记录
     * page   页码
     * ROWS   每页条数
     *
     * @return
     */
    public Observable<UncheckinBean> loadUncheckinMemberParamLogList(int page, int rows) {
        Map<String, String> map = new HashMap<>();
        map.put("page", "" + page);
        map.put("ROWS", "" + rows);
        return mComveeApi.loadUncheckinMemberParamLogList(map)
                .compose(ObjectLoader.<UncheckinBean>applySchedulersMap());
    }

    /**
     * 系统反馈
     * adviseType 建议类型
     * adviseText 建议内容
     */
    public Observable<String> addSystemAdviseLog(String adviseType,
                                                 String adviseText,
                                                 String contactWay) {
        Map<String, String> map = new HashMap<>();
        map.put("adviseType", adviseType);
        map.put("adviseText", adviseText);
        map.put("contactWay", contactWay);
        String userId = UserHelper.getUserId();
        map.put("userId", userId);
        return mComveeApi.addSystemAdviseLog(map)
                .compose(ObjectLoader.<String>applySchedulersMap());
    }

    /**
     * 今日任务未测人数
     */
    public Observable<MemberCountBean> getMemberSmbgCount() {
        Map<String, String> map = new HashMap<>();
        return mComveeApi.getMemberSmbgCount(map)
                .compose(ObjectLoader.<MemberCountBean>applySchedulersMap());
    }

    /**
     * 获取试纸列表
     * page      页数
     * "*ROWS     每页个数
     */
    public Observable<TestPaperBean> getTestpaperList(int page, int rows) {
        Map<String, String> map = new HashMap<>();
        map.put("page", "" + page);
        map.put("ROWS", "" + rows);
//        return mComveeApi.getTestpaperList(map)
//                .subscribeOn(Schedulers.io())
//                .map(new Function<BaseResponse<TestPaperBean>, TestPaperBean>() {
//                    @Override
//                    public TestPaperBean apply(@NonNull BaseResponse<TestPaperBean> tBaseResponse) throws Exception {
//                        if (!tBaseResponse.getCode().equals("0")) {
//                            throw new HttpFaultException(Integer.valueOf(tBaseResponse.getCode()), tBaseResponse.getMsg());
//                        }
//
//                        if (tBaseResponse.getObj() == null) {
//                            Type type = getClass().getGenericSuperclass();
//                            if (type instanceof ParameterizedType) {
//                                // 返回表示此类型实际类型参数的Type对象的数组.
//                                // 当有多个泛型类时，数组的长度就不是1了
//                                Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
//                                return (T) ptype[0];  //将第一个泛型T对应的类返回（这里只有一个）
//                            }
//                        }
//
//
//                        return tBaseResponse.getObj();
//                    }
//                })
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());

        return mComveeApi.getTestpaperList(map).compose(ObjectLoader.<TestPaperBean>applySchedulersMap())
                .doOnNext(new Consumer<TestPaperBean>() {
                    @Override
                    public void accept(@NonNull TestPaperBean testPaperBean) throws Exception {
                        TestPaperModelDao dao = XTYApplication.getInstance().getDaoSession().getTestPaperModelDao();
                        for (TestPaperBean.RowsBean rows : testPaperBean.getRows()) {
                            if (TextUtils.isEmpty(rows.getSid()))
                                continue;
                            TestPaperModel model = new TestPaperModel(rows);
                            List<TestPaperModel> oldModelList = dao.queryBuilder().where(TestPaperModelDao.Properties.Sid.eq(rows.getSid()))
                                    .build().list();
                            if (!oldModelList.isEmpty()) {
                                dao.deleteInTx(oldModelList);
                            }
                            dao.insertInTx(model);
                        }
                    }
                });
    }

}
