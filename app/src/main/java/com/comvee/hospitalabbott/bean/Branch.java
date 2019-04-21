package com.comvee.hospitalabbott.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.adapter.GroupBedAdapter;

/**
 * Created by F011512088 on 2018/1/2.
 */

public class Branch extends AbstractExpandableItem<HospitalBed> implements MultiItemEntity {

    private String departmentId;
    private String departmentName;

    public Branch(String departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public int getItemType() {
        return GroupBedAdapter.TYPE_DEPARTMENT;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
