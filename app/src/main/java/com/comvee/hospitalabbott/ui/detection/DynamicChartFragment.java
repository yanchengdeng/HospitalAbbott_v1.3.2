package com.comvee.hospitalabbott.ui.detection;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.BloodSugarChatDynmicInfo;
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
import java.util.List;


/**
*
* Author: 邓言诚  Create at : 2019/5/7  21:50
* Email: yanchengdeng@gmail.com
* Describle: 动态血糖
*/
public class DynamicChartFragment extends BaseFragment {


    private LineChart chart;

    private HospitalBed hospitalBed;


    //平均值  标准值 波动  异常
    private TextView tvBloodAvg, tvstandardVal, tvmeanAmplitudeOfGlycemicExcursion,tvcoefficientOfVariation;

    private TextView
            tvTirTittle,tvLess39Tittle,tvLess4Tittle,tvLess139Tittle,
            tvLessTir, tvLess39, tvLess40, tvLess139,
            tvTirUnit, tvLessUnit39, tvlessUnit40, tvLessUnit139,
            tvTirTarget,tvLess39Target,tvLess4Target,tvLess139Target;


    private View mainView,viewNoData;


    //需要一个boolean 表示  是否最后一个值 是 文字介绍（尽量少）  只有 1型  2型 才有   ）

    private boolean isValueFor139 = false;
    private float triValue,middleValue,lastValue;//如果 这三个比较值用数值表示  则这三个数值依次为


