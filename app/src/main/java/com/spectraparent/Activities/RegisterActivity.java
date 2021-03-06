package com.spectraparent.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.spectraparent.Fragments.EnterCodeFragment;
import com.spectraparent.INavigation;
import com.spectraparent.android.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity implements INavigation {

    @BindView(R.id.spring_dots_indicator)
    WormDotsIndicator dotsIndicator;

    @BindView(R.id.vpPages)
    ViewPager mViewpager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mViewpager.setAdapter(new RegisterViewPagerAdapter(getSupportFragmentManager(), getIntent().getBooleanExtra("isFromLogin", false)));

        dotsIndicator.setViewPager(mViewpager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("isFromLogin", false)) {
            EnterCodeFragment fragment = (EnterCodeFragment) ((RegisterViewPagerAdapter) mViewpager.getAdapter()).getItem(0);
            if (fragment != null) {
                fragment.scheduleResendTimer(this);
            }
        }
    }

    public void onBackClicked(View view) {
        moveBack();
    }

    @Override
    public void moveNext() {
        if (mViewpager.getCurrentItem() < mViewpager.getChildCount()) {
            mViewpager.setCurrentItem(mViewpager.getCurrentItem() + 1);
            Fragment fragment = ((RegisterViewPagerAdapter) mViewpager.getAdapter()).getItem(mViewpager.getCurrentItem());
            if (fragment instanceof EnterCodeFragment) {
                ((EnterCodeFragment) fragment).scheduleResendTimer(this);
            }
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
            finish();
        }
    }


}
