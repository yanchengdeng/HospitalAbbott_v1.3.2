//package com.comvee.blelibrary;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.AppCompatButton;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.ref.WeakReference;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by 黄嘉晖 on 2017/5/11.
// */
//
//public class SynDevicesDataFragment extends BaseFragment {
//    /**
//     * 请求code
//     */
//    private int REQUEST_ENABLE_BT=200;
//    private BluetoothAdapter bluetoothAdapter;
//    private Handler handler;
//    private BluetoothDevice connDevices = null;
//    /**
//     * 搜索到蓝牙设备的广播
//     */
//    // Create a BroadcastReceiver for ACTION_FOUND
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if(device.getAddress() != null && device.getAddress().equals("E9:56:DC:2E:47:C6") || device.getAddress().equals("ED:F8:BF:89:8C:46")){//找到设备 直接连接
//                    stopSearchBluetoothDevice();
//                    connDevices = device;
//                    int bondState = connDevices.getBondState();
//                    if(bondState == BluetoothDevice.BOND_NONE){//未配对
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            connDevices.createBond();
//                        }else{
//                            try {
//                                Method creMethod = BluetoothDevice.class.getMethod("createBond");
//                                try {
//                                    creMethod.invoke(connDevices);
//                                } catch (IllegalAccessException e) {
//                                    e.printStackTrace();
//                                } catch (InvocationTargetException e) {
//                                    e.printStackTrace();
//                                }
//                            } catch (NoSuchMethodException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }else{
//                        connction();
//                        progress(2);
//                    }
//                }
//            }else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){//蓝牙状态变化
//                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
//                switch (blueState) {
//                    case BluetoothAdapter.STATE_TURNING_ON:
//
//                        break;
//                    case BluetoothAdapter.STATE_ON:
//                        //bluetoothStatus="on";
//                        if(imageProgressBar.getProgress() < 1)
//                            progress(1);
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        progress(0);
//                        break;
//                    case BluetoothAdapter.STATE_OFF:
//                        progress(0);
//                        break;
//                }
//            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){//蓝牙状态变化
//                int cur_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
//                int previous_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);
//                if(connDevices != null)
//                switch (connDevices.getBondState()) {
//                    case BluetoothDevice.BOND_BONDING:
//                        break;
//                    case BluetoothDevice.BOND_BONDED://配对完成
//                        connction();
//                        progress(2);
//                        break;
//                    case BluetoothDevice.BOND_NONE://配对失败
//
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    };
//
//
//    public boolean isSynData = false;
//    private TextView naviTxt;
//    BluetoothMode bluetoothMode;
//    private ViewPager banner;
//    private BannerAdapter bannerAdapter;
//    private NaviAutoView nav;
//    double widthPixels;
//    double heightPixels;
//    private ImageHandler imageHandler = new ImageHandler(new WeakReference<SynDevicesDataFragment>(this));
//    /**
//     * 进度
//     */
//    private ImageProgressBar imageProgressBar;
//
//    @Override
//    public int getViewLayoutId() {
//        return R.layout.devices_ble_syndata_frag;
//    }
//
//    @Override
//    public void onLaunch(Bundle dataBundle) {
//        super.onLaunch(dataBundle);
//        initView();
//        handler = new Handler();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
//        initBluetooth();
//    }
//
//    /**
//     * 上传血糖数据
//     * @param index
//     */
//    public void upData(final int index){
//        if(measurements == null || measurements.size() < 1){
//            showToast("同步成功,没有需要同步的新数据");
//            progress(4);
//            cancelProgressDialog();
//            return;
//        }
//        ComveeHttp http = new ComveeHttp(getApplicationContext(), ConfigUrlMrg.UP_BLOOD_DATA);
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append("[");
//        Collections.reverse(measurements);
//        DecimalFormat    df   = new DecimalFormat("######0.00");
//        for(int i = 0 ; i < measurements.size(); i ++ ){
//            GlucoseMeasurement glucoseMeasurement = measurements.get(i);
//            stringBuffer.append("{"+
//                    "\"value\":"+ "\"" +df.format((glucoseMeasurement.getGlucoseConcentrationInMGDL() / 18.0182))+ "\"");
//            BaseTime baseTime = glucoseMeasurement.getBaseTime();
//            if(baseTime != null)
//                stringBuffer.append(","+
//                        "\"time\":\""+baseTime.getYear()+"-"+baseTime.getMonthString()+"-"+baseTime.getDayString()+" "+baseTime.getHoursString()+":"+baseTime.getMinutesString()+":"+baseTime.getSecondsString());
//            stringBuffer.append("\"}");
//            if(i != measurements.size() -1)
//                stringBuffer.append(",");
//        }
//        stringBuffer.append("]");
//        http.setPostValueForKey("paramStr", stringBuffer.toString());
//        http.setOnHttpListener(1, new OnHttpListener(){
//
//            @Override
//            public void onSussece(int what, byte[] b, boolean fromCache) {
//                cancelProgressDialog();
//                try {
//                    ComveePacket packet = ComveePacket.fromJsonString(b);
//                    if (packet.getResultCode() == 0) {
//                        JSONObject textX = null;
//                        try {
//                            textX = packet.getJSONObject("body");
//                            final String machineId = textX.optString("machineId");
//                            final String memberId = textX.optString("memberId");
//                            final String paramCode = textX.optString("paramCode");
//                            final String paramLogId = textX.optString("paramLogId");
//                            final String recordTime = textX.optString("recordTime");
//                            final String typeX = textX.optString("typeFlag");
//                            final String unit = textX.optString("unit");
//                            final String value = textX.optString("value");
//                            final String level = textX.optString("level");
//                            String label = textX.optString("label");
//                            SugarBloodModel model = new SugarBloodModel();
//                            model.machineId = machineId;
//                            model.memberId = memberId;
//                            model.paramCode = paramCode;
//                            model.paramLogId = paramLogId;
//                            model.recordTime = recordTime;
//                            model.typeX = typeX;
//                            model.unit = unit;
//                            model.level = level;
//                            model.value = value;
//                            model.lable = label;
//                            showToast("同步成功");
//                            progress(4);
//                            SugarMrg.setSynDevicesGlucoseIndex(connDevices.getAddress(),index);
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    BloodSugarDialog dialog = new BloodSugarDialog(getActivity(), R.style.internetDialog, model);
//                                    dialog.hinLeftButton();
//                                    dialog.show();
//                                }
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            showToast("同步失败");
//                            cancelProgressDialog();
//                            progress(0);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFialed(int what, int errorCode) {
//                System.out.println("123");
//            }
//        });
//        http.startAsynchronous();
//    }
//
//    /**
//     * 找到的血糖数据
//     */
//    private List<GlucoseMeasurement> measurements = new ArrayList<>();
//
//    /**
//     * 同步回调
//     */
//    SynTool.SynListneer synListneer = new SynTool.SynListneer() {
//
//        @Override
//        public void onReadRecordCount(final int count) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    /*if(ed_requData != null)
//                        ed_requData.append("-总条数:"+count+"条-"+"\n");*/
//                }
//            });
//        }
//
//        @Override
//        public void onReadTestCount(final int count) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    /*if(ed_requData != null)
//                        ed_requData.append("-记录位置:"+count+"-"+"\n");*/
//                }
//            });
//        }
//
//        @Override
//        public void onGlucoseMeasurementFound(final GlucoseMeasurement glucoseMeasurement) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    measurements.add(glucoseMeasurement);
//                    /*ed_requData.append(unt+glucoseMeasurement.getGlucoseConcentrationInMGDL()+"\n");
//                    BaseTime baseTime = glucoseMeasurement.getBaseTime();
//                    if(baseTime != null)
//                        ed_requData.append("血糖时间:"+baseTime.getMonth()+"-"+baseTime.getDay()+"  "+baseTime.getHours()+":"+baseTime.getMinutes()+" \n");
//                    ed_requData.append("----------------\n");*/
//                }
//            });
//        }
//
//        @Override
//        public void onSynSuccess(int index) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    upData(index);
//                }
//            });
//        }
//
//
//        @Override
//        public void onSynFailed() {
//            progress(2);
//            showToast("同步失败");
//        }
//    };
//
//    SynTool tool;
//
//    /**
//     * 开始同步
//     */
//    public void startSyn(){
//        stopSearchBluetoothDevice();
//        measurements.clear();
//        showProgressDialog("正在同步....");
//        progress(3);
//        if(tool == null)
//        tool = new SynTool(bluetoothMode);
//        int index = 0;
//        if(connDevices != null)
//            index = SugarMrg.getSynDevicesGlucoseIndex(connDevices.getAddress());
//        //index = 0;
//        tool.syn(bluetoothMode.getBluetoothGatt(),synListneer,index);
//        isSynData = true;
//    }
//
//
//    /**
//     * 连接
//     */
//    public void connction(){
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bluetoothMode.conntion(getActivity(),connDevices, new BluetoothMode.onConnectListneer() {
//                    @Override
//                    public void onConnectSuccess() {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(!isSynData){
//                                    startSyn();
//                                    isSynData = true;
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void connectFailed() {
//                        progress(1);
//                        showToast("已断开");
//                    }
//
//                    @Override
//                    public void senDataSuccess() {
//
//                    }
//
//                    @Override
//                    public void senDataFailed() {
//
//                    }
//
//                    @Override
//                    public void onReadData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//
//                        }
//                    }
//                    private int len = 0;
//                    @Override
//                    public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
//
//                    }
//
//                    @Override
//                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//
//                    }
//
//                    @Override
//                    public void onServicesDiscoveredFailed() {
//
//                    }
//                });
//            }
//        },1200);
//    }
//
//    /**
//     * 初始化蓝牙
//     * */
//    private boolean initBluetooth() {
//        boolean isOk=true;
//        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
//        if(bluetoothAdapter==null){//设备为空
//            CustomDialog customDialog = new CustomDialog.Builder(getActivity()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    FragmentMrg.toBack(getActivity());
//                    progress(0);
//                }
//            }).setMessage("您没有蓝牙设备!")
//                    .create();
//            customDialog.show();
//            isOk=false;
//        }else{
//            bluetoothMode = new BluetoothMode(getActivity(),bluetoothAdapter);
//            if(!bluetoothAdapter.isEnabled()){
//                progress(0);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        CustomDialog customDialog = new CustomDialog.Builder(getActivity()).setPositiveButton("好", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
//                            }
//                        }).setMessage("打开蓝牙来允许\"掌控糖尿病\"\n连接到配件")
//                                .create();
//                        customDialog.show();
//                    }
//                },100);
//            }else{
//                progress(1);
//                searchBluetoothDevice();
//            }
//        }
//        return  isOk;
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
//                imageProgressBar.getList().get(1).txt = "等待连接";
//                imageProgressBar.getList().get(2).txt = "同步数据";
//                naviTxt.setText("第一步打开蓝牙");
//                break;
//            case 2:
//                bannerAdapter.idList.add(R.drawable.qiangsheng_39);
//                naviTxt.setText("第二步等待连接");
//                imageProgressBar.getList().get(0).txt = "已开启";
//                imageProgressBar.getList().get(1).txt = "已连接";
//                imageProgressBar.getList().get(2).txt = "同步数据";
//                break;
//            case 4:
//            case 3:
//                bannerAdapter.idList.add(R.drawable.qiangsheng_41);
//                naviTxt.setText("第三步同步数据");
//                imageProgressBar.getList().get(0).txt = "已开启";
//                imageProgressBar.getList().get(1).txt = "已连接";
//                if(index == 4){
//                    imageProgressBar.getList().get(2).txt = "已同步";
//                }else{
//                    imageProgressBar.getList().get(2).txt = "同步数据";
//                }
//                index = 3;
//                break;
//        }
//        imageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, 1000);
//        bannerAdapter.notifyDataSetChanged();
//        imageProgressBar.setProgress(index);
//    }
//
//
//    private Timer searchBluetoothDevice = null;
//
//    /**
//     * 搜索周边蓝牙
//     */
//    public void searchBluetoothDevice(){
//        if(searchBluetoothDevice != null)
//            searchBluetoothDevice = new Timer();
//        searchBluetoothDevice = new Timer();
//        searchBluetoothDevice.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(bluetoothAdapter==null){
//                    CustomDialog customDialog = new CustomDialog.Builder(getActivity())
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    initBluetooth();
//                                }
//                            }).setMessage("当前蓝牙不可用,使用重新初始化?").create();
//                    customDialog.show();
//                    return;
//                }
//                if(bluetoothAdapter.isDiscovering()){
//                    bluetoothAdapter.cancelDiscovery();
//                    bluetoothAdapter.startDiscovery();
//                }else{
//                    bluetoothAdapter.startDiscovery();
//                }
//            }
//        },10,8000);
//    }
//
//    /**
//     * 停止搜索周边蓝牙
//     */
//    public void stopSearchBluetoothDevice(){
//        if(searchBluetoothDevice != null) {
//            searchBluetoothDevice.cancel();
//            searchBluetoothDevice = new Timer();
//        }
//        if(bluetoothAdapter!=null)
//            bluetoothAdapter.cancelDiscovery();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == getActivity().RESULT_OK) {
//                progress(1);
//                searchBluetoothDevice();
//            } else if (resultCode == getActivity().RESULT_CANCELED) {
//                showToast("请先打开蓝牙");
//                progress(0);
//            }
//        }
//
//    }
//
//    /**
//     * 初始化UI
//     */
//    private void initView() {
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title);
//        titleBarView.setLeftDefault(this);
//        getHW();
//        Double dW = widthPixels / 640;
//        Double dH = heightPixels / 1136;
//        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.qiangsheng_05);
//        findViewById(R.id.devices_ble_syndata_carousel_layout).setBackgroundDrawable(new BitmapDrawable(createRepeater(width,bitmap)));
//        nav = (NaviAutoView) findViewById(R.id.nav);
//        banner = (ViewPager) findViewById(R.id.devices_ble_syndata_carousel);
//        bannerAdapter = new BannerAdapter();
//        bannerAdapter.idList = new ArrayList<>();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
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
//                imageHandler.sendMessage(Message.obtain(imageHandler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
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
//                        imageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
//                        break;
//                    case ViewPager.SCROLL_STATE_IDLE:
//                        imageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        banner.setCurrentItem(Integer.MAX_VALUE/2);//默认在中间，使用户看不到边界
//        //开始轮播效果
//        imageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
//
//
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
//        synData.txt = "同步数据";
//
//        ArrayList<ImageProgressBar.ImagedProgressBarBean> list = new ArrayList<>();
//        list.add(startBle);
//        list.add(waitConnection);
//        list.add(synData);
//
//        imageProgressBar.setProgressData(list);
//        imageProgressBar.setOnItemClick(new ImageProgressBar.OnItemClick() {
//            @Override
//            public void onItem(int index) {
//                if(index == 1){
//                    if(!bluetoothAdapter.isEnabled()){
//                        progress(0);
//                        CustomDialog customDialog = new CustomDialog.Builder(getActivity()).setPositiveButton("好", new DialogInterface.OnClickListener() {
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
//                            searchBluetoothDevice();
//                        }
//                    }
//                }else if(index == 3){
//                    if(imageProgressBar.getProgress() == 3)
//                    startSyn();
//                }
//            }
//        });
//    }
//
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
//    private void getHW() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        widthPixels = metrics.widthPixels;
//        heightPixels = metrics.heightPixels;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(mReceiver != null)
//            getActivity().unregisterReceiver(mReceiver);
//        stopSearchBluetoothDevice();
//        if(bluetoothAdapter != null)
//            bluetoothAdapter.disable();//关闭蓝牙
//    }
//
//    class BannerAdapter extends PagerAdapter{
//
//        /**
//         * 轮播 图片资源id
//         */
//        List<Integer> idList;
//
//        public List<Integer> getList(){
//            if(idList == null)
//                return  new ArrayList<>();
//            return idList;
//        }
//
//
//        @Override
//        public int getCount() {
//            //设置成最大，使用户看不到边界
//            if(idList != null && idList.size() == 1)
//                return  1;
//            if(idList != null && idList.size() == 0)
//                return 0;
//            return idList == null ? 0 : Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position,
//                                Object object) {
//            //Warning：不要在这里调用removeView
//            container.removeView((View) object);
//        }
//        private int mChildCount = 0;
//
//        @Override
//        public void notifyDataSetChanged() {
//            mChildCount = getCount();
//            super.notifyDataSetChanged();
//        }
//
//        @Override
//        public int getItemPosition(Object object)   {
//            if ( mChildCount > 0) {
//                mChildCount --;
//                return POSITION_NONE;
//            }
//            return super.getItemPosition(object);
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            //对ViewPager页号求模取出View列表中要显示的项
//            position %= idList.size();
//            ImageView imageView = new ImageView(getActivity());
//            container.addView(imageView);
//            imageView.setImageResource(idList.get(position));
//            imageView.setTag(idList.get(position));
//            return imageView;
//
//        }
//
//    }
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
//        private WeakReference<SynDevicesDataFragment> weakReference;
//        private int currentItem = 0;
//
//        protected ImageHandler(WeakReference<SynDevicesDataFragment> wk){
//            weakReference = wk;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            SynDevicesDataFragment activity = weakReference.get();
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
