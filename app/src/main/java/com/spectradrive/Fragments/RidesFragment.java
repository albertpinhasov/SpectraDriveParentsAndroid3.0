package com.spectradrive.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectradrive.Activities.DetailsActivity;
import com.spectradrive.Activities.MainHomeActivity;
import com.spectradrive.Activities.RegisterActivity;
import com.spectradrive.Helpers.DialogsHelper;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.Helpers.colordialog.PromptDialog;
import com.spectradrive.Models.RideModel;
import com.spectradrive.Models.UserModel;
import com.spectradrive.Models.WebAPIResponseModel;
import com.spectradrive.WebAPI.ApiRequest;
import com.spectradrive.WebAPI.VolleyUtils;
import com.spectradrive.WebAPI.WebApi;
import com.spectradrive.android.R;
import com.spectradrive.Fragments.dummy.DummyContent;
import com.spectradrive.Fragments.dummy.DummyContent.DummyItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RidesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private MyRidesRecyclerViewAdapter mAdapter;

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


    private void getMyRides() {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());

        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.GetMyRidesUrl, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<RideModel>>() {
                }.getType();

                ArrayList<RideModel> rides = new Gson().fromJson(response, listType);

                if(rides != null){
                   mAdapter.updateItems(rides);
                }else {
                    DialogsHelper.showAlert(getActivity(),"Server error","Internal server error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(),"Network error","Network error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
