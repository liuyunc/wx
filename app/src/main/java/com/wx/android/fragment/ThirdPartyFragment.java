package com.wx.android.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wx.android.R;
import com.wx.android.adapter.CommonFrameFragmentAdapter;
import com.wx.android.base.BaseFragment;
import com.wx.android.basement.Health;
import com.wx.android.fx.RingView;

import java.util.LinkedList;
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
    private ListView mListView; //心率历史记录
    private CommonFrameFragmentAdapter adapter;
    private String[] datas;
    Health info=new Health();
    //ModelAdapter<Health> adapters = FlowManager.getModelAdapter(Health.class);

    LinkedList list = new LinkedList(); //历史纪录的队列




    Handler handler=new Handler(){
      @Override
        public void handleMessage(Message msg){
          super.handleMessage(msg);
          String s;
          if(msg.what==0x123){
              int i=(int) (Math.random()*100);
              s=String.valueOf(i);
              if(list.size()<=5) {
                  list.addFirst(i);
              }else{
                  list.addFirst(i);
                  list.removeLast();
              }
              if(list.get(0)!=null) {
                  info.hr = (int) list.get(0);
              }
              if(list.get(1)!=null){
                  info.hr1= (int)list.get(1);
              }
              if(list.get(2)!=null) {
                  info.hr2 = (int) list.get(2);
              }
              if(list.get(3)!=null) {
                  info.hr3 = (int) list.get(3);
              }
              if(list.get(4)!=null) {
                  info.hr4 = (int) list.get(4);
              }


              //adapters.update(info);
              info.update();
              datas = new String[]{String.valueOf(info.hr),String.valueOf(info.hr1),String.valueOf(info.hr2),String.valueOf(info.hr3),String.valueOf(info.hr4)};
              //设置适配器
              adapter = new CommonFrameFragmentAdapter(mContext,datas);
              mListView.setAdapter(adapter);

              mText.setText(s);
              mText.setTextSize(90); //心跳数字格式
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
        mListView = (ListView) view.findViewById(R.id.listview_hr);

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


        info.hid=1;
        info.hr=0;
        info.hr1=0;
        info.hr2=0;
        info.hr3=0;
        info.hr4=0;
        //adapters.insert(info);

        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);





    }
}
