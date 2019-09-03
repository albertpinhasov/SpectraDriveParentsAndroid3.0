package com.spectraparent.Fragments;


import android.annotation.SuppressLint;
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

import com.spectraparent.Activities.AddChild.AboutChildFragment;
import com.spectraparent.Activities.AddChild.ChildNeedsFragment;
import com.spectraparent.Activities.AddTrustedPerson.AddAPersonFragment;
import com.spectraparent.Adapters.ProfileChildAdapter;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Interface.AdapterClickListerner;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.TrustedPersonModel;
import com.spectraparent.Models.UserModel;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements AdapterClickListerner {

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
        rcChild.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        rcChild.setAdapter(new ProfileChildAdapter(getActivity(), this));
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
            Picasso.get().load(trustedPerson.getImages().get(0).getSmallPhotoUrl()).into(ivImage);
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
            args.putString("from", "edit");

            childNeedsFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, childNeedsFragment)
                    .addToBackStack(childNeedsFragment.getClass().getName())
                    .commit();
        } else if (type == 2) {
            AboutChildFragment aboutChildFragment = new AboutChildFragment();
            Bundle args = new Bundle();
            args.putSerializable("child", child);
            args.putString("from", "edit");
            aboutChildFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, aboutChildFragment)
                    .addToBackStack(aboutChildFragment.getClass().getName())
                    .commit();
        } else {

        }
    }

}
