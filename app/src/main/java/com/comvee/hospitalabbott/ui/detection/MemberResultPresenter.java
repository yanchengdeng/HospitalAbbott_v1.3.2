package com.comvee.hospitalabbott.ui.detection;

import android.text.TextUtils;
import android.util.Log;

import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.BLEBloodModel;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.helper.RangeHelper;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.helper.RecordHelper;
import com.comvee.hospitalabbott.network.rxbus.BloodUpdateRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.network.service.BloodUpdateService;
import com.comvee.hospitalabbott.tool.ActivityCommunicationUtil;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.ui.main.MainActivity;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by F011512088 on 2018/2/27.
 */
public class MemberResultPresenter extends BasePresenter<MemberResultViewController> {

    //血糖值等级 默认正常=3  高>3 低<3
    private int level = 3;
    private String timeLien = "";
    private TestInfo testInfo;
    private BLEBloodModel bleBloodModel;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public MemberResultPresenter(MemberResultViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {
        timeLien = System.currentTimeMillis() + "";

    }

    public void initDataTestInfo(String selectedTime) {
        String nowParamCode = TextUtils.isEmpty(selectedTime) ? TestResultDataUtil.toTimeSlot(new Date()) : selectedTime;
        String postParamCode = TestResultDataUtil.getParamCode(nowParamCode);
        testInfo = new TestInfo(UserHelper.getUserId(),
                bleBloodModel.getValue() + "",
                postParamCode,
                bleBloodModel.getReadTime(),
                bleBloodModel.getMemberId(),
                1, viewModel.getRemarksStr(), timeLien);
        TestInfoDao testInfoDao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
        List<TestInfo> localList = testInfoDao.queryBuilder().where(TestInfoDao.Properties.MemberId.eq(testInfo.getMemberId()),
                TestInfoDao.Properties.RecordTime.eq(testInfo.getRecordTime()),
                TestInfoDao.Properties.Value.eq(testInfo.getValue())).build().list();
        if (!localList.isEmpty()) {
            testInfoDao.deleteInTx(localList);
        }
        testInfoDao.insertInTx(testInfo);
    }

    /**
     * 保存血糖值
     *
     * @param selectedTime
     */
    public void completeBlood(String selectedTime) {
        viewModel.showProgress();
        initDataTestInfo(selectedTime); //更新TestInfoDao库
        String dateStr = bleBloodModel.getReadTime().split(" ")[0];
        if (Utils.isNetwork(viewModel.getActivity())) {
            getSysTimeToPost(dateStr);
        } else {
            RecordHelper.recordBloodSugar(testInfo, dateStr, level, true); //更新病床库，跟历史记录库
        }
        //回传血糖数据给主界面
        ActivityCommunicationUtil.getSingle().getCallback(MainActivity.class.getName()).onCall(testInfo);
        ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.dismissProgress();
                viewModel.finishActivity();
            }
        },500);
    }

    /**
     * 保存血糖值
     *
     * @param selectedTime
     */
    public void completeNotResultPage(String selectedTime) {
        initDataTestInfo(selectedTime); //更新TestInfoDao库
        String dateStr = bleBloodModel.getReadTime().split(" ")[0];
        if (Utils.isNetwork(viewModel.getActivity())) {
            getSysTimeToPost(dateStr);
        } else {
            RecordHelper.recordBloodSugar(testInfo, dateStr, level, true); //更新病床库，跟历史记录库
        }
        //回传血糖数据给主界面
        ActivityCommunicationUtil.getSingle().getCallback(MainActivity.class.getName()).onCall(testInfo);
        viewModel.finishActivity();
    }

    private void getSysTimeToPost(String dateStr) {
        RecordHelper.recordBloodSugar(testInfo, dateStr, level, false);//更新病床库，跟历史记录库
        RxBus.getDefault().post(new BloodUpdateRxModel()); //通知BloodUpdateService
       /* ComveeLoader.getInstance().addMemberParamLog(testInfo.getUserId(), testInfo.getValue(),
                testInfo.getParamCode(),
                testInfo.getRecordTime(),
                testInfo.getMemberId(),
                testInfo.getRemark(),
                testInfo.getStateInte() + "",
                testInfo.isManual());*/
    }

    public Observable<BloodRangeBean> getRange() {
        //显示使用本地血糖范围数据
        return Observable.create(new ObservableOnSubscribe<BloodRangeBean>() {
            @Override
            public void subscribe(ObservableEmitter<BloodRangeBean> e) throws Exception {
                e.onNext(RangeHelper.getBloodRangeBean(bleBloodModel.getMemberId()));
                e.onComplete();
            }

        }).compose(RxJavaUtil.<BloodRangeBean>toComputation());
    }


    public float getValue() {
        if (bleBloodModel == null)
            return 0;
        return bleBloodModel.getValue();
    }

    public void setBleBloodModel(BLEBloodModel bleBloodModel) {
        this.bleBloodModel = bleBloodModel;
    }

    public BLEBloodModel getBleBloodModel() {
        return bleBloodModel;
    }

    public String getTitlerStr() {
        if (bleBloodModel == null)
            return "用户信息获取失败";
        return bleBloodModel.getTitleStr();
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
