package sample.com.closebuy;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.xml.validation.Validator;


public class Checkin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback<LocationSettingsResult>
{
Button apply;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    protected LocationRequest mLocationRequest;

    protected LocationSettingsRequest mLocationSettingsRequest;

    protected GoogleApiClient mGoogleApiClient;

    private int LOCATION_PERMISSION_CODE = 3;
    private Location mCurrentLocation;
    private String lang1,lat1;
    protected static final String TAG = "Checkin";
    Context mContext;
    JSONArray arr = null;
    ConnectionDetector cd;
    private String urlParameters;
    private Boolean Isinternetpresent = false;
    String shoppid,shopid;
    String shopname,shopaddress,shopdistav,city,state,pin,shopimg,shoplat,shoplang;
    TextView shopdist,shopddr,total;
    ProgressDialog dialog,dialog1;
     EditText name,phoneno,email;
    TextView submit,cancel1;
    String namepop,phonenopop,emailpop;
    String latitude;
    String langtitude;
    Double value,value1;
    DecimalFormat df2 = new DecimalFormat(".##");
    Button ok,cancel;
    int j;
    ImageView shpimg;
    LinearLayout bottam;
    private View positiveAction;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
        j=0;
        apply=(Button)findViewById(R.id.button2);
        shopdist=(TextView)findViewById(R.id.kmdetilas);
        bottam=(LinearLayout) findViewById(R.id.bot);
        bottam.setVisibility(View.GONE);
        shopddr=(TextView)findViewById(R.id.shopaddr);
        ok=(Button)findViewById(R.id.ok);
       // cancel=(Button)findViewById(R.id.cancel);
        Bundle extras = getIntent().getExtras();
        shoppid = extras.getString("shoppid");
        shopid=extras.getString("shopid");
        //shopdist.setText( shopname+"your away"+dist);
        shpimg=(ImageView) findViewById(R.id.image1);
        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//Do what you need for this SDK


        Window window = Checkin.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(Checkin.this.getResources().getColor(R.color.colorPrimaryDark));
        }

          apply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showInputDialog();
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(Checkin.this,MapRoute.class);
                in.putExtra("slat",lat1);
                in.putExtra("slan",lang1);
                in.putExtra("shoplat",shoplat);
                in.putExtra("shoplang",shoplang);
                startActivity(in);
            }
        });

      /*  cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }


    public void showAlertDialog(Context context, String title, String message, Boolean status) {





        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }


    public void callapi(){

        if (Isinternetpresent)
        {

            checkinval check=new checkinval();
            check.execute("http://quotecp.com:444/api/shopdetails");


        }else{
            showAlertDialog(Checkin.this, "No Internet Connection", "You don't have internet connection.", false);
        }



    }
    protected void showInputDialog()
    {

      /*  MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("")
                .customView(R.layout.input_dialog, true)
                .positiveText("Ok")
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                        name    = (EditText) dialog.getCustomView().findViewById(R.id.name);
                        phoneno = (EditText) dialog.getCustomView().findViewById(R.id.phonenumber);
                        email   = (EditText) dialog.getCustomView().findViewById(R.id.email);
                        if(name.getText().toString().length() == 0)
                        {

                            name.setError("Please Enter your name");
                        }
                        else if(phoneno.getText().toString().length() < 10)
                        {
                            phoneno.setError("please enter correct Phone Number");
                        }
                        else if (email.getText().toString().length() == 0)
                        {
                            email.setError("Please Enter Your Valid email");

                        }else {
                            namepop = name.getText().toString();
                            phonenopop = phoneno.getText().toString();
                            emailpop = email.getText().toString();
                            if (Isinternetpresent)
                            {

                                senddetails send = new senddetails();
                                send.execute("http://quotecp.com:444/api/checkin");


                            } else
                            {
                                showAlertDialog(Checkin.this, "No Internet Connection", "You don't have internet connection.", false);
                            }

                        }
                    }

                   public void  onNegative(MaterialDialog dialog){
                   dialog.dismiss();
                   }


                }).show();*/




        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Checkin.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
         alertDialogBuilder = new AlertDialog.Builder(Checkin.this);
        alertDialogBuilder.setView(promptView);

         name = (EditText) promptView.findViewById(R.id.name);
         phoneno = (EditText) promptView.findViewById(R.id.phonenumber);
         email= (EditText) promptView.findViewById(R.id.email);
         submit =(TextView) promptView.findViewById(R.id.okval);
        cancel1 =(TextView) promptView.findViewById(R.id.cancelval);


          alertDialog = alertDialogBuilder.show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() == 0)
                {

                    name.setError("Please Enter your name");
                }
                else if(phoneno.getText().toString().length() < 10)
                {
                    phoneno.setError("please enter correct Phone Number");
                }
                else if (email.getText().toString().length() == 0)
                {
                    email.setError("Please Enter Your Valid email");

                }else {
                    namepop = name.getText().toString();
                    phonenopop = phoneno.getText().toString();
                    emailpop = email.getText().toString();
                    if (Isinternetpresent)
                    {

                        senddetails send = new senddetails();
                        send.execute("http://quotecp.com:444/api/checkin");


                    } else
                    {
                        showAlertDialog(Checkin.this, "No Internet Connection", "You don't have internet connection.", false);
                    }

                }
                alertDialog.show();
            }
        });




        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });


   /* cancel1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.cancel();
        }
    });*/

       /* alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new View.OnClickListener() {
                    public void onClick(View view)
                    {

                        if(name.getText().toString().length() == 0)
                        {

                            name.setError("Please Enter your name");
                        }
                        else if(phoneno.getText().toString().length() < 10 && phoneno.getText().toString().length()  > 10)
                        {
                          phoneno.setError("please enter correct Phone Number");
                        }
                        else if (email.getText().toString().length() == 0)
                        {
                            email.setError("Please Enter Your Valid email");

                        }else {
                            namepop = name.getText().toString();
                            phonenopop = phoneno.getText().toString();
                            emailpop = email.getText().toString();
                            if (Isinternetpresent)
                            {

                                senddetails send = new senddetails();
                                send.execute("http://quotecp.com:444/api/checkin");


                            } else
                            {
                                showAlertDialog(Checkin.this, "No Internet Connection", "You don't have internet connection.", false);
                            }

                        }



                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
*/
        // create an alert dialog
