package com.spectraparent.Activities.HelpFAQ;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.spectraparent.Activities.ContactUsActivity;
import com.spectraparent.Adapters.FAQsAdapter;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Models.FAQModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {


    private KProgressHUD mProgress;

    @BindView(R.id.rcFaqs)
    RecyclerView mRcFaq;
    private FAQsAdapter mAdapter;

    public FAQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRcFaq.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        mAdapter = new FAQsAdapter(getActivity());
        mRcFaq.setAdapter(mAdapter);
        getAllFaqs();
    }

    private void getAllFaqs() {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());

        mProgress =  KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting FAQs")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiRequest req = new ApiRequest(Request.Method.GET, WebApi.GetAllFaqsUrl, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<FAQModel>>() {
                }.getType();
                System.out.println("Response " + WebApi.UpdateLocationUrl + "======>" + new Gson().toJson(response));

                mProgress.dismiss();
                ArrayList<FAQModel> rides = new Gson().fromJson(response, listType);

                if(rides != null){

                    mAdapter.updateItems(rides);

                    mRcFaq.getLayoutParams().height = (int) (Tools.dpToPx(50) * rides.size());
                }else {
                    DialogsHelper.showAlert(getActivity(),"Server error","Internal server error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(),"Network error","Network error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }

    @OnClick(R.id.btnContact)
    void openContactUs()
    {
        startActivity(new Intent(getActivity(), ContactUsActivity.class));
    }

}
