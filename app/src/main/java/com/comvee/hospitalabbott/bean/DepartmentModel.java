package com.comvee.hospitalabbott.bean;


import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import com.comvee.greendao.gen.DaoSession;
import com.comvee.greendao.gen.DepartmentModelDao;
import com.comvee.greendao.gen.MemberModelDao;

/**
 * Created by F011512088 on 2017/4/14.
 */
@Entity
public class DepartmentModel{
    @Id
    private Long id;

    private String departmentId;
    private String departmentName;

    @ToMany(joinProperties = {
            @JoinProperty(name = "departmentId", referencedName = "departmentId")
    })
    private List<MemberModel> bedModelList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2099098033)
    private transient DepartmentModelDao myDao;


    @Generated(hash = 1957952264)
    public DepartmentModel(Long id, String departmentId, String departmentName) {
        this.id = id;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    @Generated(hash = 963575298)
    public DepartmentModel() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1947911278)
    public List<MemberModel> getBedModelList() {
        if (bedModelList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MemberModelDao targetDao = daoSession.getMemberModelDao();
            List<MemberModel> bedModelListNew = targetDao
                    ._queryDepartmentModel_BedModelList(departmentId);
            synchronized (this) {
                if (bedModelList == null) {
                    bedModelList = bedModelListNew;
                }
            }
        }
        return bedModelList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 151445833)
    public synchronized void resetBedModelList() {
        bedModelList = null;
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
    @Generated(hash = 1826945814)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDepartmentModelDao() : null;
    }


}
