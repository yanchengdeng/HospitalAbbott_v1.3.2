package com.comvee.hospitalabbott.tool;

import java.util.HashMap;

/**
 * activity之间通讯工具类
 * 实现：全局实例单一实例，在实现通讯接口activity设置接口，再调用地方调用即可。
 * Created by F011512088 on 2018/3/2.
 */

public class ActivityCommunicationUtil {

    private static ActivityCommunicationUtil communication;

    public static ActivityCommunicationUtil getSingle() {
        if (communication == null)
            communication = new ActivityCommunicationUtil();
        return communication;
    }

    private HashMap<String, BaseCallback> callbackHashMap = new HashMap<>();

    public ActivityCommunicationUtil() {
    }

    public void setCallback(String key, BaseCallback callback) {
        if (callbackHashMap == null) {
            callbackHashMap = new HashMap<>();
        }
        if (callbackHashMap.containsKey(key)) {//判断key是否存在
            callbackHashMap.remove(key);
        }
        callbackHashMap.put(key, callback);
    }

    public BaseCallback getCallback(String key) {
        if (callbackHashMap.containsKey(key)) {//判断key是否存在
            return callbackHashMap.get(key);
        }
        return null;
    }

    public interface BaseCallback<T> {
        void onCall(T model);
    }

}
