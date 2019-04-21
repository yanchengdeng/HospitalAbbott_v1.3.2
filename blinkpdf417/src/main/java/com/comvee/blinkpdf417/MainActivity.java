package com.comvee.blinkpdf417;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.microblink.activity.ScanActivity;
import com.microblink.activity.VerificationFlowActivity;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.view.recognition.RecognitionType;

/**
 * Created by F011512088 on 2018/3/7.
 */

public class MainActivity extends AppCompatActivity {

    public static final int MY_BLINKID_REQUEST_CODE = 0x101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.to_blink_pdf417).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BlinkHelper.buildPDF417Element(MainActivity.this), MY_BLINKID_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // onActivityResult is called whenever we are returned from activity started
        // with startActivityForResult. We need to check request code to determine
        // that we have really returned from BlinkID activity.
        if (requestCode == MY_BLINKID_REQUEST_CODE) {

            // make sure BlinkID activity returned result
            if (resultCode == Activity.RESULT_OK && data != null) {

                Bundle extras = data.getExtras();
                if (extras != null && extras.getParcelable(ScanActivity.EXTRAS_RECOGNITION_RESULTS) == null) {
                    // VerificationFlowActivity does not return results as RecognitionResults object, prepare RecognitionResults
                    // from combined recognizer result
                    BaseRecognitionResult combinedResult = extras.getParcelable(VerificationFlowActivity.EXTRAS_COMBINED_RECOGNITION_RESULT);
                    if (combinedResult != null) {
                        data.putExtra(ScanActivity.EXTRAS_RECOGNITION_RESULTS, new RecognitionResults(new BaseRecognitionResult[]{combinedResult}, RecognitionType.SUCCESSFUL));
                    }
                }

                // set intent's component to ResultActivity and pass its contents
                // to ResultActivity. ResultActivity will show how to extract
                // data from result.

            } else {
                // if BlinkID activity did not return result, user has probably
                // pressed Back button and cancelled scanning
                Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
