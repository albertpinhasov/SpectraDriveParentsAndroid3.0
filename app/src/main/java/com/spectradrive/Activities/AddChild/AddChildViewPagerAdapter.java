package com.spectradrive.Activities.AddChild;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spectradrive.Fragments.EnterCodeFragment;
import com.spectradrive.Fragments.EnterEmailFragment;
import com.spectradrive.Fragments.EnterMobileNumberFragment;
import com.spectradrive.Fragments.PersonalInfoFragment;

import java.util.ArrayList;

class AddChildViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> mItems = new ArrayList<>();

    public AddChildViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        mItems.add(new ChildInfoFragment());
        mItems.add(new ChildNeedsFragment());
        mItems.add(new AboutChildFragment());
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
