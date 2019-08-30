package com.spectraparent.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spectraparent.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RateUsFragment extends Fragment {

    @BindView(R.id.btnGoAppStore)
    Button mBtnGoAppStore;

    public RateUsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btnGoAppStore)
    void rateUs(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.spectradrive.android")));
    }

}
