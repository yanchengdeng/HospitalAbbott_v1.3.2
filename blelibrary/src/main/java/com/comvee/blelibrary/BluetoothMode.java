package com.comvee.blelibrary;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.comvee.blelibrary.commands.BleMeterSerialData;
import com.comvee.blelibrary.commands.SyncCommon;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 蓝牙管理器
 * Created by gary on 2016/11/8.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothMode {

    private Context context;

    private String NAME = "Bluetooth";
    /**
     * 蓝牙适配器
     */
    private BluetoothAdapter bluetoothAdapter = null;
    /**
     * 会调接口
     */
    private onRequestListener onRequestListener;

    private state ContionState = state.notContion;
    /**
     * 建立通讯
     */
    private BluetoothGatt bluetoothGatt;


    public BluetoothGattCharacteristic gattNoticCharacteristic;
    public BluetoothGattCharacteristic gattWriteCharacteristic;
    public BluetoothGattCharacteristic gattReadCharacteristic;
    public byte[] byteArrayMessage = null;

    private Handler handler;


    enum state {
        contion, notContion
    }

    /**
     * 默认构造函数
     */
    public BluetoothMode(Context context, BluetoothAdapter adapter) {
        this.bluetoothAdapter = adapter;
        this.context = context;
        handler = new Handler(context.getMainLooper());
    }


    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    /**
     * 获取到请求的回调接口
     */
    public interface onRequestListener {
        public void onRequest(boolean isOk, byte[] data, int len, String msg);

        public void onCreate(boolean isOk, String string);
    }


    /**
     * 连接设备
     */
    public void conntion(Context context, BluetoothDevice bluetoothDevice, onConnectListneer connectListneer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothDevice.connectGatt(context, true, gattCallback);
            onConnectListneer = connectListneer;
            return;
        }
        onConnectListneer.connectFailed();
    }


    private onConnectListneer onConnectListneer;

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {

            super.onCharacteristicChanged(gatt, characteristic);
            //收到设备notify值 （设备上报值）
            onConnectListneer.onCharacteristicChanged(gatt, characteristic);
//            Log.e("jiahui", "red:______" + BleMeterSerialData.byteArrayToHexaStr(characteristic.getValue()));

            for (Listneer l : listneers) {
                l.onCharacteristicChanged(gatt, characteristic);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            //读取到值
            onConnectListneer.onReadData(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //write成功（发送值成功）
                onConnectListneer.senDataSuccess();
            }

        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    // 连接成功
                    bluetoothGatt = gatt;
                    gatt.discoverServices();
                } else if (newState == BluetoothGatt.STATE_DISCONNECTING) {
                    // 断开连接
                    onConnectListneer.connectFailed();
                }
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //获取到RSSI，  RSSI 正常情况下 是 一个 负值，如 -33 ； 这个值的绝对值越小，代表设备离手机越近
                //通过mBluetoothGatt.readRemoteRssi();来获取
                Log.e(BluetoothMode.this.getClass() + "df", "rssi:" + rssi);
            }
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                displayGattServices(getSupportedGattServices());
                //寻找到服务
                onConnectListneer.onServicesDiscovered(gatt, status);
                setCharacteristicNotification(gattNoticCharacteristic, true);
                ThreadHandler.postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onConnectListneer != null)
                            onConnectListneer.onConnectSuccess();
                    }
                }, 500);
            } else {
                onConnectListneer.onServicesDiscoveredFailed();
            }
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        for (BluetoothGattService gattService : gattServices) { // 遍历出gattServices里面的所有服务
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) { // 遍历每条服务里的所有Characteristic
//                if (gattCharacteristic.getUuid().toString().equalsIgnoreCase("af9df7a3-e595-11e3-96b4-0002a5d5c51b")) {
                if (gattCharacteristic.getUuid().toString().equalsIgnoreCase("6e400003-b5a3-f393-e0a9-e50e24dcca9e")) {

                    gattNoticCharacteristic = gattCharacteristic;
                    gattReadCharacteristic = gattCharacteristic;
                } else if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(SyncCommon.UUID_OT_BTLEMETER_GENERAL_UART_RX_WRITE_CHARACTERISTIC.toString())) {
                    gattWriteCharacteristic = gattCharacteristic;
                }
                Log.e(getClass().getName(), "mServerUUid = " + gattCharacteristic.getService().getUuid());
                Log.e(getClass().getName(), "mUUid = " + gattCharacteristic.getUuid());
            }
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (bluetoothGatt == null)
            return null;
        return bluetoothGatt.getServices();   //此处返回获取到的服务列表
    }

    /**
     * 启动 关闭 通知
     *
     * @param characteristic
     * @param enabled
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Log.w(getClass().getName(), "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(SyncCommon.UUID_CLIENT_CHARACTERISTIC_CONFIGURATION);
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /***
     * 读操作
     ***/
    public void readBatrery(BluetoothGatt gatt) {

    }

    private Calendar calendar;

    /***
     * 写操作
     ***/
    public void sendSetting(final BluetoothGatt gatt, final byte[] data) {
        if (gatt == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            int sleepTime = 100;
            if (data.length < 5) {
                sleepTime = 0;
            } else if (data.length > 5 && data.length < 12) {
                sleepTime = 10;
            } else {
                sleepTime = 300;
            }
            if (calendar != null) {
                long time = 0;
                Calendar calendar1 = Calendar.getInstance();
                time = calendar.getTimeInMillis() - calendar1.getTimeInMillis();
                if (data.length > 5 && time < 50 && sleepTime < 11) {
                    sleepTime += 38;
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gattWriteCharacteristic.setValue(data);
                    long time = 0;
                    if (calendar != null) {
                        Calendar calendar1 = Calendar.getInstance();
                        time = calendar.getTimeInMillis() - calendar1.getTimeInMillis();
                        calendar = calendar1;
                    } else {
                        calendar = Calendar.getInstance();
                    }
                    Log.e("jiahui", "sen:______" + BleMeterSerialData.byteArrayToHexaStr(data) + "---" + time);
                    gatt.writeCharacteristic(gattWriteCharacteristic);
                    if (listneers != null)
                        for (Listneer l : listneers) {
                            if (l != null)
                                l.writeNotify(data);
                        }
                }
            }, sleepTime);

        }
    }

    /**
     * 连接蓝牙
     *
     * @param bytes
     */
    public void senData(byte[] bytes) {
        sendSetting(bluetoothGatt, bytes);
    }


    /**
     * 观察者
     */
    private Set<Listneer> listneers;

    /**
     * 订阅
     *
     * @param onListneer
     */
    public void subject(Listneer onListneer) {
        if (listneers == null)
            listneers = new HashSet<>();
        listneers.add(onListneer);
    }

    /**
     * 事件回调
     */
    public interface Listneer {
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic);

        public void writeNotify(byte[] bytes);
    }

    /**
     * 连接终端事件
     */
    public interface onConnectListneer {
        public void onConnectSuccess();

        public void connectFailed();

        public void senDataSuccess();

        public void senDataFailed();

        public void onReadData(BluetoothGatt gatt,
                               BluetoothGattCharacteristic characteristic, int status);

        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic);

        public void onServicesDiscovered(BluetoothGatt gatt, int status);

        public void onServicesDiscoveredFailed();
    }


    /**
     * 通讯事件回掉
     */
    public interface onConnectedListener {
        public void onSenDataSuccess();

        public void onSenDataFailed();
    }


    public void cancel() {
        if (bluetoothGatt != null)
            bluetoothGatt.close();
    }

}
