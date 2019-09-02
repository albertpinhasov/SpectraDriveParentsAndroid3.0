package com.spectraparent.Activities.AddChild;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.colordialog.util.DisplayUtil;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.ChildModel;
import com.spectraparent.android.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildNeedsFragment extends Fragment {

    @BindView(R.id.checkBox1)
    CheckBox mCb1;

    @BindView(R.id.checkBox2)
    CheckBox mCb2;

    @BindView(R.id.checkBox3)
    CheckBox mCb3;

    @BindView(R.id.checkBox4)
    CheckBox mCb4;

    @BindView(R.id.checkBox5)
    CheckBox mCb5;

    @BindView(R.id.checkBox6)
    CheckBox mCb6;

    @BindView(R.id.checkBox7)
    CheckBox mCb7;

    @BindView(R.id.checkBox8)
    CheckBox mCb8;

    ArrayList<CheckBox> mCbs = new ArrayList<>();

    Child mChild = null;

    public ChildNeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_needs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mCbs.add(mCb1);
        mCbs.add(mCb2);
        mCbs.add(mCb3);
        mCbs.add(mCb4);
        mCbs.add(mCb5);
        mCbs.add(mCb6);
        mCbs.add(mCb7);

        mCb8.setTag("8");
        mCbs.add(mCb8);

        for(CheckBox cb : mCbs){
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(mChild == null)
                        mChild = LocalStorage.getChild();

                    if(buttonView.getTag() != null && buttonView.getTag().toString().equals("8") && buttonView.isChecked()){
                        mChild.setOtherSpecialNeeds("");
                        askOtherSpecialNeeds();
                        return;
                    }

                    String needs = "";

                    for(CheckBox c: mCbs){
                        if(c.isChecked() && !(c.getTag() != null && c.getTag().toString().equals("8"))){
                            needs += c.getText() + ",";
                        }
                    }
                    mChild.setSpecialNeeds(needs);
                }
            });
        }
    }

    private void askOtherSpecialNeeds() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);


        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        final TextView title =  mView.findViewById(R.id.dialogTitle);
        final TextView subTitle =  mView.findViewById(R.id.dialogSubTitle);
        title.setText("Other Special Needs");
        subTitle.setText("Enter other special needs here");

        userInputDialogEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        userInputDialogEditText.setGravity(Gravity.TOP);
        userInputDialogEditText.getLayoutParams().height = DisplayUtil.dp2px(getContext(),200);

        userInputDialogEditText.setHint("Type in here");
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String txt = userInputDialogEditText.getText().toString().trim();
                        if(txt.length() == 0){
                            DialogsHelper.showAlert(getActivity(),"Invalid Value","Please enter other special needs for your children.", "Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
                            return;
                        }
                        mChild.setOtherSpecialNeeds(txt);

                        dialogBox.cancel();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                mCb8.setChecked(false);
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @OnClick(R.id.btnNext)
    void onNext(){
        LocalStorage.storeChild(mChild);
        ((AddChildActivity)getActivity()).moveNext();
    }
}
