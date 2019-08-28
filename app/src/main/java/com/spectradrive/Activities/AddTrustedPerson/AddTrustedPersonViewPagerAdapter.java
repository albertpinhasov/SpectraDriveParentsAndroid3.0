package com.spectradrive.Activities.AddTrustedPerson;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spectradrive.Activities.AddChild.ChildInfoFragment;

import java.util.ArrayList;

class AddTrustedPersonViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> mItems = new ArrayList<>();

    public AddTrustedPersonViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        mItems.add(new AddAPersonFragment());
        mItems.add(new PersonRelationFragment());
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
