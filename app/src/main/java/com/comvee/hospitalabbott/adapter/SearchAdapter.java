package com.comvee.hospitalabbott.adapter;


import android.app.Activity;
import android.view.View;

import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.MemberModel;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by comv098 on 2017/2/8.
 */

public class SearchAdapter extends QuickAdapter<SearchBean> {

    private WeakReference<Activity> weakReference;

    public SearchAdapter(List data, Activity activity) {
        super(R.layout.fragment_search_adapter, data);
        weakReference = new WeakReference<Activity>(activity);
    }

    /**
     * bedId : 1
     * bedNo : 01
     * departmentId :
     * departmentName :
     * memberId : 1
     * memberName : 沈蔚
     * roomId : 1
     * roomNo : 601
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(MyBaseViewHolder helper, final SearchBean item) {
        if (item == null) return;
        helper.setText(R.id.searchAdapter_tv_bed, item.getBedNo() + "床")
                .setText(R.id.searchAdapter_tv_name, item.getMemberName())
                .setText(R.id.searchAdapter_tv_departmentName, item.getDepartmentName());

        helper.getView(R.id.searchAdapter_main_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberModelDao modelDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
                MemberModel departmentModel = modelDao.queryBuilder()
                        .where(MemberModelDao.Properties.MemberId.eq(item.getMemberId())).unique();
                if (departmentModel != null && weakReference.get() != null) {
                    //TODO 跳转测血糖界面
                    HospitalBed bed = new HospitalBed(departmentModel);
                    TestBloodActivity.startBloodActivity(weakReference.get(), bed, "");
                    weakReference.get().finish();
                } else {
                    ToastUtil.showToast(mContext, "未在本地查找到改患者，请刷新病床界面");
                }
            }
        });
    }
}
