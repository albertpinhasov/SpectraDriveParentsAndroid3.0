package com.spectraparent.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectraparent.Activities.RegisterActivity;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterEmailFragment extends Fragment {

    @BindView(R.id.txtEmail)
     EditText mEmail;

    @BindView(R.id.btnNext)
    CircularProgressImageButton mBtnNext;



    public EnterEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.btnNext)
    void onNextClicked(){
        updateEmail();
    }

    private void updateEmail() {

        UserModel user = LocalStorage.getStudent();

        user.setEmail(mEmail.getText().toString());

        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            DialogsHelper.showAlert(getActivity(),"Invalid Email","Please enter a valid email address to proceed with registration.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        mBtnNext.startAnimation();

        VolleyUtils v = VolleyUtils.getInstance(getActivity());

        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.UpdateProfileUrl, user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<WebAPIResponseModel<UserModel>>() {
                }.getType();

                WebAPIResponseModel<UserModel> student = new Gson().fromJson(response, listType);

                mBtnNext.revertAnimation();

                if(student != null){
                    if (student.isSuccess()) {
                        LocalStorage.storeStudent(student.getData());

                        ((RegisterActivity)getActivity()).moveNext();

                    }else {
                        DialogsHelper.showAlert(getActivity(),"Server error",student.getMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                    }
                }else {
                    DialogsHelper.showAlert(getActivity(),"Server error","Internal server error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mBtnNext.revertAnimation();
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(),"Network error","Network error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
