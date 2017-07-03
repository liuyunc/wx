package com.wx.android.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wx.android.R;
import com.wx.android.base.BaseFragment;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/21 19:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：自定义Fragment
 */
public class CustomFragment extends BaseFragment {


    private static final String TAG = CustomFragment.class.getSimpleName();//"CommonFrameFragment"
    private Button mButton;
    private TextView mText;

    @Override
    protected View initView() {
        Log.e(TAG,"自定义Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.hr_frame,null);
         mButton= (Button) view.findViewById(R.id.h_button);
        mText = (TextView) view.findViewById(R.id.heart);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    mText.setText("更新后");
                }
            }
        };

        mText.setText("更新前");
        final Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

        });
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thread.start();
            }
        });




        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "自定义Fragment数据被初始化了...");

    }
}
