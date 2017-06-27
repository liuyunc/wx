package com.wx.android.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.wx.android.R;
import com.wx.android.adapter.CommonFrameFragmentAdapter;
import com.wx.android.base.BaseFragment;
import com.wx.android.basement.User;

import java.util.UUID;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/21 19:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：常用框架Fragment
 */
public class CommonFrameFragment extends BaseFragment {

    private ListView mListView;

    private String[] datas;

    private CommonFrameFragmentAdapter adapter;


    private static final String TAG = CommonFrameFragment.class.getSimpleName();//"CommonFrameFragment"

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
        Log.e(TAG, "常用框架Fragment数据被初始化了...");
        //准备数据
        User user=new User();
        user.id = UUID.randomUUID();
        user.name = "Andrew Grosner";
        user.age = 27;
        ModelAdapter<User> adapters = FlowManager.getModelAdapter(User.class);
        adapters.insert(user);

        user.name = "Not Andrew Grosner";
        adapters.update(user);
        datas = new String[]{user.name};
        //设置适配器
        adapter = new CommonFrameFragmentAdapter(mContext,datas);
        mListView.setAdapter(adapter);
    }
}
