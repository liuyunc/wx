package com.wx.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.wx.android.adapter.MFragmentPagerAdapter;
import com.wx.android.base.BaseFragment;
import com.wx.android.fragment.dayFragment;
import com.wx.android.fragment.monthFragment;
import com.wx.android.fragment.weekFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class FHistoryActivity extends FragmentActivity {

    /**
     * 滑动
     */
    private ViewPager mViewPager;

    private FragmentManager fragmentManager;

    private List<BaseFragment> mBaseFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        //初始化Fragment
        initFragment();
        //初始化ViewPager
        InitViewPager();

        Button mButton1 = (Button) findViewById(R.id.month);
        mButton1.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            mViewPager.setCurrentItem(0,false);
                                            return true;
                                        }
        });



        Button mButton2 = (Button) findViewById(R.id.week);
        mButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mViewPager.setCurrentItem(1,false);
                return true;
            }
        });
        Button mButton3 = (Button) findViewById(R.id.day);
        mButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mViewPager.setCurrentItem(2,false);
                return true;
            }
        });
    }


    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new dayFragment());//日Fragment
        mBaseFragment.add(new weekFragment());//周Fragment
        mBaseFragment.add(new monthFragment());//月Fragment
    }

    private void InitViewPager() {

        fragmentManager = getSupportFragmentManager();

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager_history);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, mBaseFragment));


        //设置默认打开第一页
        mViewPager.setCurrentItem(0, false);


        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    /**
     * 页卡切换监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            /*;


            switch (position) {

                //当前为页卡1
                case 0:
                    mButton1.setChecked(true);
                    mButton2.setChecked(false);
                    mButton3.setChecked(false);


                    break;

                //当前为页卡2
                case 1:
                    mButton1.setChecked(false);
                    mButton2.setChecked(true);
                    mButton3.setChecked(false);


                    break;

                //当前为页卡3
                case 2:
                    mButton1.setChecked(false);
                    mButton2.setChecked(false);
                    mButton3.setChecked(true);

                    break;


            }*/


        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

