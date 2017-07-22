package com.wx.android.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.android.R;
import com.wx.android.adapter.CommonFrameFragmentAdapter;
import com.wx.android.base.BaseFragment;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/21 19:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：其他Fragment
 */
public class setFragment extends BaseFragment {


    private static final String TAG = setFragment.class.getSimpleName();//"CommonFrameFragment"
    private TextView textView;



    private ListView mListView;

    private String[] datas;

    private CommonFrameFragmentAdapter adapter;




    @Override
    protected View initView() {
        Log.e(TAG,"常用框架Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_common_frame,null);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data =  datas[position];
                Toast.makeText(mContext, "data=="+data, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "设置框架Fragment数据被初始化了...");
        //准备数据
        datas = new String[]{"健康目标", "资讯","设备管理","关于",};
        //设置适配器
        adapter = new CommonFrameFragmentAdapter(mContext,datas);
        mListView.setAdapter(adapter);
    }
}
