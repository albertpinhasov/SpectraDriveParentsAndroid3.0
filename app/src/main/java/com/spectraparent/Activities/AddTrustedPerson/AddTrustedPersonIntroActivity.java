package com.spectraparent.Activities.AddTrustedPerson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.android.R;

public class AddTrustedPersonIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trusted_person_intro);
    }

    public void onSKipClicked(View view) {
        LocalStorage.setTrustedPersonIntroSkipped(true);
        finish();
    }

    public void onAddClick(View view) {
        startActivity(new Intent(this,AddTrustedPersonActivity.class));
        finish();
    }
}
