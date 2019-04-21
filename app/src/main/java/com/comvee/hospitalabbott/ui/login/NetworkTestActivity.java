package com.comvee.hospitalabbott.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.network.ConnectTestTool;
import com.comvee.hospitalabbott.network.config.ApiConfig;

public class NetworkTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        TextView top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("网络检测");
        findViewById(R.id.top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.top_history).setVisibility(View.GONE);

        TextView tv_route_ip = (TextView) findViewById(R.id.tv_route_ip);
        TextView tv_ip = (TextView) findViewById(R.id.tv_ip);
        tv_route_ip.setText(ConnectTestTool.getWifiRouteIPAddress(this));
        tv_ip.setText(ConnectTestTool.getWifiIp(this));

        findViewById(R.id.tv_ping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectTestTool.judgeTheConnect("172.19.19.5",NetworkTestActivity.this);
            }
        });

    }
}
