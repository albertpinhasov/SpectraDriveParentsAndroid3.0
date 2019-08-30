package com.spectraparent.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spectraparent.Adapters.ProfileChildAdapter;
import com.spectraparent.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.txtFirstName)
    TextView mFirstName;

    @BindView(R.id.txtLastName)
    TextView mLastName;

    @BindView(R.id.txtEmail)
    TextView mEmail;

    @BindView(R.id.txtPhone)
    TextView mPhone;

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
        rcChild.setAdapter(new ProfileChildAdapter(getActivity()));
        return view;
    }

    @OnClick(R.id.btnEditProfile)
    void EditProfile() {
    }
}
