package com.comvee.hospitalabbott.network.rxbus;
/**
 * Created by xingkong on 2018/5/14.
 */

public class IndexRefreshRxModel {
    public IndexRefreshRxModel() {
    }

    public IndexRefreshRxModel(String tag, String concernStatus) {
        this.tag = tag;
        this.concernStatus = concernStatus;
    }

    public String tag = "";
    public String concernStatus = "";
}
