package com.spectraparent.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    String result = "{\n" +
            "    \"success\": true,\n" +
            "    \"message\": \"Get Driver rides\",\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"child\": [\n" +
            "                {\n" +
            "                    \"child\": {\n" +
            "                        \"childId\": \"c282bb79-09e8-4001-9d18-4982632c1505\",\n" +
            "                        \"firstName\": \"testPa123\",\n" +
            "                        \"lastName\": \"user\",\n" +
            "                        \"fullName\": \"testPa123 user\",\n" +
            "                        \"about\": \"test childs\",\n" +
            "                        \"images\": [\n" +
            "                            {\n" +
            "                                \"photoId\": \"82b575f2-981d-4853-97b0-d832c9c22631\",\n" +
            "                                \"smallPhotoUrl\": \"https://etapi.softprodigy.in//ChildImages/USER_2/8f8e14849adc4bd0999f922a55df1cca.jpg\",\n" +
            "                                \"standardPhotoUrl\": \"https://etapi.softprodigy.in//ChildImages/USER_2/8f8e14849adc4bd0999f922a55df1cca.jpg\",\n" +
            "                                \"name\": null,\n" +
            "                                \"createdOn\": \"2019-09-18T16:19:07.4903696Z\"\n" +
            "                            }\n" +
            "                        ],\n" +
            "                        \"specialNeeds\": \"Visually impaired\",\n" +
            "                        \"otherSpecialNeeds\": null,\n" +
            "                        \"parent\": null,\n" +
            "                        \"createdOn\": null,\n" +
            "                        \"parentPhoneNumber\": \"+1 (485) 783-9475\",\n" +
            "                        \"trustedPersonPhoneNumber\": \"+1 (424) 234-2342\"\n" +
            "                    },\n" +
            "                    \"pickup\": {\n" +
            "                        \"locationId\": null,\n" +
            "                        \"lat\": 30.7410517,\n" +
            "                        \"lon\": 76.779015000000072,\n" +
            "                        \"name\": \"Sector 17\",\n" +
            "                        \"googleJson\": null,\n" +
            "                        \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"drop\": {\n" +
            "                        \"locationId\": null,\n" +
            "                        \"lat\": 30.679282399999991,\n" +
            "                        \"lon\": 76.729323300000033,\n" +
            "                        \"name\": \"Sector 67\",\n" +
            "                        \"googleJson\": null,\n" +
            "                        \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"contacts\": null,\n" +
            "                    \"events\": [],\n" +
            "                    \"index\": 0\n" +
            "                }\n" +
            "            ],\n" +
            "            \"scheduledOn\": \"2019-09-19T03:30:00Z\",\n" +
            "            \"rideId\": \"47086597-8125-4b3c-bd41-93bc7fd50079\",\n" +
            "            \"driver\": {\n" +
            "                \"assignedCar\": {\n" +
            "                    \"assignedCarId\": null,\n" +
            "                    \"car\": {\n" +
            "                        \"carId\": \"9739850f-74aa-4089-9b19-e4bbd5436ad8\",\n" +
            "                        \"make\": \"2010-09-19\",\n" +
            "                        \"mfgDate\": null,\n" +
            "                        \"number\": \"1234567890\",\n" +
            "                        \"brand\": null,\n" +
            "                        \"model\": \"Swift\",\n" +
            "                        \"color\": \"White\",\n" +
            "                        \"registrationNumber\": null,\n" +
            "                        \"vin\": null,\n" +
            "                        \"insuranceExpiry\": null,\n" +
            "                        \"licenseExpiry\": null,\n" +
            "                        \"insuranceExpiryDate\": null,\n" +
            "                        \"licenseExpiryDate\": null,\n" +
            "                        \"expiryInsuranceDate\": null,\n" +
            "                        \"expiryLicenseDate\": null,\n" +
            "                        \"manufatureDate\": null,\n" +
            "                        \"createdOn\": \"2019-09-19T06:56:30.3559739Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                    \"updatedOn\": null,\n" +
            "                    \"isActive\": false\n" +
            "                },\n" +
            "                \"drivingLicenseFront\": null,\n" +
            "                \"vehicleInsurance\": null,\n" +
            "                \"vehicleRegistrationDocument\": null,\n" +
            "                \"currentLocation\": null,\n" +
            "                \"vehicleImageUploadDocument\": null,\n" +
            "                \"phoneNumber\": null,\n" +
            "                \"deviceToken\": null,\n" +
            "                \"deviceType\": null,\n" +
            "                \"birthday\": null,\n" +
            "                \"profile\": null,\n" +
            "                \"token\": null,\n" +
            "                \"userIdStr\": null,\n" +
            "                \"child\": null,\n" +
            "                \"trustedPersons\": null,\n" +
            "                \"schoolLocation\": null,\n" +
            "                \"learningCenterLocation\": null,\n" +
            "                \"homeLocation\": null,\n" +
            "                \"userId\": \"f7d50f43-cc3b-4125-8c4e-649e5b0db766\",\n" +
            "                \"title\": null,\n" +
            "                \"firstName\": \"Rahul\",\n" +
            "                \"lastName\": \"Singh\",\n" +
            "                \"gender\": -1,\n" +
            "                \"photo\": {\n" +
            "                    \"photoId\": \"6f663fb9-a438-42d3-976b-61a9568ff3de\",\n" +
            "                    \"smallPhotoUrl\": \"https://etapi.softprodigy.in//UserDocuments/DRIVER_7/7f0f03d3a1724896a7510ba76c383889.jpg\",\n" +
            "                    \"standardPhotoUrl\": \"https://etapi.softprodigy.in//UserDocuments/DRIVER_7/7f0f03d3a1724896a7510ba76c383889.jpg\",\n" +
            "                    \"name\": null,\n" +
            "                    \"createdOn\": \"2019-09-19T06:59:29.8357217Z\"\n" +
            "                },\n" +
            "                \"isFirstLogin\": false,\n" +
            "                \"role\": null,\n" +
            "                \"email\": null\n" +
            "            },\n" +
            "            \"driverId\": \"f7d50f43-cc3b-4125-8c4e-649e5b0db766\",\n" +
            "            \"locationData\": [],\n" +
            "            \"poolData\": null,\n" +
            "            \"userId\": null,\n" +
            "            \"rideName\": \"Ride 34\",\n" +

            "            \"comments\": null,\n" +
            "            \"scheduledRideOn\": \"0001-01-01T00:00:00Z\",\n" +
            "            \"rideStatus\": 0,\n" +
            "            \"createdOn\": \"2019-09-19T07:01:34.2158526Z\",\n" +
            "            \"updatedOn\": null,\n" +
            "            \"isActive\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"child\": [\n" +
            "                {\n" +
            "                    \"child\": {\n" +
            "                        \"childId\": \"126ee70f-7fb5-4947-81ce-26d2bdd8bc9c\",\n" +
            "                        \"firstName\": \"Albert\",\n" +
            "                        \"lastName\": \"Raw\",\n" +
            "                        \"fullName\": \"Albert Raw\",\n" +
            "                        \"about\": \"bxbx\",\n" +
            "                        \"images\": [],\n" +
            "                        \"specialNeeds\": \"Multiple Disabilities,\",\n" +
            "                        \"otherSpecialNeeds\": \"null\",\n" +
            "                        \"parent\": null,\n" +
            "                        \"createdOn\": null,\n" +
            "                        \"parentPhoneNumber\": \"+91988 883 4530\",\n" +
            "                        \"trustedPersonPhoneNumber\": \"(997) 798 8888\"\n" +
            "                    },\n" +
            "                    \"pickup\": {\n" +
            "                        \"locationId\": null,\n" +
            "                        \"lat\": 30.7320385,\n" +
            "                        \"lon\": 76.772633400000018,\n" +
            "                        \"name\": \"Sector 22\",\n" +
            "                        \"googleJson\": null,\n" +
            "                        \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"drop\": {\n" +
            "                        \"locationId\": null,\n" +
            "                        \"lat\": 30.6961314,\n" +
            "                        \"lon\": 76.71584919999998,\n" +
            "                        \"name\": \"Sector 70\",\n" +
            "                        \"googleJson\": null,\n" +
            "                        \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"contacts\": null,\n" +
            "                    \"events\": [],\n" +
            "                    \"index\": 1\n" +
            "                },\n" +
            "                {\n" +
            "                    \"child\": {\n" +
            "                        \"childId\": \"89193a96-33ee-4794-a116-1c0643d1e685\",\n" +
            "                        \"firstName\": \"Raw\",\n" +
            "                        \"lastName\": \"Albert\",\n" +
            "                        \"fullName\": \"Raw Albert\",\n" +
            "                        \"about\": \"bffb\",\n" +
            "                        \"images\": [],\n" +
            "                        \"specialNeeds\": \"Development delay,\",\n" +
            "                        \"otherSpecialNeeds\": \"null\",\n" +
            "                        \"parent\": null,\n" +
            "                        \"createdOn\": null,\n" +
            "                        \"parentPhoneNumber\": \"+91988 883 4530\",\n" +
            "                        \"trustedPersonPhoneNumber\": \"(997) 798 8888\"\n" +
            "                    },\n" +
            "                    \"pickup\": {\n" +
            "                        \"locationId\": null,\n" +
            "                        \"lat\": 30.7410517,\n" +
            "                        \"lon\": 76.779015000000072,\n" +
            "                        \"name\": \"Sector 17\",\n" +
            "                        \"googleJson\": null,\n" +
            "                        \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"drop\": {\n" +
            "                        \"locationId\": null,\n" +
            "                        \"lat\": 30.6961314,\n" +
            "                        \"lon\": 76.71584919999998,\n" +
            "                        \"name\": \"Sector 70\",\n" +
            "                        \"googleJson\": null,\n" +
            "                        \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"contacts\": null,\n" +
            "                    \"events\": [],\n" +
            "                    \"index\": 0\n" +
            "                }\n" +
            "            ],\n" +
            "            \"scheduledOn\": \"2019-09-19T03:30:55.068Z\",\n" +
            "            \"rideId\": \"5bca9e6e-faa5-4edd-9ca2-47a8ea666198\",\n" +
            "            \"driver\": {\n" +
            "                \"assignedCar\": {\n" +
            "                    \"assignedCarId\": null,\n" +
            "                    \"car\": {\n" +
            "                        \"carId\": \"9739850f-74aa-4089-9b19-e4bbd5436ad8\",\n" +
            "                        \"make\": \"2010-09-19\",\n" +
            "                        \"mfgDate\": null,\n" +
            "                        \"number\": \"1234567890\",\n" +
            "                        \"brand\": null,\n" +
            "                        \"model\": \"Swift\",\n" +
            "                        \"color\": \"White\",\n" +
            "                        \"registrationNumber\": null,\n" +
            "                        \"vin\": null,\n" +
            "                        \"insuranceExpiry\": null,\n" +
            "                        \"licenseExpiry\": null,\n" +
            "                        \"insuranceExpiryDate\": null,\n" +
            "                        \"licenseExpiryDate\": null,\n" +
            "                        \"expiryInsuranceDate\": null,\n" +
            "                        \"expiryLicenseDate\": null,\n" +
            "                        \"manufatureDate\": null,\n" +
            "                        \"createdOn\": \"2019-09-19T06:56:30.3559739Z\",\n" +
            "                        \"updatedOn\": null,\n" +
            "                        \"isActive\": false\n" +
            "                    },\n" +
            "                    \"createdOn\": \"0001-01-01T00:00:00Z\",\n" +
            "                    \"updatedOn\": null,\n" +
            "                    \"isActive\": false\n" +
            "                },\n" +
            "                \"drivingLicenseFront\": null,\n" +
            "                \"vehicleInsurance\": null,\n" +
            "                \"vehicleRegistrationDocument\": null,\n" +
            "                \"currentLocation\": null,\n" +
            "                \"vehicleImageUploadDocument\": null,\n" +
            "                \"phoneNumber\": null,\n" +
            "                \"deviceToken\": null,\n" +
            "                \"deviceType\": null,\n" +
            "                \"birthday\": null,\n" +
            "                \"profile\": null,\n" +
            "                \"token\": null,\n" +
            "                \"userIdStr\": null,\n" +
            "                \"child\": null,\n" +
            "                \"trustedPersons\": null,\n" +
            "                \"schoolLocation\": null,\n" +
            "                \"learningCenterLocation\": null,\n" +
            "                \"homeLocation\": null,\n" +
            "                \"userId\": \"f7d50f43-cc3b-4125-8c4e-649e5b0db766\",\n" +
            "                \"title\": null,\n" +
            "                \"firstName\": \"Rahul\",\n" +
            "                \"lastName\": \"Singh\",\n" +
            "                \"gender\": -1,\n" +
            "                \"photo\": {\n" +
            "                    \"photoId\": \"6f663fb9-a438-42d3-976b-61a9568ff3de\",\n" +
            "                    \"smallPhotoUrl\": \"https://etapi.softprodigy.in//UserDocuments/DRIVER_7/7f0f03d3a1724896a7510ba76c383889.jpg\",\n" +
            "                    \"standardPhotoUrl\": \"https://etapi.softprodigy.in//UserDocuments/DRIVER_7/7f0f03d3a1724896a7510ba76c383889.jpg\",\n" +
            "                    \"name\": null,\n" +
            "                    \"createdOn\": \"2019-09-19T06:59:29.8357217Z\"\n" +
            "                },\n" +
            "                \"isFirstLogin\": false,\n" +
            "                \"role\": null,\n" +
            "                \"email\": null\n" +
            "            },\n" +
            "            \"driverId\": \"f7d50f43-cc3b-4125-8c4e-649e5b0db766\",\n" +
            "            \"locationData\": [],\n" +
            "            \"poolData\": {\n" +
            "                \"poolDataId\": \"3afa0213-3dce-4543-b37a-e477680ae675\",\n" +
            "                \"dropPoint\": {\n" +
            "                    \"locationId\": \"232fafaf-82fa-4152-b61e-0950cc335542\",\n" +
            "                    \"lat\": 30.6961314,\n" +
            "                    \"lon\": 76.71584919999998,\n" +
            "                    \"name\": \"Sector 70\",\n" +
            "                    \"googleJson\": null,\n" +
            "                    \"createdOn\": \"2019-09-19T07:08:17.3955579Z\",\n" +
            "                    \"updatedOn\": null,\n" +
            "                    \"isActive\": false\n" +
            "                },\n" +
            "                \"pickupPoint\": null,\n" +
            "                \"createdOn\": \"2019-09-19T07:08:17.3955579Z\",\n" +
            "                \"updatedOn\": null,\n" +
            "                \"isActive\": false\n" +
            "            },\n" +
            "            \"userId\": null,\n" +
            "            \"rideName\": \"Ride 35\",\n" +

            "            \"comments\": null,\n" +
            "            \"scheduledRideOn\": \"0001-01-01T00:00:00Z\",\n" +
            "            \"rideStatus\": 0,\n" +
            "            \"createdOn\": \"2019-09-19T07:08:17.3955488Z\",\n" +
            "            \"updatedOn\": null,\n" +
            "            \"isActive\": false\n" +
            "        }\n" +
            "    ]\n" +
            "}";

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
                getData();
//                mEmpty.setVisibility(View.VISIBLE);
//                mProgress.dismiss();
//                System.out.println(error);
//                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
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

    void getData() {
        currentRideList.clear();
        Type listType = new TypeToken<RideResponse>() {
        }.getType();
        mProgress.dismiss();

        RideResponse rides = new Gson().fromJson(result, listType);

        if (rides != null) {

            Collections.sort(rides.getData(), new Comparator<RideModel>() {
                @Override
                public int compare(RideModel o1, RideModel o2) {
                    if (o1.getCreatedOn().getTime() == o2.getCreatedOn().getTime())
                        return 0;
                    return (o1.getCreatedOn().getTime() > o2.getCreatedOn().getTime() ? -1 : 1);
                }
            });

            switch (1) {
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
}
