package com.comvee.hospitalabbott.ui.detection;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.BloodSugarChatInfo;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.widget.view.BubbleChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;

import java.util.ArrayList;
import java.util.Date;


public class TodayChartFragment extends BaseFragment {


    private BubbleChart bubbleChart;

    //    private int[] hours = new int[25];
    private HospitalBed hospitalBed;

    private TextView tvBloodAvg,tvBloodTime,tvBloodEvent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hospitalBed = (HospitalBed) getArguments().getSerializable(TestBloodNewActivity.MEMBER_BEAN);
    }

    public TodayChartFragment() {

    }

    @Override
    protected int getAbsLayoutId() {
        return R.layout.fragment_chart_today;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        if (hospitalBed == null) {
            return;
        }
        bubbleChart = view.findViewById(R.id.bubbleChart);
        tvBloodAvg = view.findViewById(R.id.tv_blood_avg);
        tvBloodEvent = view.findViewById(R.id.tv_low_blood_event);
        tvBloodTime = view.findViewById(R.id.tv_blood_keep_time);

//        for (int i =0;i<25;i++){
//            hours[i] = i;
//        }

        //设置X轴数据范围
        bubbleChart.getXAxis().setAxisMaximum(25);
        bubbleChart.getXAxis().setAxisMinimum(-1);
        bubbleChart.getAxisLeft().setAxisMinimum(0);
        bubbleChart.getAxisLeft().setAxisMaximum(14);


        XAxis xl = bubbleChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        bubbleChart.setDrawGridBackground(false);


        //隐藏Y轴左右侧树线
        bubbleChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.transparent));
        bubbleChart.getAxisRight().setAxisLineColor(getResources().getColor(R.color.transparent));
        //隐藏Y轴右侧数字
        bubbleChart.getAxisRight().setTextColor(getResources().getColor(R.color.transparent));
        //隐藏数据grid Y 轴线
        bubbleChart.getAxisRight().setGridColor(getResources().getColor(R.color.transparent));
        //隐藏九宫格Y 轴线
        bubbleChart.getXAxis().setGridColor(getResources().getColor(R.color.transparent));


        //x轴数据格式化
        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value > 10 ? String.valueOf((int) value + ":00") : String.valueOf("0" + (int) value + ":00");
            }
        });

        initChart(new BloodSugarChatInfo());

        getDataInfo();

    }

    private void getDataInfo() {

        String today = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        ComveeLoader.getInstance().getBloodChartInfo(hospitalBed.getMemberId(), "2018-10-17", "2018-10-17", "1")
//        ComveeLoader.getInstance().getBloodChartInfo(hospitalBed.getMemberId(), today,today,"1")
                .subscribe(new HttpCall<BloodSugarChatInfo>(null) {
                    @Override
                    public void onNext(BloodSugarChatInfo bloodSugarChatInfo) {
                        Log.w("dyc", bloodSugarChatInfo + "");
                        if (bloodSugarChatInfo != null) {
                            initChart(bloodSugarChatInfo);

                            if (!TextUtils.isEmpty(bloodSugarChatInfo.avgNum)){
                                tvBloodAvg.setText(bloodSugarChatInfo.avgNum+" mmol/L");
                            }

                            if (!TextUtils.isEmpty(bloodSugarChatInfo.awiTime)){
                                tvBloodTime.setText(bloodSugarChatInfo.awiTime+" 分钟L");
                            }

                            if (!TextUtils.isEmpty(bloodSugarChatInfo.eventCount)){
                                tvBloodEvent.setText(bloodSugarChatInfo.eventCount+" 次");
                            }
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Log.w("dyc", "=====");

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }

    // 1 偏低  3  正常   5  偏高

    private void initChart(BloodSugarChatInfo bloodSugarChatInfo) {
        ArrayList<IBubbleDataSet> dataSets = new ArrayList<>();
        ArrayList<BubbleEntry> values1 = new ArrayList<>();
        ArrayList<BubbleEntry> values2 = new ArrayList<>();
        ArrayList<BubbleEntry> values3 = new ArrayList<>();


        if (bloodSugarChatInfo.chartData != null && bloodSugarChatInfo.chartData.dataSource != null && bloodSugarChatInfo.chartData.dataSource.length > 0) {
            for (int i = 0; i < bloodSugarChatInfo.chartData.dataSource.length; i++) {
                if (bloodSugarChatInfo.chartData.dataSource[i] != null && bloodSugarChatInfo.chartData.dataSource[i].length == 3) {
                    if (bloodSugarChatInfo.chartData.dataSource[i][2] == 1) {
                        values1.add(new BubbleEntry(bloodSugarChatInfo.chartData.dataSource[i][0], bloodSugarChatInfo.chartData.dataSource[i][1], bloodSugarChatInfo.chartData.dataSource[i][1], getBloodVlueIcon(bloodSugarChatInfo.chartData.dataSource[i][2])));
                    } else if (bloodSugarChatInfo.chartData.dataSource[i][2] == 3) {
                        values2.add(new BubbleEntry(bloodSugarChatInfo.chartData.dataSource[i][0], bloodSugarChatInfo.chartData.dataSource[i][1], bloodSugarChatInfo.chartData.dataSource[i][1], getBloodVlueIcon(bloodSugarChatInfo.chartData.dataSource[i][2])));
                    } else if (bloodSugarChatInfo.chartData.dataSource[i][2] == 5) {
                        values3.add(new BubbleEntry(bloodSugarChatInfo.chartData.dataSource[i][0], bloodSugarChatInfo.chartData.dataSource[i][1], bloodSugarChatInfo.chartData.dataSource[i][1], getBloodVlueIcon(bloodSugarChatInfo.chartData.dataSource[i][2])));
                    }
                }
            }
        }

        // create a dataset and give it a type
        BubbleDataSet set1 = new BubbleDataSet(values1, "偏低");
        set1.setDrawIcons(false);
        set1.setColor(Color.rgb(72, 192, 255), 255);
        set1.setDrawValues(false);

        BubbleDataSet set2 = new BubbleDataSet(values2, "正常");
        set2.setDrawIcons(false);
//        set2.setIconsOffset(new MPPointF(0, 15));
        set2.setColor(Color.rgb(75, 199, 119), 255);
        set2.setDrawValues(false);


        BubbleDataSet set3 = new BubbleDataSet(values3, "偏高");
        set3.setDrawIcons(false);
        set3.setColor(Color.rgb(255, 100, 100), 255);
        set3.setDrawValues(false);

        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        dataSets.add(set3);


        // create a data object with the data sets
        BubbleData data = new BubbleData(dataSets);
        data.setDrawValues(false);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.WHITE);
        data.setHighlightCircleWidth(0);
        Description description = new Description();
        description.setText("");
        bubbleChart.setDoubleTapToZoomEnabled(false);
        bubbleChart.setPinchZoom(false);
        bubbleChart.setDragEnabled(false);
        bubbleChart.setDescription(description);
        bubbleChart.setData(data);
        bubbleChart.animateY(2000);
        bubbleChart.invalidate();



    }

    private Drawable getBloodVlueIcon(float value) {
        if (value == 1) {
            return getResources().getDrawable(R.drawable.chart_circle_blue);
        } else if (value == 3) {
            return getResources().getDrawable(R.drawable.chart_circle_gree);
        } else {
            return getResources().getDrawable(R.drawable.chart_circle_red);
        }
    }
}
