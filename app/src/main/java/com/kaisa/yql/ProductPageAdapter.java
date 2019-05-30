package com.kaisa.yql;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ProductPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> list;
    private ArrayList<String> titles;

    public ProductPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public ProductPageAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override

    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
