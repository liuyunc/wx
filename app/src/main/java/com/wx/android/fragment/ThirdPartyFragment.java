package com.wx.android.fragment;

import android.util.Log;
import android.view.View;

import com.wx.android.R;
import com.wx.android.base.BaseFragment;
import com.wx.android.fx.RingView;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/21 19:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：第三方Fragment
 */
public class ThirdPartyFragment extends BaseFragment {


    private static final String TAG = ThirdPartyFragment.class.getSimpleName();//"CommonFrameFragment"
    RingView mringView;

    @Override
    protected View initView() {
        Log.e(TAG,"第三方Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.activity_heart,null);
        mringView =(RingView) view.findViewById(R.id.ringView);
        view.findViewById(R.id.startHeartBeatTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mringView.startAnim();
            }
        });

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "第三方Fragment数据被初始化了...");

    }
}
