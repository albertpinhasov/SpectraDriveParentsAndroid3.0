package com.spectraparent.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spectraparent.Activities.ContactUsActivity;
import com.spectraparent.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    @BindView(R.id.txtPhoneNumber)
    TextView mPhoneNumber;
    @BindView(R.id.txtEmail)
    TextView mEmail;
    //@BindView(R.id.btnFabChat)
    //CircularProgressImageButton mChat;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnContact)
    void onContact(){
        startActivity(new Intent(getActivity(), ContactUsActivity.class));
    }

}
