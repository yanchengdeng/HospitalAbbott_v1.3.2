package com.comvee.hospitalabbott.ui.detection;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.BloodDiffListAdapter;
import com.comvee.hospitalabbott.base.BaseRxActivity;
import com.comvee.hospitalabbott.bean.BloodSugarChatDynmicInfo;
import com.comvee.hospitalabbott.bean.DiffBloodValue;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BloodDiffRecordActivity extends BaseRxActivity {

    private HospitalBed hospitalBed;
    private RecyclerView recyclerView;
    private BloodDiffListAdapter bloodDiffListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_blood_record);

        findViewById(R.id.top_history).setVisibility(View.GONE);
        recyclerView= findViewById(R.id.recyclerView);

        hospitalBed = (HospitalBed) getIntent().getSerializableExtra(TestBloodNewActivity.MEMBER_BEAN);

        ((TextView) findViewById(R.id.top_title)).setText("日间血糖平均绝对差汇总");
        bloodDiffListAdapter = new BloodDiffListAdapter(R.layout.adapter_blood_diff,new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bloodDiffListAdapter);

        findViewById(R.id.top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getDataInfo();
    }


    private void getDataInfo() {

        String today = DateUtil.date2Str(Calendar.getInstance().getTime(), DateUtil.FORMAT_YMD);
        Calendar calendarBeforWeek = Calendar.getInstance();
        calendarBeforWeek.add(Calendar.DAY_OF_YEAR,-14);
        String todayBeforWeek = DateUtil.date2Str(calendarBeforWeek.getTime(), DateUtil.FORMAT_YMD);
        ComveeLoader.getInstance().getBloodChartInfoSuper(hospitalBed.getMemberId(), todayBeforWeek, today, "3")
//        ComveeLoader.getInstance().getBloodChartInfo(hospitalBed.getMemberId(), today,today,"1")
                .subscribe(new HttpCall<BloodSugarChatDynmicInfo>(null) {
                    @Override
                    public void onNext(BloodSugarChatDynmicInfo bloodSugarChatInfo) {
                        Log.w("dyc", bloodSugarChatInfo + "");
                        if (bloodSugarChatInfo != null) {
                            bloodDiffListAdapter.setNewData(getTestData());
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

    /**
     "04/25~04/26":3.34,
     "04/26~04/27":3.52,
     "04/27~04/28":1.98,
     "04/28~04/29":2.65,
     "04/29~04/30":2.18,
     "04/30~05/01":3.77,
     "05/01~05/02":3.07,
     "05/02~05/03":2.53,
     "05/03~05/04":1.61,
     "05/04~05/05":4.19,
     "05/05~05/06":3.4
     */

    private List<DiffBloodValue> getTestData() {
        List<DiffBloodValue> diffBloodValues = new ArrayList<>();
        diffBloodValues.add(new DiffBloodValue("04/25~04/26",3.34f));
        diffBloodValues.add(new DiffBloodValue("04/26~04/27",3.52f));
        diffBloodValues.add(new DiffBloodValue("04/27~04/28",1.98f));
        diffBloodValues.add(new DiffBloodValue("04/28~04/29",2.65f));
        diffBloodValues.add(new DiffBloodValue("04/29~04/30",2.18f));
        diffBloodValues.add(new DiffBloodValue("04/30~05/01",3.77f));
        diffBloodValues.add(new DiffBloodValue("05/01~05/02",3.07f));
        diffBloodValues.add(new DiffBloodValue("05/02~05/03",2.53f));
        diffBloodValues.add(new DiffBloodValue("05/03~05/04",1.61f));
        diffBloodValues.add(new DiffBloodValue("05/04~05/05",4.19f));
        diffBloodValues.add(new DiffBloodValue("05/05~05/06",3.4f));


        return diffBloodValues;
    }
}
