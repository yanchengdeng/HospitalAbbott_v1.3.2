package com.comvee.hospitalabbott.ui.main;

import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.base.CallObserver;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.helper.BedHelper;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.model.TitleData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by F011512088 on 2018/1/25.
 */
public class TaskIndexPresenter extends BasePresenter<TaskIndexViewController> {

    public int pageSelected = -1;
    public String showParamCode = TestResultDataUtil.PARAM_CODE_0AM;
    private List<TitleData> screeList = new ArrayList<>();

    private BedHelper bedHelper;
    private CallObserver observer;
    private MemberCountBean smbgCount;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param model
     */
    public TaskIndexPresenter(TaskIndexViewController model) {
        super(model);
    }

    @Override
    public void initData() {
        showParamCode = TestResultDataUtil.getParamCode(new Date());
        screeList.clear();
        if (smbgCount == null) {
            List<String> mData = TestResultDataUtil.getTimeStr();
            for (int i = 0; i < mData.size(); i++) {
                String time = mData.get(i);
                screeList.add(new TitleData(time, 0, i));
            }
        } else {
            upDataSmbgCount(smbgCount);
        }
        bedHelper = new BedHelper();
    }

    public void refreshParam() {
        showParamCode = TestResultDataUtil.getParamCode(new Date());
        for (int i = 0; i < screeList.size(); i++) {
            TitleData titleData = screeList.get(i);
            if (titleData.getTitle().equals(TestResultDataUtil.getPeriod(showParamCode))) {
                viewModel.upDataMagicIndicator(i);
            }
        }
    }

    public void switchPages(int position) {
        if (pageSelected == position) {
            return;
        }
      /*  if (viewModel != null)
            viewModel.autoRefresh();*/
        pageSelected = position;
        showParamCode = screeList.get(pageSelected).getParamCode();

        getLocalFilterRowBean(showParamCode, 0);
//        Observable.just(1)
//                .throttleFirst(100, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        Log.e("switchPages", "--accept--");
//                        getLocalFilterRowBean(showParamCode, 0);
//                    }
//                });
    }

    /**
     * 发起异步查询本地数据库返回筛选后的最新数据
     */
    public void getLocalFilterRowBean(String paramCode, int concernStatus) {
        if (observer != null) {
            observer.disposable();
            observer = null;
        }
        observer = new CallObserver<List<MultiItemEntity>>() {

            @Override
            public void onNext(List<MultiItemEntity> multiItemEntities) {
                if (viewModel != null)
                    viewModel.setAdapterList(multiItemEntities);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("getLocalFilterRowBean ", "Message: " + e.getMessage());
                if (viewModel != null)
                    viewModel.onRefreshError();
            }
        };
        if (bedHelper == null)
            bedHelper = new BedHelper();
        bedHelper.getBedList(paramCode, concernStatus)
                .subscribe(observer);
    }

    /**
     * 更新未测量数据
     *
     * @param memberCountBean
     */
    public void upDataSmbgCount(MemberCountBean memberCountBean) {
        if (memberCountBean != null && memberCountBean.getCountList() != null) {
            MemberCountBean.CountListBean countBean = memberCountBean.getCountList();
            if (countBean == null) return;
            screeList.clear();
            TitleData titleData = new TitleData(TestResultDataUtil.TIME_0AM, countBean.getBeforedawn(), 0);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_3AM, countBean.getBeforedawn(), 1);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_FAST, countBean.getBeforeBreakfast(), 2);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_AFTER_BREAKFAST, countBean.getAfterBreakfast(), 3);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_BEFORE_LUNCH, countBean.getBeforeLunch(), 4);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_AFTER_LUNCH, countBean.getAfterLunch(), 5);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_BEFORE_DINNER, countBean.getBeforeDinner(), 6);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_AFTER_DINNER, countBean.getAfterDinner(), 7);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_BEFORE_BED, countBean.getBeforeSleep(), 8);
            screeList.add(titleData);
            titleData = new TitleData(TestResultDataUtil.TIME_RANDOM, countBean.getRandomtime(), 9);
            screeList.add(titleData);
        }
    }

    public String getShowParamCode() {
        return showParamCode;
    }

    public List<TitleData> getScreeList() {
        return screeList;
    }

    public void setSmbgCount(MemberCountBean smbgCount) {
        this.smbgCount = smbgCount;
    }
}
