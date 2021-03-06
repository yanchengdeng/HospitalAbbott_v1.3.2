package com.comvee.hospitalabbott.tool;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHandler {
    private static Handler mHandler;
    private static ExecutorService mTask;

    public ThreadHandler() {
    }

    public static void init() {
        mHandler = new Handler();
        mTask = Executors.newFixedThreadPool(5);
    }

    public static void postUiThread(Runnable run) {
        mHandler.post(run);
    }

    public static void postUiThread(Runnable run, long delay) {
        mHandler.postDelayed(run, delay);
    }

    public static void postWorkThread(Runnable run) {
        mTask.execute(run);
    }
}