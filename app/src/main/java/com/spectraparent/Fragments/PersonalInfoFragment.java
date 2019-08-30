package com.spectraparent.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectraparent.Activities.RegisterActivity;
import com.spectraparent.Helpers.ActionSheet;
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
public class PersonalInfoFragment extends Fragment implements ActionSheet.ActionSheetListener {

    @BindView(R.id.txtFirstName)
    EditText mFirstName;

    @BindView(R.id.txtLastName)
    EditText mLastName;

    @BindView(R.id.txtNumChild)
    EditText mNumChild;

    @BindView(R.id.btnNext)
    CircularProgressImageButton mBtnNext;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mNumChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheet.createBuilder(getActivity(), getChildFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("1", "2", "3", "Other")
                        .setCancelableOnTouchOutside(true)
                        .setListener(PersonalInfoFragment.this).show();
            }
        });
    }

    @OnClick(R.id.btnNext)
    void onNextClicked(){

        updateProfile();
    }

    private void updateProfile() {

        UserModel user = LocalStorage.getStudent();

        user.setFirstName(mFirstName.getText().toString());
        user.setLastName(mLastName.getText().toString());

        if (user.getFirstName() == null || user.getFirstName().length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid First Name","Please enter your First Name to proceed with registration.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (user.getLastName() == null || user.getLastName().length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid Last Name","Please enter your Last Name to proceed with registration.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
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

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
if(index == 3){
    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
    View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
    alertDialogBuilderUserInput.setView(mView);


    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
    final TextView title =  mView.findViewById(R.id.dialogTitle);
    final TextView subTitle =  mView.findViewById(R.id.dialogSubTitle);
    title.setText("Other number of children");
    subTitle.setText("Enter number of children");
    userInputDialogEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    userInputDialogEditText.setHint("Number");
    alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    String txt = userInputDialogEditText.getText().toString().trim();
                    if(txt.length() == 0){
                        DialogsHelper.showAlert(getActivity(),"Invalid Value","Please enter correct number of childern.", "Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
return;
                    }
                    LocalStorage.storeInt("numChild", Integer.parseInt(txt));
                    mNumChild.setText(txt);
                    dialogBox.cancel();
                }
            })

            .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            dialogBox.cancel();
                        }
                    });

    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
    alertDialogAndroid.show();
}else {
    LocalStorage.storeInt("numChild", index + 1);
    mNumChild.setText(String.valueOf(index + 1));
}
    }
}
