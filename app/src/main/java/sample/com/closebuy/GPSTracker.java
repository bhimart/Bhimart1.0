package sample.com.closebuy;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import pojo.AppUtils;

public class GPSTracker extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */

    int j =0;
    private static final String TAG = "FetchAddressIS";

    /**
     * The receiver where results are forwarded from this service.
     */
    protected ResultReceiver mReceiver;


    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    protected GoogleApiClient mGoogleApiClient;



    /*public GPSTracker(Context applicationContext) {
    }*/

    public GPSTracker() {
        super(TAG);

//        getLocation();
    }

    public void getLocation() {
        try {


            locationManager = (LocationManager) GPSTracker.this.getSystemService(LOCATION_SERVICE);


            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
            Log.d("Network", "Network");
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String lat, lon;
                    lat = String.valueOf(latitude);
                    lon = String.valueOf(longitude);
                    deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);

                } else {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            String lat, lon;
                            lat = String.valueOf(latitude);
                            lon = String.valueOf(longitude);

                            deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);

                        } else {
                            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mGoogleApiClient);
                            if (mLastLocation != null) {

                                location = mLastLocation;

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                String lat, lon;
                                lat = String.valueOf(latitude);
                                lon = String.valueOf(longitude);

                                deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);


                                Log.d(TAG, "ON connected");

                            } else {
                                deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
                            }
                        }
                    } else {
                        deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
                    }
                }
            } else {
                deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
            }
            // If GPS enabled, get latitude/longitude using GPS Services

            if (location == null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {

                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        String lat, lon;
                        lat = String.valueOf(latitude);
                        lon = String.valueOf(longitude);

                        deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);

                    } else {
                        deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
                    }
                } else {
                    deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String lat, lon;
                lat = String.valueOf(latitude);
                lon = String.valueOf(longitude);
                if (lat == null && lon == null) {

                } else {
                    deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);
                }

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mReceiver = intent.getParcelableExtra("abc");
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;

        }
        getLocationApi();
    }

    private void getLocationApi() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            location = mLastLocation;

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String lat, lon;
            lat = String.valueOf(latitude);
            lon = String.valueOf(longitude);

            deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);


            Log.d(TAG, "ON connected");

        }else{
            deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, String address, String address1) {
       if (j<1) {
           try {
               Bundle bundle = new Bundle();
               bundle.putString("message", message);

               bundle.putString("lat", address);

               bundle.putString("lon", address1);
               mReceiver.send(resultCode, bundle);
               j++;
               onDestroy();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            location = mLastLocation;

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String lat, lon;
            lat = String.valueOf(latitude);
            lon = String.valueOf(longitude);

            deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT, "message", lat, lon);
            Log.d(TAG, "ON connected");

        }else{
            deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, "message", null, null);
    }
}