    private boolean isRenShen;//是否是妊娠    目标小于


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalBed = (HospitalBed) getArguments().getSerializable(TestBloodNewActivity.MEMBER_BEAN);

    }

    public DynamicChartFragment() {

    }

    @Override
    protected int getAbsLayoutId() {
        return R.layout.fragment_chart_dynamic;
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
//        tvLess39 = view.findViewById(R.id.tv_lessthan_39_tir);
//        tvLess139 = view.findViewById(R.id.tv_lessthan_139_tir);
//        tvLessUnit39 = view.findViewById(R.id.tv_less_39_unit);
//        tvLessUnit139 = view.findViewById(R.id.tv_less_139_unit);
//        tvTirUnit = view.findViewById(R.id.tv_tir_unit);
//        tvLessTir = view.findViewById(R.id.tv_avg_tir);
        mainView = view.findViewById(R.id.ll_main_content);
        viewNoData = view.findViewById(R.id.tv_error);


        chart.setVisibility(View.INVISIBLE);


        initBloodParams(view);
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

        view.findViewById(R.id.tv_blood_diff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(TestBloodNewActivity.MEMBER_BEAN, hospitalBed);
                ActivityUtils.startActivity(bundle, BloodDiffRecordActivity.class);
            }
        });

        getDataInfo();

    }

    private void initBloodParams(View view) {

        //TODO  为了针对需求会根据 体弱 等参数 来显示数据标准  ，动态生成view 添加至底部
        //只有 2 型  1型 对应 四个参数指标      其他对应三个指标
        View bootomView;
        if (!TextUtils.isEmpty(hospitalBed.getDiabetesTxt()) && (hospitalBed.getDiabetesTxt().endsWith("1型") || hospitalBed.getDiabetesTxt().endsWith("2型")) && TextUtils.isEmpty(hospitalBed.getBtTxt())){
            bootomView = LayoutInflater.from(getActivity()).inflate(R.layout.layou_bottom_blood_params_big,null);
            isValueFor139 = false;
            middleValue = 1;
            triValue = 70;
            lastValue=90;

            tvLessTir = bootomView.findViewById(R.id.tv_avg_tir);
            tvLess39 = bootomView.findViewById(R.id.tv_lessthan_39_tir);
            tvLess40 = bootomView.findViewById(R.id.tv_lessthan_4_tir);
            tvLess139 = bootomView.findViewById(R.id.tv_lessthan_139_tir);

            tvTirTittle = bootomView.findViewById(R.id.tv_tir_area);
            tvLess39Tittle = bootomView.findViewById(R.id.tv_lessthan_39_title);
            tvLess4Tittle = bootomView.findViewById(R.id.tv_lessthan_4_title);
            tvLess139Tittle = bootomView.findViewById(R.id.tv_lessthan_139_title);






            tvTirTarget = bootomView.findViewById(R.id.tv_tir_target);
            tvLess39Target = bootomView.findViewById(R.id.tv_lessthan_39_target);
            tvLess4Target = bootomView.findViewById(R.id.tv_lessthan_4_target);
            tvLess139Target = bootomView.findViewById(R.id.tv_lessthan_139_target);


            tvlessUnit40 = bootomView.findViewById(R.id.tv_less_4_unit);
            tvLessUnit39 = bootomView.findViewById(R.id.tv_less_39_unit);
            tvLessUnit139 = bootomView.findViewById(R.id.tv_less_139_unit);
            tvTirUnit = bootomView.findViewById(R.id.tv_tir_unit);


            bootomView.findViewById(R.id.ll_tir_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(0);
                    }
                }
            });


            //3.9
            bootomView.findViewById(R.id.ll_less_39_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(1);
                    }
                }
            });


            bootomView.findViewById(R.id.ll_less_40_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(2);
                    }
                }
            });

            //139
            bootomView.findViewById(R.id.ll_less_139_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(3);
                    }
                }
            });
        }else{
            if (!TextUtils.isEmpty(hospitalBed.getBtTxt())){
                if (hospitalBed.getBtTxt().equals("妊娠")){
                    isRenShen = true;
                }
            }
            bootomView = LayoutInflater.from(getActivity()).inflate(R.layout.layou_bottom_blood_params_big_three,null);
            isValueFor139 = true;
            tvLess39 = bootomView.findViewById(R.id.tv_lessthan_39_tir);
//            tvLess40 = bootomView.findViewById(R.id.tv_lessthan_4_tir);
            tvLess139 = bootomView.findViewById(R.id.tv_lessthan_139_tir);
//            tvlessUnit40 = bootomView.findViewById(R.id.tv_less_4_unit);
            tvLessUnit39 = bootomView.findViewById(R.id.tv_less_39_unit);
            tvLessUnit139 = bootomView.findViewById(R.id.tv_less_139_unit);
            tvTirUnit = bootomView.findViewById(R.id.tv_tir_unit);
            tvLessTir = bootomView.findViewById(R.id.tv_avg_tir);


            tvTirTittle = bootomView.findViewById(R.id.tv_tir_area);
            tvLess39Tittle = bootomView.findViewById(R.id.tv_lessthan_39_title);
//            tvLess4Tittle = bootomView.findViewById(R.id.tv_lessthan_4_title);
            tvLess139Tittle = bootomView.findViewById(R.id.tv_lessthan_139_title);


            tvTirTittle = bootomView.findViewById(R.id.tv_tir_area);
            tvLess39Tittle = bootomView.findViewById(R.id.tv_lessthan_39_title);
//            tvLess4Tittle = bootomView.findViewById(R.id.tv_lessthan_4_title);
            tvLess139Tittle = bootomView.findViewById(R.id.tv_lessthan_139_title);




            tvTirTarget = bootomView.findViewById(R.id.tv_tir_target);
            tvLess39Target = bootomView.findViewById(R.id.tv_lessthan_39_target);
//            tvLess4Target = bootomView.findViewById(R.id.tv_lessthan_4_target);
            tvLess139Target = bootomView.findViewById(R.id.tv_lessthan_139_target);


            tvTirTarget = bootomView.findViewById(R.id.tv_tir_target);
            tvLess39Target = bootomView.findViewById(R.id.tv_lessthan_39_target);
//            tvLess4Target = bootomView.findViewById(R.id.tv_lessthan_4_target);
            tvLess139Target = bootomView.findViewById(R.id.tv_lessthan_139_target);
            if (!TextUtils.isEmpty(hospitalBed.getBtTxt()) && !TextUtils.isEmpty(hospitalBed.getDiabetesTxt())) {
                //1型  2型  体弱
                if (hospitalBed.getBtTxt().equals("体弱")){
                    triValue = 50;
                    middleValue = 1;
                    lastValue = 90;
                    tvTirTarget.setText("目标≥50%");
                    tvLess39Target.setText("目标<1%");
                    tvLess139Target.setText("目标≥90%");
                }else if ( hospitalBed.getDiabetesTxt().equals("1型") && hospitalBed.getBtTxt().equals("妊娠")){
                    //1型    妊娠
                    triValue = 70;
                    middleValue = 4;
                    lastValue = 25;
                    tvTirTarget.setText("目标≥70%");
                    tvLess39Target.setText("目标<4%");
                    tvLess139Target.setText("目标<25%");
                    tvTirTittle.setText("目标范围：3.5~7.8mmol/L");
                    tvLess39Tittle.setText("<3.5mmol/L的时间占比");
                    tvLess139Tittle.setText(">7.8mmol/L的时间占比");
                }else{
                    //2型  妊娠
                    triValue = 85;
                    middleValue = 4;
                    lastValue = 10;
                    tvTirTarget.setText("目标≥85%");
                    tvLess39Target.setText("目标<4%");
                    tvLess139Target.setText("目标<10%");
                    tvTirTittle.setText("目标范围：3.5~7.8mmol/L");
                    tvLess39Tittle.setText("<3.5mmol/L的时间占比");
                    tvLess139Tittle.setText(">7.8mmol/L的时间占比");
                }
            }






            bootomView.findViewById(R.id.ll_tir_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(0);
                    }
                }
            });


            //3.9
            bootomView.findViewById(R.id.ll_less_39_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(1);
                    }
                }
            });


