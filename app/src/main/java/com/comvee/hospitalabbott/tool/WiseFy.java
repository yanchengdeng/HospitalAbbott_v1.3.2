/**
 * Copyright 2017 Patches Klinefelter
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comvee.hospitalabbott.tool;


import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Main class to manipulate and query network settings on an Android device
 * <p>
 * Uses the builder pattern for creation - {@link withContext}
 */
public class WiseFy {

    private static final String TAG = WiseFy.class.getSimpleName();

    ConnectivityManager mConnectivityManager;

    WifiManager mWifiManager;

    /**
     * Private constructor that accepts builder input
     * 接受构建器输入的私有构造函数
     */
    private WiseFy(withContext withContext) {
        this.mConnectivityManager = GetManagerUtil.getInstance().getConnectivityManager(withContext.context);
        this.mWifiManager = GetManagerUtil.getInstance().getWiFiManager(withContext.context);
    }

    /**
     * Static class for builder pattern
     * 构建器模式的静态类
     * <p>
     * Implements builder interfaces #{@link Logging} #{@link GetSmarts}
     * 实现builder接口
     */
    public static class withContext implements Logging, GetSmarts {

        private Context context;

        private boolean loggingEnabled;

        /**
         * Mandatory - The public constructor for the builder that requires a context
         *
         * @param context - The activity or application context to get a WifiConfiguration and
         *                ConnectivityManager instance
         */
        public withContext(Context context) {
            this.context = context;
        }

        /**
         * Mandatory - To build and return a WiseFy instance
         * <p>
         * Must be called after withContext
         * {@link #withContext(Context)}
         *
         * @return WiseFy - The instance created by the builder
         */
        @Override
        public WiseFy getSmarts() {
            return new WiseFy(this);
        }

        /**
         * Optional - Builder method that enables/disables logging for a WiseWy instance
         *
         * @param loggingEnabled - If logging is enabled or disabled for an instance
         *                       {@link Logging}
         * @return withContext - The builder with updated logging setting
         */
        @Override
        public withContext logging(boolean loggingEnabled) {
            this.loggingEnabled = loggingEnabled;
            return this;
        }
    }

    /**
     * An interface that enables/disables logging for a WiseFy instance
     */
    interface Logging {
        withContext logging(boolean loggingEnabled);
    }

    /**
     * An interface that builds a WiseFy instance
     */
    interface GetSmarts {
        WiseFy getSmarts();
    }


    /**
     * To disable Wifi on a user's device
     * 关闭WIFI
     *
     * @return boolean - If the command succeeded in disabling wifi
     */
    public boolean disableWifi() {
        Log.d(TAG, "Disabling WiFi");
        if (mWifiManager != null) {
            return mWifiManager.setWifiEnabled(false);
        } else {
            Log.e(TAG, "No mWifiManager to disable Wifi");
        }
        return false;
    }


    /**
     * To enable Wifi on a user's device
     *
     * @return boolean - If the command succeeded in enabling wifi
     */
    public boolean enableWifi() {
        Log.d(TAG, "Enabling WiFi");
        if (mWifiManager != null) {
            return mWifiManager.setWifiEnabled(true);
        } else {
            Log.e(TAG, "No mWifiManager to enable wifi");
        }
        return false;
    }


    /**
     * 让一个网络连接失效
     *
     * @param netId
     */
    public void disableNetwork(int netId) {
        mWifiManager.disableNetwork(netId);
    }

    /**
     * 连接netId所指的WIFI网络，并是其他的网络都被禁用
     *
     * @param netId
     * @param disableOthers
     */
    public boolean enableNetwork(int netId, Boolean disableOthers) {
        return mWifiManager.enableNetwork(netId, disableOthers);
    }

    /**
     * 获取加密类型
     *
     * @param capabilities
     * @return
     */
    public String getScanResultSecurity(String capabilities) {
        final String[] securityModes = {"WEP", "PSK", "EAP"};
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (capabilities.contains(securityModes[i])) {
                return securityModes[i];
            }
        }
        return "OPEN";
    }

}