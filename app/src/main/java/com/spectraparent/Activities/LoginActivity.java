package com.spectraparent.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.KeyboardUtils;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressButton;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.txtPhoneNumber)
    EditText mPhoneNumber;

    @BindView(R.id.btnLogin)
    CircularProgressButton mBtnLogin;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    UserModel mUser = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                LocalStorage.storeStudent(mUser);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                mBtnLogin.revertAnimation();
                DialogsHelper.showAlert(mSelf,"Could not send","Verification code could not be sent as following error occurred: " + e.getLocalizedMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mBtnLogin.revertAnimation();
                LocalStorage.storeStudent(mUser);
                Intent intent = new Intent(mSelf, RegisterActivity.class);
                intent.putExtra("isFromLogin", true);
                LocalStorage.storeString("verificationId", verificationId);
                startActivity(intent);
            }
        };

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
if(s.toString().length() == 12){
    KeyboardUtils.hideKeyboard(mSelf, null);
}
            }
        });

        Tools.setDismissKeyboard(null, this);
    }


    void sendVerificationCode(){
        mBtnLogin.startAnimation();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mUser.getPhoneNumber(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    @OnClick(R.id.btnLogin)
    void onLogin(){

        mUser.setPhoneNumber("+91" + mPhoneNumber.getText().toString());

        if(mUser.getPhoneNumber().trim().isEmpty() || mUser.getPhoneNumber().length() < 5){
            DialogsHelper.showAlert(this,"Invalid Number","Please enter a valid mobile phone number so that we can send you a verification code in SMS","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
        }else {
            sendVerificationCode();
        }
    }

    @OnClick(R.id.btnRegister)
    void onRegister(){
        Intent intent = new Intent(mSelf, RegisterActivity.class);
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

      FirebaseAuth  mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            signInWithAPI();
                        } else {
                            mBtnLogin.revertAnimation();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                DialogsHelper.showAlert(mSelf,"Invalid Code","Verification code entered is wrong, please try again with correct code","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);

                            }else {
                                DialogsHelper.showAlert(mSelf,"Verification failed",task.getException().getLocalizedMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                            }
                        }
                    }
                });
    }

    private void signInWithAPI() {
        VolleyUtils v = VolleyUtils.getInstance(this);

        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.SignInUrl, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<WebAPIResponseModel<UserModel>>() {
                }.getType();
                System.out.println("Response " + WebApi.SignInUrl + "======>" + new Gson().toJson(response));
                WebAPIResponseModel<UserModel> student = new Gson().fromJson(response, listType);
                mBtnLogin.revertAnimation();

                if(student != null){
                    if (student.isSuccess()) {
                        LocalStorage.storeStudent(student.getData());

                        if(student.getData().getFirstName() != null && student.getData().getFirstName().length() >0){
                            Intent intent = new Intent(mSelf, MainHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(mSelf, RegisterActivity.class);
                            intent.putExtra("isFromLogin", true);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        DialogsHelper.showAlert(mSelf,"Server error",student.getMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                    }
                }else {
                    DialogsHelper.showAlert(mSelf,"Server error","Internal server error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mBtnLogin.revertAnimation();
                System.out.println(error);
                DialogsHelper.showAlert(mSelf,"Network error","Network error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
