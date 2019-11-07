package com.spectraparent.Activities.AddChild;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectraparent.Fragments.ProfileFragment;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.colordialog.util.DisplayUtil;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.SpectraDrive;
import com.spectraparent.WebAPI.VolleyMultipartRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildNeedsFragment extends Fragment {

    @BindView(R.id.checkBox1)
    CheckBox mCb1;

    @BindView(R.id.checkBox2)
    CheckBox mCb2;

    @BindView(R.id.checkBox3)
    CheckBox mCb3;

    @BindView(R.id.checkBox4)
    CheckBox mCb4;

    @BindView(R.id.checkBox5)
    CheckBox mCb5;

    @BindView(R.id.checkBox6)
    CheckBox mCb6;

    @BindView(R.id.checkBox7)
    CheckBox mCb7;

    @BindView(R.id.checkBox8)
    CheckBox mCb8;
    @BindView(R.id.btnNext)
    CircularProgressImageButton btnNext;

    ArrayList<CheckBox> mCbs = new ArrayList<>();

    Child mChild = null;
    String from = "";

    public ChildNeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_needs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCbs.add(mCb1);
        mCb1.setTag("Visually impaired");

        mCbs.add(mCb2);
        mCb2.setTag("Hearing impaired");

        mCbs.add(mCb3);
        mCb3.setTag("Development delay");

        mCbs.add(mCb4);
        mCb4.setTag("Autism spectrum");

        mCbs.add(mCb5);
        mCb5.setTag("Intellectual Disability");

        mCbs.add(mCb6);
        mCb6.setTag("Emotional Disturbance");

        mCbs.add(mCb7);
        mCb7.setTag("Multiple Disabilities");

        mCb8.setTag("8");
        mCbs.add(mCb8);
        if (getArguments() != null || (getActivity() instanceof AddChildActivity && ((AddChildActivity) getActivity()).childModel != null)) {
            if (getArguments() != null) {
                mChild = (Child) getArguments().getSerializable("child");
                from = getArguments().getString("from");
            } else if ((getActivity() instanceof AddChildActivity && ((AddChildActivity) getActivity()).childModel != null)) {
                mChild = ((AddChildActivity) getActivity()).childModel;
                from = ((AddChildActivity) getActivity()).from;
            }
            if (mChild.getSpecialNeeds() != null && !mChild.getSpecialNeeds().isEmpty()) {
                String checkedItem[] = mChild.getSpecialNeeds().split(",");

                for (int i = 0; i < checkedItem.length; i++) {
                    for (int j = 0; j < mCbs.size(); j++) {
                        if (checkedItem[i].equals(mCbs.get(j).getTag().toString().trim())) {
                            mCbs.get(j).setChecked(true);
                        }
                    }
                }
            }
            if (mChild.getOtherSpecialNeeds() != null && !mChild.getOtherSpecialNeeds().isEmpty()) {
                mCb8.setChecked(true);
            } else {
                mCb8.setChecked(false);
            }
        } else {
            mChild = LocalStorage.getChild();
        }


        for (CheckBox cb : mCbs) {
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (mChild == null)
                        mChild = LocalStorage.getChild();

                    if (buttonView.getTag() != null && buttonView.getTag().toString().equals("8") && buttonView.isChecked()) {
                        mChild.setOtherSpecialNeeds("");
                        askOtherSpecialNeeds();
                        return;
                    } else if (buttonView.getTag().toString().equals("8") && !buttonView.isChecked()) {
                        mChild.setOtherSpecialNeeds("");
                    }

                    String needs = "";

                    for (CheckBox c : mCbs) {
                        if (c.isChecked() && !(c.getTag() != null && c.getTag().toString().equals("8"))) {
                            needs += c.getText() + ",";
                        }
                    }
                    mChild.setSpecialNeeds(needs);
                }
            });
        }
    }

    private void askOtherSpecialNeeds() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);


        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        final TextView title = mView.findViewById(R.id.dialogTitle);
        final TextView subTitle = mView.findViewById(R.id.dialogSubTitle);
        title.setText("Other Special Needs");
        subTitle.setText("Enter other special needs here");

        userInputDialogEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        userInputDialogEditText.setGravity(Gravity.TOP);
        userInputDialogEditText.getLayoutParams().height = DisplayUtil.dp2px(getContext(), 200);

        userInputDialogEditText.setHint("Type in here");
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String txt = userInputDialogEditText.getText().toString().trim();
                        if (txt.length() == 0) {
                            DialogsHelper.showAlert(getActivity(), "Invalid Value", "Please enter other special needs for your children.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
                            return;
                        }
                        mChild.setOtherSpecialNeeds(txt);

                        dialogBox.cancel();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                mCb8.setChecked(false);
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
///             (getActivity() instanceof AddChildActivity && ((AddChildActivity) getActivity()).childModel == null)) {
    @OnClick(R.id.btnNext)
    void onNext() {
        if (getArguments() != null ){
            btnNext.startAnimation();
            LocalStorage.storeChild(mChild);
            editChild();
        }  else {
            mChild.setFirstName(LocalStorage.getChild().getFirstName());
            mChild.setLastName(LocalStorage.getChild().getLastName());
            mChild.setDateOfBirth(LocalStorage.getChild().getDateOfBirth());
            LocalStorage.storeChild(mChild);
            ((AddChildActivity) getActivity()).moveNext();
        }
    }

    private void editChild() {
        String url = WebApi.AddChildUrl;
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                Type type = new TypeToken<WebAPIResponseModel<ArrayList<Child>>>() {
                }.getType();

                WebAPIResponseModel<ArrayList<Child>> data = new Gson().fromJson(resultResponse, type);

                if (data == null) {
                    DialogsHelper.showAlert(getContext(), "Server Error", "Internal server error, please try again later.", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                if (!data.isSuccess()) {
                    DialogsHelper.showAlert(getContext(), "Server Error", data.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }
                System.out.println("Response " + WebApi.AddChildUrl + "======>" + response.toString());

                UserModel user = LocalStorage.getStudent();
                user.setChild(data.getData());

                LocalStorage.storeStudent(user);

                DialogsHelper.showAlert(getContext(), "Success", data.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_SUCCESS, new Runnable() {
                    @Override
                    public void run() {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new ProfileFragment())
                                .commit();
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
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (btnNext.isAnimating()) btnNext.revertAnimation();
                if (btnNext.isAnimating()) btnNext.revertAnimation();

                DialogsHelper.showAlert(getContext(), "Error While Saving", errorMessage, "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);

                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("childId", mChild.getChildId());
                params.put("FirstName", mChild.getFirstName());
                params.put("LastName", mChild.getLastName());
                params.put("DateOfBirth", mChild.getDateOfBirth());
                params.put("About", mChild.getAbout());
                params.put("SpecialNeeds", mChild.getSpecialNeeds());
                params.put("OtherSpecialNeeds", mChild.getOtherSpecialNeeds());
                System.out.println("Request " + WebApi.AddChildUrl + "======>" + new Gson().toJson(params.toString()));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0; i < SpectraDrive.PickedImages.size(); i++) {
                    params.put("Image" + String.valueOf(i + 1), new DataPart("Image" + String.valueOf(i + 1) + ".jpg", SpectraDrive.PickedImages.get(i), "image/jpeg"));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + LocalStorage.getStudent().getToken());
                return headers;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }
}