//            bootomView.findViewById(R.id.ll_less_40_num).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (getActivity() != null) {
//                        ((ChartScanActivity) getActivity()).showDialogTips(2);
//                    }
//                }
//            });

            //139
            bootomView.findViewById(R.id.ll_less_139_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((ChartScanActivity) getActivity()).showDialogTips(3);
                    }
                }
            });
        }



        //tir

        ((LinearLayout)view.findViewById(R.id.ll_bootom_view)).addView(bootomView);
    }

    //获取动态血糖
    private void getDataInfo() {

        String today = DateUtil.date2Str(Calendar.getInstance().getTime(), DateUtil.FORMAT_YMD);
        Calendar calendarBeforWeek = Calendar.getInstance();
        calendarBeforWeek.add(Calendar.DAY_OF_YEAR,-13);
        String todayBeforWeek = DateUtil.date2Str(calendarBeforWeek.getTime(), DateUtil.FORMAT_YMD);
        ComveeLoader.getInstance().getBloodChartInfoSuper(hospitalBed.getMemberId(), todayBeforWeek, today, "2")
                .subscribe(new HttpCall<BloodSugarChatDynmicInfo>(null) {
                    @Override
                    public void onNext(BloodSugarChatDynmicInfo bloodSugarChatInfo) {
                        if (bloodSugarChatInfo != null) {
                            if (bloodSugarChatInfo.chartShow==0){
                                viewNoData.setVisibility(View.VISIBLE);
                                mainView.setVisibility(View.GONE);

                                return;
                            }
                            viewNoData.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);

                            initChartView(bloodSugarChatInfo);
                            tvBloodAvg.setText(""+bloodSugarChatInfo.avgNum);
                            tvstandardVal.setText(""+bloodSugarChatInfo.standardVal);
                            tvmeanAmplitudeOfGlycemicExcursion.setText(""+bloodSugarChatInfo.meanAmplitudeOfGlycemicExcursion);
                            tvcoefficientOfVariation.setText(""+bloodSugarChatInfo.coefficientOfVariation);
                            tvLessTir.setText(""+bloodSugarChatInfo.awiTimeRateOfNormal);
                            tvLess39.setText(""+bloodSugarChatInfo.awiTimeRateOf3_9);
                            tvLess139.setText(""+bloodSugarChatInfo.awiTimeRateOf13_9);



                            if (bloodSugarChatInfo.awiTimeRateOfNormal>=triValue){
                                tvLessTir.setTextColor(getResources().getColor(R.color.blue));
                                tvTirUnit.setTextColor(getResources().getColor(R.color.blue));
                            }else{
                                tvLessTir.setTextColor(getResources().getColor(R.color.red));
                                tvTirUnit.setTextColor(getResources().getColor(R.color.red));
                            }

                            if (bloodSugarChatInfo.awiTimeRateOf3_9<middleValue){
                                tvLess39.setTextColor(getResources().getColor(R.color.blue));
                                tvLessUnit39.setTextColor(getResources().getColor(R.color.blue));
                            }else{
                                tvLess39.setTextColor(getResources().getColor(R.color.red));
                                tvLessUnit39.setTextColor(getResources().getColor(R.color.red));
                            }


                            if (tvLess40!=null) {
                                tvLess40.setText("" + bloodSugarChatInfo.awiTimeRateOf4_0);
                                if (bloodSugarChatInfo.awiTimeRateOf4_0 < 4) {
                                    tvLess40.setTextColor(getResources().getColor(R.color.blue));
                                    tvlessUnit40.setTextColor(getResources().getColor(R.color.blue));
                                } else {
                                    tvLess40.setTextColor(getResources().getColor(R.color.red));
                                    tvlessUnit40.setTextColor(getResources().getColor(R.color.red));
                                }
                            }


                            if (isValueFor139) {
                                if (isRenShen){
                                    if (bloodSugarChatInfo.awiTimeRateOf13_9 < lastValue) {
                                        tvLess139.setTextColor(getResources().getColor(R.color.blue));
                                        tvLessUnit139.setTextColor(getResources().getColor(R.color.blue));
                                    } else {
                                        tvLess139.setTextColor(getResources().getColor(R.color.red));
                                        tvLessUnit139.setTextColor(getResources().getColor(R.color.red));
                                    }
                                }else{
                                    if (bloodSugarChatInfo.awiTimeRateOf13_9 >= lastValue) {
                                        tvLess139.setTextColor(getResources().getColor(R.color.blue));
                                        tvLessUnit139.setTextColor(getResources().getColor(R.color.blue));
                                    } else {
                                        tvLess139.setTextColor(getResources().getColor(R.color.red));
                                        tvLessUnit139.setTextColor(getResources().getColor(R.color.red));
                                    }
                                }
                            }else{
                                tvLess139.setTextColor(getResources().getColor(R.color.red));
                                tvLessUnit139.setTextColor(getResources().getColor(R.color.red));
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }



    //初始化 chartView
    private void initChartView(BloodSugarChatDynmicInfo bloodSugarChatInfo) {

        chart.setVisibility(View.VISIBLE);

        {   // // Chart Style // //

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
            //禁止放大 拖拽
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);
            chart.setPinchZoom(false);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.setGridLineWidth(0);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(24);
            //强制将分割线 设置为5个   保证  4个区间 每个区间 6小时  满足需求
            xAxis.setLabelCount(5,true);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            chart.getAxisLeft().setDrawGridLines(false);
            yAxis.enableGridDashedLine(10f, 10f, 0f);
            yAxis.setDrawGridLines(false);
            yAxis.setDrawZeroLine(true);
            yAxis.setDrawAxisLine(true);
            yAxis.setAxisLineWidth(1);
            //y轴右边画线
            chart.getAxisRight().setDrawGridLines(false);
            chart.getAxisRight().enableGridDashedLine(0f, 0f, 0f);
            chart.getAxisRight().setDrawAxisLine(true);
            chart.getAxisRight().setAxisLineWidth(1);
            //不显示数值
            chart.getAxisRight().setDrawLabels(false);
            // Y轴数据权健
            yAxis.setAxisMaximum(21f);
            yAxis.setAxisMinimum(0f);
            yAxis.setLabelCount(8,true);
        }


        {

            //水平线 （最大值 最小值 ..） 先删除 再添加）
            yAxis.removeAllLimitLines();
            LimitLine ll1 = new LimitLine(bloodSugarChatInfo.highLineVal, "");
            ll1.setLineWidth(2f);
            ll1.setLineColor(Color.rgb(00, 00, 00));
            ll1.enableDashedLine(5f, 15f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);

            LimitLine ll2 = new LimitLine(bloodSugarChatInfo.lowLineVal, "");
            ll2.setLineWidth(2f);
            ll2.setLineColor(Color.rgb(00, 00, 00));
            ll2.enableDashedLine(5f, 15f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);

            // draw limit lines behind data instead of on top
//            yAxis.setDrawLimitLinesBehindData(true);
//            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
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
        chart.animateY(1500);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        //隐藏折现标签说明
        l.setEnabled(false);
        setData(parseEntity(bloodSugarChatInfo));

    }

    /**
     * 解析接口数据
     * @param bloodSugarChatInfo
     * @return
     */
    private ArrayList<ILineDataSet> parseEntity(BloodSugarChatDynmicInfo bloodSugarChatInfo) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if (bloodSugarChatInfo==null || bloodSugarChatInfo.chartData==null || bloodSugarChatInfo.chartData.dataSource==null){
            return dataSets;
        }
//        if (bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent!=null && bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent.length>0 ){
//            ArrayList<Entry> values = new ArrayList<>();
//            for (int i = 0;i<bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent.length;i++){
//                values.add(new Entry(bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent[i][0]*6*0.9f,bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent[i][1]*0.9f));
//            }
//            LineDataSet d = new LineDataSet(values, "模拟假数据");
//            d.setColor(Color.rgb(255,  00, 00));
//            d.setLineWidth(2);
//            dataSets.add(d);
//
//        }


        int colorLineLight = Color.parseColor("#cbdbe5");//浅色折现
        int colorLineDeep = Color.parseColor("#a4bdd3");//深色折现
        int colorAreaLight = Color.parseColor("#EBF1F5");//浅色区域
        int colorAreaDeep = Color.parseColor("#CDDCE9");//深色区域


        //TODO  小技巧： 要按照顺序排才可以在最后一个折现的地方使用  白色区域覆盖原有自带的颜色

        if (bloodSugarChatInfo.chartData.dataSource.curveOfNinetyPercent!=null && bloodSugarChatInfo.chartData.dataSource.curveOfNinetyPercent.length>0 ){
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0;i<bloodSugarChatInfo.chartData.dataSource.curveOfNinetyPercent.length;i++){
                values.add(new Entry(bloodSugarChatInfo.chartData.dataSource.curveOfNinetyPercent[i][0]*6,bloodSugarChatInfo.chartData.dataSource.curveOfNinetyPercent[i][1]));
            }
            LineDataSet d = new LineDataSet(values, "90%分位 ");
            //设置这线颜色
            d.setColor(colorLineLight);
            //设置折现包含区域颜色
            d.setFillColor(colorAreaLight);
            //设置折现宽度
            d.setLineWidth(2);
            //设置折现区域颜色透明度
            d.setFillAlpha(255);
            dataSets.add(d);

        }

        //同理
        if (bloodSugarChatInfo.chartData.dataSource.curveOfSeventyFivePercent!=null && bloodSugarChatInfo.chartData.dataSource.curveOfSeventyFivePercent.length>0 ){
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0;i<bloodSugarChatInfo.chartData.dataSource.curveOfSeventyFivePercent.length;i++){
                values.add(new Entry(bloodSugarChatInfo.chartData.dataSource.curveOfSeventyFivePercent[i][0]*6,bloodSugarChatInfo.chartData.dataSource.curveOfSeventyFivePercent[i][1]));
            }
            LineDataSet d = new LineDataSet(values, "75%分位 ");
            d.setColor(colorLineDeep);
            d.setFillColor(colorAreaDeep);
            d.setLineWidth(2);
            d.setFillAlpha(255);
            dataSets.add(d);

        }

        if (bloodSugarChatInfo.chartData.dataSource.curveOfFiftyPercent!=null && bloodSugarChatInfo.chartData.dataSource.curveOfFiftyPercent.length>0 ){
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0;i<bloodSugarChatInfo.chartData.dataSource.curveOfFiftyPercent.length;i++){
                values.add(new Entry(bloodSugarChatInfo.chartData.dataSource.curveOfFiftyPercent[i][0]*6,bloodSugarChatInfo.chartData.dataSource.curveOfFiftyPercent[i][1]));
            }
            LineDataSet d = new LineDataSet(values, "50%分位 ");
            d.setColor(Color.parseColor("#0099ff"));
            d.setFillColor(colorAreaDeep);
            d.setLineWidth(2);
            d.setFillAlpha(255);
            dataSets.add(d);

        }

        if (bloodSugarChatInfo.chartData.dataSource.curveOfTwentyFivePercent!=null && bloodSugarChatInfo.chartData.dataSource.curveOfTwentyFivePercent.length>0 ){
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0;i<bloodSugarChatInfo.chartData.dataSource.curveOfTwentyFivePercent.length;i++){
                values.add(new Entry(bloodSugarChatInfo.chartData.dataSource.curveOfTwentyFivePercent[i][0]*6,bloodSugarChatInfo.chartData.dataSource.curveOfTwentyFivePercent[i][1]));
            }
            LineDataSet d = new LineDataSet(values, "25%分位 ");
            d.setColor(colorLineDeep);
            d.setFillColor(colorAreaLight);
            d.setFillAlpha(255);
            d.setLineWidth(2);
            dataSets.add(d);

        }

        if (bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent!=null && bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent.length>0 ){
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0;i<bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent.length;i++){
                values.add(new Entry(bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent[i][0]*6,bloodSugarChatInfo.chartData.dataSource.curveOfTenPercent[i][1]));
            }
            LineDataSet d = new LineDataSet(values, "10%分位 ");
            d.setColor(colorLineLight);
            d.setFillColor(Color.WHITE);
            d.setFillAlpha(255);
            d.setLineWidth(2);
            dataSets.add(d);

        }




//        resetDataSet(dataSets);
        return dataSets;
    }

    private void resetDataSet(ArrayList<LineDataSet> dataSets) {
        for (LineDataSet set1:dataSets) {

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

            dataSets.add(set1); // add the data sets


            //设置曲线  0  不是曲线
            ((LineDataSet) dataSets.get(0)).enableDashedLine(0, 0, 0);
            ((LineDataSet) dataSets.get(0)).setLineWidth(2);
        }
    }


    /**
     * 设置数据到图标
     * @param values
     */
    private void setData(ArrayList<ILineDataSet> values) {
            LineData data = new LineData(values);
            chart.setData(data);
            List<ILineDataSet> sets = chart.getData()
                    .getDataSets();

            for (ILineDataSet iSet : sets) {
                LineDataSet set = (LineDataSet) iSet;
                //设置数据圆点显示
                set.setDrawCircles(false);
                //填充颜色
                set.setDrawFilled(true);
                //设置取现模式
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            }
        }
    }
