package com.comvee.hospitalabbott.ui.detection;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.DateListAdapter;
import com.comvee.hospitalabbott.bean.BloodSugarChatInfo;
import com.comvee.hospitalabbott.bean.DateListBean;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.widget.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: 邓言诚  Create at : 2019/5/7  21:51
 * Email: yanchengdeng@gmail.com
 * Describle:日趋势图  新
 */
public class TodayNewChartFragment extends BaseFragment {


    private LineChart chart;

    //    private int[] hours = new int[25];
    private HospitalBed hospitalBed;

    //平均值  标准值 波动  异常
    private TextView tvBloodAvg, tvstandardVal, tvmeanAmplitudeOfGlycemicExcursion,tvcoefficientOfVariation;

    private TextView tvLessTir, tvLess39,tvLess40,tvLess139,tvLessUnit39,tvlessUnit40,tvLessUnit139;

    private RecyclerView recyclerView;

    private DateListAdapter dateListAdapter;






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hospitalBed = (HospitalBed) getArguments().getSerializable(TestBloodNewActivity.MEMBER_BEAN);
    }

    public TodayNewChartFragment() {

    }

    @Override
    protected int getAbsLayoutId() {
        return R.layout.fragment_chart_today_new;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        if (hospitalBed == null) {
            return;
        }
        chart = view.findViewById(R.id.bubbleChart);
        tvBloodAvg = view.findViewById(R.id.tv_avg_num);
        tvstandardVal = view.findViewById(R.id.tv_avg_standardVal);
        tvmeanAmplitudeOfGlycemicExcursion = view.findViewById(R.id.tv_avg_bodong);
        tvcoefficientOfVariation = view.findViewById(R.id.tv_avg_bianyi);
        tvLess39 = view.findViewById(R.id.tv_lessthan_39_tir);
        tvLess40 = view.findViewById(R.id.tv_lessthan_4_tir);
        tvLess139 = view.findViewById(R.id.tv_lessthan_139_tir);
        tvlessUnit40 = view.findViewById(R.id.tv_less_4_unit);
        tvLessUnit39 = view.findViewById(R.id.tv_less_39_unit);
        tvLessUnit139 = view.findViewById(R.id.tv_less_139_unit);
        tvLessTir = view.findViewById(R.id.tv_avg_tir);



        recyclerView = view.findViewById(R.id.recyclerView);
        dateListAdapter = new DateListAdapter(R.layout.adapter_date_chat_item, getDateList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dateListAdapter);




        //tir


        view.findViewById(R.id.ll_tir_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(0);
                }
            }
        });

        //3.9
        view.findViewById(R.id.ll_less_39_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(1);
                }
            }
        });




        view.findViewById(R.id.ll_less_40_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(2);
                }
            }
        });

        //139
        view.findViewById(R.id.ll_less_139_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(3);
                }
            }
        });




        //血糖标准差
        view.findViewById(R.id.ll_avg_standardVal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(4);
                }
            }
        });

        //平均血糖波动
        view.findViewById(R.id.ll_avg_avg_bodong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(5);
                }
            }
        });

        //变异系数
        view.findViewById(R.id.ll_avg_avg_bianyi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null){
                    ((ChartScanActivity) getActivity()).showDialogTips(6);
                }
            }
        });






        String today = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        getDataInfo(today);

        dateListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (DateListBean item : dateListAdapter.getData()) {
                    item.isSelected = false;
                }
                dateListAdapter.getData().get(position).isSelected = true;
                dateListAdapter.notifyDataSetChanged();
                getDataInfo(dateListAdapter.getData().get(position).getDateString());
            }
        });

    }

    //初始化 chartView
    private void initChartView(BloodSugarChatInfo bloodSugarChatInfo) {

        {   // // Chart Style // //

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(false);

            // set listeners
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
//            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);
//            chart.setGridBackgroundColor(getResources().getColor(R.color.transparent));

            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(false);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setDrawGridLines(false);
//            xAxis.setGridColor(Color.argb(255,255,153,50));
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(24);
            xAxis.setSpaceMax(6);
            xAxis.setSpaceMin(6);


        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);
            yAxis.setDrawGridLines(false);


            // axis range
            yAxis.setAxisMaximum(30f);
            yAxis.setAxisMinimum(0f);
        }


        {   // // Create Limit Lines // //
//            LimitLine llXAxis = new LimitLine(9f, "Index 10");
//            llXAxis.setLineWidth(4f);
//            llXAxis.enableDashedLine(10f, 10f, 0f);
//            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            llXAxis.setTextSize(10f);

            yAxis.removeAllLimitLines();
            LimitLine ll1 = new LimitLine(Float.parseFloat(bloodSugarChatInfo.avgNum), "");
            ll1.setLineWidth(2f);
            //F2F8EE
            ll1.setLineColor(Color.rgb(00, 00, 00));
            ll1.enableDashedLine(5f, 15f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);

//            LimitLine ll2 = new LimitLine(6f, "低");
//            ll2.setLineWidth(2f);
//            ll2.setLineColor(Color.rgb(242, 35, 38));
//            ll2.enableDashedLine(10f, 15f, 0f);
//            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            ll2.setTextSize(10f);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
//            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }


        //x轴数据格式化
        XAxis xl = chart.getXAxis();
        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value > 10 ? String.valueOf((int) value + ":00") : String.valueOf("0" + (int) value + ":00");
            }
        });

        // add data


        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

        setData(parseEntity(bloodSugarChatInfo));

    }


    private void setData(ArrayList<Entry> values) {


        LineDataSet set1;


        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setDrawFilled(false);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "平均值");

            set1.setDrawIcons(false);
            set1.setDrawFilled(false);


            // draw dashed line

