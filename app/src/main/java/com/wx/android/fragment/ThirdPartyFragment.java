package com.wx.android.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wx.android.R;
import com.wx.android.base.BaseFragment;
import com.wx.android.fx.RingView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/21 19:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：第三方Fragment
 */
public class ThirdPartyFragment extends BaseFragment {


    private static final String TAG = ThirdPartyFragment.class.getSimpleName();//"CommonFrameFragment"
    RingView mringView;
    private TextView mText;
    int j=0;
    Handler handler=new Handler(){
      @Override
        public void handleMessage(Message msg){
          super.handleMessage(msg);

          char i=60;
          String s;
          if(msg.what==0x123){

                  s=String.valueOf(i+j);
                  mText.setText(s);
                  mText.setTextSize(90);
                  mText.setTextColor(Color.WHITE);
          }
      }
    };

    /**Thread thread = new Thread(new Runnable(){

        @Override
        public void run() {
            Message message = new Message();
            message.what = 0x123;
            handler.sendMessage(message);
        }

    });*/

    @Override
    protected View initView() {
        Log.e(TAG,"第三方Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.activity_heart,null);
        mringView =(RingView) view.findViewById(R.id.ringView);
        mText = (TextView) view.findViewById(R.id.hrtextView);
        view.findViewById(R.id.startHeartBeatTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mringView.startAnim();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x123);
                    }
                },18000,2000);

            }
        });

        return view;
    }

    /**延迟执行
     *
     */




    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "第三方Fragment数据被初始化了...");


    }
}
