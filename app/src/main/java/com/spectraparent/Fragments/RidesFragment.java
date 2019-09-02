package com.spectraparent.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.spectraparent.Activities.DetailsActivity;
import com.spectraparent.Activities.MapsActivity;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Models.RideModel;
import com.spectraparent.Models.RideRequest;
import com.spectraparent.Models.RideResponse;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

public class RidesFragment extends Fragment {

    @BindView(R.id.txtRideType)
    TextView mRideType;
    @BindView(R.id.segment)
    SegmentedGroup mSegment;
    RecyclerView mRc;
    @BindView(R.id.lEmpty)
    LinearLayout mEmpty;

    private OnListFragmentInteractionListener mListener;
    private MyRidesRecyclerViewAdapter mAdapter;
    private MyRidesRecyclerViewAdapter mAdapterEmpty;
    private KProgressHUD mProgress;

    public RidesFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSegment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.b1) {
                    mRideType.setText("Current rides");
                    getMyRides(1);
                } else if (checkedId == R.id.b2) {
                    mRideType.setText("Scheduled rides");
                    getMyRides(3);
                } else if (checkedId == R.id.b3) {
                    mRideType.setText("Past rides");
                    getMyRides(2);

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rides_list, container, false);


        mRc = view.findViewById(R.id.list);

        mListener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(RideModel item) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("json", new Gson().toJson(item));
                startActivity(intent);
            }
        };

        mAdapter = new MyRidesRecyclerViewAdapter(new ArrayList<RideModel>(), mListener);

        mRc.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getMyRides(int type) {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());

        mProgress = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting your rides")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RideRequest rideRequest = new RideRequest(1, 10, "6bf08be9-2e85-4775-b792-a8396c969d87", type);
        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.GetMyRidesUrl, rideRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<RideResponse>() {
                }.getType();
                mProgress.dismiss();
                System.out.println("Response " + WebApi.GetMyRidesUrl + "======>" + new Gson().toJson(response));
                RideResponse rides = new Gson().fromJson(response, listType);

                if (rides != null) {

                    Collections.sort(rides.getData(), new Comparator<RideModel>() {
                        @Override
                        public int compare(RideModel o1, RideModel o2) {
                            if (o1.getCreatedOn().getTime() == o2.getCreatedOn().getTime())
                                return 0;
                            return (o1.getCreatedOn().getTime() > o2.getCreatedOn().getTime() ? -1 : 1);
                        }
                    });

                    mAdapter.updateItems((ArrayList<RideModel>) rides.getData());
                } else {
                    DialogsHelper.showAlert(getActivity(), "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                }

                int visibility = mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE;

                mEmpty.setVisibility(visibility);

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyRides(1);
    }

    @OnClick({R.id.btnSamplePoolFromSchoolRide, R.id.btnSamplePoolToSchoolRide, R.id.btnSampleRide})
    void createSampleRide(View view) {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());

        int type = 0;

        if (view.getId() == R.id.btnSamplePoolToSchoolRide) {
            type = 1;
        } else if (view.getId() == R.id.btnSamplePoolFromSchoolRide) {
            type = 2;
        }

        mProgress = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                //.setDetailsLabel("Getting your rides")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.CreateDummyRideUrl + "?type=" + String.valueOf(type), null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<RideModel>>() {
                }.getType();
                mProgress.dismiss();

                getMyRides(1);

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(RideModel item);
    }

}
