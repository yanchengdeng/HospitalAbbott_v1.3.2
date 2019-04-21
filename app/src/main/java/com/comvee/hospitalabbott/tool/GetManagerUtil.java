/**
 * Copyright 2017 Patches Klinefelter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comvee.hospitalabbott.tool;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;


public class GetManagerUtil {

    private static final GetManagerUtil GET_MANAGER_UTIL = new GetManagerUtil();

    /**
     * Private constructor with no setup
     */
    private GetManagerUtil() {
    }

    /**
     * @return instance of GetManagerUtil
     */
    public static GetManagerUtil getInstance() {
        return GET_MANAGER_UTIL;
    }

    /**
     * To get a Connectivity manger instance from an activity's context.
     *
     * @see ConnectivityManager
     * @see WiseFy
     *
     * @param context - The context to use to retrieve a connectivity manager via getSystemService
     * @return ConnectivityManager|null
     */
    public ConnectivityManager getConnectivityManager(Context context) {
        if(context != null) {
            return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        } else {
            return null;
        }
    }

    /**
     * To get a WiFi manger instance from an activity's context.
     *
     * @see WifiManager
     * @see WiseFy
     *
     * @param context - The context to use to retrieve a wifi manager via getSystemService
     * @return WifiManager|null
     */
    public WifiManager getWiFiManager(Context context) {
        if(context != null) {
            return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        } else {
            return null;
        }
    }
}