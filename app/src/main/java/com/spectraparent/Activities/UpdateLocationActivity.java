//package com.spectraparent.Activities;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.spectraparent.Helpers.DialogsHelper;
//import com.spectraparent.Helpers.LocalStorage;
//import com.spectraparent.Helpers.colordialog.PromptDialog;
//import com.spectraparent.Helpers.loading_button_lib.customViews.CircularProgressButton;
//import com.spectraparent.Models.LocationModel;
//import com.spectraparent.Models.UserModel;
//import com.spectraparent.Models.WebAPIResponseModel;
//import com.spectraparent.WebAPI.ApiRequest;
//import com.spectraparent.WebAPI.VolleyUtils;
//import com.spectraparent.WebAPI.WebApi;
//import com.spectraparent.android.R;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//import static com.android.volley.VolleyLog.TAG;
//
//public class UpdateLocationActivity extends BaseActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private TextView mTitle;
//
//    LocationModel mLocation;
//    private String mType;
//    private UserModel mUser;
//
//    @BindView(R.id.txtAddress)
//    EditText mAddress;
//    private Marker mMaker;
//
//    @BindView(R.id.btnSave)
//    CircularProgressButton mBtnSave;
//
//    @BindView(R.id.txtInfo)
//    TextView mInfo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_location);
//        ButterKnife.bind(this);
//
//        mUser = LocalStorage.getStudent();
//        mType = getIntent().getStringExtra("type");
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        mTitle = toolbar.findViewById(R.id.toolbar_title);
//        String title = "Home address";
//
//        switch (mType) {
//            case "home": {
//                title = "Home Address";
//                break;
//            }
//            case "school": {
//                title = "School Address";
//                break;
//            }
//            case "learning": {
//                title = "Learning Center";
//                break;
//            }
//        }
////        mLocation = mUser.getHomeLocation();
////        if(mType.equals("school")){
////            title = "School address";
////            mLocation = mUser.getSchoolLocation();
////        }else if(mType.equals("learning")){
////            title = "Learning center address";
////            mLocation = mUser.getLearningCenterLocation();
////        }
//        mTitle.setText(title);
//
//        if (mLocation == null) {
//            mLocation = new LocationModel();
//        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
//    }
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
//        // mMap.setPadding(0, 0, 0, mBottom.getLayoutParams().height);
//
//        //  LocationModel p = new LocationModel(28.612782, 77.424586, mRide.getPickupPoint().getName());
//        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onMarkerDragEnd(Marker arg0) {
//                new getAddress(arg0.getPosition()).execute();
//                mLocation.setLon(arg0.getPosition().longitude);
//                mLocation.setLat(arg0.getPosition().latitude);
//
//                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
//                setTouched();
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
//            }
//
//            @Override
//            public void onMarkerDrag(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.i("System out", "onMarkerDrag...");
//            }
//        });
//
////Don't forget to Set draggable(true) to marker, if this not set marker does not drag.
//
//        if (mLocation != null && mLocation.getLat() != 0) {
//            mMaker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(mLocation.getLat(), mLocation.getLon()))
//                    .title(mLocation.getName())
//                    .draggable(true)
//                    .snippet(mLocation.getName()));
//            mMaker.showInfoWindow();
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMaker.getPosition(), 17.0f));
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 4);
//
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//    private void drawDriver() {
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        finish();
//        return super.onOptionsItemSelected(item);
//    }
//
//    @OnClick(R.id.txtAddress)
//    void openAC() {
//
//        try {
//            Intent intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                            .build(this);
//            startActivityForResult(intent, 1);
//        } catch (GooglePlayServicesRepairableException e) {
//            // TODO: Handle the error.
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                Log.i(TAG, "Place: " + place.getName());
//
//                mAddress.setText(place.getName());
//
//                mLocation.setName(place.getName().toString());
//                mLocation.setLat(place.getLatLng().latitude);
//                mLocation.setLon(place.getLatLng().longitude);
//
//                if (mMaker != null) {
//                    mMaker.remove();
//                }
//                mMaker = mMap.addMarker(new MarkerOptions()
//                        .position(place.getLatLng())
//                        .title(place.getName().toString())
//                        .draggable(true)
//                        .snippet(place.getName().toString()));
//                mMaker.showInfoWindow();
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMaker.getPosition(), 17.0f));
//                setTouched();
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }
//
//
//    class getAddress extends AsyncTask {
//
//        private LatLng mPosition;
//
//        public getAddress(LatLng position) {
//            mPosition = position;
//        }
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            Geocoder geocoder;
//            List<Address> addresses = null;
//            geocoder = new Geocoder(UpdateLocationActivity.this, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(mPosition.latitude, mPosition.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (addresses != null) {
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//
//                return address;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//
//            String address = (String) o;
//
//            if (address != null) {
//                mLocation.setName(address);
//
//                mAddress.setText(address);
//
//                if (mMaker != null) {
//                    mMaker.remove();
//                }
//                mMaker = mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(mLocation.getLat(), mLocation.getLon()))
//                        .title(mLocation.getName())
//                        .draggable(true)
//                        .snippet(mLocation.getName()));
//                mMaker.showInfoWindow();
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMaker.getPosition(), 17.0f));
//            }
//        }
//    }
//
//    @OnClick(R.id.btnSave)
//    void saveLocation() {
//        VolleyUtils v = VolleyUtils.getInstance(this);
//
//        mBtnSave.startAnimation();
//
//        ApiRequest req = new ApiRequest(Request.Method.POST, WebApi.UpdateLocationUrl + "?locationType=" + mType, mLocation, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Type listType = new TypeToken<WebAPIResponseModel<LocationModel>>() {
//                }.getType();
//                System.out.println("Response " + WebApi.UpdateLocationUrl + "======>" + new Gson().toJson(response));
//
//                mBtnSave.revertAnimation();
//                WebAPIResponseModel<LocationModel> student = new Gson().fromJson(response, listType);
//
//                if (student != null) {
//                    if (student.isSuccess()) {
//
////                        if(mType.equals("school")){
////                            mUser.setSchoolLocation(student.getData());
////                        }else if(mType.equals("learning")){
////                            mUser.setLearningCenterLocation(student.getData());
////                        }else {
////                            mUser.setHomeLocation(student.getData());
////                        }
//                        LocalStorage.storeStudent(mUser);
//
//                        DialogsHelper.showAlertWithCloseActivity(mSelf, "Success", "Location updated", "Ok", null, PromptDialog.DIALOG_TYPE_SUCCESS);
//                    } else {
//                        DialogsHelper.showAlert(mSelf, "Server error", student.getMessage(), "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
//                    }
//                } else {
//                    DialogsHelper.showAlert(mSelf, "Server error", "Internal server error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
//                }
//
//            }
//
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mBtnSave.revertAnimation();
//                System.out.println(error);
//                DialogsHelper.showAlert(mSelf, "Network error", "Network error, please try again later", "Ok", null, PromptDialog.DIALOG_TYPE_WRONG);
//            }
//        }, new HashMap<String, String>());
//
//        v.addToRequestQueue(req);
//    }
//
//    void setTouched() {
//        mInfo.setVisibility(View.GONE);
//        mBtnSave.setVisibility(View.VISIBLE);
//    }
//}
