package com.spectraparent.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressButton;
import com.spectraparent.Models.ContactQueryModel;
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

public class ContactUsActivity extends BaseActivity {

    private TextView mTitle;

    @BindView(R.id.btnSave)
    CircularProgressButton mBtnSave;

    @BindView(R.id.txtBody)
    EditText mBody;
    private KProgressHUD mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Contact Support");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnSave)
     void signInWithAPI() {
        VolleyUtils v = VolleyUtils.getInstance(this);

        ContactQueryModel model = new ContactQueryModel();
        model.setBody(mBody.getText().toString());

        if(model.getBody().trim().length() < 1){
            DialogsHelper.showAlert(mSelf,null,"Please enter a message","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        mProgress =  KProgressHUD.create(mSelf)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
               // .setDetailsLabel("Getting your rides")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.SaveQueryUrl, model, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<WebAPIResponseModel<ContactQueryModel>>() {
                }.getType();
                System.out.println("Response " + WebApi.UpdateLocationUrl + "======>" + new Gson().toJson(response));

                WebAPIResponseModel<ContactQueryModel> student = new Gson().fromJson(response, listType);
                //mBtnSave.revertAnimation();
                mProgress.dismiss();

                if(student != null){
                    if (student.isSuccess()) {

                        DialogsHelper.showAlert(mSelf,"Success","Contact query sent!","Ok",null, PromptDialog.DIALOG_TYPE_SUCCESS);

                    } else {
                        DialogsHelper.showAlert(mSelf,"Server error", student.getMessage(),"Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                    }
                }else {
                    DialogsHelper.showAlert(mSelf,"Server error","Internal server error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                System.out.println(error);
                DialogsHelper.showAlert(mSelf,"Network error","Network error, please try again later","Ok",null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }
}
