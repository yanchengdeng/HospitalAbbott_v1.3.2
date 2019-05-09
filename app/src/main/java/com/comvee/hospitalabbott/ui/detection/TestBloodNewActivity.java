package com.comvee.hospitalabbott.ui.detection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.comvee.greendao.gen.NewBloodItemDao;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.adapter.BloodResultTimeAdapter;
import com.comvee.hospitalabbott.base.BaseFragmentActivity;
import com.comvee.hospitalabbott.bean.BLEBloodModel;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.NewBloodItem;
import com.comvee.hospitalabbott.bean.QualityModel;
import com.comvee.hospitalabbott.helper.RangeHelper;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.device.CH34xDriverUtil;
import com.comvee.hospitalabbott.widget.AlertDialog;
import com.comvee.hospitalabbott.widget.calendar.TimeUtil;
import com.comvee.hospitalabbott.widget.view.SugarCircleView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * @author dengyancheng
 * 血糖 新  ：批量获取血糖
 */
public class TestBloodNewActivity extends BaseFragmentActivity<TestBloodNewDeviceApiPresenter, TestBloodViewApiController>
        implements TestBloodViewApiController, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String MEMBER_BEAN = "memberBean";//患者模式
    public static final String QUALITY_BEAN = "qualityBean";//质控模式
    public static final String PARAM_CODE = "paramCode";
    @BindView(R.id.top_back)
    ImageView topBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.log)
    TextView logTv;
    @BindView(R.id.top_history)
    TextView topHistory;
    @BindView(R.id.test_title_bar)
    RelativeLayout testTitleBar;
    @BindView(R.id.error_iv)
    ImageView errorIv;
    @BindView(R.id.circle)
    SugarCircleView circle;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.blood_value)
    TextView bloodValue;
    @BindView(R.id.countDown)
    TextView countDownTime;
    @BindView(R.id.img_level)
    ImageView imgLevel;
    @BindView(R.id.rel_circle)
    RelativeLayout relCircle;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.reference_value)
    TextView referenceValue;
    @BindView(R.id.test_state_tv)
    TextView stateTv;
    @BindView(R.id.operating_btn)
    TextView operatingBtn;
    @BindView(R.id.rl_connect)
    RelativeLayout rlConnect;
    @BindView(R.id.ll_operating_btn)
    LinearLayout llOperatingBtn;
    private BLEBloodModel bleBloodModel;
    //返回的结果ASCII
    //  private StringBuffer stringBuffer = new StringBuffer();
    private StringBuffer hexBuffer = new StringBuffer();
    // private CountDownTimer countDownTimer;
    //当血糖仪正在读取数据的时候，不能返回
    private boolean readDoing = false;
    private BloodResultTimeAdapter bloodResultTimeAdapter;
    private BloodRangeBean rangeBean;
    private String lowStr;
    private String highStr;
    private int level;
    private CountDownTimer buttonCountDown;


    public static void startBloodNewActivity(Activity activity, HospitalBed bedModel, String paramCode) {
        Intent intent = new Intent(activity, TestBloodNewActivity.class);
        intent.putExtra(TestBloodNewActivity.MEMBER_BEAN, bedModel);
        if (TextUtils.isEmpty(paramCode)) {
            paramCode = "";
        }
        intent.putExtra(TestBloodNewActivity.PARAM_CODE, paramCode);
        activity.startActivity(intent);
    }

    public static void startQuality(Activity activity, QualityModel qualityModel) {
        Intent intent = new Intent(activity, TestBloodNewActivity.class);
        intent.putExtra(TestBloodNewActivity.QUALITY_BEAN, qualityModel);
        activity.startActivity(intent);
    }


    private String paramCode;
    private HospitalBed hospitalBed;
    private QualityModel qualityModel;
    private Drawable errorDrawable;
    private Drawable sucessDrawable;
    private Drawable doingDrawable;

    @Override
    protected TestBloodNewDeviceApiPresenter initPresenter() {
        return new TestBloodNewDeviceApiPresenter(this);
    }

    @Override
    protected void initEvent() {
        topBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!readDoing) {
                    finish();
                } else {
                    showToast("获取血糖中...");
                }
            }
        });
        RxJavaUtil.clicks(topHistory, new NextSubscriber() {
            @Override
            public void call(Object o) {
//                Intent intent = new Intent(getActivity(), HistoryActivity.class);
//                intent.putExtra(TestBloodNewActivity.MEMBER_BEAN, hospitalBed);
//                getActivity().startActivity(intent);


                Bundle bundle = new Bundle();
                bundle.putSerializable(TestBloodNewActivity.MEMBER_BEAN, hospitalBed);
                ActivityUtils.startActivity(bundle, ChartScanActivity.class);
            }
        });
    }


    @Override
    protected void initView() {
        doingDrawable = getResources().getDrawable(R.drawable.test_doing);
        errorDrawable = getResources().getDrawable(R.drawable.test_error);
        sucessDrawable = getResources().getDrawable(R.drawable.test_sucess_bg);
        Intent intent = getIntent();
        if (intent != null) {
            paramCode = intent.getStringExtra(PARAM_CODE);
            hospitalBed = (HospitalBed) intent.getSerializableExtra(MEMBER_BEAN);
            qualityModel = (QualityModel) intent.getSerializableExtra(QUALITY_BEAN);
        }
        if (hospitalBed != null) {
            topTitle.setText(hospitalBed.getBedNo() + "床 " + hospitalBed.getMemberName());
            topHistory.setVisibility(View.VISIBLE);
            //获取血糖范围
            mPresenter.getRange(TestBloodNewActivity.this, hospitalBed.getMemberId());
        } else if (qualityModel != null) {//质控模式
            topTitle.setText("质控模式");
            topHistory.setVisibility(View.GONE);
        }
        llOperatingBtn.setOnClickListener(this);
        onCompleteBtnEnable(false);


        topHistory.setText("血糖统计");



        //时间段
        bloodResultTimeAdapter = new BloodResultTimeAdapter(this, TestResultDataUtil.getTimeStr());
        gridView.setAdapter(bloodResultTimeAdapter);
        gridView.setOnItemClickListener(this);

        //默认进入的时段
        if (TextUtils.isEmpty(paramCode)) {
            paramCode = TestResultDataUtil.toTimeSlot(new Date());
        }
        int position = TestResultDataUtil.getPosition(paramCode);
        if (position > 0 && position < bloodResultTimeAdapter.getCount()) {
            bloodResultTimeAdapter.setSelectedIndex(position);
        }

        //血糖范围
        rangeBean = RangeHelper.getBloodRangeBean(hospitalBed.getMemberId());
        //默认的血糖值范围
        upDataHighLow(paramCode);

//        onUpdateBloodNew();


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_test_blood_new;
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    protected void doOther() {
        super.doOther();
        //血糖仪设备是否连接
        mPresenter.isConnect();
    }

    /**
     * 设备未连接
     */

    public void onDeviceConnectionless() {
        if (topBack != null) {
            topBack.setVisibility(View.VISIBLE);
        }
        if (llOperatingBtn != null)
            llOperatingBtn.setVisibility(View.VISIBLE);
        if (rlConnect != null)
            rlConnect.setVisibility(View.GONE);
    }

    /**
     * 请开始测血糖
     */

    public void onTestHand() {
        if (topBack != null) {
            topBack.setVisibility(View.VISIBLE);
        }
//        if (rlConnect != null)
//            rlConnect.setVisibility(View.VISIBLE);
        if (stateTv != null) {
            stateTv.setVisibility(View.VISIBLE);
            stateTv.setTextColor(getResources().getColor(R.color.black));
            stateTv.setText(getResources().getString(R.string.test_state_hand));
        }
    }

    /**
     * 获取血糖
     */

    public void onTestDoing() {

//        if (errorIv != null)
//            errorIv.setVisibility(View.GONE);
        if (rlConnect != null)
            rlConnect.setVisibility(View.GONE);
        if (stateTv != null) {
            stateTv.setVisibility(View.VISIBLE);
            stateTv.setTextColor(getResources().getColor(R.color.black));
            stateTv.setText(getResources().getString(R.string.test_doing));
            stateTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if (operatingBtn != null) {
            operatingBtn.setVisibility(View.VISIBLE);
            operatingBtn.setText(getResources().getString(R.string.test_doing));
            operatingBtn.setCompoundDrawablesWithIntrinsicBounds(doingDrawable, null, null, null);
        }
    }

    /**
     * 血糖获取成功
     */
    public void onTestSucess() {
        TestBloodNewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (topBack != null) {
                    topBack.setVisibility(View.VISIBLE);
                }
                if (errorIv != null) {
                    errorIv.setImageResource(R.drawable.test_device_connectionless);
                }
//                if (rlConnect != null)
//                    rlConnect.setVisibility(View.VISIBLE);
                if (stateTv != null) {
                    stateTv.setVisibility(View.VISIBLE);
                    stateTv.setTextColor(getResources().getColor(R.color.black));
                    if (hasData) {
                        stateTv.setText(getResources().getString(R.string.test_state_sucess));
                    } else {
                        stateTv.setText("血糖记录为空");
                    }
                    stateTv.setCompoundDrawablesWithIntrinsicBounds(sucessDrawable, null, null, null);
                }
                if (operatingBtn != null) {
                    operatingBtn.setVisibility(View.VISIBLE);
                    if (hasData) {
                        operatingBtn.setText(getResources().getString(R.string.test_value));
                    } else {
                        operatingBtn.setText(getResources().getString(R.string.test_state_doing));
                    }

                    operatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

//                if (countDownTime != null)
//                    countDownTime.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 血糖获取失败
     */
    public void onTestError() {
        TestBloodNewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (topBack != null) {
                    topBack.setVisibility(View.VISIBLE);
                }
                if (errorIv != null) {
                    errorIv.setImageResource(R.drawable.dongtaixetang_error);
                }
                if (rlConnect != null)
                    //TODO  yancheng    rlConnect.setVisibility(View.VISIBLE);
                    rlConnect.setVisibility(View.GONE);
                if (stateTv != null) {
                    stateTv.setVisibility(View.VISIBLE);
                    stateTv.setTextColor(getResources().getColor(R.color.red));

                    stateTv.setText(getResources().getString(R.string.test_state_error));

                    stateTv.setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null);
                }
                if (operatingBtn != null) {
                    operatingBtn.setVisibility(View.VISIBLE);
                    operatingBtn.setText(getResources().getString(R.string.test_again_obtain));
                    operatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });
    }

    //血糖数据返回
    public void onBloodMode(final BLEBloodModel bleBloodModel) {
        if (bleBloodModel != null && hospitalBed != null) {
            bleBloodModel.setMemberId(hospitalBed.getMemberId());
            bleBloodModel.setTitleStr(hospitalBed.getBedNo() + "床 " + hospitalBed.getMemberName());


            //血糖值
            bloodValue.setText(bleBloodModel.getValue() + "");
            circle.setCircleAnim((int) ((bleBloodModel.getValue() / Float.parseFloat(highStr)) * 360), true);

            //血糖值状态
            //正常 0: 高 1: 低 -1
            int type = TestResultDataUtil.getBloodType(bleBloodModel.getValue(), highStr, lowStr);
            switch (type) {
                case -1:
                    level = 1;
                    circle.setColor(Color.parseColor("#daedff"), Color.parseColor("#208fff"));
                    bloodValue.setTextColor(Color.parseColor("#42a0ff"));
                    imgLevel.setVisibility(View.VISIBLE);
                    imgLevel.setImageResource(R.drawable.index_down);
                    imgIcon.setImageResource(R.drawable.shchxuetan_06);
                    break;
                case 0:
                    level = 3;
                    circle.setColor(Color.parseColor("#d7f4d8"), Color.parseColor("#3cc83c"));
                    bloodValue.setTextColor(Color.parseColor("#48c248"));

                    imgLevel.setVisibility(View.GONE);
                    imgIcon.setImageResource(R.drawable.shchxuetan_08);
                    break;
                case 1:
                    level = 5;
                    circle.setColor(Color.parseColor("#fee4dd"), Color.parseColor("#ff6547"));
                    bloodValue.setTextColor(Color.parseColor("#ff6c4f"));
                    imgLevel.setVisibility(View.VISIBLE);
                    imgLevel.setImageResource(R.drawable.index_up);
                    imgIcon.setImageResource(R.drawable.shchxuetan_10);
                    break;
            }

        }

    }


    public void onCompleteBtnEnable(final boolean enable) {
        ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                if (llOperatingBtn == null) {
                    return;
                }
                if (enable) {
                    llOperatingBtn.setClickable(true);
                    llOperatingBtn.setBackgroundResource(R.drawable.rounded_rectangle_btn);
                } else {
                    llOperatingBtn.setClickable(false);
                    llOperatingBtn.setBackgroundResource(R.drawable.shape_solid_gray_circle_btn);
                }
            }
        });

    }

    /**
     * 血糖上传
     */

    public void onUpdateBlood() {
        //血糖上传
        if (bleBloodModel != null && mPresenter != null) {
            bleBloodModel.setParamCode(paramCode);
            mPresenter.setLevel(level);
            mPresenter.setBleBloodModel(bleBloodModel);
            mPresenter.completeBlood(bleBloodModel.getParamCode());
        }


    }

    /**
     * 批量上传
     */
    public void onUpdateBloodNew() {

        NewBloodItemDao newBloodItemDao = XTYApplication.getInstance().getDaoSession().getNewBloodItemDao();
        List<NewBloodItem> databaseAll = newBloodItemDao.loadAll();
        ComveeLoader.getInstance().batchList(hospitalBed, databaseAll)
                .subscribe(new HttpCall<String>(null) {
                    @Override
                    public void onNext(String s) {
                        ToastUtil.showToast(XTYApplication.getInstance(), "数据同步成功");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        ToastUtil.showToast(XTYApplication.getInstance(), "数据同步成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });

    }

    //TODO   更改下
    @Override
    public void onClick(View v) {

        mPresenter.requestDataConnect();
        switch (v.getId()) {
            case R.id.ll_operating_btn:
                String operatingStr = operatingBtn.getText().toString().trim();
                if (operatingStr.equals(getResources().getString(R.string.test_state_doing))) {
                    log("请求数据");
                    lastTimeRec = "";
                    mPresenter.requestDataConnect();
                } else if (operatingStr.equals(getResources().getString(R.string.test_device_error)) || operatingStr.equals(getResources().getString(R.string.test_again_obtain))) {
                    log("是否连接");
                    mPresenter.isConnect();
                } else if (operatingStr.equals(getResources().getString(R.string.test_value))) {
                    log("上传血糖");
                    //血糖上传
                    onUpdateBloodNew();

                }
                break;
        }
    }


    /**
     * singleTask启动模式下：Activity第一启动的时候执行onCreate()---->onStart()---->onResume()等后续生命周期函数，
     * 也就时说第一次启动Activity并不会执行到onNewIntent().(如果不是复用之前的activity实例是不会调用onNewIntent)，
     * 后面如果再有想启动Activity的时候，那就是执行onNewIntent()---->onResart()------>onStart()----->onResume()
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one。必须存储新的意图，否则getIntent（）将返回旧的意图
        processExtraData();
    }

    private void processExtraData() {
        Intent intent = getIntent();
    }


    @Override
    public void showProgress() {
        initProgressDialog(false);
    }


    /**
     * 设备连接
     */
    @Override
    public void onUsbAttached() {
        //设备连接成功，打开设备
        mPresenter.openBloodDevice();
    }

    /**
     * 设备未连接
     */
    @Override
    public void onUsbDetached() {
        onDeviceConnectionless();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //不用的时候关闭设备连接
        mPresenter.closeDevice();
       /* if (countDownTimer != null) {
            countDownTimer.cancel();
        }*/
    }

    /**
     * 打开设备失败
     */
    @Override
    public void onOpenFail() {
        if (mPresenter != null)
            mPresenter.closeDevice();
        if (operatingBtn != null) {
            operatingBtn.setText(getResources().getString(R.string.test_device_error));
            onCompleteBtnEnable(true);
        }

    }

    /**
     * //打开设备成功
     */
    @Override
    public void onOpenSucess() {
        if (operatingBtn != null) {
            if (operatingBtn.getText().toString().trim().equals(getResources().getString(R.string.test_device_error)) || operatingBtn.getText().toString().trim().equals(getResources().getString(R.string.test_again_obtain))) {
                mPresenter.requestDataConnect();
                onCompleteBtnEnable(false);
                onTestDoing();
            } else {
                operatingBtn.setText(getResources().getString(R.string.test_state_doing));
                onCompleteBtnEnable(true);
                onTestHand();
            }
        }

    }

    /**
     * ff aa 55 8c 8e 01 01
     * aa 55 88 07 0c 00 00 00 2e 0c 0a 13 12 0e 12 00 ea
     * aa 55 88 d1 0c 00 00 01 2e 0b 0a 13 12 0e 0b 00 bb
     * aa 55 88 b7 0c 00 00 02 2e 0a 0a 13 12 0e 07 00 a5
     * aa 55 88 0d 0c 00 00 03 2e 09 0a 13 12 0e 04 00 fe
     * aa 55 88 a8 0c 00 00 04 2e 08 0a 13 12 0e 03 00 9a
     */
    public void onBloodNew() {
        try {
            String hexString = hexBuffer.toString();
            if (hexString.startsWith("ff aa 55 8c 8e 01 01")) {
                String hexListString = hexString.replaceFirst("ff aa 55 8c 8e 01 01", "");
                Log.w("dyc", "最终字符串：" + hexListString.length() + "----" + hexListString.replaceAll(" ", "").length() + hexListString);

                String[] datas = hexListString.split("aa 55 86 ");

                Log.w("dyc", "切换数组: " + datas);

                List<NewBloodItem> newBloodItems = parseBlood(datas);


                NewBloodItemDao newBloodItemDao = XTYApplication.getInstance().getDaoSession().getNewBloodItemDao();
                newBloodItemDao.deleteAll();
                List<NewBloodItem> databaseAll = newBloodItemDao.loadAll();
                LogUtils.w("dyc", databaseAll);
                if (databaseAll != null && databaseAll.size() > 0) {
                    NewBloodItem lasterItem = databaseAll.get(databaseAll.size() - 1);
                    if (lasterItem.getHistory_id() == newBloodItems.get(newBloodItems.size() - 1).getHistory_id()) {
                        //无需操作  新旧数据相同 直接跳转到
                        hasData = false;
                    } else {
                        hasData = true;
                        newBloodItemDao.insertOrReplaceInTx(getDiff(databaseAll, newBloodItems));
                    }
                } else {
                    hasData = true;
                    newBloodItemDao.insertInTx(newBloodItems);
                }
            } else {
                showToast("未有血糖数据！");
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    private List<NewBloodItem>
    getDiff(List<NewBloodItem> databaseAll, List<NewBloodItem> newBloodItems) {
        List<NewBloodItem> newDiff = new ArrayList<>();
        long lastId = databaseAll.get(databaseAll.size() - 1).getHistory_id();
        for (NewBloodItem item : newBloodItems) {
            if (item.getHistory_id() > lastId) {
                newDiff.add(item);
            }
        }
        return newDiff;
    }

    private List<NewBloodItem> parseBlood(String[] datas) {
        List<NewBloodItem> newBloodItems = new ArrayList<>();
        for (String item : datas) {
            if (!TextUtils.isEmpty(item) && item.trim().split(" ").length == 14) {
                newBloodItems.add(parseItem(item.split(" ")));
            }
        }
        return newBloodItems;
    }


    //07 0c 00   00 00    2e 0c    0a月 13日 12年 0e时 12分    00 ea
    private NewBloodItem parseItem(String[] args) {
        NewBloodItem newBloodItem = new NewBloodItem();

        newBloodItem.setHistory_id(Integer.parseInt(args[3] + args[4], 16));
        newBloodItem.setMemberId(Integer.parseInt(args[5] + args[6], 16));
        newBloodItem.setMouth(Integer.parseInt(args[7], 16));
        newBloodItem.setDay(Integer.parseInt(args[8], 16));
        newBloodItem.setYear(Integer.parseInt(args[9], 16));
        newBloodItem.setHour(Integer.parseInt(args[10], 16));
        newBloodItem.setMinus(Integer.parseInt(args[11], 16));
        newBloodItem.setSugar(String.format("%.1f", Long.parseLong(args[12] + args[13], 16) / 18.0f));

        return newBloodItem;
    }

    /**
     * 血糖数据处理
     */
    public void onBlood1() {
        /**
         * 血糖仪设备多次回调，字符串需要拼接，原因不明
         * 数据格式 ： #6R|11|^^^Glucose|9.89|mmol/L^P|||||||201808051802#67 #7L|1|N#0A
         * 最后一条数据为最新数据
         */

        try {
            String hexString = hexBuffer.toString();
            Log.d(TAG, "onBlood() returned:最终结果：  " + hexString);
            String[] split = hexString.split("0d" + "\\s" + "0a");
            if (split.length < 7) {
                hasData = false;
                showToast("未有血糖数据！");
            } else {
                String lastData = split[split.length - 3].trim();
                String[] split1 = lastData.split("7c");
                String sugarValue = CH34xDriverUtil.parseAscii(split1[3].trim());
                if (Float.parseFloat(sugarValue) > 0) {
                    hasData = true;
                    bleBloodModel = new BLEBloodModel(TimeUtil.getNormalTime(), Float.parseFloat(sugarValue));
                    TestBloodNewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onBloodMode(bleBloodModel);
                        }
                    });
                } else {
                    hasData = false;
                    showToast("未有血糖数据！");
                }

            }
        } catch (Exception e) {
            onReadFail();
            log("发生异常：" + e.getMessage());
        }


    }

    private boolean hasData = false;

    public void readBloodSucess() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TestBloodNewActivity.MEMBER_BEAN, hospitalBed);
        ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPresenter != null)
                    mPresenter.closeDevice();
                onCompleteBtnEnable(true);
                readDoing = false;
                onBloodNew();
                onTestSucess();

                if (hasData) {
                    hasData = false;
                    countDownTime.setVisibility(View.VISIBLE);
                    onUpdateBloodNew();
                    llOperatingBtn.setClickable(false);
                    llOperatingBtn.setBackgroundResource(R.drawable.shape_solid_gray_circle_btn);
                    //zhangq 去掉在按钮上显示等待5秒的效果
                    buttonCountDown = new CountDownTimer(5500, 1000) {
                        @Override
                        public void onTick(long l) {
                            if (countDownTime != null)
                                countDownTime.setText(String.valueOf(l / 1000));
                        }

                        @Override
                        public void onFinish() {
//                            onUpdateBlood();
                            if (buttonCountDown != null)
                                buttonCountDown.cancel();
                            if (countDownTime != null)
                                countDownTime.setVisibility(View.GONE);


                            llOperatingBtn.setClickable(true);
                            llOperatingBtn.setBackgroundResource(R.drawable.rounded_rectangle_btn);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(TestBloodNewActivity.MEMBER_BEAN, hospitalBed);
                            ActivityUtils.startActivity(bundle, ChartScanActivity.class);

                        }
                    }.start();
                } else {
                    ActivityUtils.startActivity(bundle, ChartScanActivity.class);
                }
                hexBuffer = new StringBuffer();
            }
        });
        readDoing = false;
    }

    private String lastTimeRec = "";

    /**
     * 读取成功
     *
     * @param bloodData #6R|11|^^^Glucose|9.89|mmol/L^P|||||||201808051802#67#7L|1|N#0A #
     */
    @Override
    public void onReadSucess(final String recv, final String bloodData) {
        //  stringBuffer.append(bloodData);
        gotData = true;
        hexBuffer.append(recv);
//        Log.e("合并数据 长度", "==" + hexBuffer.toString().replace(" ", "").length());

//        Log.e("合并数据", "==" + lastTimeRec + recv);
        //结束符号
        if (recv.contains("0d 0a 04") || (lastTimeRec + recv).contains("0d 0a 04")) {
         /*   if (countDownTimer != null)
                countDownTimer.cancel();*/
            lastTimeRec = "";
            log("数据读取成功");
            readBloodSucess();
        }
        lastTimeRec = recv;

    }


    // 数据读取完成
    @Override
    public void onReadFinish() {
        if (hexBuffer != null && hexBuffer.length() > 0) {
            lastTimeRec = "";
            Log.e("数据读取成功", "==" + hexBuffer.toString().replaceAll(" ", "").length() + "  =正确： " + (7 + 17 * 6));
            readBloodSucess();
        }

    }

    /**
     * 读取失败
     */
    @Override
    public void onReadFail() {
        readDoing = false;
        if (mPresenter != null)
            mPresenter.closeDevice();
        onCompleteBtnEnable(true);
        onTestError();

    }

    boolean gotData = false;

    @Override
    public void onReadDoing() {
        gotData = false;
        readDoing = true;
        onCompleteBtnEnable(false);
        onTestDoing();
        //8秒未读取成功，需重新获取血糖
        /*countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                if (l < 22000 && !gotData) { //8秒都没数据回来
                    onReadFail();
                    countDownTimer.cancel();
                }
                Log.d(TAG, "onTick() returned: " + l);
            }

            @Override
            public void onFinish() {
                //30秒数据没有读取完成，默认读取失败
                onReadFail();
            }
        }.start();*/

    }

    @Override
    public void onFinishActivity() {
        dismissProgressDialog();
        this.finish();
    }

    @Override
    public void onTestPaperNoPullOut() {
        readDoing = false;
        if (mPresenter != null)
            mPresenter.closeDevice();
        onCompleteBtnEnable(true);
        onTestError();
        new AlertDialog(TestBloodNewActivity.this)
                .builder()
                .setTitle("错误信息")
                .setMsg("设备试纸未拔出!请拔出后重新获取血糖!")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();

    }

    StringBuilder builder = new StringBuilder();

    @Override
    public void log(final String log) {
       /* ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                builder.append(log);
                builder.append("\n");
                logTv.setText(builder.toString());
            }
        });*/

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (buttonCountDown != null)
            buttonCountDown.cancel();
        if (countDownTime != null)
            countDownTime.setVisibility(View.GONE);
        //选择时间段
        paramCode = TestResultDataUtil.getTimeStr().get(position);
        bloodResultTimeAdapter.setSelectedIndex(position);
        upDataHighLow(paramCode);
        if (bleBloodModel != null)
            onBloodMode(bleBloodModel);
    }

    /**
     * 按照时间段进行不同血糖范围的监测
     *
     * @param paramCode
     */
    private void upDataHighLow(String paramCode) {
        if (paramCode.equals(TestResultDataUtil.TIME_RANDOM)) {//随机就是当前时段
            paramCode = TestResultDataUtil.toTimeSlot(new Date());
        }
        if (rangeBean != null) {
            String[] lowAndHigh = TestResultDataUtil.getLowAndHigh(paramCode, rangeBean);
            lowStr = lowAndHigh[0];
            highStr = lowAndHigh[1];
        } else {
            lowStr = "4";
            highStr = "10";
        }
        referenceValue.setText(String.format(getContext().getString(R.string.reference_value),
                lowStr, highStr));
    }
}
