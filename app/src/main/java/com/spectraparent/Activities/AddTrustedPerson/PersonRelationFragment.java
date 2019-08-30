package com.spectraparent.Activities.AddTrustedPerson;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectraparent.Activities.AddChild.OtherPersonRelationActivity;
import com.spectraparent.Helpers.ActionSheet;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.KeyboardUtils;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.cropper.CropImage;
import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectraparent.Models.KeyValueModel;
import com.spectraparent.Models.TrustedPersonModel;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonRelationFragment extends Fragment implements ActionSheet.ActionSheetListener {
    ArrayList<ImageView> mImages = new ArrayList<>();
    ArrayList<Button> mBtns = new ArrayList<>();

    @BindView(R.id.img1)
    ImageView mImage1;

    @BindView(R.id.img2)
    ImageView mImage2;

    @BindView(R.id.img3)
    ImageView mImage3;

    @BindView(R.id.img4)
    ImageView mImage4;

    @BindView(R.id.lPlace)
    LinearLayout mLPlace;

    @BindView(R.id.lImages)
    LinearLayout mLImages;


    @BindView(R.id.btn1)
    Button mBtn1;

    @BindView(R.id.btn2)
    Button mBtn2;

    @BindView(R.id.btn3)
    Button mBtn3;

    @BindView(R.id.btn4)
    Button mBtn4;

    @BindView(R.id.btn5)
    Button mBtn5;

    @BindView(R.id.btn6)
    Button mBtn6;

    @BindView(R.id.btn7)
    Button mBtn7;

    @BindView(R.id.btn8)
    Button mBtn8;

    @BindView(R.id.btn9)
    Button mBtn9;

    @BindView(R.id.txtAddress)
    EditText mAddress;

    @BindView(R.id.btnNext)
    CircularProgressImageButton mBtnNext;

    int selectingIndex = 0;
    private Uri mCropImageUri;

    ArrayList<KeyValueModel<Integer, ImageView>> mSelectedImages = new ArrayList<>();
    TrustedPersonModel mPerson;

    public PersonRelationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_relation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mBtns.add(mBtn1);
        mBtns.add(mBtn2);
        mBtns.add(mBtn3);
        mBtns.add(mBtn4);
        mBtns.add(mBtn5);
        mBtns.add(mBtn6);
        mBtns.add(mBtn7);
        mBtns.add(mBtn8);
        mBtns.add(mBtn9);

        mBtn9.setTag("9");

        for(Button btn: mBtns){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClicked((Button) v);
                }
            });
        }

        mImage1.setTag("0");
        mImage2.setTag("1");
        mImage3.setTag("2");
        mImage4.setTag("3");

        mImages.add(mImage1);
        mImages.add(mImage2);
        mImages.add(mImage3);
        mImages.add(mImage4);

        for(ImageView img: mImages){
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClicked((ImageView)v);
                }
            });
        }
    }

    private void onButtonClicked(Button v) {
        if(mPerson == null)
            mPerson = LocalStorage.getTrustedPerson();

        unCheckAll();
        if(v.getTag() == null){
            v.setBackground(getActivity().getResources().getDrawable(R.drawable.relation_button_border_selected));
            v.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            v.setTag("88");
            mPerson.setRelationToChild(v.getText().toString());
        }else {

            if(v.getTag().toString().equals("9"))
            {
                openPopup();
                return;
            }
            v.setBackground(getActivity().getResources().getDrawable(R.drawable.relation_button_border));
            v.setTextColor(getActivity().getResources().getColor(R.color.black));
            v.setTag(null);
            mPerson.setRelationToChild(null);
        }
    }

    void unCheckAll(){
        for(Button btn: mBtns){
            btn.setBackground(getActivity().getResources().getDrawable(R.drawable.relation_button_border));
            btn.setTextColor(getActivity().getResources().getColor(R.color.black));
            if(btn.getTag()!= null && !btn.getTag().toString().equals("9"))
            btn.setTag(null);
        }
    }

    private void openPopup() {
       Intent intent = new Intent(getActivity(), OtherPersonRelationActivity.class);
       startActivityForResult(intent, 3);
    }

    private void onImageClicked(ImageView v) {
        if(mPerson == null)
            mPerson = LocalStorage.getTrustedPerson();

        selectingIndex = Integer.parseInt(v.getTag().toString());

        KeyValueModel<Integer, ImageView> item = null;
        for(KeyValueModel<Integer, ImageView> m: mSelectedImages){
            if(m.Val == v){
                item = m;
                break;
            }
        }

        if(item != null){
            ActionSheet.createBuilder(getActivity(), getChildFragmentManager())
                    .setCancelButtonTitle("Cancel")
                    .setOtherButtonTitles("Replace", "Remove")
                    .setCancelableOnTouchOutside(true)
                    .setListener(PersonRelationFragment.this).show();
        }else {
            CropImage.startPickImageActivity(getActivity(), this);
        }
    }

    @OnClick(R.id.btnAddPhoto)
    void addPhoto(){
        mLPlace.setVisibility(View.GONE);
        mLImages.setVisibility(View.VISIBLE);

        selectingIndex = 0;

        CropImage.startPickImageActivity(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }else
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImages.get(selectingIndex).setImageURI(resultUri);
                mSelectedImages.add(new KeyValueModel<Integer, ImageView>(selectingIndex, mImages.get(selectingIndex)));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if(requestCode == 3 && resultCode == Activity.RESULT_OK){
            mPerson.setRelationToChild(data.getStringExtra("relation"));
            unCheckAll();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(getContext(), this);
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if(index == 0){
            CropImage.startPickImageActivity(getActivity(), this);
        }else {
            KeyValueModel<Integer, ImageView> item = null;
            for(KeyValueModel<Integer, ImageView> m: mSelectedImages){
                if(m.Key == selectingIndex){
                    item = m;
                    break;
                }
            }

            if(item != null){
                mSelectedImages.remove(item);
            }

            mImages.get(selectingIndex).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.add_photo));
        }
    }

    @OnClick(R.id.btnNext)
    void onNext(){
        KeyboardUtils.hideKeyboard(getActivity(),getView());
        mPerson.setAddress(mAddress.getText().toString());

        if(mPerson.getOtherRelationToChild() == null && mPerson.getRelationToChild() == null){
            DialogsHelper.showAlert(getActivity(),"Select Relation", "Please select a relation to child","Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        SpectraDrive.PickedImages.clear();

        for(KeyValueModel<Integer, ImageView> item: mSelectedImages){
            SpectraDrive.PickedImages.add(Tools.getFileDataFromDrawable(getContext(), item.Val.getDrawable()));
        }

        mBtnNext.startAnimation();
        savePerson();
    }


    private void savePerson() {
        String url = WebApi.AddTrustedPersonUrl;
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                Type type = new TypeToken<WebAPIResponseModel<ArrayList<TrustedPersonModel>>>(){}.getType();

                WebAPIResponseModel<ArrayList<TrustedPersonModel>> data = new Gson().fromJson(resultResponse, type);

                if(data == null){
                    DialogsHelper.showAlert(getContext(), "Server Error","Internal server error, please try again later.","Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                if(!data.isSuccess()){
                    DialogsHelper.showAlert(getContext(), "Server Error",data.getMessage(),"Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                    return;
                }

                UserModel user = LocalStorage.getStudent();
                user.setTrustedPersons(data.getData());

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

                DialogsHelper.showAlert(getContext(), "Error While Saving",errorMessage,"Ok", null, PromptDialog.DIALOG_TYPE_WRONG);

                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("FirstName", mPerson.getFirstName());
                params.put("LastName", mPerson.getLastName());
                params.put("Address", mPerson.getAddress());
                params.put("DOB", mPerson.getdOB());
                params.put("Email", mPerson.getEmail());
                params.put("OtherRelationToChild", mPerson.getOtherRelationToChild());
                params.put("PhoneNumber", mPerson.getPhoneNumber());
                params.put("RelationToChild", mPerson.getRelationToChild());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for(int i = 0; i< SpectraDrive.PickedImages.size(); i++){
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
