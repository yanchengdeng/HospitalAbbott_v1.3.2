package com.comvee.hospitalabbott.helper;

import android.content.SharedPreferences;

import com.comvee.hospitalabbott.XTYApplication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by F011512088 on 2018/3/5.
 */

public class AbbottHelper {

    private final static String fileName = "AbbottInfo";

    private static final String SID = "SID";//血糖ID
    private static final String VALUE = "VALUE";//血糖值
    private static final String READ_TIME = "READ_TIME";//记录时间

    public static void putAbbottBlood(String sId, float values, String readTime) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SID, sId);
        editor.putFloat(VALUE, values);
        editor.putString(READ_TIME, readTime);
        editor.commit();
    }

    public static String getSId() {
        return getString(SID);
    }

    public static Float getValue() {
        return getFloat(VALUE);
    }

    public static String getReadTime() {
        return getString(READ_TIME);
    }

    private static String getString(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return sharedPreferences.getString(tag, "");
    }

    private static Float getFloat(String tag) {
        SharedPreferences sharedPreferences = XTYApplication.getInstance().
                getSharedPreferences(fileName, 0);
        return sharedPreferences.getFloat(tag, 0);
    }

//    public static String getMachineNo() {
//        SharedPreferences sharedPreferences = XTYApplication.getInstance().
//                getSharedPreferences(fileName, 0);
//        String machineNo = sharedPreferences.getString("MachineNo", "");
//        if (TextUtils.isEmpty(machineNo)) {
//            machineNo = "s" + System.currentTimeMillis() + testRandom();//时间戳+随机生成10位不重复字符串
//            setMachineNo(machineNo);
//        }
//        return machineNo;
//    }

    //    private static void setMachineNo(String machineNo) {
//        SharedPreferences sharedPreferences = XTYApplication.getInstance().
//                getSharedPreferences(fileName, 0);
//        sharedPreferences.edit().putString("MachineNo", machineNo).commit();
//    }
    //在一定范围内生成不重复的随机数
    //在testRandom2中生成的随机数可能会重复.
    //在此处避免该问题
    private static String testRandom() {
        StringBuffer buffer = new StringBuffer();
        HashSet<Integer> set = new HashSet<Integer>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomInt = random.nextInt(50);
            if (!set.contains(randomInt)) {
                set.add(randomInt);
            }
        }

        for (Iterator it = set.iterator(); it.hasNext(); ) {
            buffer.append(it.next() + "");
        }
        return buffer.toString();
    }



}
