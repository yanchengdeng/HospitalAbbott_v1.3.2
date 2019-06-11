package com.comvee.hospitalabbott.ui.detection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.BaseRxActivity;
import com.comvee.hospitalabbott.bean.HospitalBed;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: 邓言诚  Create at : 2019/4/22  22:36
 * Email: yanchengdeng@gmail.com
 * Describle: 走势图浏览
 */
public class ChartScanActivity extends BaseRxActivity {


    private HospitalBed hospitalBed;

    List<Fragment> fragments = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String[] titles = {"日趋势血糖", "动态血糖"};

    //8条提示

    private String[] tips = {
            "TIR是“血糖在目标范围的时间”，指个体在其目标血糖范围内所持续的时间，它能够提供关于高/低血糖的频率和持续时间是否随着时间的推移有所改善的信息。TIR还能用于评估和对比不同降糖干预方法。",
            "葡萄糖低于目标范围时间的控制目标",
            "葡萄糖低于目标范围时间的控制目标",
            "葡萄糖高于目标范围时间的控制目标",
            "血糖值的标准差（SDBG）：代表所有血糖测定值偏离平均血糖的程度，反映血糖的离散特征，可作为估测血糖稳定性的简易参数；可用来评估患者日内血糖的波动变化的参数。",
            "平均血糖波动幅度（MAGE）:MAGE为所有血糖波动幅度的平均值，可用来评估患者日内血糖的波动变化的指标。",
            "血糖变异系数（CT）:在标准差（SDBG）的基础上校正平均血糖对血糖波动的影响可以消除平均水平不同对变异程度比较的影响，可作为评估患者日间血糖波动的参数。",
            "日间血糖平均绝对差（MODD）：体现每日之间血糖的重复性，可评估血糖的波动程度。"
    };


    /**
     * 1型   0
     * 2型  1
     * 1 型（体弱） 2
     * 2型（体弱）  3
     * 1型（妊娠）  4
     * 2型（妊娠）  5
     */
    private String tipsTxt[] ={
            "血糖在3.9-10mmol/L的时间占比不小于70%\n" +
                    "血糖低于3.9mmol/L的时间占比不超过4%\n" +
                    "血糖低于4.0mmol/L的时间占比不超过1%\n" +
                    "  将高血糖时间减少到最小",

            "血糖在3.9-10mmol/L的时间占比不小于70%\n" +
                    "血糖低于3.9mmol/L的时间占比不超过4%\n" +
                    "血糖低于4.0mmol/L的时间占比不超过1%\n" +
                    "将高血糖时间减少到最小",

            "血糖在3.9-10mmol/L的时间占比不小于50%\n" +
                    "血糖低于3.9mmol/L的时间占比不超过1%\n" +
                    "血糖低于13.9mmol/L的时间占比不小于90%",

            "血糖在3.9-10mmol/L的时间占比不小于50%\n" +
                    "血糖低于3.9mmol/L的时间占比不超过1%\n" +
                    "血糖低于13.9mmol/L的时间占比不小于90%",

            "血糖在3.5-7.8mmol/L的时间占比不小于70%\n" +
                    "血糖低于3.5mmol/L的时间占比不超过4%\n" +
                    "血糖高于7.8mmol/L的时间占比不超过于25%",

            "血糖在3.5-7.8mmol/L的时间占比不小于85%\n" +
                    "血糖低于3.5mmol/L的时间占比不超过4%\n" +
                    "血糖高于7.8mmol/L的时间占比不超过于10%"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_scan);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpage);


        hospitalBed = (HospitalBed) getIntent().getSerializableExtra(TestBloodNewActivity.MEMBER_BEAN);


        ((TextView)findViewById(R.id.top_history)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.question),null);


        StringBuilder sbTopHistry = new StringBuilder();
        if (!TextUtils.isEmpty(hospitalBed.getDiabetesTxt())){
            sbTopHistry.append(hospitalBed.getDiabetesTxt());
        }
        if (!TextUtils.isEmpty(hospitalBed.getBtTxt())){
            sbTopHistry
                    .append("(")
                    .append(hospitalBed.getBtTxt())
                    .append(")")
            ;
        }

        ((TextView)findViewById(R.id.top_history)).setText(sbTopHistry.toString());


        ((TextView)findViewById(R.id.top_history)).setTextColor(getResources().getColor(R.color.yellow));

        findViewById(R.id.top_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content =  ((TextView)findViewById(R.id.top_history)).getText().toString();
                if (!TextUtils.isEmpty(content)){
                    if (content.contains("1型") && content.contains("体弱")){
                        showDialogTips(false,2);
                    }else if (content.contains("1型") && content.contains("妊娠")){
                        showDialogTips(false,4);
                    }else if (content.contains("2型") && content.contains("体弱")){
                        showDialogTips(false,3);
                    }else if (content.contains("2型") && content.contains("妊娠")){
                        showDialogTips(false,5);
                    }else if (content.contains("1型")){
                        showDialogTips(false,0);
                    }else if (content.contains("2型")){
                        showDialogTips(false,1);
                    }
                }
            }
        });

        ((TextView) findViewById(R.id.top_title)).setText(hospitalBed.getBedNo() + "床 " + hospitalBed.getMemberName());

        findViewById(R.id.top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TodayNewChartFragment todayChartFragment = new TodayNewChartFragment();
        DynamicChartFragment todayChartFragment1 = new DynamicChartFragment();
//        TodayChartFragment todayChartFragment2 = new TodayChartFragment();
        todayChartFragment.setArguments(getIntent().getExtras());
        todayChartFragment1.setArguments(getIntent().getExtras());
//        todayChartFragment2.setArguments(getIntent().getExtras());


        fragments.add(todayChartFragment);
        fragments.add(todayChartFragment1);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles[position];
            }
        });
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 20, 20);
            }
        });


    }


    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public void showDialogTips(boolean isTips,int flag){
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ChartScanActivity.this);
        //    设置Title的图标
        //    设置Title的内容
        View view  = LayoutInflater.from(ChartScanActivity.this).inflate(R.layout.blood_tips_dialog,null);

        TextView textView = view.findViewById(R.id.tv_content);
        textView.setText(isTips?tips[flag]:tipsTxt[flag]);


        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        ((AlertDialog) dialog).setView(view);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void showDialogTips(int flag) {
        showDialogTips(true,flag);
    }


}
