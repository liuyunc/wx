package com.wx.android.adapter;

import android.support.v4.view.PagerAdapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import com.wx.android.base.BaseFragment;

import java.util.List;

/**
 * Fragment适配器
 * @author weizhi
 * @version 1.0
 */
public class MFragmentPagerAdapter extends FragmentPagerAdapter {

    //存放Fragment的数组
    private List<BaseFragment> fragmentsList;

    public MFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}
