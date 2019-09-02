package com.spectraparent.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectraparent.Activities.DetailsActivity;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Models.RideModel;
import com.spectraparent.Models.RideRequest;
import com.spectraparent.Models.RideResponse;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class RidesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private MyRidesRecyclerViewAdapter mAdapter;
    private ProgressDialog progressDialog;
    public RidesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMyRides();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rides_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);

        // recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mListener = new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(RideModel item) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("ride", new Gson().toJson(item));
                startActivity(intent);
            }
        };

        mAdapter = new MyRidesRecyclerViewAdapter(new ArrayList<RideModel>(), mListener);

        recyclerView.setAdapter(mAdapter);

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


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(RideModel item);
    }


    private void   getMyRides() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        VolleyUtils v = VolleyUtils.getInstance(getActivity());
     //   RideRequest rideRequest = new RideRequest(1, 10, LocalStorage.getStudent().getUserId());
        RideRequest rideRequest = new RideRequest(1, 10, "6bf08be9-2e85-4775-b792-a8396c969d87");
        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.GetMyRidesUrl, rideRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<RideResponse>() {
                }.getType();

                System.out.println("Response " + WebApi.GetMyRidesUrl + "======>" + new Gson().toJson(response));

                RideResponse ridesModel = new Gson().fromJson(response, listType);
                progressDialog.hide();
                if (ridesModel != null) {
                    mAdapter.updateItems((ArrayList<RideModel>) ridesModel.getData());
                } else {
                    DialogsHelper.showAlert(getActivity(), "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();

                System.out.println(error);
                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
