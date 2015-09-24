package com.example.devel.timesapiproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devel on 9/20/2015.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    public final int MIN_NUM_ITEMS = 2;

    List<Fragment> fragments;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>(getCount());
    }

    public void add(Fragment fragment){
        if(fragments.size() < MIN_NUM_ITEMS)
            fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return MIN_NUM_ITEMS;
    }
}
