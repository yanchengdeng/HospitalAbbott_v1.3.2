package com.comvee.hospitalabbott.tool;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityMrg {
    private static ActivityMrg instance;
    private List<Activity> list = new ArrayList<>();

    private ActivityMrg() {
    }

    public static ActivityMrg getInstance() {    //对获取实例的方法进行同步
        if (instance == null) {
            synchronized (ActivityMrg.class) {
                if (instance == null)
                    instance = new ActivityMrg();
            }
        }
        return instance;
    }

    public void addActivity(Activity activity){
        list.add(activity);
    }

    public void removeActivity(Activity activity){
        list.remove(activity);
    }

    public Activity getCurrentActivity(){
        if (list.size() == 0){
            return null;
        }
        return list.get(list.size() -1);
    }
}
