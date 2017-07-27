package com.wx.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wx.android.fx.RingView;

/**
 * Created by Administrator on 2017/7/18.
 */

public class pressureActivity extends Activity {

    RingView mringView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);
        mringView =(RingView) findViewById(R.id.ringView);
        findViewById(R.id.startHeartBeatTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mringView.startAnim();
            }
        });
    }
}
