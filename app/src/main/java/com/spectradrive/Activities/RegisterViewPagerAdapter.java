package com.spectradrive.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spectradrive.Activities.AddTrustedPerson.PersonRelationFragment;
import com.spectradrive.Fragments.EnterCodeFragment;
import com.spectradrive.Fragments.EnterEmailFragment;
import com.spectradrive.Fragments.EnterMobileNumberFragment;
import com.spectradrive.Fragments.PersonalInfoFragment;

import java.util.ArrayList;

class RegisterViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> mItems = new ArrayList<>();

    public RegisterViewPagerAdapter(FragmentManager supportFragmentManager, boolean isFromLogin) {
        super(supportFragmentManager);
        if(!isFromLogin)
        mItems.add(EnterMobileNumberFragment.newInstance());
        mItems.add(new EnterCodeFragment());
        mItems.add(new EnterEmailFragment());
        mItems.add(new PersonalInfoFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}
