package com.spectradrive.Activities.AddTrustedPerson;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.spectradrive.Helpers.CustomMaskedEditText.MaskedEditText;
import com.spectradrive.Helpers.DialogsHelper;
import com.spectradrive.Helpers.KeyboardUtils;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.Helpers.Tools;
import com.spectradrive.Helpers.colordialog.PromptDialog;
import com.spectradrive.Helpers.loading_button_lib.customViews.CircularProgressImageButton;
import com.spectradrive.Models.TrustedPersonModel;
import com.spectradrive.android.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAPersonFragment extends Fragment {

    @BindView(R.id.txtFirstName)
    EditText mFirstName;

    @BindView(R.id.txtLastName)
    EditText mLastName;

    @BindView(R.id.txtDob)
    EditText mDob;

    @BindView(R.id.txtPhoneNumber)
    MaskedEditText mPhoneNumber;

    @BindView(R.id.txtEmail)
    EditText mEmail;

    @BindView(R.id.btnNext)
    CircularProgressImageButton mBtnNext;

    TrustedPersonModel mPerson = new TrustedPersonModel();

    public AddAPersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_aperson, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.datePicker(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month,dayOfMonth);
                        mDob.setText(df.format(calendar.getTime()));
                    }
                }).show();
            }
        });
    }

    @OnClick(R.id.btnNext)
    void onNext(){

        KeyboardUtils.hideKeyboard(getActivity(),getView());

String firstName = mFirstName.getText().toString();
String lastName = mLastName.getText().toString();
String dob = mDob.getText().toString();
String phoneNumber = mPhoneNumber.getText().toString();
String email = mEmail.getText().toString();

        if (firstName.length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid First Name","Please enter First Name to proceed.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (lastName.length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid Last Name","Please enter Last Name to proceed.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (dob.length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid D.O.B","Please select Date Of Birth to proceed.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (phoneNumber.length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid Last Name","Please enter Phone Number to proceed.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        if (email.length() < 2) {
            DialogsHelper.showAlert(getActivity(),"Invalid Last Name","Please enter Email to proceed.","Ok",null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        mPerson.setFirstName(firstName);
        mPerson.setLastName(lastName);
        mPerson.setdOB(dob);
        mPerson.setPhoneNumber(phoneNumber);
        mPerson.setEmail(email);

        LocalStorage.storeTrustedPerson(mPerson);

        ((AddTrustedPersonActivity)getActivity()).moveNext();
    }
}
