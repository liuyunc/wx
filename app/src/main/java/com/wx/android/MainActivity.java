package com.wx.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.wx.android.adapter.MFragmentPagerAdapter;
import com.wx.android.base.BaseFragment;
import com.wx.android.fragment.CommonFrameFragment;
import com.wx.android.fragment.CustomFragment;
import com.wx.android.fragment.OtherFragment;
import com.wx.android.fragment.ThirdPartyFragment;
import com.wx.android.fx.AddPopWindow;

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

    /**
    * 菜单
    * */
    private ImageView iv_add;

    private List<BaseFragment> mBaseFragment;
    /**
     * 页面号
     */


    /**
     * 选中的Fragment的对应的位置
     */
    private int position=0;


    /**
     * 滑动
     */
    private ViewPager mViewPager;

    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据库
        FlowManager.init(this);
        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //初始化ViewPager
        InitViewPager();
        //设置RadioGroup的监听
        setListener();
        iv_add = (ImageView) this.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddPopWindow addPopWindow = new AddPopWindow(MainActivity.this);
                addPopWindow.showPopupWindow(iv_add);
            }

        });

    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        fragmentManager = getSupportFragmentManager();

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, mBaseFragment));


        //设置默认打开第一页
        mViewPager.setCurrentItem(0,false);


        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }


    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                    switch (checkedId) {
                                                        case R.id.rb_common_frame://常用框架
                                                            mViewPager.setCurrentItem(0,false);
                                                            break;
                                                        case R.id.rb_thirdparty://第三方
                                                            mViewPager.setCurrentItem(1,false);
                                                            break;
                                                        case R.id.rb_custom://自定义
                                                            mViewPager.setCurrentItem(2,false);
                                                            break;
                                                        case R.id.rb_other://其他
                                                            mViewPager.setCurrentItem(3,false);
                                                            break;
                                                        default:
                                                            mViewPager.setCurrentItem(0,false);
                                                            break;
                                                    }
                                                }
                                            });
    }




     //@param from 刚显示的Fragment,马上就要被隐藏了
     //@param to   马上要切换到的Fragment，一会要显示

/*    private void switchFrament(Fragment from, Fragment to) {
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

    }*/

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
            RadioButton mButton1 = (RadioButton)findViewById(R.id.rb_common_frame);
            RadioButton mButton2 = (RadioButton)findViewById(R.id.rb_thirdparty);
            RadioButton mButton3 = (RadioButton)findViewById(R.id.rb_custom);
            RadioButton mButton4 = (RadioButton)findViewById(R.id.rb_other);

            switch (position) {

                //当前为页卡1
                case 0:
                    mButton1.setChecked(true);
                    mButton2.setChecked(false);
                    mButton3.setChecked(false);
                    mButton4.setChecked(false);

                    break;

                //当前为页卡2
                case 1:
                    mButton1.setChecked(false);
                    mButton2.setChecked(true);
                    mButton3.setChecked(false);
                    mButton4.setChecked(false);

                    break;

                //当前为页卡3
                case 2:
                    mButton1.setChecked(false);
                    mButton2.setChecked(false);
                    mButton3.setChecked(true);
                    mButton4.setChecked(false);
                    break;

                case 3:
                    mButton1.setChecked(false);
                    mButton2.setChecked(false);
                    mButton3.setChecked(false);
                    mButton4.setChecked(true);
                    break;
            }


        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}