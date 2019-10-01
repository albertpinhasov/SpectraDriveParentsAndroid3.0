package com.spectraparent.Activities.AddTrustedPerson;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.spectraparent.Activities.MainHomeActivity;
import com.spectraparent.Fragments.ProfileFragment;
import com.spectraparent.Helpers.CustomMaskedEditText.MaskedEditText;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.KeyboardUtils;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectraparent.Models.TrustedPersonModel;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.SpectraDrive;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyMultipartRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAPersonFragment extends Fragment {

    @BindView(R.id.txtFirstName)
    EditText mFirstName;

    @BindView(R.id.txtLastName)
    EditText mLastName;

    @BindView(R.id.txtDob)
    EditText mDob;

    @BindView(R.id.tvDob)
    TextView tvDob;

    @BindView(R.id.mPhoneNo)
    MaskedEditText mPhoneNo;

    @BindView(R.id.txtEmail)
    EditText mEmail;

    @BindView(R.id.btnNext)
    CircularProgressImageButton mBtnNext;

    TrustedPersonModel mPerson = new TrustedPersonModel();
    UserModel userModel = new UserModel();

    public AddAPersonFragment() {
        // Required empty public constructor
    }

    String from = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_aperson, container, false);
        if (getArguments() != null)
            from = getArguments().getString("from");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = Tools.datePicker(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        view.setMaxDate(System.currentTimeMillis());
                        SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        mDob.setText(df.format(calendar.getTime()));
                    }
                });
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -18);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        if (from.equals("edit_profile")) {
            mPhoneNo.setEnabled(false);
            mEmail.setEnabled(false);
            tvDob.setVisibility(View.GONE);
            mDob.setVisibility(View.GONE);
            UserModel userModel = LocalStorage.getStudent();
            mFirstName.setText(userModel.getFirstName());
            mLastName.setText(userModel.getLastName());
            String country_code = LocalStorage.getString("country_code").trim();
            if (country_code.length() == 3) {
                mPhoneNo.setMask("+##(###) ### ####");
            } else if (country_code.length() == 2) {
                mPhoneNo.setMask("+#(###) ### ####");
            }
            mPhoneNo.setText(userModel.getPhoneNumber());
            mEmail.setText(userModel.getEmail());
        } else if (from.equals("add_Trusted")) {
            tvDob.setVisibility(View.VISIBLE);
            mDob.setVisibility(View.VISIBLE);
            TrustedPersonModel trustedPerson = LocalStorage.getTrustedPerson();
            mFirstName.setText(trustedPerson.getFirstName());
            mLastName.setText(trustedPerson.getLastName());
            mPhoneNo.setText(trustedPerson.getPhoneNumber());
            mDob.setText(trustedPerson.getdOB());
            mEmail.setText(trustedPerson.getEmail());
        }
    }

    @OnClick(R.id.btnNext)
    void onNext() {

        KeyboardUtils.hideKeyboard(getActivity(), getView());

        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String dob = mDob.getText().toString();
        String phoneNumber = mPhoneNo.getText().toString();
        String email = mEmail.getText().toString();

        if (firstName.length() < 2) {
            DialogsHelper.showAlert(getActivity(), "Invalid First Name", "Please enter First Name to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (lastName.length() < 2) {
            DialogsHelper.showAlert(getActivity(), "Invalid Last Name", "Please enter Last Name to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }
        if (!from.equals("edit_profile")) {
            if (dob.length() < 2) {
                DialogsHelper.showAlert(getActivity(), "Invalid D.O.B", "Please select Date Of Birth to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
                return;
            }
        }
        if (phoneNumber.length() < 8) {
            DialogsHelper.showAlert(getActivity(), "Invalid Phone Number", "Please enter Phone Number to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }
        if (phoneNumber.replace(" ", "").trim().length() > 15) {
            DialogsHelper.showAlert(getActivity(), "Invalid Phone Number", "Please enter Phone Number to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }
        if (email.length() < 2) {
            DialogsHelper.showAlert(getActivity(), "Invalid Email ", "Please enter Email to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (!(email.contains("@") && email.contains("."))) {
            DialogsHelper.showAlert(getActivity(), "Invalid Email", "Please enter a valid email address to proceed with registration.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }


        mPerson.setFirstName(firstName);
        mPerson.setLastName(lastName);
        mPerson.setdOB(dob);
        mPerson.setPhoneNumber(phoneNumber);
        mPerson.setEmail(email);

        userModel.setFirstName(firstName);
        userModel.setLastName(lastName);
        userModel.setPhoneNumber(phoneNumber);
        userModel.setEmail(email);

        if (from.equals("edit_profile")) {

            mBtnNext.startAnimation();

            VolleyUtils v = VolleyUtils.getInstance(getActivity());

            ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.UpdateProfileUrl, userModel, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Type listType = new TypeToken<WebAPIResponseModel<UserModel>>() {
                    }.getType();

                    WebAPIResponseModel<UserModel> student = new Gson().fromJson(response, listType);
                    System.out.println("Response " + WebApi.UpdateProfileUrl + "======>" + response);

                    mBtnNext.revertAnimation();

                    if (student != null) {
                        if (student.isSuccess()) {
                            LocalStorage.storeStudent(student.getData());
                            ((MainHomeActivity) getActivity()).updateHeaderName();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, new ProfileFragment())
                                    .commit();

                        } else {
                            DialogsHelper.showAlert(getActivity(), "Server error", student.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                        }
                    } else {
                        DialogsHelper.showAlert(getActivity(), "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    mBtnNext.revertAnimation();
                    System.out.println(error);
                    DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                }
            }, new HashMap<String, String>());

            v.addToRequestQueue(req);

        } else {
            if (getActivity() instanceof AddTrustedPersonActivity) {
                LocalStorage.storeTrustedPerson(mPerson);
                ((AddTrustedPersonActivity) getActivity()).moveNext();
            } else {
//                mBtnNext.startAnimation();
                LocalStorage.storeTrustedPerson(mPerson);
                PersonRelationFragment aboutChildFragment = new PersonRelationFragment();
                Bundle args = new Bundle();
                args.putString("from", "editChild");
                aboutChildFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, aboutChildFragment)
                        .addToBackStack(aboutChildFragment.getClass().getName())
                        .commit();
                //savePerson();
            }

        }
    }

    private void savePerson() {
        String url = WebApi.AddTrustedPersonUrl;
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                System.out.println("Response " + WebApi.AddTrustedPersonUrl + "======>" + new Gson().toJson(resultResponse));

                Type type = new TypeToken<WebAPIResponseModel<ArrayList<TrustedPersonModel>>>() {
                }.getType();

                WebAPIResponseModel<ArrayList<TrustedPersonModel>> data = new Gson().fromJson(resultResponse, type);

                if (data == null) {
                    DialogsHelper.showAlert(getContext(), "Server Error", "Internal server error, please try again later.", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                if (!data.isSuccess()) {
                    DialogsHelper.showAlert(getContext(), "Server Error", data.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                UserModel user = LocalStorage.getStudent();
                user.setTrustedPersons(data.getData());
                LocalStorage.storeTrustedPerson(data.getData().get(0));

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
                if (mBtnNext.isAnimating()) mBtnNext.revertAnimation();

                DialogsHelper.showAlert(getContext(), "Error While Saving", errorMessage, "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);

                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trustedPersonId", LocalStorage.getTrustedPerson().getTrustedPersonId());
                params.put("FirstName", mPerson.getFirstName());
                params.put("LastName", mPerson.getLastName());
                params.put("Address", mPerson.getAddress());
                params.put("DOB", mPerson.getdOB());
                params.put("Email", mPerson.getEmail());
                params.put("OtherRelationToChild", mPerson.getOtherRelationToChild());
                params.put("PhoneNumber", mPerson.getPhoneNumber());
                params.put("RelationToChild", mPerson.getRelationToChild());
                System.out.println("Request " + WebApi.AddTrustedPersonUrl + "======>" + new Gson().toJson(params.toString()));
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
