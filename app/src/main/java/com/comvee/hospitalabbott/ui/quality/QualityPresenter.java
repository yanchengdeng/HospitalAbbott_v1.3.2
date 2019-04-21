package com.comvee.hospitalabbott.ui.quality;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.comvee.greendao.gen.TestPaperModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.base.BaseRxActivity;
import com.comvee.hospitalabbott.bean.QualityBean;
import com.comvee.hospitalabbott.bean.QualityModel;
import com.comvee.hospitalabbott.bean.QualityResultBean;
import com.comvee.hospitalabbott.bean.TestPaperBean;
import com.comvee.hospitalabbott.bean.TestPaperModel;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;
import com.comvee.hospitalabbott.widget.popwindow.DataPopWin;
import com.comvee.hospitalabbott.widget.popwindow.OneLoopPopWin;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by F011512088 on 2018/2/24.
 */
public class QualityPresenter extends BasePresenter<QualityViewController> {

    private DataPopWin dataPopWin;
    private OneLoopPopWin rangWin;
    private OneLoopPopWin qualityWin;
    private OneLoopPopWin paperWin;

    private List<String> paperList = new ArrayList<>();
    private List<String> qualityList = new ArrayList<>();
    private List<String> rangList = new ArrayList<>();
    private List<QualityBean> qualityBeanList = new ArrayList<>();
    private Map<String, TestPaperBean.RowsBean> map = new HashMap<>();

    private OneLoopPopWin.OnItemListener paperListener;
    private OneLoopPopWin.OnItemListener qualityListener;
    private OneLoopPopWin.OnItemListener rangListener;


    private String qcLiquidLevel;
    private String qcLiquidLow;
    private String qcLiquidHigh;
    private String qcLiquidNo;
    private String qcPagerNo;


