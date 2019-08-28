package com.spectradrive.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spectradrive.Helpers.Tools;
import com.spectradrive.Models.RideModel;
import com.spectradrive.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends BaseActivity {

    @BindView(R.id.txtRideName)
    TextView mRideName;

    @BindView(R.id.txtCreatedOn)
    TextView mCreatedOn;

    @BindView(R.id.txtPickup)
    TextView mPickup;

    @BindView(R.id.txtDrop)
    TextView mDrop;

    private RideModel mRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Ride 123456 Details");

        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRide = new Gson().fromJson(getIntent().getStringExtra("ride"), RideModel.class);

        fillData();
    }

    private void fillData() {
        mRideName.setText(mRide.getRideName());
        mPickup.setText(mRide.getPickupPoint().getName());
        mDrop.setText(mRide.getDropPoint().getName());
        mCreatedOn.setText(Tools.getFormattedDate(this, mRide.getCreatedOn().getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
