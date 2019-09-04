package com.spectraparent.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.spectraparent.android.R;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendsActivity extends AppCompatActivity {
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @OnClick(R.id.lFb)
    void onFb() {
//        ShareLinkContent content = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://developers.facebook.com"))
//                .setQuote("Hi, check out Spectra Drive app")
//                .build();
//
//        ShareDialog shareDialog = new ShareDialog(this);
//        shareDialog.show(content, ShareDialog.Mode.WEB);
    }

    @OnClick(R.id.lTw)
    void onTw() {
        shareTwitter("Hi, check out Spectra Drive app");
    }

    @OnClick(R.id.lInsta)
    void onInsta() {
        Intent i = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (i != null) {
            i=new Intent();
                i.setAction(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Invitation to download Spectra Drive");
            i.putExtra(Intent.EXTRA_TEXT, "Hi, check out Spectra Drive app");
            i .setPackage("com.instagram.android");
            startActivity(i);
        } else {
            i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            startActivity(i);
        }
    }

    @OnClick(R.id.lEmail)
    void onEmail() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 4);

            return;
        }
        new MultiContactPicker.Builder(InviteFriendsActivity.this) //Activity/fragment context
                //.theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(InviteFriendsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(InviteFriendsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                //.setSelectedContacts("10", "5" / myList) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.EMAIL) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (requestCode == 4) {
            onEmail();
        } else {
            onContact();
        }
    }

    @OnClick(R.id.lContact)
    void onContact() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS}, 5);

            return;
        }
        new MultiContactPicker.Builder(InviteFriendsActivity.this) //Activity/fragment context
                //.theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(InviteFriendsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(InviteFriendsActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                //.setSelectedContacts("10", "5" / myList) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.PHONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(22);
    }

    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to download Spectra Drive");
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "Hi, check out Spectra Drive app");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("ddd", "UTF-8 should always be supported", e);
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                Log.d("MyTag", results.get(0).getDisplayName());

                if (results.size() > 0) {
                    SmsManager smsManager = SmsManager.getDefault();
                    for (ContactResult contact : results) {
                        if (contact.getPhoneNumbers() != null && contact.getPhoneNumbers().size() > 0) {
                            smsManager.sendTextMessage(contact.getPhoneNumbers().get(0).getNumber(), null, "Hi, check out Spectra Drive app", null, null);
                        }
                    }

                    Toast.makeText(this, "Invitation sent!", Toast.LENGTH_LONG);
                }
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                Log.d("MyTag", results.get(0).getDisplayName());

                if (results.size() > 0) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");

                    String[] to = new String[results.size()];

                    for (int j = 0; j < results.size(); j++) {
                        to[j] = results.get(j).getEmails().get(0);
                    }
                    i.putExtra(Intent.EXTRA_EMAIL, to);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Invitation to download Spectra Drive");
                    i.putExtra(Intent.EXTRA_TEXT, "Hi, check out Spectra Drive app");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail invitation..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                }
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
