package com.comvee.hospitalabbott.bean;

import com.comvee.hospitalabbott.helper.HistoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by F011512088 on 2018/1/15.
 */

public class HistoryCountModel {

    private int high = 0;
    private int normal = 0;
    private int low = 0;
    private List<MemberHistoryModel> historyModels = new ArrayList<>();

    public List<MemberHistoryModel> getHistoryModels() {
        return historyModels;
    }

    public void setHistoryModels(List<MemberHistoryModel> historyModels) {
        this.historyModels = historyModels;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public void setCountMap(Map<String, Integer> countMap) {
        normal += countMap.get(HistoryHelper.COUNT_NORMAL);
        high += countMap.get(HistoryHelper.COUNT_HIGH);
        low += countMap.get(HistoryHelper.COUNT_LOW);
    }
}
