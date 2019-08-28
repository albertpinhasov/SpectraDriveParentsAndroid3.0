package com.spectradrive.Activities.AddTrustedPerson;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.spectradrive.Activities.MainHomeActivity;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.INavigation;
import com.spectradrive.android.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import butterknife.ButterKnife;

public class AddTrustedPersonActivity extends AppCompatActivity implements INavigation {

    private WormDotsIndicator dotsIndicator;
    private ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trusted_person);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Add trusted person");

        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dotsIndicator = findViewById(R.id.spring_dots_indicator);

        mViewpager = findViewById(R.id.vpPages);
        mViewpager.setAdapter(new AddTrustedPersonViewPagerAdapter(getSupportFragmentManager()));

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
            mViewpager.setCurrentItem(mViewpager.getCurrentItem()-1);
        }else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        LocalStorage.setTrustedPersonIntroSkipped(true);
        return super.onOptionsItemSelected(item);
    }
}
