package shy7lo.com.shy7lo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 28-11-2017.
 */

public class AddressMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, View.OnClickListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener {

    GoogleMap mGoogleMap;

    //    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    //    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    //    @BindView(R.id.ibSearch)
    ImageButton ibSearch;
    //    @BindView(R.id.ibCurrentLocation)
    ImageButton ibCurrentLocation;
    //    @BindView(R.id.tvSetAddress)
    TextView tvSetAddress;
    //    @BindView(R.id.tvTitle)
    TextView tvTitle;
    //    @BindView(R.id.etAddress)
    EditText etAddress;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x12;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;

    Handler mHandler = new Handler();
    Runnable mRunnable;

    public static String BNDL_MAP_ADDRESS = "BNDL_MAP_ADDRESS";
    public static String BNDL_MAP_CITY = "BNDL_MAP_CITY";

    String mResultAddress = "", mResultCity = "";
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_map);
//        ButterKnife.bind(getActivity());
        InitializeControls();
        InitializeControlsAction();

    }

    private void InitializeControls() {

        rltTitle = (RelativeLayout) findViewById(R.id.rltTitle);
        ibCancel = (ImageButton) findViewById(R.id.ibCancel);
        ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        ibCurrentLocation = (ImageButton) findViewById(R.id.ibCurrentLocation);
        tvSetAddress = (TextView) findViewById(R.id.tvSetAddress);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etAddress = (EditText) findViewById(R.id.etAddress);

        mRequestingLocationUpdates = false;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frmtMap);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mGoogleMap != null) {
                    LatLng mLatLng = mGoogleMap.getCameraPosition().target;
                    if (mLatLng != null) {
//                        getAddressFromLatLng(mLatLng);
                        new GetAddressFromLatLngAsync(mLatLng).execute();
                    }
                }
            }
        };

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            ibSearch.setScaleX(-1f);
            etAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pin_small, 0);
            etAddress.setGravity(Gravity.RIGHT);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ibCurrentLocation.getLayoutParams();
            params.gravity = Gravity.LEFT;
            ibCurrentLocation.setLayoutParams(params);
        } else {
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            ibSearch.setScaleX(1f);

            etAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pin_small, 0, 0, 0);
            etAddress.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ibCurrentLocation.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            ibCurrentLocation.setLayoutParams(params);
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null) {
                    ++count;
                    if (count == 1) {
                        LatLng mLatlng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatlng, 18));
                        new GetAddressFromLatLngAsync(mLatlng).execute();
                    }
                }
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void InitializeControlsAction() {
        ibCancel.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
        tvSetAddress.setOnClickListener(this);
        ibCurrentLocation.setOnClickListener(this);
    }

    private Activity getActivity() {
        return AddressMapActivity.this;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            LogUtils.i("", "Displaying permission rationale to provide additional context.");
            Utils.showSnackbar(getActivity(), getResources().getString(R.string.permission_rationale), getResources().getString(R.string.pf_ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                }
            });
        } else {
            LogUtils.i("", "Requesting permission");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getLocationPermission();
//        } else {
//            getDeviceLocation();
//        }

        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);
//        mGoogleMap.setOnCameraChangeListener(this);

    }

//    private void getLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//            getDeviceLocation();
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }

//    private void getDeviceLocation() {
//
//        try {
//            if (mLocationPermissionGranted) {
//
//                if (!Utils.isLocationEnabled(getActivity())) {
//                    Utils.showAlertOkCancelDialog(getActivity(), getString(R.string.enable_location), getString(R.string.settings), new Utils.OnAlertOkayDialogClick() {
//                        @Override
//                        public void onOkayClicked(Dialog dialog) {
//                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        }
//                    });
//                    return;
//                }
//
//                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                    locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Location> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                LatLng mLatLng = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
//                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 18));
//                                new GetAddressFromLatLngAsync(mLatLng).execute();
//                            } else {
//                                Log.d("", "Current location is null. Using defaults.");
//                                Log.e("", "Exception: %s", task.getException());
//                                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                            }
//                        }
//                    });
//                }
//            }
//        } catch (SecurityException e) {
//            Log.e("Exception: %s", e.getMessage());
//        }

