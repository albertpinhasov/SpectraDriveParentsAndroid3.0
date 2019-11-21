package com.spectraparent.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.spectraparent.Activities.AddChild.AddChildActivity;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Models.Child;
import com.spectraparent.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.txtHome)
    LinearLayout mHome;
    @BindView(R.id.txtSchool)
    LinearLayout mSchool;
    @BindView(R.id.txtLearningCenter)
    LinearLayout mCenter;
    @BindView(R.id.txtPushNotification)
    LinearLayout mNotification;



    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.txtHome, R.id.txtSchool, R.id.txtLearningCenter})
    void onLocChangeClicked(View view){
//        Intent intent = new Intent(getActivity(), UpdateLocationActivity.class);
//        intent.putExtra("type",view.getTag().equals("1") ? "home" : (view.getTag().equals("2") ? "school" : "learning"));
//        startActivity(intent);
    }

    @OnClick(R.id.txtAddChild)
    void onAddChildClicked(View view){
        Intent intent = new Intent(getActivity(), AddChildActivity.class);
        LocalStorage.storeChild(new Child());
        startActivity(intent);
    }
}