    private int pager = 1;
    private final int rows = 50;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public QualityPresenter(QualityViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    public void initRangWindow(String paperNumber, Activity activity, View customTitleBar) {
        rangList.clear();
        qualityBeanList.clear();
        initDataRang(map.get(paperNumber));
        if (rangWin == null) {
            rangWin = new OneLoopPopWin.Builder(activity)
                    .setData(rangList).setListener(rangListener).build();
            rangWin.setTextSize(15);
        } else {
            rangWin.setTextSize(15);
            rangWin.setList(rangList);
            rangWin.setOnItemListener(rangListener);
        }
        rangWin.showAtLocation(customTitleBar, Gravity.TOP, 0, 0);
    }

    public void initPaperNumberWindow(Activity activity, View customTitleBar) {
        if (paperWin == null) {
            paperWin = new OneLoopPopWin.Builder(activity)
                    .setData(paperList).setListener(paperListener).build();
        } else {
            paperWin.setList(paperList);
            paperWin.setOnItemListener(paperListener);
        }
        paperWin.showAtLocation(customTitleBar, Gravity.TOP, 0, 0);
    }

    public void initQualityNumberWindow(Activity activity, View customTitleBar) {
        if (qualityWin == null) {
            qualityWin = new OneLoopPopWin.Builder(activity)
                    .setData(qualityList).setListener(qualityListener).build();
        } else {
            qualityWin.setList(qualityList);
            qualityWin.setOnItemListener(qualityListener);
        }
        qualityWin.showAtLocation(customTitleBar, Gravity.TOP, 0, 0);
    }

    private void initDataRang(TestPaperBean.RowsBean bean) {
        rangList.add("level 1: " + bean.getLevel1L() + " - " + bean.getLevel1H() + " mmol/L"); //level 1: 5.4 - 7.2 mmol/L
        rangList.add("level 2: " + bean.getLevel2L() + " - " + bean.getLevel2H() + " mmol/L");
        qualityBeanList.add(new QualityBean(bean.getLevel1H(), "1", bean.getLevel1L()));
        qualityBeanList.add(new QualityBean(bean.getLevel2H(), "1", bean.getLevel2L()));
    }

    public void initListener() {
        qualityListener = new OneLoopPopWin.OnItemListener() {
            @Override
            public void onListener(int position) {
                if (!qualityList.isEmpty() && position < qualityList.size()) {
                    qcLiquidNo = qualityList.get(position);
                    if (viewModel != null)
                        viewModel.setQualityNumber(qcLiquidNo);
                }
            }
        };
        paperListener = new OneLoopPopWin.OnItemListener() {
            @Override
            public void onListener(int position) {
                if (!paperList.isEmpty() && position < paperList.size()) {
                    qcPagerNo = paperList.get(position);
                    if (viewModel != null)
                        viewModel.setPaperNumber(qcPagerNo);
                }
            }
        };
        rangListener = new OneLoopPopWin.OnItemListener() {
            @Override
            public void onListener(int position) {
                if (!qualityBeanList.isEmpty() && position < qualityBeanList.size()) {
                    QualityBean bean = qualityBeanList.get(position);
                    qcLiquidLevel = bean.getLevel();
                    qcLiquidLow = bean.getLow();
                    qcLiquidHigh = bean.getHigh();
                    if (viewModel != null)
                        viewModel.setQualityControlRange(qcLiquidLow + " - " + qcLiquidHigh + " mmol/L");
                }
            }
        };
    }


    public void showDataPopWin(Activity activity, DataPopWin.OnDataListener listener, View showView) {
        Utils.closeKeyboard(activity);
        if (dataPopWin == null)
            dataPopWin = new DataPopWin.Builder(activity, listener).build();
        dataPopWin.showPopWin(showView);
    }


    public void netWorkData(final BaseRxActivity activity) {
        if (viewModel != null) {
            viewModel.showProgress();
            ComveeLoader.getInstance().getTestpaperList(pager, rows)
                    .compose(activity.<TestPaperBean>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new HttpCall<TestPaperBean>(viewModel.getActivity()) {
                        @Override
                        public void onNext(TestPaperBean response) {
                            if (viewModel != null)
                                viewModel.onNetWorkSuccess();
                            pager = response.getPageNum();
                            if (pager == 1) {
                                qualityList.clear();
                                paperList.clear();
                                map.clear();
                            }
                            if (pager < response.getTotalPages()) {
                                pager++;
                                netWorkData(activity);
                            }

                            if (response.getRows() != null && response.getRows().size() > 0) {
                                for (TestPaperBean.RowsBean bean : response.getRows()) {
                                    paperList.add(bean.getTestNo());
                                    qualityList.add(bean.getWaterNo());
                                    map.put(bean.getTestNo(), bean);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (viewModel != null)
                                viewModel.dismissProgress();
                            TestPaperModelDao dao = XTYApplication.getInstance().getDaoSession().getTestPaperModelDao();
                            List<TestPaperModel> modelList = dao.loadAll();
                            if (modelList != null && modelList.size() > 0) {
                                qualityList.clear();
                                paperList.clear();
                                map.clear();
                                for (TestPaperModel model : modelList) {
                                    paperList.add(model.getTestNo());
                                    qualityList.add(model.getWaterNo());
                                    TestPaperBean.RowsBean rowBean = new TestPaperBean.RowsBean();
                                    rowBean.setSid(model.getSid());
                                    rowBean.setLevel1H(model.getLevel1H());
                                    rowBean.setLevel1L(model.getLevel1L());
                                    rowBean.setLevel2H(model.getLevel2H());
                                    rowBean.setLevel2L(model.getLevel2L());
                                    rowBean.setWaterNo(model.getWaterNo());
                                    rowBean.setTestNo(model.getTestNo());
                                    rowBean.setUserId(model.getUserId());
                                    map.put(model.getTestNo(), rowBean);
                                }
                            } else {
                                if (viewModel != null)
                                    viewModel.onNetWorkError();
                                super.onError(e);
                            }
                        }
                    });
        }
    }

    public void setPager(int pager) {
        this.pager = pager;
    }

    /**
     * 跳转获取血糖界面
     */
    public void toBlood(Activity activity, String date, String qcPagerNo, String qcLiquidNo) {
        QualityModel qualityModel = new QualityModel(date, Float.valueOf(qcLiquidHigh), Float.valueOf(qcLiquidLow),
                qcPagerNo, qcLiquidNo, qcLiquidLevel, UserHelper.getMachineId());
        TestBloodActivity.startQuality(activity, qualityModel);
    }
}