//        final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        final Location currentGeoLocation = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (currentGeoLocation != null) {
//            double currentLat = currentGeoLocation.getLatitude();
//            double currentLon = currentGeoLocation.getLongitude();
//            LogUtils.e("", "onMapReady currentLat::" + currentLat + " currentLon::" + currentLon);
//            LatLng currentLatLng = new LatLng(currentLat, currentLon);
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
//            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//            getAddressFromLatLng(currentLatLng);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
//                    getDeviceLocation();
                }
            }
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (mRequestingLocationUpdates) {
                    LogUtils.i("", "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onCameraIdle() {
//        Toast.makeText(this, "The camera has stopped moving.",
//                Toast.LENGTH_SHORT).show();
        LogUtils.e("", "onCameraIdle call");
        if (mGoogleMap != null) {
            LatLng mLatLng = mGoogleMap.getCameraPosition().target;
            if (mLatLng != null) {
                new GetAddressFromLatLngAsync(mLatLng).execute();
            }
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        LogUtils.e("", "onCameraMoveStarted");
//        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//            Toast.makeText(this, "The user gestured on the map.",
//                    Toast.LENGTH_SHORT).show();
//        } else if (reason == GoogleMap.OnCameraMoveStartedListener
//                .REASON_API_ANIMATION) {
//            Toast.makeText(this, "The user tapped something on the map.",
//                    Toast.LENGTH_SHORT).show();
//        } else if (reason == GoogleMap.OnCameraMoveStartedListener
//                .REASON_DEVELOPER_ANIMATION) {
//            Toast.makeText(this, "The app moved the camera.",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onCameraMove() {
        LogUtils.e("", "onCameraMove");
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 50);
//        if (mGoogleMap != null) {
//            LatLng mLatLng = mGoogleMap.getCameraPosition().target;
//            if (mLatLng != null) {
//                getAddressFromLatLng(mLatLng);
//            }
//        }
    }

    @Override
    public void onCameraMoveCanceled() {
        LogUtils.e("", "onCameraMoveCanceled");
    }

    private class GetAddressFromLatLngAsync extends AsyncTask<Object, Object, ArrayList<String>> {


        LatLng location;

        public GetAddressFromLatLngAsync(LatLng location) {
            this.location = location;
        }

        @Override
        protected void onPostExecute(ArrayList<String> addressList) {
            super.onPostExecute(addressList);
            if (addressList != null && addressList.size() > 0) {
                etAddress.setText("" + TextUtils.join(System.getProperty("line.separator"), addressList));
            }
        }

        @Override
        protected ArrayList<String> doInBackground(Object... voids) {
            ArrayList<String> addressList = new ArrayList<>();
            if (!Geocoder.isPresent()) {
                Utils.showToast(getActivity(), getString(R.string.no_geocoder_available));
                return addressList;
            }

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            try {
                LogUtils.e("", "latitude::" + location.latitude + " longitude::" + location.longitude);
                addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1);
            } catch (IOException ioException) {
                LogUtils.e("", getString(R.string.service_not_available));
            } catch (IllegalArgumentException illegalArgumentException) {
                LogUtils.e("", getString(R.string.invalid_lat_long_used) + ". " +
                        "Latitude = " + location.latitude +
                        ", Longitude = " + location.longitude);
            }

            if (addresses == null || addresses.size() == 0) {
                LogUtils.e("", getString(R.string.no_address_found));
            } else {
                Address address = addresses.get(0);

                if (address != null) {
//                    LogUtils.e("", "address getLocality::"+address.getLocality());
//                    LogUtils.e("", "address getAdminArea::"+address.getAdminArea());
//                    LogUtils.e("", "address getPostalCode::"+address.getPostalCode());
//                    LogUtils.e("", "address getSubLocality::"+address.getSubLocality());
//                    LogUtils.e("", "address getUrl::"+address.getUrl());
//                    LogUtils.e("", "address getAddressLine::"+address.getAddressLine(0));
//                    LogUtils.e("", "address getAddressLine::"+address.getAddressLine(1));
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        LogUtils.e("", i + " Address " + address.getAddressLine(i));
                        addressList.add(address.getAddressLine(i));
                    }
                    mResultAddress = address.getAddressLine(0);
                    mResultCity = address.getLocality();
                    LogUtils.i("", getString(R.string.address_found));
                }
            }
            return addressList;
        }

    }

//    public void getAddressFromLatLng(LatLng location) {
//
//        if (!Geocoder.isPresent()) {
//            Utils.showToast(getActivity(), getString(R.string.no_geocoder_available));
//            return;
//        }
//
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//
//        try {
//            LogUtils.e("", "latitude::" + location.latitude + " longitude::" + location.longitude);
//            addresses = geocoder.getFromLocation(
//                    location.latitude,
//                    location.longitude,
//                    1);
//        } catch (IOException ioException) {
//            LogUtils.e("", getString(R.string.service_not_available));
//        } catch (IllegalArgumentException illegalArgumentException) {
//            LogUtils.e("", getString(R.string.invalid_lat_long_used) + ". " +
//                    "Latitude = " + location.latitude +
//                    ", Longitude = " + location.longitude);
//        }
//
//        if (addresses == null || addresses.size() == 0) {
//            LogUtils.e("", getString(R.string.no_address_found));
//        } else {
//            Address address = addresses.get(0);
//            ArrayList<String> addressFragments = new ArrayList<>();
//
//            this.address = address.getAddressLine(0);
//            this.city = address.getAddressLine(1);
//            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            LogUtils.i("", getString(R.string.address_found));
//            etAddress.setText("" + TextUtils.join(System.getProperty("line.separator"), addressFragments));
//        }
//    }

    @Override
    public void onClick(View view) {

        if (view == ibCancel) {
            finish();
        } else if (view == ibCurrentLocation) {
//            getDeviceLocation();
            if (mCurrentLocation != null) {
                LatLng mLatlng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatlng, 18));
                new GetAddressFromLatLngAsync(mLatlng).execute();
            }
        } else if (view == ibSearch) {
            try {

                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .build();

                Intent intent =
                        new PlaceAutocomplete
                                .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .setFilter(typeFilter)
                                .build(this);
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (view == tvSetAddress) {

            Intent back = new Intent();
            back.putExtra(BNDL_MAP_ADDRESS, mResultAddress);
            back.putExtra(BNDL_MAP_CITY, mResultCity);
            setResult(RESULT_OK, back);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                LogUtils.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                if (place != null) {
                    if (place.getLatLng() != null) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18));
                        new GetAddressFromLatLngAsync(place.getLatLng()).execute();
                    }
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e("Tag", status.toString());
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_CANCELED) {
            mRequestingLocationUpdates = false;
        }
    }

    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        LogUtils.i("", "All location settings are satisfied.");

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                LogUtils.i("", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    LogUtils.i("", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                LogUtils.e("", errorMessage);
                                Utils.showToast(getActivity(), errorMessage);
                                mRequestingLocationUpdates = false;
                        }

                    }
                });
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            LogUtils.d("", "stopLocationUpdates: updates never requested, no-op.");
            return;
        }
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

}
