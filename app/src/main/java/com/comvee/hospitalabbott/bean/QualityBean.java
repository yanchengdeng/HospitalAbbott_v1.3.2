package com.comvee.hospitalabbott.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by F011512088 on 2017/4/25.
 */

@Entity
public class QualityBean {

    /**
     * high : 7.2
     * level : 2
     * low : 5.4
     */
    @Id
    private Long id;

    private String high;
    private String level;
    private String low;

    public QualityBean(String high, String level, String low) {
        this.high = high;
        this.level = level;
        this.low = low;
    }

    @Generated(hash = 774532631)
    public QualityBean(Long id, String high, String level, String low) {
        this.id = id;
        this.high = high;
        this.level = level;
        this.low = low;
    }

    @Generated(hash = 281976286)
    public QualityBean() {
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
