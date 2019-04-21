package com.comvee.hospitalabbott.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by F011512088 on 2017/4/1.
 */
@Entity
public class RecordBean{

    @Id
    private Long id;

    private String time;
    private String memberId;

    public RecordBean(String time) {
        this.time = time;
    }

    @Generated(hash = 1317378573)
    public RecordBean(Long id, String time, String memberId) {
        this.id = id;
        this.time = time;
        this.memberId = memberId;
    }

    @Generated(hash = 96196931)
    public RecordBean() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
