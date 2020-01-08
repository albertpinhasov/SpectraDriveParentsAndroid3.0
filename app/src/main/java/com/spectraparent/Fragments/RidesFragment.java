package com.spectraparent.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.spectraparent.Activities.Cancel_Ride_Page_Activity;
import com.spectraparent.Activities.MapsActivity;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.EndlessRecyclerViewScrollListener;
import com.spectraparent.Helpers.LocalStorage;
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
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private OnListFragmentInteractionListener mListener;
    private MyRidesRecyclerViewAdapter mAdapter;
    private MyRidesRecyclerViewAdapter mAdapterEmpty;
    private KProgressHUD mProgress;
    int checkedId = R.id.b1;
    int current_Ride_Page = 1;
    int past_Ride_Page = 1;
    int scedual_ride_page = 1;
    ArrayList<RideModel> currentRideList = new ArrayList<>();
    ArrayList<RideModel> pastRideList = new ArrayList<>();
    ArrayList<RideModel> scedualRideList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = null;


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
                    RidesFragment.this.checkedId = R.id.b1;
                    mRideType.setText("Current rides");
                    if (currentRideList.isEmpty())
                        getMyRides(1, 1);
                    else {
                        mEmpty.setVisibility(View.GONE);
                        mAdapter.updateItems(currentRideList, 1);
                    }
                } else if (checkedId == R.id.b2) {
                    RidesFragment.this.checkedId = R.id.b2;
                    mRideType.setText("Scheduled rides");
                    if (scedualRideList.isEmpty())
                        getMyRides(3, 1);
                    else {
                        mEmpty.setVisibility(View.GONE);
                        mAdapter.updateItems(scedualRideList, 3);
                    }
                } else if (checkedId == R.id.b3) {
                    RidesFragment.this.checkedId = R.id.b3;
                    mRideType.setText("Past rides");
                    if (pastRideList.isEmpty())
                        getMyRides(2, 1);
                    else {
                        mEmpty.setVisibility(View.GONE);
                        mAdapter.updateItems(pastRideList, 2);
                    }

                }
            }
        });

        mAdapter = new MyRidesRecyclerViewAdapter(new ArrayList<RideModel>(), mListener);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        mRc.setLayoutManager(linearLayoutManager);
        mRc.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (checkedId == R.id.b1) {
                    RidesFragment.this.current_Ride_Page++;
                    mRideType.setText("Current rides");
                    getMyRides(1, current_Ride_Page);
                } else if (checkedId == R.id.b2) {
                    scedual_ride_page++;
                    mRideType.setText("Scheduled rides");
                    getMyRides(3, scedual_ride_page);

                } else if (checkedId == R.id.b3) {
                    past_Ride_Page++;
                    mRideType.setText("Past rides");
                    getMyRides(2, past_Ride_Page);
                }
            }
        });
        mRc.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkedId == R.id.b1) {
                    currentRideList.clear();
                    current_Ride_Page = 1;
                    getMyRides(1, current_Ride_Page);
                } else if (checkedId == R.id.b2) {
                    scedualRideList.clear();
                    scedual_ride_page = 1;
                    getMyRides(3, scedual_ride_page);
                } else if (checkedId == R.id.b3) {
                    pastRideList.clear();
                    past_Ride_Page = 1;
                    getMyRides(2, past_Ride_Page);
                }
                swipeRefreshLayout.setRefreshing(false);

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
            public void onListFragmentInteraction(RideModel item, int type) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 4);
                    return;
                }
                final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                    return;

                }
                if (type == 1) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("json", new Gson().toJson(item));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), Cancel_Ride_Page_Activity.class);
                    intent.putExtra("json", new Gson().toJson(item));
                    startActivity(intent);
                }
            }
        };

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 4) {
            final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
        }

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

    private void getMyRides(final int type, int page) {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());
        mProgress = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting your rides")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RideRequest rideRequest = new RideRequest(page, 10, LocalStorage.getStudent().getUserId(), type);
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

                    switch (type) {
                        case 1: {
                            currentRideList.addAll(rides.getData());
                            if (currentRideList.size() > 0) {
                                mEmpty.setVisibility(View.GONE);
                            } else {
                                mEmpty.setVisibility(View.VISIBLE);

                            }
                            mAdapter.updateItems(currentRideList, 1);

                            return;
                        }
                        case 2: {
                            pastRideList.addAll(rides.getData());
                            if (pastRideList.size() > 0) {
                                mEmpty.setVisibility(View.GONE);
                            } else {
                                mEmpty.setVisibility(View.VISIBLE);

                            }
                            mAdapter.updateItems(pastRideList, 2);
                            return;
                        }
                        case 3: {
                            scedualRideList.addAll(rides.getData());
                            if (scedualRideList.size() > 0) {
                                mEmpty.setVisibility(View.GONE);
                            } else {
                                mEmpty.setVisibility(View.VISIBLE);

                            }
                            mAdapter.updateItems(scedualRideList, 3);

                            return;
                        }
                    }

                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                    DialogsHelper.showAlert(getActivity(), "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                }


            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mEmpty.setVisibility(View.VISIBLE);
                mProgress.dismiss();
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        current_Ride_Page = 1;
        past_Ride_Page = 1;
        scedual_ride_page = 1;
        currentRideList.clear();
        scedualRideList.clear();
        pastRideList.clear();
        if (checkedId == R.id.b2) {
            mRideType.setText("Scheduled rides");
            getMyRides(3, scedual_ride_page);

        } else if (checkedId == R.id.b3) {
            mRideType.setText("Past rides");
            getMyRides(2, past_Ride_Page);
        } else {
            mRideType.setText("Current rides");
            getMyRides(1, current_Ride_Page);
        }
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

                getMyRides(1, current_Ride_Page);

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
        void onListFragmentInteraction(RideModel item, int type);
    }

