package com.spectraparent.Activities.AddChild;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spectraparent.Helpers.ActionSheet;
import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Helpers.DialogsHelper;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Helpers.cropper.CropImage;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.KeyValueModel;
import com.spectraparent.SpectraDrive;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildInfoFragment extends Fragment implements ActionSheet.ActionSheetListener {

    ArrayList<ImageView> mImages = new ArrayList<>();

    @BindView(R.id.img1)
    ImageView mImage1;
    @BindView(R.id.txtDob)
    EditText mDob;
    @BindView(R.id.img2)
    ImageView mImage2;

    @BindView(R.id.img3)
    ImageView mImage3;
    @BindView(R.id.lblChildIndex)
    TextView lblChildIndex;

    @BindView(R.id.img4)
    ImageView mImage4;


    @BindView(R.id.lPlace)
    LinearLayout mLPlace;

    @BindView(R.id.lImages)
    LinearLayout mLImages;

    @BindView(R.id.txtFirstName)
    EditText mFirstName;

    @BindView(R.id.txtLastName)
    EditText mLastName;

    int selectingIndex = 0;
    private Uri mCropImageUri;

    ArrayList<KeyValueModel<Integer, ImageView>> mSelectedImages = new ArrayList<>();
    Child mChild;

    public ChildInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_info, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = Tools.datePicker(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        view.setMaxDate(System.currentTimeMillis());
                        SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        mDob.setText(df.format(calendar.getTime()));
                    }
                });
                Calendar calendar = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        mImage1.setTag("0");
        mImage2.setTag("1");
        mImage3.setTag("2");
        mImage4.setTag("3");

        mImages.add(mImage1);
        mImages.add(mImage2);
        mImages.add(mImage3);
        mImages.add(mImage4);

        if (((AddChildActivity) getActivity()).from != null &&
                !((AddChildActivity) getActivity()).from.isEmpty()) {
            lblChildIndex.setText("Child Information");
        }
        if (((AddChildActivity) getActivity()).childModel != null) {
            mChild = ((AddChildActivity) getActivity()).childModel;
            mFirstName.setText(mChild.getFirstName());
            mLastName.setText(mChild.getLastName());
            mDob.setText(mChild.getDateOfBirth());
            if (mChild.getImages() != null && mChild.getImages().size() > 0) {
                mLPlace.setVisibility(View.GONE);
                mLImages.setVisibility(View.VISIBLE);
                for (int i = 0; i < mChild.getImages().size(); i++) {
                    Picasso.get().load(mChild.getImages().get(i).getSmallPhotoUrl()).
                            placeholder(R.drawable.no_profile)
                            .transform(new CircleTransform()).fit().centerCrop().into(mImages.get(i))
                    ;
                }
            }
        } else {
            mChild = new Child();
        }


        mFirstName.setText(mChild.getFirstName());
        mLastName.setText(mChild.getLastName());

        for (ImageView img : mImages) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClicked((ImageView) v);
                }
            });
        }
    }

    private void onImageClicked(ImageView v) {
        selectingIndex = Integer.parseInt(v.getTag().toString());

        KeyValueModel<Integer, ImageView> item = null;
        for (KeyValueModel<Integer, ImageView> m : mSelectedImages) {
            if (m.Val == v) {
                item = m;
                break;
            }
        }

        if (item != null) {
            ActionSheet.createBuilder(getActivity(), getChildFragmentManager())
                    .setCancelButtonTitle("Cancel")
                    .setOtherButtonTitles("Replace", "Remove")
                    .setCancelableOnTouchOutside(true)
                    .setListener(ChildInfoFragment.this).show();
        } else {
            CropImage.startPickImageActivity(getActivity(), this);
        }
    }

    @OnClick(R.id.btnAddPhoto)
    void addPhoto() {
        mLPlace.setVisibility(View.GONE);
        mLImages.setVisibility(View.VISIBLE);

        selectingIndex = 0;

        CropImage.startPickImageActivity(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImages.get(selectingIndex).setImageURI(resultUri);
                mSelectedImages.add(new KeyValueModel<Integer, ImageView>(selectingIndex, mImages.get(selectingIndex)));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(getContext(), this);
    }

    @OnClick(R.id.fab)
    void onNext() {

        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString();
        String dob = mDob.getText().toString();
        if (firstName.length() == 0) {
            DialogsHelper.showAlert(getContext(), "Incomplete information", "Please enter first name of your child.", "Ok", null, PromptDialog.DIALOG_TYPE_INFO);
            return;
        }

        if (lastName.length() == 0) {
            DialogsHelper.showAlert(getContext(), "Incomplete information", "Please enter last name of your child.", "Ok", null, PromptDialog.DIALOG_TYPE_INFO);
            return;
        }
        if (dob.length() < 2) {
            DialogsHelper.showAlert(getActivity(), "Invalid D.O.B", "Please select Date Of Birth to proceed.", "Ok", null, PromptDialog.DIALOG_TYPE_WARNING);
            return;
        }

        SpectraDrive.PickedImages.clear();

        for (KeyValueModel<Integer, ImageView> item : mSelectedImages) {
            SpectraDrive.PickedImages.add(Tools.getFileDataFromDrawable(getContext(), item.Val.getDrawable()));
        }

        mChild.setFirstName(firstName);
        mChild.setLastName(lastName);
        mChild.setDateOfBirth(dob);

        LocalStorage.storeChild(mChild);

        ((AddChildActivity) getActivity()).moveNext();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if (index == 0) {
            CropImage.startPickImageActivity(getActivity(), this);
        } else {
            KeyValueModel<Integer, ImageView> item = null;
            for (KeyValueModel<Integer, ImageView> m : mSelectedImages) {
                if (m.Key == selectingIndex) {
                    item = m;
                    break;
                }
            }

            if (item != null) {
                mSelectedImages.remove(item);
            }

            mImages.get(selectingIndex).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.add_photo));
        }
    }
}
