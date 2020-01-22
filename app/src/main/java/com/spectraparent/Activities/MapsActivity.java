package com.spectraparent.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.spectraparent.Helpers.DirectionHelper;
import com.spectraparent.Helpers.*;
import com.spectraparent.Models.RideModel;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView mTitle;
    private RideModel mRide;
    DirectionHelper directionHelper;
    LinearLayout mBottom;
    private Marker mDriver;
    @BindView(R.id.tvdriverName)
    TextView tvdriverName;
    @BindView(R.id.statusOfDriverWithTime)
    TextView statusOfDriverWithTime;
    @BindView(R.id.tvDrivingCar)
    TextView tvDrivingCar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvCarNo)
    TextView tvCarNo;
    @BindView(R.id.tvPickupTime)
    TextView tvPickupTime;
    @BindView(R.id.tvTimeRequired)
    TextView tvTimeRequired;
    @BindView(R.id.ivDriveImage)
    ImageView ivDriveImage;
    @BindView(R.id.llDroppedOff)
    LinearLayout llDroppedOff;
    @BindView(R.id.llOnTheWay)
    LinearLayout llOnTheWay;
    @BindView(R.id.llPickedUp)
    LinearLayout llPickedUp;
    FirebaseDatabase database;
    DatabaseReference myRef;
    LinkedHashMap locationHashMap = new LinkedHashMap<String, String>();
    LatLng oldCordinates;
    boolean isTrackingStarted = false;
    LiveTrackingUtils liveTrackingUtils;
    int count = 0;
    private KProgressHUD mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        database = FirebaseDatabase.getInstance();
        liveTrackingUtils = new LiveTrackingUtils(MapsActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        mProgress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting routes")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f).show();
        //View toolbarLayout = findViewById(R.id.toolbarLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRide = new Gson().fromJson(getIntent().getStringExtra("json"), RideModel.class);
        myRef = database.getReference(mRide.getDriverId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String locationString = dataSnapshot.getValue(String.class);
                if (locationString != null && !locationString.isEmpty()) {
                    String driverLatLng[] = locationString.split(",");
                    Log.e("currentLocationLatLng", locationString);
                    if (!isTrackingStarted) {
                        locationHashMap.put("drop_lat", mRide.getChildModel().get(0).getDrop().getLat());
                        locationHashMap.put("drop_long", mRide.getChildModel().get(0).getDrop().getLon());
                        locationHashMap.put("driver_lat", driverLatLng[0]);
                        locationHashMap.put("driver_lon", driverLatLng[1]);
                        liveTrackingUtils.mapPathDraw(mMap, locationHashMap, MapsActivity.this,      mProgress, isTrackingStarted, directionHelper);
                        isTrackingStarted = true;
                    } else {
                        oldCordinates = new LatLng(Double.parseDouble(locationHashMap.get("driver_lat").toString()),
                                Double.parseDouble(locationHashMap.get("driver_lon").toString()));
                        locationHashMap.put("driver_lat", driverLatLng[0]);
                        locationHashMap.put("driver_lon", driverLatLng[1]);
                        if (checkDistance(oldCordinates)) {
                            if (count == 3) {
                                count = 0;
                                liveTrackingUtils.mapPathDrawWithoutmarker(locationHashMap, MapsActivity.this, mProgress);
                                //   liveTrackingUtils.updateUI(locationHashMap, MapsActivity.this);
                            } else {
                                count++;
                                liveTrackingUtils.updateUI(locationHashMap, MapsActivity.this);
                            }
                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        setData();
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Routes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = getResources().getDrawable(R.drawable.back );
        backArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle("");
    }

    private void setData() {
        markOntheWay(mRide.getRideStatus());
        tvdriverName.setText(mRide.getDriver().getFirstName());
      //  tvPickupTime.setText(mRide.getChild().get(0).getPickup().getCreatedOn());
        Picasso.get().load(mRide.getDriver().getPhoto().getSmallPhotoUrl()).
                placeholder(R.drawable.no_profile)
                .fit().centerCrop().into(ivDriveImage);
        String carColor = mRide.getDriver().getAssignedCar().getCar().getColor() != null ? mRide.getDriver().getAssignedCar().getCar().getColor() : "" + " ";
        String carBrand = mRide.getDriver().getAssignedCar().getCar().getBrand() != null ? mRide.getDriver().getAssignedCar().getCar().getBrand() : "" + " ";
        String carModel = mRide.getDriver().getAssignedCar().getCar().getModel() != null ? mRide.getDriver().getAssignedCar().getCar().getModel() : "" + " ";
        tvDrivingCar.setText("is driving " + carColor + carBrand + carModel);
        tvCarNo.setText(mRide.getDriver().getAssignedCar().getCar().getNumber() != null ? mRide.getDriver().getAssignedCar().getCar().getNumber() : "");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        directionHelper = new DirectionHelper(mMap, this);

        //  setupSinglePax();

        //   drawDriver();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 4);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.details) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("ride", new Gson().toJson(mRide));
            startActivity(intent);
        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_track_ride, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void drawDriver() {
        if (mRide.getDriver().getCurrentLocation() == null) return;
        mDriver = mMap.addMarker(new MarkerOptions().position(new LatLng(mRide.getDriver().getCurrentLocation().getLat(), mRide.getDriver().getCurrentLocation().getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.car2)));
        //final Handler handler = new Handler();
        // animateMarkerNew();
    }

    private void setupSinglePax() {
        directionHelper.clear();

        //pickup to driver - driver to drop
        if (mRide.getDriver().getCurrentLocation() != null) {
            directionHelper.drawPath(mRide.getChildModel().get(0).getPickup().getLat() + "," + mRide.getChildModel().get(0).getPickup().getLon(), mRide.getDriver().getCurrentLocation().getLat() + "," + mRide.getDriver().getCurrentLocation().getLon(), getResources().getColor(R.color.colorPrimary), 0, 0);
            directionHelper.drawPath(mRide.getDriver().getCurrentLocation().getLat() + "," + mRide.getDriver().getCurrentLocation().getLon(), mRide.getChildModel().get(0).getDrop().getLat() + "," + mRide.getChildModel().get(0).getDrop().getLon(), Color.parseColor("#000000"), 0, 0);
        } else {
            directionHelper.drawPath(mRide.getChildModel().get(0).getPickup().getLat() + "," + mRide.getChildModel().get(0).getPickup().getLon(), mRide.getChildModel().get(0).getDrop().getLat() + "," + mRide.getChildModel().get(0).getDrop().getLon(), getResources().getColor(R.color.colorPrimary), 0, 0);
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(mRide.getChildModel().get(0).getPickup().getLat(), mRide.getChildModel().get(0).getPickup().getLon()))
                .anchor(0.5F, 0.5F)

                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_child))
                .title(mRide.getChildModel().get(0).getPickup().getName()));


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int o_vectorResId) {
        int h = 50;
        int w = 50;
        Drawable vectorDrawable = ContextCompat.getDrawable(context, o_vectorResId);
        if (vectorDrawable == null) return null;
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    void markOntheWay(int rideStatus) {
        if (rideStatus == 0 || rideStatus == 1 || rideStatus == 2 ) {
            llDroppedOff.setAlpha((float) 0.6);
            llOnTheWay.setAlpha((float) 0.6);
            llPickedUp.setAlpha((float) 0.6);
        } else if (rideStatus == 3) {
            llDroppedOff.setAlpha((float) 0.6);
            llOnTheWay.setAlpha((float) 1.0);
            llPickedUp.setAlpha((float) 1.0);
        } else if (rideStatus == 4) {
            llDroppedOff.setAlpha((float) 1.0);
            llOnTheWay.setAlpha((float) 1.0);
            llPickedUp.setAlpha((float) 1.0);
        }

    }

    public void startStopProgressBar(int start) {
        if (start == 1) {
            mProgress.show();
        } else if (start == 0) {
            mProgress.dismiss();
        } else if (start == 2) {
            Toast.makeText(this, "No Path Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setDistanceAndTime(String distan, String duration) {
        tvTimeRequired.setText("Trip Time " + duration);
        if(mRide.getRideStatus() == 3 && duration!=null && !duration.isEmpty()) {
            statusOfDriverWithTime.setText("Arriving in " + duration + "\nDriver is on the way");
            statusOfDriverWithTime.setVisibility(View.VISIBLE);
        }
        else
            statusOfDriverWithTime.setVisibility(View.GONE);

    }
    boolean checkDistance(LatLng source) {
        android.location.Location locationA = new android.location.Location("destination");
        locationA.setLatitude(source.latitude);
        locationA.setLongitude(source.longitude);
        android.location.Location locationB = new android.location.Location("Source");
        locationB.setLatitude(Double.parseDouble(locationHashMap.get("driver_lat").toString()));
        locationB.setLongitude(Double.parseDouble(locationHashMap.get("driver_lon").toString()));
        int distance = (int) locationA.distanceTo(locationB);
        if (distance <= 20) {
            return false;
        }
        return true;
    }
}
