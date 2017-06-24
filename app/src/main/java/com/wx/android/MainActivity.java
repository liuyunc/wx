package com.wx.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.wx.android.adapter.MFragmentPagerAdapter;
import com.wx.android.base.BaseFragment;
import com.wx.android.fragment.CommonFrameFragment;
import com.wx.android.fragment.CustomFragment;
import com.wx.android.fragment.OtherFragment;
import com.wx.android.fragment.ThirdPartyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/21 18:42
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主页面
 */
public class MainActivity  extends FragmentActivity {

    private RadioGroup mRg_main;
    private List<BaseFragment> mBaseFragment;
    /**
     * 页面号
     */
    private int currIndex = 0;

    /**
     * 选中的Fragment的对应的位置
     */
    private int position;

    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;
    /**
     * 滑动
     */
    private ViewPager mViewPager;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
        //初始化ViewPager
        InitViewPager();
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, mBaseFragment));

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(2);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);


        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_common_frame);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_common_frame://常用框架
                    position = 0;
                    break;
                case R.id.rb_thirdparty://第三方
                    position = 1;
                    break;
                case R.id.rb_custom://自定义
                    position = 2;
                    break;
                case R.id.rb_other://其他
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换
            switchFrament(mContent, to);

        }
    }


    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFrament(Fragment from, Fragment to) {
        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                if (to != null) {
                    ft.add(R.id.fl_content, to).commit();
                }
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                if (to != null) {
                    ft.show(to).commit();
                }
            }
        }

    }

//    private void switchFrament(BaseFragment fragment) {
//        //1.得到FragmentManger
//        FragmentManager fm = getSupportFragmentManager();
//        //2.开启事务
//        FragmentTransaction transaction = fm.beginTransaction();
//        //3.替换
//        transaction.replace(R.id.fl_content, fragment);
//        //4.提交事务
//        transaction.commit();
//    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new CommonFrameFragment());//常用框架Fragment
        mBaseFragment.add(new ThirdPartyFragment());//第三方Fragment
        mBaseFragment.add(new CustomFragment());//自定义控件Fragment
        mBaseFragment.add(new OtherFragment());//其他Fragment

        fragmentManager = getSupportFragmentManager();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);

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

            switch (position) {

                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 1) {


                    } else if (currIndex == 2) {//从页卡1跳转转到页卡3

                    }
                    break;

                //当前为页卡2
                case 1:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {

                    } else if (currIndex == 2) { //从页卡1跳转转到页卡2

                    }
                    break;

                //当前为页卡3
                case 2:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {

                    } else if (currIndex == 1) {//从页卡1跳转转到页卡2

                    }
                    break;
            }
            currIndex = position;

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}