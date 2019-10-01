package com.spectraparent.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.spectraparent.Activities.AddChild.AboutChildFragment;
import com.spectraparent.Activities.AddChild.AddChildActivity;
import com.spectraparent.Activities.AddChild.ChildNeedsFragment;
import com.spectraparent.Activities.AddTrustedPerson.AddAPersonFragment;
import com.spectraparent.Adapters.ProfileChildAdapter;
import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.EditOrDeleteChildInterface;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Interface.AdapterClickListerner;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.TrustedPersonModel;
import com.spectraparent.Models.UserModel;
import com.spectraparent.Models.WebAPIResponseModel;
import com.spectraparent.WebAPI.ApiRequest;
import com.spectraparent.WebAPI.VolleyUtils;
import com.spectraparent.WebAPI.WebApi;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements AdapterClickListerner, EditOrDeleteChildInterface {

    @BindView(R.id.txtFirstName)
    TextView mFirstName;

    @BindView(R.id.txtLastName)
    TextView mLastName;

    @BindView(R.id.txtEmail)
    TextView mEmail;

    @BindView(R.id.txtPhone)
    TextView mPhone;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    //// TP

    @BindView(R.id.txtTPFullName)
    TextView mTPFullName;

    @BindView(R.id.btnAddChild)
    TextView btnAddChild;

    @BindView(R.id.txtTPDOB)
    TextView mDateOfBirth;

    @BindView(R.id.txtTPPhone)
    TextView mTpPhone;

    @BindView(R.id.txtTPEmail)
    TextView mTpEmail;


    @BindView(R.id.txtTPAddress)
    TextView mTpAddress;

    @BindView(R.id.txtTPRelation)
    TextView mTpRelation;

    @BindView(R.id.btnEditProfile)
    TextView btnEditProfile;

    @BindView(R.id.rcChild)
    RecyclerView rcChild;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setChildAdapter();
        setData();


        return view;
    }

    private void setData() {
        UserModel userModel = LocalStorage.getStudent();
        mFirstName.setText(userModel.getFirstName());
        mLastName.setText(userModel.getLastName());
        mPhone.setText(userModel.getPhoneNumber());
        mEmail.setText(userModel.getEmail());

        TrustedPersonModel trustedPerson = LocalStorage.getTrustedPerson();
        mTPFullName.setText(trustedPerson.getFirstName());
        mTpPhone.setText(trustedPerson.getPhoneNumber());
        mDateOfBirth.setText(trustedPerson.getdOB());
        mTpEmail.setText(trustedPerson.getEmail());
        mTpRelation.setText(trustedPerson.getRelationToChild());
        mTpAddress.setText(trustedPerson.getAddress());
        if (trustedPerson.getImages() != null && trustedPerson.getImages().size() > 0)
            Picasso.get().load(trustedPerson.getImages().get(0).getSmallPhotoUrl()).
                    placeholder(R.drawable.no_profile)
                    .transform(new CircleTransform()).fit().centerCrop().into(ivImage);
    }

    @OnClick(R.id.btnAddChild)
    void addChild() {
        Intent i = new Intent(getActivity(), AddChildActivity.class);
        i.putExtra("from", "add");
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.btnEditProfile)
    void EditProfile() {
        AddAPersonFragment addAPersonFragment = new AddAPersonFragment();
        Bundle args = new Bundle();
        args.putString("from", "edit_profile");
        addAPersonFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, addAPersonFragment)
                .addToBackStack(addAPersonFragment.getClass().getName())
                .commit();
    }

    @OnClick(R.id.btnEditTrustedPerson)
    void editTrustedPerson() {
        AddAPersonFragment addAPersonFragment = new AddAPersonFragment();
        Bundle args = new Bundle();
        args.putString("from", "add_Trusted");
        addAPersonFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, addAPersonFragment)
                .addToBackStack(addAPersonFragment.getClass().getName())
                .commit();
    }

    @Override
    public void onClickItem(int type, Child child) {
        if (type == 1) {
            ChildNeedsFragment childNeedsFragment = new ChildNeedsFragment();
            Bundle args = new Bundle();
            args.putSerializable("child", child);
            args.putString("from", "editChild");

            childNeedsFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, childNeedsFragment)
                    .addToBackStack(childNeedsFragment.getClass().getName())
                    .commit();
        } else if (type == 2) {
            AboutChildFragment aboutChildFragment = new AboutChildFragment();
            Bundle args = new Bundle();
            args.putSerializable("child", child);
            args.putString("from", "editChild");
            aboutChildFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, aboutChildFragment)
                    .addToBackStack(aboutChildFragment.getClass().getName())
                    .commit();
        } else {

        }
    }

    public void setChildAdapter() {
        rcChild.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        rcChild.setAdapter(new ProfileChildAdapter(getActivity(), this, this));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                setData();
                setChildAdapter();
            }
        }
    }

    @Override
    public void editChild(Child childModel) {
        Intent i = new Intent(getActivity(), AddChildActivity.class);
        i.putExtra("from", "add");
        i.putExtra("child", new Gson().toJson(childModel));
        startActivityForResult(i, 1);
    }

    @Override
    public void deleteChild(Child childModel) {
        final KProgressHUD mProgress = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Deleting Child")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        VolleyUtils v = VolleyUtils.getInstance(getActivity());
        mProgress.show();

        ApiRequest req = new ApiRequest(Request.Method.GET, WebApi.RemoveChild + "?childId=" + childModel.getChildId(), null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type type = new TypeToken<WebAPIResponseModel<ArrayList<Child>>>() {
                }.getType();
                System.out.println("Response " + WebApi.RemoveChild + "======>" + new Gson().toJson(response));
                mProgress.dismiss();

                WebAPIResponseModel<ArrayList<Child>> data = new Gson().fromJson(response, type);
                if (data != null && data.isSuccess()) {
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
                } else {
                    DialogsHelper.showAlert(getActivity(), "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();

                DialogsHelper.showAlert(getActivity(), "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
            }
        }, new HashMap<String, String>());

        v.addToRequestQueue(req);
    }

}
