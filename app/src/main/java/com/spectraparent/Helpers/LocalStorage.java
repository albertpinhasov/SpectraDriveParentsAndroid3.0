
package com.spectraparent.Helpers;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.spectraparent.Models.ChildModel;
import com.spectraparent.Models.TrustedPersonModel;
import com.spectraparent.Models.UserModel;
import com.spectraparent.SpectraDrive;


/**
 * Created by VikasNokhwal on 03-10-2017.
 */

public class LocalStorage {

    public LocalStorage() {

    }

    public static Object storeObject(String key, Object val) {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, new Gson().toJson(val));

        editor.apply();

        return val;
    }

    public static int storeInt(String key, int val) {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, val);
        editor.apply();

        return val;
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        return sharedPreferences.getInt(key, 0);
    }

    public static String storeString(String key, String val) {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();

        return val;
    }


    public static String getString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        return sharedPreferences.getString(key, null);
    }

    public static SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void storeStudent(UserModel data) {
        storeObject("student", data);
    }

    public static UserModel getStudent() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        String studentStr = sharedPreferences.getString("student", null);

        UserModel model = new Gson().fromJson(studentStr, UserModel.class);

        if (model == null) return new UserModel();

        return model;
    }

    public static void storeChild(ChildModel data) {
        storeObject("child", data);
    }

    public static ChildModel getChild() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        String studentStr = sharedPreferences.getString("child", null);

        ChildModel model = new Gson().fromJson(studentStr, ChildModel.class);

        if (model == null) return new ChildModel();

        return model;
    }


    public static void storeTrustedPerson(TrustedPersonModel data) {
        storeObject("tp", data);
    }

    public static TrustedPersonModel getTrustedPerson() {
        SharedPreferences sharedPreferences = getSharedPreferences();

        String studentStr = sharedPreferences.getString("tp", null);

        TrustedPersonModel model = new Gson().fromJson(studentStr, TrustedPersonModel.class);

        if (model == null) return new TrustedPersonModel();

        return model;
    }

    public static void clearData() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("student", null);
        editor.apply();
    }

    public static boolean isTrustedPersonIntroShown() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("trustedPersonIntroShown", false);
    }

    public static void setTrustedPersonIntroShown(boolean val) {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("trustedPersonIntroShown", val);
        editor.apply();
    }

    public static boolean isTrustedPersonIntroSkipped() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("trustedPersonIntroSkipped", false);
    }


    public static void setTrustedPersonIntroSkipped(boolean val) {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("trustedPersonIntroSkipped", val);
        editor.apply();
    }


    public static boolean isAddChildSkipped() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("addChildSkipped", false);
    }

    public static void setAddChildSkipped(boolean val) {
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("addChildSkipped", val);
        editor.apply();
    }


    public static void clearAll(){
        SharedPreferences sharedPreferences = SpectraDrive.AppContext.getSharedPreferences("default_storage", SpectraDrive.AppContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