//    void getData() {
//        currentRideList.clear();
//        Type listType = new TypeToken<RideResponse>() {
//        }.getType();
//        mProgress.dismiss();
//
//        RideResponse rides = new Gson().fromJson(result, listType);
//
//        if (rides != null) {
//
//            Collections.sort(rides.getData(), new Comparator<RideModel>() {
//                @Override
//                public int compare(RideModel o1, RideModel o2) {
//                    if (o1.getCreatedOn().getTime() == o2.getCreatedOn().getTime())
//                        return 0;
//                    return (o1.getCreatedOn().getTime() > o2.getCreatedOn().getTime() ? -1 : 1);
//                }
//            });
//
//            switch (1) {
//                case 1: {
//                    currentRideList.addAll(rides.getData());
//                    if (currentRideList.size() > 0) {
//                        mEmpty.setVisibility(View.GONE);
//                    } else {
//                        mEmpty.setVisibility(View.VISIBLE);
//
//                    }
//                    mAdapter.updateItems(currentRideList, 1);
//
//                    return;
//                }
//                case 2: {
//                    pastRideList.addAll(rides.getData());
//                    if (pastRideList.size() > 0) {
//                        mEmpty.setVisibility(View.GONE);
//                    } else {
//                        mEmpty.setVisibility(View.VISIBLE);
//
//                    }
//                    mAdapter.updateItems(pastRideList, 2);
//                    return;
//                }
//                case 3: {
//                    scedualRideList.addAll(rides.getData());
//                    if (scedualRideList.size() > 0) {
//                        mEmpty.setVisibility(View.GONE);
//                    } else {
//                        mEmpty.setVisibility(View.VISIBLE);
//
//                    }
//                    mAdapter.updateItems(scedualRideList, 3);
//
//                    return;
//                }
//            }
//
//        } else {
//            mEmpty.setVisibility(View.VISIBLE);
//            DialogsHelper.showAlert(getActivity(), "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
//        }
//
//    }
}