//            set1.setLineWidth(0);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            //填充颜色
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.TRANSPARENT);
//            }


            set1.setFillColor(Color.argb(255, 255, 255, 255));
            //显示数值
            set1.setDrawValues(false);
            set1.setDrawCircleHole(false);
            set1.setColor(Color.argb(255, 0, 153, 255));
            set1.setDrawFilled(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets


            //设置曲线  0  不是曲线
            ((LineDataSet) dataSets.get(0)).enableDashedLine(0, 0, 0);
            ((LineDataSet) dataSets.get(0)).setLineWidth(2);

            //隐藏DateSet


            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);

            List<ILineDataSet> sets = chart.getData()
                    .getDataSets();

            for (ILineDataSet iSet : sets) {

                LineDataSet set = (LineDataSet) iSet;
                set.setDrawCircles(false);
            }
        }
        chart.invalidate();
    }


    private List<DateListBean> getDateList() {
        List<DateListBean> dateListBeans = new ArrayList<>();


        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -6);
            calendar.add(Calendar.DAY_OF_YEAR, i);
            dateListBeans.add(new DateListBean(
                    DateUtil.date2Str(calendar.getTime(), DateUtil.FORMAT_YMD),
                    getWeekByIndex(calendar.get(Calendar.DAY_OF_WEEK)),
                    calendar.get(Calendar.DAY_OF_MONTH) < 10 ?
                            ("0" + calendar.get(Calendar.DAY_OF_MONTH))
                            : String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        }
        dateListBeans.get(6).isSelected = true;
        return dateListBeans;
    }

    private String getWeekByIndex(int i) {
        String week = "日";
        switch (i) {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
        }

        return week;
    }

    private void getDataInfo(String today) {


        ComveeLoader.getInstance().getBloodChartInfo(hospitalBed.getMemberId(), today, today, "1")
//        ComveeLoader.getInstance().getBloodChartInfo(hospitalBed.getMemberId(), today,today,"1")
                .subscribe(new HttpCall<BloodSugarChatInfo>(null) {
                    @Override
                    public void onNext(BloodSugarChatInfo bloodSugarChatInfo) {
                        Log.w("dyc", bloodSugarChatInfo + "");
                        if (bloodSugarChatInfo != null) {
//                            if (bloodSugarChatInfo.chartData!=null && bloodSugarChatInfo.chartData.dataSource!=null && bloodSugarChatInfo.chartData.dataSource.length>0) {
                                initChartView(bloodSugarChatInfo);


                            tvBloodAvg.setText(""+bloodSugarChatInfo.avgNum);
                            tvstandardVal.setText(""+bloodSugarChatInfo.standardVal);
                            tvmeanAmplitudeOfGlycemicExcursion.setText(""+bloodSugarChatInfo.meanAmplitudeOfGlycemicExcursion);
                            tvcoefficientOfVariation.setText(""+bloodSugarChatInfo.coefficientOfVariation);



                            tvLessTir.setText(""+bloodSugarChatInfo.awiTimeRateOfNormal);
                            tvLess39.setText(""+bloodSugarChatInfo.awiTimeRateOf3_9);
                            tvLess40.setText(""+bloodSugarChatInfo.awiTimeRateOf4_0);
                            tvLess139.setText(""+bloodSugarChatInfo.awiTimeRateOf13_9);




//                            }
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

    private ArrayList<Entry> parseEntity(BloodSugarChatInfo bloodSugarChatInfo) {
        ArrayList<Entry> entries = new ArrayList<>();

        if (bloodSugarChatInfo.chartData != null && bloodSugarChatInfo.chartData.dataSource != null && bloodSugarChatInfo.chartData.dataSource.length > 0) {
            for (int i = 0; i < bloodSugarChatInfo.chartData.dataSource.length; i++) {
                if (bloodSugarChatInfo.chartData.dataSource[i] != null && bloodSugarChatInfo.chartData.dataSource[i].length == 3) {
                    entries.add(new Entry(bloodSugarChatInfo.chartData.dataSource[i][0] * 6, bloodSugarChatInfo.chartData.dataSource[i][1]));
                }
            }
        }
        return entries;
    }
}
