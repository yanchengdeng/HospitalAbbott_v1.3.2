package com.comvee.hospitalabbott.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by F011512088 on 2017/4/5.
 */

public class DayBean implements Parcelable {

    private String recordTime;
    private String paramCode;
    private List<ParamLogListBean> mData;

    public DayBean(String recordTime, String paramCode, List<ParamLogListBean> mData) {
        this.recordTime = recordTime;
        this.paramCode = paramCode;
        this.mData = mData;
//        if (mData != null)
//            if (mData.size() > 0) {
//                /**
//                 * 按照日期进行排序
//                 */
//                ComparatorMember comparator = new ComparatorMember();
//                Collections.sort(mData, comparator);
//            }
    }

    protected DayBean(Parcel in) {
        recordTime = in.readString();
        paramCode = in.readString();
        mData = in.createTypedArrayList(ParamLogListBean.CREATOR);
    }

    public static final Creator<DayBean> CREATOR = new Creator<DayBean>() {
        @Override
        public DayBean createFromParcel(Parcel in) {
            return new DayBean(in);
        }

        @Override
        public DayBean[] newArray(int size) {
            return new DayBean[size];
        }
    };

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public List<ParamLogListBean> getmData() {
        return mData;
    }

    public void setmData(List<ParamLogListBean> mData) {
        this.mData = mData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recordTime);
        dest.writeString(paramCode);
        dest.writeTypedList(mData);
    }
}
