package com.spectraparent.Activities.HelpFAQ;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectraparent.android.R;

import butterknife.BindView;

public class FAQMessageActivity extends AppCompatActivity {
    private TextView mTitle;

    @BindView(R.id.txtMessage)
    EditText mMessage;

    @BindView(R.id.btnSend)
    CircularProgressImageButton mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqmessage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Write Us a Message");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

}
