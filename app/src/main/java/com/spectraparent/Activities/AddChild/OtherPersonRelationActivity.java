package com.spectraparent.Activities.AddChild;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherPersonRelationActivity extends AppCompatActivity {

    @BindView(R.id.txtRelation)
    EditText mTxtRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_person_relation);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Relation to a child");

        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save){

            String relation = mTxtRelation.getText().toString().trim();

            if(relation.isEmpty()){
                DialogsHelper.showAlert(this, "Invalid Value", "Please enter a valid relation in the box","Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
                return false;
            }

            Intent intent = new Intent();
            intent.putExtra("relation",relation);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
