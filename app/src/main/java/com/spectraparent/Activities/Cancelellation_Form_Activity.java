package com.spectraparent.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.gson.Gson;
import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Models.RideModel;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cancelellation_Form_Activity extends AppCompatActivity {

    DateRangeCalendarView dateRangeCalendarView;
    private RideModel mRide;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.rdGroup)
    RadioGroup rdGroup;

    public Cancelellation_Form_Activity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_ride);
        ButterKnife.bind(this);
        mRide = new Gson().fromJson(getIntent().getStringExtra("json"), RideModel.class);
        toolbar_title.setText("Cancellation of " + mRide.getRideName());
    }

    @OnClick(R.id.btnCancelledRequest)
    void cancelledRequest() {
        int selectedId = rdGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        if (radioButton != null) {
            String selectedReason = radioButton.getText().toString();
        } else {

        }
    }

    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }

}