package com.spectradrive.Fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
import com.spectradrive.Activities.MainHomeActivity;
import com.spectradrive.Activities.RegisterActivity;
import com.spectradrive.Helpers.DialogsHelper;
import com.spectradrive.Helpers.KeyboardUtils;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.Helpers.Tools;
import com.spectradrive.Helpers.colordialog.PromptDialog;
import com.spectradrive.Helpers.loading_button_lib.customViews.CircularProgressButton;
import com.spectradrive.Models.UserModel;
import com.spectradrive.Models.WebAPIResponseModel;
import com.spectradrive.WebAPI.ApiRequest;
import com.spectradrive.WebAPI.VolleyUtils;
import com.spectradrive.WebAPI.WebApi;
import com.spectradrive.android.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spectradrive.Helpers.Tools.removeUnderlines;

public class EnterMobileNumberFragment extends Fragment {

    @BindView(R.id.txtPhoneNumber)
    EditText mPhoneNumber;

    @BindView(R.id.btnLogin)
    CircularProgressButton mBtnLogin;

    @BindView(R.id.links)
    TextView mLinks;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    UserModel mUser = new UserModel();

    public EnterMobileNumberFragment() {
        // Required empty public constructor
    }

    public static EnterMobileNumberFragment newInstance() {
        Bundle args = new Bundle();
        EnterMobileNumberFragment fragment = new EnterMobileNumberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_phone_number, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                LocalStorage.storeStudent(mUser);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mBtnLogin.revertAnimation();
                DialogsHelper.showAlert(getActivity(),"Could not send","Verification code could not be sent as following error occurred: " + e.getLocalizedMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mBtnLogin.revertAnimation();
                LocalStorage.storeStudent(mUser);
                LocalStorage.storeString("verificationId", verificationId);
                EnterCodeFragment.mResendToken = forceResendingToken;
                ((RegisterActivity)getActivity()).moveNext();
            }
        };

        String value = "By signing up you are agree to our <b><a href=\"https://www.google.com\">Terms &amp; Conditions</a></b>, <b><a href=\"\">Privacy Policy</a></b> and <b><a href=\"\">Waiver of Class Action Claims</a></b>";

        Spannable spannedText = (Spannable)
                Html.fromHtml(value);


        Spannable processedText = removeUnderlines(spannedText);
            mLinks.setText(processedText);
        mLinks.setMovementMethod(LinkMovementMethod.getInstance());
    }

    void sendVerificationCode(){
        mBtnLogin.startAnimation();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mUser.getPhoneNumber(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    @OnClick(R.id.btnLogin)
    void onLogin(){

        mUser.setPhoneNumber("+1" + mPhoneNumber.getText().toString());

        if(mUser.getPhoneNumber().trim().isEmpty() || mUser.getPhoneNumber().length() < 5){
            DialogsHelper.showAlert(getActivity(),"Invalid Number","Please enter a valid mobile phone number so that we can send you a verification code in SMS","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
        }else {
            sendVerificationCode();
        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mBtnLogin.revertAnimation();
                        if (task.isSuccessful()) {
                            signInWithAPI();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                DialogsHelper.showAlert(getActivity(),"Invalid Code","Verification code entered is wrong, please try again with correct code","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);

                            }else {
                                DialogsHelper.showAlert(getActivity(),"Verification failed",task.getException().getLocalizedMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                            }
                        }
                    }
                });
    }

    private void signInWithAPI() {
        VolleyUtils v = VolleyUtils.getInstance(getActivity());

        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.SignInUrl, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<WebAPIResponseModel<UserModel>>() {
                }.getType();

                WebAPIResponseModel<UserModel> student = new Gson().fromJson(response, listType);

                if(student != null){
                    if (student.isSuccess()) {
                        LocalStorage.storeStudent(student.getData());

                        if(student.getData().getFirstName() != null && student.getData().getFirstName().length() >0){
                            Intent intent = new Intent(getActivity(), MainHomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            ((RegisterActivity)getActivity()).moveNext();
                        }
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
                System.out.println(error);
                DialogsHelper.showAlert(getActivity(),"Network error","Network error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
