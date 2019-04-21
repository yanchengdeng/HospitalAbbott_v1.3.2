package com.comvee.hospitalabbott.helper;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.LoginModel;
import com.comvee.hospitalabbott.network.NetWorkManger;
import com.comvee.hospitalabbott.tool.Utils;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 一个存储和获取用户信息的辅助工具类
 *
 * @author 缪培松
 */
public class UserHelper {

    private final static String fileName = "DoctorInfo";

    public static final String refreshBedTime = "refreshBedTime";
    private final static String isLoginTag = "isLogin";
    private final static String DoctorInfo = "DoctorInfo";
    private final static String isInitBed = "isInitBed";

    private final static String userNameTag = "userNameSet";

    public final static String upDataRemind = "upDataRemind";

    public static final String MachineId = "MachineId";
    public static final String LastUserNo = "LastUserNo";
    public static final String LastUserPasswordMD5 = "LastUserPasswordMD5";

    public static final String isSound = "isSound";

    public static boolean isLogin() {
        return getBoolean(isLoginTag);
    }

    public static void setIsLogin(boolean isLogin) {
        putBoolean(isLoginTag, isLogin);
    }

    /**
     * @return 服务器地址
     */
    public static String getBaseUrl() {
        String baseUrl = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0).getString("baseUrl", "");
        return baseUrl;
    }

    /**
     * 设置服务器地址
     */
    public static void setBaseUrl(String str) {
        if (str.indexOf("http://") == 0) {//包含http:// 判断最好末尾字符串是否是/
            putString("baseUrl", str.endsWith("/") ? str : str + "/");
        } else {
            putString("baseUrl", str.endsWith("/") ? "http://" + str : "http://" + str + "/");
        }
        NetWorkManger.refreshNetWorkManger();
    }

    public static void setMachineId(String machineId) {
        putString(MachineId, machineId);
    }

    public static String getMachineId() {
        return getString(MachineId);
    }

    public static void setSessionToken(String sessionToken) {
        putString("sessionToken", sessionToken);
    }

    public static String getSessionToken() {
        return getString("sessionToken");
    }

    public static int getInt(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance()
                .getSharedPreferences(fileName, 0);
        return sharedPreferences.getInt(tag, -1);
    }

    public static int getInt(String tag, int defValue) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance()
                .getSharedPreferences(fileName, 0);
        return sharedPreferences.getInt(tag, defValue);
    }

    public static boolean getBoolean(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance()
                .getSharedPreferences(fileName, 0);
        return sharedPreferences.getBoolean(tag, false);
    }

    public static boolean getBoolean(String tag, boolean is) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance()
                .getSharedPreferences(fileName, 0);
        return sharedPreferences.getBoolean(tag, is);
    }


    public static float getFloat(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return sharedPreferences.getFloat(tag, -1);
    }

    public static Long getLong(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return sharedPreferences.getLong(tag, 0);
    }

    public static String getString(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return sharedPreferences.getString(tag, "");
    }

    public static Set<String> getStringSet(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return new HashSet<>(sharedPreferences.getStringSet(tag, new HashSet<String>()));
    }


    public static Map<String, ?> getAll() {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return sharedPreferences.getAll();
    }

    public static void putBoolean(String tag, boolean values) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(tag, values);
        editor.commit();
    }

    public static void putString(String tag, String values) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, values);
        editor.commit();
    }

    public static void putInt(String tag, int values) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(tag, values);
        editor.commit();
    }

    public static void putLong(String tag, long values) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(tag, values);
        editor.commit();
    }

    public static void putStringSet(String tag, Set<String> values) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(tag, values);
        editor.commit();
    }

    //获取用户信息
    public static LoginModel.DoctorInfoBean getDoctorInfo() {
        if (!TextUtils.isEmpty(UserHelper.getString(DoctorInfo))) {
            LoginModel loginModel = new Gson().fromJson(UserHelper.getString(DoctorInfo), LoginModel.class);
            if (loginModel != null)
//            LogUtils.e("loginModel:" + loginModel.toString());
                return loginModel.getDoctorInfo();
        }
        return null;
    }

    public static void setDoctorInfo(LoginModel loginBean) {
        if (loginBean != null)
            putString(DoctorInfo, new Gson().toJson(loginBean));
    }

    //姓名
    public static String getUserName() {
        if (getDoctorInfo() != null)
            return getDoctorInfo().getDoctorName();
        return "";
    }

    //职位名称
    public static String getPositionName() {
        if (getDoctorInfo() != null)
            if (getDoctorInfo().getPositionName() != null)
                return getDoctorInfo().getPositionName();
        return "";
    }

    //科室
    public static String getDepartName() {
        if (getDoctorInfo() != null)
            if (getDoctorInfo().getDepartName() != null)
                return getDoctorInfo().getDepartName();
        return "";
    }


    public static String getUserId() {
        if (getDoctorInfo() != null)
            if (getDoctorInfo().getDoctorId() != null)
                return getDoctorInfo().getDoctorId();
        return "";
    }

    public static String getMachineSn() {
//        return SystemProperties.get("gsm.serial");;
        return "";
    }

    public static String getMachineNo() {
        return Utils.getDeviceID(XTYApplication.getInstance());
    }

    public static void outLogin() {
        UserHelper.putString(UserHelper.refreshBedTime, "");
        UserHelper.setMachineId("");
        UserHelper.setSessionToken("");
        UserHelper.setIsLogin(false);
        UserHelper.setInitBed(false);
        UserHelper.putString(DoctorInfo, "");
    }

    public static void setMobileDataSwitcher(boolean is) {
        putBoolean("MobileDataSwitcher", is);
    }

    public static boolean getMobileDataSwitcher() {
        return getBoolean("MobileDataSwitcher", true);
    }


    public static boolean isInitBed() {
        return XTYApplication.getInstance().
                getSharedPreferences(fileName, 0)
                .getBoolean(isInitBed, false);
    }

    public static void setInitBed(boolean isLogin) {
        XTYApplication.getInstance().
                getSharedPreferences(fileName, 0)
                .edit().putBoolean(isInitBed, isLogin).commit();
    }

}
