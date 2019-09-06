package com.spectraparent.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.spectraparent.Fragments.ProfileFragment;
import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Models.CancelRideModel;
import com.spectraparent.Models.RideCancelResponse;
import com.spectraparent.Models.RideModel;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cancelellation_Form_Activity extends AppCompatActivity {


    @BindView(R.id.calendar)
    DateRangeCalendarView dateRangeCalendarView;
    private RideModel mRide;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.rdGroup)
    RadioGroup rdGroup;
    String stardate = "";
    String enddate = "";
    String selectedReason = "";
    private KProgressHUD mProgress;

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
        dateRangeCalendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                stardate = convert(startDate.getTime().toString());
                enddate ="";
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                stardate = convert(startDate.getTime().toString());
                enddate = convert(endDate.getTime().toString());

            }
        });
    }

    @OnClick(R.id.btnCancelledRequest)
    void cancelledRequest() {
        VolleyUtils v = VolleyUtils.getInstance(Cancelellation_Form_Activity.this);

        int selectedId = rdGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        if (radioButton != null) {
            selectedReason = radioButton.getText().toString();
        }
        if (selectedReason.isEmpty()) {
            DialogsHelper.showAlert(Cancelellation_Form_Activity.this, "No reason Selected", "Please selected a reason", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }
        if (stardate.isEmpty()) {
            DialogsHelper.showAlert(Cancelellation_Form_Activity.this, "No Date Selected", "Please select start date.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }
        mProgress = KProgressHUD.create(Cancelellation_Form_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Cancelling your ride")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        CancelRideModel cancelRideModel = new CancelRideModel();
        cancelRideModel.setCancellationStartDate(stardate);
        cancelRideModel.setCancellationEndDate(enddate);
        cancelRideModel.setReason(selectedReason);
        cancelRideModel.setRideID(mRide.getRideId());
        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.CancelRide, cancelRideModel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<RideCancelResponse>() {
                }.getType();
                mProgress.dismiss();

                System.out.println("Response " + WebApi.CancelRide + "======>" + new Gson().toJson(response));

                RideCancelResponse student = new Gson().fromJson(response, listType);

                if (student != null) {
                    if (student.isSuccess()) {
                        DialogsHelper.showAlert(Cancelellation_Form_Activity.this, "Success", student.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_SUCCESS, new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });

                    } else {
                        DialogsHelper.showAlert(Cancelellation_Form_Activity.this, "Server error", student.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    }
                } else {
                    DialogsHelper.showAlert(Cancelellation_Form_Activity.this, "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();

                System.out.println(error);
                DialogsHelper.showAlert(Cancelellation_Form_Activity.this, "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }


    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }

    String convert(String dateString) {
        Log.e("dateee", dateString);
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Log.e("dateee", targetFormat.format(date));
            return targetFormat.format(date);
        } else {
            return null;
        }
    }
}
