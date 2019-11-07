package com.spectraparent.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.gson.Gson;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Models.RideModel;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cancel_Ride_Page_Activity extends AppCompatActivity {

    DateRangeCalendarView dateRangeCalendarView;
    private RideModel mRide;

    public Cancel_Ride_Page_Activity() {
        // Required empty public constructor
    }

    @BindView(R.id.txtRideName)
    TextView mRideName;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.txtCreatedOn)
    TextView mCreatedOn;

    @BindView(R.id.txtPickup)
    TextView mPickup;

    @BindView(R.id.txtDrop)
    TextView mDrop;

    @BindView(R.id.tvdriverName)
    TextView tvdriverName;
    @BindView(R.id.tvDrivingCar)
    TextView tvDrivingCar;
    @BindView(R.id.tvCarNo)
    TextView tvCarNo;
    @BindView(R.id.ivDriveImage)
    ImageView ivDriveImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_ride_page);
        ButterKnife.bind(this);
        mRide = new Gson().fromJson(getIntent().getStringExtra("json"), RideModel.class);
        fillData();
        setData();

    }

    private void fillData() {
        toolbar_title.setText(mRide.getRideName());
        mRideName.setText(mRide.getRideName());
        if (mRide.getChildModel().get(0).getPickup() != null && mRide.getChildModel().get(0).getPickup().getName() != null)
            mPickup.setText(mRide.getChildModel().get(0).getPickup().getName());
        if (mRide.getChildModel().get(0).getDrop() != null && mRide.getChildModel().get(0).getDrop().getName() != null)
            mDrop.setText(mRide.getChildModel().get(0).getDrop().getName());
        if (mRide.getCreatedOn() != null)
            mCreatedOn.setText(Tools.getFormattedDate(this, mRide.getCreatedOn().getTime()));
    }

    private void setData() {
        tvdriverName.setText(mRide.getDriver().getFirstName());

        Picasso.get().load(mRide.getDriver().getPhoto().getSmallPhotoUrl()).
                placeholder(R.drawable.no_profile)
                .fit().centerCrop().into(ivDriveImage);
        String carColor = mRide.getDriver().getAssignedCar().getCar().getColor() != null ? mRide.getDriver().getAssignedCar().getCar().getColor() : "" + " ";
        String carBrand = mRide.getDriver().getAssignedCar().getCar().getBrand() != null ? mRide.getDriver().getAssignedCar().getCar().getBrand() : "" + " ";
        String carModel = mRide.getDriver().getAssignedCar().getCar().getModel() != null ? mRide.getDriver().getAssignedCar().getCar().getModel() : "" + " ";
        tvDrivingCar.setText("is driving " + carColor + carBrand + carModel);
        tvCarNo.setText(mRide.getDriver().getAssignedCar().getCar().getNumber() != null ? mRide.getDriver().getAssignedCar().getCar().getNumber() : "");
    }



    @OnClick(R.id.btnCancelRide)
    void cancelRide() {
        DialogsHelper.showAlert(this,"Information","Please call your contact for the school district.","Ok","", PromptDialog.DIALOG_TYPE_INFO);
//        Intent intent = new Intent(this, Cancelellation_Form_Activity.class);
//        intent.putExtra("json", new Gson().toJson(mRide));
//        startActivity(intent);
//        finish();
    }

    @OnClick(R.id.btnEditRide)
    void btnEditRide() {
    }
    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }
}