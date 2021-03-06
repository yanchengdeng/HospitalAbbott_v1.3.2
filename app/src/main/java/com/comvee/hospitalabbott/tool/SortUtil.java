package com.comvee.hospitalabbott.tool;

import android.text.TextUtils;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortUtil {
    /** 
     * 将字符串中的数字转换为 指定 长度的数字串 
     *  
     * @param content 
     * @return 返回转换后的字符串 
     */  
    public static String changeIntToSpecifyLength(String content) {  
  
        List<String> intMatchList = new ArrayList<String>();// 匹配的数字的List
        List<String> unIntSubList = new ArrayList<String>();// 截取的非数字的List  
        List<String> intChangeList = new ArrayList<String>();// 匹配的数字转换后的List  
  
        // 表达式的功能：验证必须为数字（整数或小数）  
        String pattern = "[0-9]+([0-9]+)?";  
        // 对()的用法总结：将()中的表达式作为一个整体进行处理，必须满足他的整体结构才可以。  
        // (.[0-9]+)? ：表示()中的整体出现一次或一次也不出现  
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
  
        int unIntSubStartIndex = 0; // 非数子截取的开始index，默认为0  
  
        while (m.find()) {  
            intMatchList.add(m.group());  
  
            int unIntSubEndIndex = 0; // 本次非数字截取的结束index（值是数字匹配的开始），默认为0  
            if (m.start() != 0) {  
                unIntSubEndIndex = m.start();  
            }  
  
            // 截取 非数字 字符串  
            String sub = content  
                    .substring(unIntSubStartIndex, unIntSubEndIndex);  
            unIntSubList.add(sub);  
  
            // 本次数字匹配的结束index是 下一次 非数字截取的开始  
            unIntSubStartIndex = m.end();  
        }  
  
        // 将匹配的数字转换为 指定长度的数字  
        if (intMatchList != null && !intMatchList.isEmpty()) {  
            System.out.println(intMatchList.toString());  
            for (String string : intMatchList) {  
                String changeStr = changeIntLength(string, 9);  
                intChangeList.add(changeStr);  
            }  
        }  
  
        System.out.println(unIntSubList.toString());  
        System.out.println(intChangeList.toString());  
  
        // 将数据拼接  
        String temp = "";  
        if (unIntSubList != null && unIntSubList != null  
                && unIntSubList.size() == intChangeList.size()) {  
            for (int i = 0; i < intMatchList.size(); i++) {  
                // 非数字 + 转化后的数字  
                temp += unIntSubList.get(i) + intChangeList.get(i);  
            }  
  
            content = temp;  
        }  
  
        return content;  
  
    }  
  
    /** 
     * 将数字转换为指定长度的数字 
     *  
     * @param value 
     * @param retLength 
     * @return 返回指定长度的数字 字符串 
     */  
    private static String changeIntLength(String value, int retLength) {  
        String ret = value;  
        if (value != null && !value.equals("")) {  
            int intValue = Integer.valueOf(value);  
  
            char[] cc = new char[retLength];  
            int i = 0;  
            for (i = 0; i < retLength; i++) {  
                cc[i] = '0';  
            }  
  
            DecimalFormat df = new DecimalFormat(new String(cc));
            ret = df.format(intValue);  
        }  
  
        return ret;  
    }

    public static float getBedNumNew(String bn){
        if (TextUtils.isEmpty(bn)) {
            return 0;
        }
        float numF = 0;
        if (bn.contains("加") || bn.contains("抢")){
            String num = bn.substring(1);
            numF = Integer.parseInt(num);
            numF = numF + 0.1f;
        }else {
            numF = Integer.parseInt(bn);
        }
        return numF;
    }
}  