//        alertDialogBuilder.show();
    }
    private boolean isReadStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);


        if (result == PackageManager.PERMISSION_GRANTED)
            return true;


        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

        }


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        if (isReadStorageAllowed()) {
                            //If permission is already having then showing the toast

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
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    mGoogleApiClient, mLocationRequest, this);


//                            startIntentService();
                            //Existing the method with return
                            return;
                        }

                        //If the app has not the permission then asking for the permission
                        requestStoragePermission();

                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        Toast.makeText(mContext, "Cannot start Application without Location", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.

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
        if (mCurrentLocation != null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            lat1 = String.valueOf(mCurrentLocation.getLatitude());
            lang1 = String.valueOf(mCurrentLocation.getLongitude());
            System.out.println("lat="+lat1+"lang="+lang1);


        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    protected void checkLocationSettings()
    {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void buildLocationSettingsRequest()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected synchronized void buildGoogleApiClient()
    {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();


    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }



    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onLocationChanged(Location location) {
        lat1 = String.valueOf(location.getLatitude());
        lang1 = String.valueOf(location.getLongitude());
        System.out.println("lat222="+lat1+"lang222="+lang1);

        if (j<1)
        {

            latitude=lat1;
            langtitude=lang1;
            j++;
            callapi();
        }


    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                if (isReadStorageAllowed()) {



                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient, mLocationRequest, this);


                    return;
                }


                requestStoragePermission();

                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {

                    status.startResolutionForResult(Checkin.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }



    public class checkinval extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           dialog = ProgressDialog.show(Checkin.this, "Loading",
                    "Please wait...", true);
            dialog.show();


        }

        @Override
        protected String doInBackground(String... urls) {


            URL url;
            HttpURLConnection connection = null;
            try {
                urlParameters = "&lati=" + URLEncoder.encode(latitude, "UTF-8")+
                        "&longi=" + URLEncoder.encode(langtitude, "UTF-8")+
                        "&shopproductid=" + URLEncoder.encode(shoppid, "UTF-8")+
                        "&shopid=" + URLEncoder.encode(shopid, "UTF-8");

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {

                e.printStackTrace();
                return null;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {

            try {


                arr = new JSONArray(result);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    //  String state = obj.getString("status");
                    if (obj != null) {

                        shopname=obj.getString("ShopName");
                        shopdistav=obj.getString("Distance");
                        shopaddress=obj.getString("ShopAddress");
                        city=obj.getString("City");
                        state=obj.getString("State");
                        pin=obj.getString("Pincode");
                        shopimg=obj.getString("ShopImage");
                        shoplat=obj.getString("Latitude");
                        shoplang=obj.getString("Longitude");
                        shopdist.setText(shopname+" just "+shopdistav+" away");
                        shopddr.setText(shopaddress+city+state+pin);
                        Glide.with(Checkin.this).load(shopimg).into(shpimg);
                        bottam.setVisibility(View.VISIBLE);



                    } else {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }

    }
    public class senddetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = ProgressDialog.show(Checkin.this, "Loading",
                    "Please wait...", true);
            dialog1.show();


        }

        @Override
        protected String doInBackground(String... urls) {


            URL url;
            HttpURLConnection connection = null;
            try {
                urlParameters = "&lati=" + URLEncoder.encode(lat1, "UTF-8")+
                        "&longi=" + URLEncoder.encode(lang1, "UTF-8")+
                        "&shopproductid=" + URLEncoder.encode(shoppid, "UTF-8")+
                        "&customername=" + URLEncoder.encode(namepop, "UTF-8")+
                        "&Emailid=" + URLEncoder.encode(emailpop, "UTF-8")+
                        "&phoneno=" + URLEncoder.encode(phonenopop, "UTF-8");

                        url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {

                e.printStackTrace();
                return null;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {

            try {


                arr = new JSONArray(result);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                     String state = obj.getString("Status");
                    if (state.equals("Success")) {
                    String code=obj.getString("CCode");
                    Toast.makeText(Checkin.this,code,Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                        //       Toast.makeText(getApplicationContext(),"Please Try Again After Somtime",Toast.LENGTH_LONG).show();
                        //dialog1.dismiss();
                    }
                    // dialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog1.dismiss();
        }

    }


}




