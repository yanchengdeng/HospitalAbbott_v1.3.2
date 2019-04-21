package com.comvee.hospitalabbott.ui.record;

import android.content.Intent;
import android.text.TextUtils;

import com.comvee.greendao.gen.RecordBeanDao;
import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.RecordBean;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.bean.TimeModel;
import com.comvee.hospitalabbott.helper.RangeHelper;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.network.rxbus.BloodUpdateRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.ActivityCommunicationUtil;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.helper.RecordHelper;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.ui.main.MainActivity;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static java.security.AccessController.getContext;

/**
 * Created by F011512088 on 2018/1/29.
 */
public class ManualRecordPresenter extends BasePresenter<ManualRecordViewController> {

    public static final String SEARCH_BEAN = "SearchBean";

    private SearchBean searchBean;
    private BloodRangeBean rangeBean;

    private String timeParamCode;
    private String timeStr;

    private float bloodValues = 0;
    private String highStr = "10";
    private String lowStr = "4";
    //血糖值等级 默认正常=3  高>3 低<3
    private int level = 3;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public ManualRecordPresenter(ManualRecordViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {


    }

    public int getParamCodePosition() {
        //默认选择时间段
        timeParamCode = TestResultDataUtil.toTimeSlot(new Date());
        for (int i = 0; i < TestResultDataUtil.getTimeStr().size(); i++) {
            if (timeParamCode.equals(TestResultDataUtil.getTimeStr().get(i))) {
                return i;
            }
        }
        return 0;
    }

    public void setSearchBean(Intent intent) {
        searchBean = intent == null ? null : (SearchBean) intent.getSerializableExtra(SEARCH_BEAN);
    }

    public void setTimeModel(TimeModel timeModel) {
        if (!timeParamCode.equals(timeModel.getTimeParamCode())) {
            timeParamCode = timeModel.getTimeParamCode();
            updateParamCode();
        }
    }

    public void updateParamCode() {
        if (rangeBean != null) {
            String[] lowAndHigh = TestResultDataUtil.getLowAndHigh(timeParamCode, rangeBean);
            lowStr = lowAndHigh[0];
            highStr = lowAndHigh[1];
        } else {
            lowStr = "4.0";
            highStr = "10.0";
        }

        viewModel.upDataReference(String.format(viewModel.getActivity().getString(R.string.reference_value),
                lowStr, highStr));

        updateLevel();
    }

    public void updateLevel() {
        //正常 0: 高 1: 低 -1
        int type = TestResultDataUtil.getBloodType(bloodValues, highStr, lowStr);
        switch (type) {
            case -1:
                level = 1;
                break;
            case 0:
                level = 3;
                break;
            case 1:
                level = 5;
                break;
        }

        viewModel.upDataCircleProgress(bloodValues, type);
    }

    /**
     * 保存血糖
     */
    public void postRecord(String remarksStr, String dateStr) {
        if (searchBean == null || TextUtils.isEmpty(searchBean.getMemberId())) {
            showToast("患者信息获取失败，请重新选择患者！");
            return;
        }
        if (TextUtils.isEmpty(timeParamCode)) {
            showToast("请选择血糖时间点");
            return;
        }
        if (rangeBean == null) {
            showToast("获取血糖值数据失败.");
            return;
        }

        if (bloodValues == 0) {
            showToast("请添加患者血糖值！");
            return;
        }
        //手动记录只能记录当前时间未来时间段
//        if (paramCodePosition > maxParamCode) {
//            ToastUtil.showToast(getContext(), "不能记录未来时间段！");
//            return;
//        }
        timeStr = dateStr + " " + DateUtil.getCurDateStr("HH:mm:ss");
        String postParamCode = TestResultDataUtil.getParamCode(timeParamCode);
        TestInfo testInfo = new TestInfo(UserHelper.getUserId(),
                bloodValues + "",
                postParamCode,
                timeStr,
                searchBean.getMemberId(),
                1, remarksStr, false);

        //greendao数据库
        TestInfoDao testInfoDao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
        testInfoDao.insert(testInfo);
        /**
         * 需要添加网络判断 进行本地保存还是上交服务器
         */
        if (AppUtils.isNetworkConnected(viewModel.getActivity())) {
            postServer(testInfo, dateStr);
        } else {
            RecordHelper.recordBloodSugar(testInfo, dateStr, level, true);
        }
        //手动记录保存本地时间值
        setRecordHistoryData(dateStr);
        //返回血糖数据
        ActivityCommunicationUtil.getSingle().getCallback(MainActivity.class.getName()).onCall(testInfo);

        viewModel.getActivity().finish();
    }

    private void postServer(final TestInfo testInfo, String dateStr) {
        RecordHelper.recordBloodSugar(testInfo, dateStr, level, false);
        RxBus.getDefault().post(new BloodUpdateRxModel()); //通知BloodUpdateService
       /* //手动记录
        ComveeLoader.getInstance().addMemberParamLog(testInfo.getUserId(), testInfo.getValue(),
                testInfo.getParamCode(),
                testInfo.getRecordTime(),  //recordTime,
                testInfo.getMemberId(),
                testInfo.getRemark(),
                testInfo.getStateInte() + "",
                testInfo.isManual());*/
    }

    /**
     * 添加手动上传数据添加时间值到本地
     */
    private void setRecordHistoryData(String dayStra) {
        RecordBeanDao recordBeanDao = XTYApplication.getInstance().getDaoSession().getRecordBeanDao();
        List<RecordBean> mRecordList = recordBeanDao.queryBuilder().where(RecordBeanDao.Properties.Time.eq(dayStra)).list();
        if (mRecordList.size() == 0 || mRecordList.isEmpty()) {
            RecordBean recordBean = new RecordBean();
            recordBean.setTime(dayStra);
            recordBean.setMemberId(searchBean.getMemberId());
            recordBeanDao.insert(recordBean);
        }
    }

    public SearchBean getSearchBean() {
        return searchBean;
    }

    private void showToast(String toastStr) {
        ToastUtil.showToast(viewModel.getActivity(), toastStr);
    }

    public void getRangeBean(final String memberId) {
        //获取该用户的血糖范围值
        ComveeLoader.getInstance().getMemberRange(memberId)
                .subscribe(new HttpCall<BloodRangeBean>(viewModel.getActivity()) {
                    @Override
                    public void onNext(BloodRangeBean rangeBean) {
                        RangeHelper.setBloodRangeBean(memberId, rangeBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
        //显示使用本地血糖范围数据
        Observable.create(new ObservableOnSubscribe<BloodRangeBean>() {
            @Override
            public void subscribe(ObservableEmitter<BloodRangeBean> e) throws Exception {
                e.onNext(RangeHelper.getBloodRangeBean(memberId));
            }
        }).compose(RxJavaUtil.<BloodRangeBean>applySchedulers())
                .subscribe(new Consumer<BloodRangeBean>() {
                    @Override
                    public void accept(@NonNull BloodRangeBean bloodRangeBean) throws Exception {
                        if (getContext() != null) {
                            rangeBean = bloodRangeBean;
                            updateParamCode();
                        }
                    }
                });
    }

    public void setBlood(String dateDesc) {
        try {
            bloodValues = Float.valueOf(dateDesc);
            updateLevel();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
