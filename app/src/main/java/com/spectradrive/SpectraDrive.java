package com.spectradrive;

import android.content.Context;

import java.util.ArrayList;

public class SpectraDrive extends android.support.multidex.MultiDexApplication {
    public static ArrayList<byte[]> PickedImages = new ArrayList<byte[]>();
    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = this;
    }
}
