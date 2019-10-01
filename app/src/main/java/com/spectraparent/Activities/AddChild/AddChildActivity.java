package com.spectraparent.Activities.AddChild;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.spectraparent.Activities.MainHomeActivity;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.INavigation;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.RideModel;
import com.spectraparent.android.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class AddChildActivity extends AppCompatActivity implements INavigation {

    private WormDotsIndicator dotsIndicator;
    private ViewPager mViewpager;
    public String from = "";
    public Child childModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            childModel = new Gson().fromJson(getIntent().getStringExtra("child"), Child.class);
        }
        dotsIndicator = findViewById(R.id.spring_dots_indicator);


        mViewpager = (ViewPager) findViewById(R.id.vpPages);
        mViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mViewpager.setAdapter(new AddChildViewPagerAdapter(getSupportFragmentManager()));

        dotsIndicator.setViewPager(mViewpager);
    }

    public void onBackClicked(View view) {
        moveBack();
    }

    @Override
    public void moveNext() {
        if (mViewpager.getCurrentItem() < mViewpager.getChildCount()) {
            mViewpager.setCurrentItem(mViewpager.getCurrentItem() + 1);
        } else {
            Intent intent = new Intent(this, MainHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void moveBack() {
        if (mViewpager.getCurrentItem() > 0) {
            mViewpager.setCurrentItem(mViewpager.getCurrentItem() - 1);
        } else {
            LocalStorage.setAddChildSkipped(true);
            finish();
        }
    }
}
