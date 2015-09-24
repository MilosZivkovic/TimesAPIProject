package com.example.devel.timesapiproject;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

/**
 * Created by Devel on 9/18/2015.
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    MyPagerAdapter myPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageView Logo = (ImageView) findViewById(R.id.Logo);
        Logo.setImageResource(R.drawable.logo);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.add(new TopArticlesFragment());
        myPagerAdapter.add(new SearchArticlesFragment());

        viewPager = (ViewPager) findViewById(R.id.selection);
        viewPager.setAdapter(myPagerAdapter);

        viewPager.addOnPageChangeListener(this);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
