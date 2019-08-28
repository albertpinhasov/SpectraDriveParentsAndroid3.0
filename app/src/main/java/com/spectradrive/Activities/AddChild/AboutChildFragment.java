package com.spectradrive.Activities.AddChild;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectradrive.Helpers.DialogsHelper;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.Helpers.colordialog.PromptDialog;
import com.spectradrive.Helpers.loading_button_lib.customViews.CircularProgressButton;
import com.spectradrive.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectradrive.Models.ChildModel;
import com.spectradrive.Models.UserModel;
import com.spectradrive.Models.WebAPIResponseModel;
import com.spectradrive.SpectraDrive;
import com.spectradrive.WebAPI.VolleyMultipartRequest;
import com.spectradrive.WebAPI.VolleyUtils;
import com.spectradrive.WebAPI.WebApi;
import com.spectradrive.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutChildFragment extends Fragment {

    @BindView(R.id.txtAbout)
    EditText mAbout;
    private ChildModel mChild;

    @BindView(R.id.btnNext)
    CircularProgressImageButton mBtnNext;

    @BindView(R.id.btnNextChild)
    CircularProgressButton mBtnNextChild;

    public AboutChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btnNextChild)
    public void onNextChildClicked(View view) {
        if(mChild == null) mChild = LocalStorage.getChild();

        mChild.setAbout(mAbout.getText().toString().trim());

        if(mChild.getAbout() == null || mChild.getAbout().length() == 0){
            mChild.setAbout("");
        }

        mBtnNext.startAnimation();

        saveChild();
    }

    @OnClick(R.id.btnNext)
    public void onNextClicked(View view) {
        if(mChild == null) mChild = LocalStorage.getChild();

        mChild.setAbout(mAbout.getText().toString().trim());

        if(mChild.getAbout() == null || mChild.getAbout().length() == 0){
            mChild.setAbout("");
        }

        mBtnNext.startAnimation();

        saveChild();
    }

    private void saveChild() {
        String url = WebApi.AddChildUrl;
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                Type type = new TypeToken<WebAPIResponseModel<ArrayList<ChildModel>>>(){}.getType();

                WebAPIResponseModel<ArrayList<ChildModel>> data = new Gson().fromJson(resultResponse, type);

                if(data == null){
                    DialogsHelper.showAlert(getContext(), "Server Error","Internal server error, please try again later.","Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                if(!data.isSuccess()){
                    DialogsHelper.showAlert(getContext(), "Server Error",data.getMessage(),"Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                UserModel user = LocalStorage.getStudent();
                user.setChild(data.getData());

                LocalStorage.storeStudent(user);

                DialogsHelper.showAlert(getContext(), "Success", data.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_SUCCESS, new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(mBtnNext.isAnimating())mBtnNext.revertAnimation();
                if(mBtnNextChild.isAnimating())mBtnNextChild.revertAnimation();

                DialogsHelper.showAlert(getContext(), "Error While Saving",errorMessage,"Ok", null, PromptDialog.DIALOG_TYPE_WRONG);

                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("FirstName", mChild.getFirstName());
                params.put("LastName", mChild.getLastName());
                params.put("About", mChild.getAbout());
                params.put("SpecialNeeds", mChild.getSpecialNeeds());
                params.put("OtherSpecialNeeds", mChild.getOtherSpecialNeeds());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for(int i=0; i< SpectraDrive.PickedImages.size();i++){
                    params.put("Image" + String.valueOf(i+1), new DataPart("Image" + String.valueOf(i+1) +".jpg", SpectraDrive.PickedImages.get(i), "image/jpeg"));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers =  new HashMap<>();
                headers.put("Authorization","Bearer " + LocalStorage.getStudent().getToken());
                return  headers;
            }
        };

        VolleyUtils.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }
}
