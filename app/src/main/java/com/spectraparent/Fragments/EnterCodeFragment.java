package com.spectraparent.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectraparent.Activities.MainHomeActivity;
import com.spectraparent.Activities.RegisterActivity;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.DigitEditText;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressButton;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterCodeFragment extends Fragment {

    ArrayList<CardView> mDigits = new ArrayList<>();
    ArrayList<DigitEditText> mDigitBoxes = new ArrayList<>();
    private View.OnFocusChangeListener onDigitFocus;

    @BindView(R.id.btnConfirm)
    CircularProgressButton mBtnConfirm;

    @BindView(R.id.lblPhoneNumber)
    TextView mPhoneNumber;

    @BindView(R.id.lblResend)
    TextView mLblResend;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static PhoneAuthProvider.ForceResendingToken mResendToken;
    private Timer mTimer;
    private boolean allowToResend = false;

    public EnterCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_code, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mDigits.add((CardView) view.findViewById(R.id.d1));
        mDigitBoxes.add((DigitEditText) mDigits.get(0).findViewWithTag("1"));
        mDigits.add((CardView) view.findViewById(R.id.d2));
        mDigitBoxes.add((DigitEditText) mDigits.get(1).findViewWithTag("1"));
        mDigits.add((CardView) view.findViewById(R.id.d3));
        mDigitBoxes.add((DigitEditText) mDigits.get(2).findViewWithTag("1"));
        mDigits.add((CardView) view.findViewById(R.id.d4));
        mDigitBoxes.add((DigitEditText) mDigits.get(3).findViewWithTag("1"));
        mDigits.add((CardView) view.findViewById(R.id.d5));
        mDigitBoxes.add((DigitEditText) mDigits.get(4).findViewWithTag("1"));
        mDigits.add((CardView) view.findViewById(R.id.d6));
        mDigitBoxes.add((DigitEditText) mDigits.get(5).findViewWithTag("1"));

        onDigitFocus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                fixFocus(view, b);
            }
        };

        mPhoneNumber.setText("Sent to " + LocalStorage.getStudent().getPhoneNumber());

        for (EditText cv : mDigitBoxes) {
            cv.setOnFocusChangeListener(onDigitFocus);
            CardView cardView = (CardView) cv.getParent();
            cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorBackground));
        }

        for (EditText cv : mDigitBoxes) {
            cv.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    Log.d("key", String.valueOf(i));
                    DigitEditText editText = (DigitEditText) view;
                    CardView cardView = (CardView) view.getParent();
                    int index = mDigitBoxes.indexOf(view);

                    if (i == KeyEvent.KEYCODE_DEL) {
                        if (index > 0 && editText.getText().toString().trim().isEmpty())
                            mDigitBoxes.get(index - 1).requestFocus();
                    } else if (keyEvent.getAction() == KeyEvent.ACTION_UP) {

                    } else if (index < 5) {
                        mDigitBoxes.get(index + 1).requestFocus();
                    } else if (index == 5) {
                        Log.d("done", "finish");
                        // KeyboardUtils.hideKeyboard(getActivity(), getView());

//                        String code = mDigitBoxes.get(0).getText().toString() + mDigitBoxes.get(1).getText().toString() +
//                                mDigitBoxes.get(2).getText().toString() + mDigitBoxes.get(3).getText().toString() +
//                                mDigitBoxes.get(4).getText().toString() + mDigitBoxes.get(5).getText().toString();
//
//                        if(code.length() == 6){
//                            validateCode();
//                        }
                    }

                    cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));

                    return false;
                }
            });
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                DialogsHelper.showAlert(getActivity(), "Could not send", "Verification code could not be sent as following error occurred: " + e.getLocalizedMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mResendToken = forceResendingToken;
                LocalStorage.storeString("verificationId", verificationId);
                Toast.makeText(getActivity(), "Verification code sent again..!!", Toast.LENGTH_LONG).show();
            }
        };

        //Tools.setDismissKeyboard(getView(),getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    int sec = 60;

    public void scheduleResendTimer(final Activity activity) {
        allowToResend = false;
        mTimer = new Timer();
        sec = 60;


        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sec > 0) {
                            sec--;
                            mLblResend.setText("Resend in " + String.valueOf(sec));
                        } else {
                            allowToResend = true;
                            mLblResend.setText("Resend");
                            mTimer.cancel();
                        }
                    }
                });
            }
        }, Calendar.getInstance().getTime(), 1000);

    }

    @OnClick(R.id.lblResend)
    void onResend() {
        if (!allowToResend) {
            Toast.makeText(getActivity(), "Please wait sometime to resend verification code", Toast.LENGTH_LONG).show();
            return;
        }

        scheduleResendTimer(getActivity());
        Toast.makeText(getActivity(), "Resending verification code..", Toast.LENGTH_LONG).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                LocalStorage.getStudent().getPhoneNumber(),        // Phone number to verify
                1,
                TimeUnit.MINUTES,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,
                mResendToken);
    }

    private void fixFocus(View view, boolean focused) {
        CardView cardView = (CardView) view.getParent();

        for (CardView c : mDigits) {
            DigitEditText e = c.findViewWithTag("1");
            if (e.getText().toString().trim().isEmpty()) {
                c.setCardElevation(2);
                c.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorBackground));
            } else {
                c.setCardElevation(0);
                c.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
            }
        }

        if (focused) {
            cardView.setCardElevation(4);
            cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
        }
    }


    @OnClick(R.id.btnConfirm)
    void validateCode() {
        String code = mDigitBoxes.get(0).getText().toString() + mDigitBoxes.get(1).getText().toString() +
                mDigitBoxes.get(2).getText().toString() + mDigitBoxes.get(3).getText().toString() +
                mDigitBoxes.get(4).getText().toString() + mDigitBoxes.get(5).getText().toString();

        if (code.length() < 6) {
            DialogsHelper.showAlert(getActivity(), "Invalid Code", "Please enter a valid verification code received in SMS", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(LocalStorage.getString("verificationId"), code);

        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mBtnConfirm.startAnimation();
      //  signInWithAPI();
//
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            signInWithAPI();
                        } else {

                            mBtnConfirm.revertAnimation();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                DialogsHelper.showAlert(getActivity(), "Invalid Code", "Verification code entered is wrong, please try again with correct code", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);

                            } else {
                                DialogsHelper.showAlert(getActivity(), "Verification failed", task.getException().getLocalizedMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                            }
                        }
                    }
                });
    }

    private void signInWithAPI() {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());
        UserModel userModel = LocalStorage.getStudent();
        userModel.setDeviceToken(LocalStorage.getString("firebase_token"));
        userModel.setDeviceType("android");
        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.SignInUrl, userModel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<WebAPIResponseModel<UserModel>>() {
                }.getType();

                mBtnConfirm.revertAnimation();
                System.out.println("Response " + WebApi.SignInUrl + "======>" + new Gson().toJson(response));

                WebAPIResponseModel<UserModel> student = new Gson().fromJson(response, listType);

                if (student != null) {
                    if (student.isSuccess()) {
                        LocalStorage.storeStudent(student.getData());
                        if (student.getData().getTrustedPersons() != null && student.getData().getTrustedPersons().size() > 0)
                            LocalStorage.storeTrustedPerson(student.getData().getTrustedPersons().get(0));

                        if (student.getData().getFirstName() != null && student.getData().getFirstName().length() > 0) {
                            Intent intent = new Intent(getActivity(), MainHomeActivity.class);
                            startActivity(intent);
                            getActivity().finishAffinity();
                        } else {
                            ((RegisterActivity) getActivity()).moveNext();
                        }

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

                mBtnConfirm.revertAnimation();
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
