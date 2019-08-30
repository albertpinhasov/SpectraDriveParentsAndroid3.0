package com.spectraparent.Activities.HelpFAQ;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spectraparent.Models.FAQModel;
import com.spectraparent.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FAQActivity extends AppCompatActivity {

    @BindView(R.id.txtQuestion)
    TextView mQuestion;

    @BindView(R.id.txtAnswer)
    TextView mAnswer;

private TextView mTitle;
    private FAQModel mFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Help and FAQs");

        mFaq = new Gson().fromJson(getIntent().getStringExtra("json"), FAQModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mAnswer.setText(mFaq.getAnswer());
        mQuestion.setText(mFaq.getQuestion());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
