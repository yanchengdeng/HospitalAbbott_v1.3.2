package com.comvee.hospitalabbott.tool;

import android.content.Context;
import android.widget.Toast;

import com.comvee.hospitalabbott.XTYApplication;


/**
 * Created by F011512088 on 2018/1/3.
 */

public class ToastUtil {
    public static void showToast(String toast) {
        Toast.makeText(XTYApplication.getInstance(), toast, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String toast,  int duration) {
        Toast.makeText(context, toast, duration).show();
    }
}
