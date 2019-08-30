package com.spectraparent.Activities.AddChild;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.spectraparent.Activities.MainHomeActivity;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.INavigation;
import com.spectraparent.android.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class AddChildActivity extends AppCompatActivity implements INavigation {

    private WormDotsIndicator dotsIndicator;
    private ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        dotsIndicator = findViewById(R.id.spring_dots_indicator);


        mViewpager = (ViewPager) findViewById(R.id.vpPages);
        mViewpager.setAdapter(new AddChildViewPagerAdapter(getSupportFragmentManager()));

        dotsIndicator.setViewPager(mViewpager);
    }

    public void onBackClicked(View view) {
        moveBack();
    }

    @Override
    public void moveNext() {
        if(mViewpager.getCurrentItem() < mViewpager.getChildCount()){
            mViewpager.setCurrentItem(mViewpager.getCurrentItem() + 1);
        }else {
            Intent intent = new Intent(this, MainHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void moveBack() {
        if(mViewpager.getCurrentItem() > 0){
            mViewpager.setCurrentItem(mViewpager.getCurrentItem() - 1);
        }else {
            LocalStorage.setAddChildSkipped(true);
            finish();
        }
    }
}
