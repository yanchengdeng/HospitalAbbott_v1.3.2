package com.comvee.hospitalabbott.bean;

/**
 * Created by F011512088 on 2017/4/18.
 */

public class DownloadModel {
    private  long total;
    private int progress;

    public DownloadModel(long total, int progress) {
        this.total = total;
        this.progress = progress;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
