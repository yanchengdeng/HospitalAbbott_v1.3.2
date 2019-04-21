//package com.comvee.blelibrary;
//
//import android.bluetooth.BluetoothAdapter;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.app.Activity;
//import android.os.Handler;
//import android.os.Message;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//
///**
// *
// */
//public class OmlActivityy extends Activity {
//    /**
//     * 请求code
//     */
//    private int REQUEST_ENABLE_BT=200;
//    double widthPixels;
//    double heightPixels;
//    private ViewPager banner;
//    /**
//     * 轮播适配器
//     */
//    private SynDevicesDataFragment.BannerAdapter bannerAdapter;
//    /**
//     * 进度角标
//     */
//    private ImageProgressBar imageProgressBar;
//    /**
//     * 圆点
//     */
//    private NaviAutoView nav;
//    private TextView naviTxt;
//    private ImageHandler imageHandler = new ImageHandler(new WeakReference<OmlActivityy>(this));
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_oml_activityy);
//        initView();
//    }
//
//    /**
//     * 初始化View
//     */
//    public void initView(){
//        getHW();
//        Double dW = widthPixels / 640;
//        Double dH = heightPixels / 1136;
//        int width = getWindowManager().getDefaultDisplay().getWidth();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_05);
//        findViewById(R.id.devices_ble_syndata_carousel_layout).setBackgroundDrawable(new BitmapDrawable(createRepeater(width,bitmap)));
//        nav = (NaviAutoView) findViewById(R.id.nav);
//        banner = (ViewPager) findViewById(R.id.devices_ble_syndata_carousel);
//        bannerAdapter = new SynDevicesDataFragment.BannerAdapter(this);
//        bannerAdapter.idList = new ArrayList<>();
//        LayoutInflater inflater = LayoutInflater.from(this);
//        banner.setAdapter(bannerAdapter);
//        imageProgressBar = (ImageProgressBar) findViewById(R.id.devices_ble_syndata_carousel_progressbar);
//        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            //配合Adapter的currentItem字段进行设置。
//            @Override
//            public void onPageSelected(int arg0) {
//                if(bannerAdapter.getList().size() == 1){
//                    nav.setVisibility(View.GONE);
//                }else{
//                    nav.setVisibility(View.VISIBLE);
//                    nav.setValue(arg0 % bannerAdapter.getList().size(), 0.0f, bannerAdapter.getList().size(), (float) ((dW - 0.4) * 1));
//                }
//                imageHandler.sendMessage(Message.obtain(imageHandler, SynDevicesDataFragment.ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            //覆写该方法实现轮播效果的暂停和恢复
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                switch (arg0) {
//                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        imageHandler.sendEmptyMessage(SynDevicesDataFragment.ImageHandler.MSG_KEEP_SILENT);
//                        break;
//                    case ViewPager.SCROLL_STATE_IDLE:
//                        imageHandler.sendEmptyMessageDelayed(SynDevicesDataFragment.ImageHandler.MSG_UPDATE_IMAGE, SynDevicesDataFragment.ImageHandler.MSG_DELAY);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        banner.setCurrentItem(Integer.MAX_VALUE/2);//默认在中间，使用户看不到边界
//        //开始轮播效果
//        imageHandler.sendEmptyMessageDelayed(SynDevicesDataFragment.ImageHandler.MSG_UPDATE_IMAGE, SynDevicesDataFragment.ImageHandler.MSG_DELAY);
//        naviTxt = (TextView) findViewById(R.id.devices_ble_syndata_carousel_txt);
//        ImageProgressBar.ImagedProgressBarBean startBle = new ImageProgressBar.ImagedProgressBarBean();
//        startBle.activationBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_16);
//        startBle.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_18);
//        startBle.txt = "开启蓝牙";
//
//        ImageProgressBar.ImagedProgressBarBean waitConnection = new ImageProgressBar.ImagedProgressBarBean();
//        waitConnection.activationBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_20);
//        waitConnection.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_22);
//        waitConnection.txt = "等待连接";
//
//        ImageProgressBar.ImagedProgressBarBean synData = new ImageProgressBar.ImagedProgressBarBean();
//        synData.activationBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_24);
//        synData.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_26);
//        synData.txt = "测量血压";
//
//        ArrayList<ImageProgressBar.ImagedProgressBarBean> list = new ArrayList<>();
//        list.add(startBle);
//        list.add(synData);
//
//        imageProgressBar.setProgressData(list);
//        imageProgressBar.setOnItemClick(new ImageProgressBar.OnItemClick() {
//            @Override
//            public void onItem(int index) {
//                if(index == 1){
//                    if(!BPManager.getInstance(getApplication()).isEnabled()){
//                        progress(0);
//                        CustomDialog customDialog = new CustomDialog.Builder(OmlActivityy.this).setPositiveButton("好", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
//                            }
//                        }).setMessage("打开蓝牙来允许\"掌控糖尿病\"\n连接到配件")
//                                .create();
//                        customDialog.show();
//                    }else{
//                        if(imageProgressBar.getProgress() < 2){
//                            progress(1);
//                            waitOmlDate();
//                        }
//                    }
//                }else if(index == 3){
//                    if(imageProgressBar.getProgress() == 3)
//                        waitOmlDate();
//                }
//            }
//        });
//    }
//
//    /**
//     * 开启等待数据
//     */
//    public void waitOmlDate(){
//        if(BPManager.getInstance(this).OmronBPDeviceBonded()){
//            BPDataManager.getInstance(getApplication()).getHealthData(new IOmronGetDataResultCallback() {
//                @Override
//                public void onProcess(int i) {
//
//                }
//
//                @Override
//                public void onSuccess(HashMap<Integer, ArrayList<Object>> hashMap) {
//                    TextView txt = ((TextView)findViewById(R.id.data));
//                    ArrayList<Object> arrayList = hashMap.get(OmronVariableHealthDataType.BLOOD_PRESSURE_DATA);
//                    if(arrayList == null || arrayList.size() < 1){
//                        Toast.makeText(getApplication(),"没有血压数据",Toast.LENGTH_LONG).show();
//                        return;
//                    }else{
//                        txt.setText("");
//                        for(Object o : arrayList){
//                            StringBuffer stringBuffer = new StringBuffer();
//                            if(o instanceof OmronDataRawBloodPressureData){
//                                OmronDataRawBloodPressureData omronDataRawBloodPressureData = (OmronDataRawBloodPressureData) o;
//                                stringBuffer.append("收缩压: "+ omronDataRawBloodPressureData.getSystolic() + "mmHg \n");
//                                stringBuffer.append("舒张压: "+ omronDataRawBloodPressureData.getDiastolic() + "mmHg \n");
//                                stringBuffer.append("脉搏: "+ omronDataRawBloodPressureData.getPulse() + " \n");
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.setTimeInMillis(omronDataRawBloodPressureData.getStart_time());
//                                stringBuffer.append("测量时间: "+ calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " \n");
//                                stringBuffer.append(" 脉搏是否规则: "+ omronDataRawBloodPressureData.isIrregular() + " \n");
//                                stringBuffer.append("----------end\n");
//                            }
//                            txt.append(stringBuffer.toString());
//                            progress(3);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(int i, String s) {
//
//                }
//            });
//        }else{
//            connect();
//        }
//    }
//
//    /**
//     * 连接
//     */
//    public void connect(){
//        BPManager.getInstance(this).startBinding(new IOmronBaseResponseCallback() {
//            @Override
//            public void onSuccess(Object o) {
//                waitOmlDate();
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
//    }
//
//    /**
//     * 获取屏幕宽高
//     */
//    private void getHW() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        widthPixels = metrics.widthPixels;
//        heightPixels = metrics.heightPixels;
//    }
//
//    /**
//     * 跟进进度 变更进度条
//     */
//    public void progress(int index){
//        //开始轮播效果
//        bannerAdapter.idList.clear();
//        switch (index){
//            case 0:
//            case 1:
//                bannerAdapter.idList.add(R.drawable.qiangsheng_34);
//                bannerAdapter.idList.add(R.drawable.qiangsheng_37);
//                if(index == 0){
//                    imageProgressBar.getList().get(0).txt = "开启蓝牙";
//                }else{
//                    imageProgressBar.getList().get(0).txt = "已开启";
//                }
//                //imageProgressBar.getList().get(1).txt = "等待连接";
//                imageProgressBar.getList().get(1).txt = "测量血压";
//                naviTxt.setText("第一步打开蓝牙");
//                break;
//            case 2:
//                bannerAdapter.idList.add(R.drawable.qiangsheng_39);
//                naviTxt.setText("第二步等待连接");
//                imageProgressBar.getList().get(0).txt = "已开启";
//                //imageProgressBar.getList().get(1).txt = "已连接";
//                imageProgressBar.getList().get(1).txt = "测量血压";
//                break;
//            case 4:
//            case 3:
//                bannerAdapter.idList.add(R.drawable.qiangsheng_41);
//                naviTxt.setText("第三步同步数据");
//                imageProgressBar.getList().get(0).txt = "已开启";
//                //imageProgressBar.getList().get(1).txt = "已连接";
//                if(index == 4){
//                    imageProgressBar.getList().get(1).txt = "测量完成";
//                }else{
//                    imageProgressBar.getList().get(1).txt = "测量血压";
//                }
//                index = 3;
//                break;
//        }
//        imageHandler.sendEmptyMessageDelayed(SynDevicesDataFragment.ImageHandler.MSG_UPDATE_IMAGE, 1000);
//        bannerAdapter.notifyDataSetChanged();
//        imageProgressBar.setProgress(index);
//    }
//
//    /**
//     * 绘制背景墙
//     * @param width
//     * @param src
//     * @return
//     */
//    public  Bitmap createRepeater(int width, Bitmap src){
//        int count = (width + src.getWidth() - 1) / src.getWidth();
//        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        for(int idx = 0; idx < count; ++ idx){
//            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
//        }
//        return bitmap;
//    }
//
//
//
//    static class ImageHandler extends Handler {
//
//        /**
//         * 请求更新显示的View。
//         */
//        protected static final int MSG_UPDATE_IMAGE  = 1;
//        /**
//         * 请求暂停轮播。
//         */
//        protected static final int MSG_KEEP_SILENT   = 2;
//        /**
//         * 请求恢复轮播。
//         */
//        protected static final int MSG_BREAK_SILENT  = 3;
//        /**
//         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
//         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
//         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
//         */
//        protected static final int MSG_PAGE_CHANGED  = 4;
//
//        //轮播间隔时间
//        protected static final long MSG_DELAY = 3000;
//
//        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
//        private WeakReference<OmlActivityy> weakReference;
//        private int currentItem = 0;
//
//        protected ImageHandler(WeakReference<OmlActivityy> wk){
//            weakReference = wk;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            OmlActivityy activity = weakReference.get();
//            if (activity==null){
//                //Activity已经回收，无需再处理UI了
//                return ;
//            }
//            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
//            if (activity.imageHandler.hasMessages(MSG_UPDATE_IMAGE)){
//                activity.imageHandler.removeMessages(MSG_UPDATE_IMAGE);
//            }
//            switch (msg.what) {
//                case MSG_UPDATE_IMAGE:
//                    currentItem++;
//                    activity.banner.setCurrentItem(currentItem);
//                    //准备下次播放
//                    activity.imageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_KEEP_SILENT:
//                    //只要不发送消息就暂停了
//                    break;
//                case MSG_BREAK_SILENT:
//                    activity.imageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_PAGE_CHANGED:
//                    //记录当前的页号，避免播放的时候页面显示不正确。
//                    currentItem = msg.arg1;
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//}
