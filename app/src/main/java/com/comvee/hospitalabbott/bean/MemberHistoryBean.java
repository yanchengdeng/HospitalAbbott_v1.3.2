package com.comvee.hospitalabbott.bean;


import com.comvee.greendao.gen.DaoSession;
import com.comvee.greendao.gen.MemberHistoryBeanDao;
import com.comvee.greendao.gen.ParamLogListBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by comv098 on 2017/2/19.
 */
@Entity
public class MemberHistoryBean {

    /**
     * date : 2017-02-19
     * paramLogList : [{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 17:06:51","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 17:06:51","paramCode":"randomtime","paramLogId":"170219170600007","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 17:06:37","remark":"","value":"30.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 17:04:22","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 17:04:22","paramCode":"afterDinner","paramLogId":"170219170400005","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 17:04:08","remark":"","value":"11.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 16:53:48","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 16:53:48","paramCode":"beforedawn","paramLogId":"170219165300002","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 16:53:33","remark":"","value":"21.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 16:45:24","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 16:45:24","paramCode":"beforeBreakfast","paramLogId":"170219164500001","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 16:45:10","remark":"","value":"19.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 16:11:03","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 16:11:03","paramCode":"beforeDinner","paramLogId":"170219161000007","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 16:10:49","remark":"","value":"14.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 16:06:58","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 16:06:58","paramCode":"afterBreakfast","paramLogId":"170219160600005","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 16:06:44","remark":"","value":"21.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 15:55:33","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 15:55:33","paramCode":"beforeLunch","paramLogId":"170219155500001","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 15:55:19","remark":"","value":"22.0"},{"batchId":"","innerCode":"","innerId":"1","insertDt":"2017-02-19 14:29:33","isValid":"1","level":"5","memberId":"1","modifyDt":"2017-02-19 14:29:33","paramCode":"beforedawn","paramLogId":"170219142900002","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordOrigin":"XTY_ORIGIN","recordTime":"2017-02-19 14:28:11","remark":"","value":"15"}]
     */
    @Id
    private Long id;

    private String memberId;
    private String date;

    //    @ToMany(referencedJoinProperty = "ownerId")
    @Keep
    @ToMany(joinProperties = {
            @JoinProperty(name = "date", referencedName = "recordDt"),
            @JoinProperty(name = "memberId", referencedName = "memberId")
    })
    private List<ParamLogListBean> paramLogList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1531796637)
    private transient MemberHistoryBeanDao myDao;


    @Generated(hash = 231234834)
    public MemberHistoryBean(Long id, String memberId, String date) {
        this.id = id;
        this.memberId = memberId;
        this.date = date;
    }

    @Generated(hash = 1647609922)
    public MemberHistoryBean() {
    }

    public MemberHistoryBean(String date, String memberId) {
        this.date = date;
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setParamLogList(List<ParamLogListBean> paramLogList) {
        this.paramLogList = paramLogList;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 46931443)
    public List<ParamLogListBean> getParamLogList() {
        if (paramLogList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ParamLogListBeanDao targetDao = daoSession.getParamLogListBeanDao();
            List<ParamLogListBean> paramLogListNew = targetDao._queryMemberHistoryBean_ParamLogList(date, memberId);
            synchronized (this) {
                if (paramLogList == null) {
                    paramLogList = paramLogListNew;
                }
            }
        }
        return paramLogList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 948409058)
    public synchronized void resetParamLogList() {
        paramLogList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 775465442)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMemberHistoryBeanDao() : null;
    }

    @Override
    public String toString() {
        return "MemberHistoryBean{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
