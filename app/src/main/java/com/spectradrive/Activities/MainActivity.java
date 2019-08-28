package com.spectradrive.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.spectradrive.Activities.AddChild.AddChildActivity;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // startActivity(new Intent(this,LoginActivity.class));

        if(LocalStorage.getStudent() !=null && LocalStorage.getStudent().getToken() != null){
            startActivity(new Intent(this,MainHomeActivity.class));
        }else {
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
