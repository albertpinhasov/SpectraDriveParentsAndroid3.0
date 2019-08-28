package com.spectradrive.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.spectradrive.Helpers.KeyboardUtils;
import com.spectradrive.Helpers.Tools;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    public BaseActivity mSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    mSelf = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


}
