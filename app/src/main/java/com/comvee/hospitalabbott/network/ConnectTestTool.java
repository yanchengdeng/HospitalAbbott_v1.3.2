package com.comvee.hospitalabbott.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.widget.Toast;

import java.net.InetAddress;

import okhttp3.internal.Util;

public class ConnectTestTool {
    /**
     * wifi获取 路由ip地址
     *
     * @param context
     * @return
     */
    public static String getWifiRouteIPAddress(Context context) {
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
        return routeIp;
    }


    public static String getWifiIp(Context myContext) {
        if (myContext == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if (isWifiEnabled(myContext)) {
            int ipAsInt = wifiMgr.getConnectionInfo().getIpAddress();
            if (ipAsInt == 0) {
                return null;
            } else {
                return Formatter.formatIpAddress(ipAsInt);
            }
        } else {
            return null;
        }
    }

    public static boolean isWifiEnabled(Context myContext) {
        if (myContext == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) myContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * =========通过ip ping 来判断ip是否通
     *
     * @param ip
     */
    public static void judgeTheConnect(String ip,Context context) {

        try {

            if (ip != null) {
                //代表ping 3 次 超时时间为10秒
                Process p = Runtime.getRuntime().exec("ping -c 3 -w 10 " + ip);//ping3次
                int status = p.waitFor();
                if (status == 0) {
                    //代表成功
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                } else {
                    //代表失败
                    Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                //代表失败
                Toast.makeText(context, "IP为空", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
