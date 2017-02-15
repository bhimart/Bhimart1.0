package sample.com.closebuy;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Adapters.DbHelper;
import Adapters.DbHelpertopcat;
import Adapters.Topcategories;
import Adapters.Topdadd;
import Adapters.Topproducts;
import Adapters.Topshops;
import SessionManager.LoginSessionManager;
import SessionManager.seekbarsession;
import pojo.AppUtils;
import pojo.ShoppingCartResults;
import pojo.advertisment;
import pojo.cartvalues;
import pojo.category;
import pojo.products;
import pojo.shops;
import pojo.topcatresults;

public class Homepage extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener, ResultCallback<LocationSettingsResult> {
    ImageView navbtn;
    RecyclerView recyclerviewhome, recyclerviewproduct, recyleviewadd, recycleviewshops;
    JSONArray arr = null, arr2 = null, arr3 = null;
    private Boolean Isinternetpresent = false;
    ConnectionDetector cd;
    private String urlParameters;
    ProgressDialog dialog;
    protected final String TAG = "homepage";
    protected String mAddressOutput;
    String range;
    LoginSessionManager loginSessionManager;
    private Boolean exit = false;
    CircleProgressBar progress1;
    private List<topcatresults> mcatlist;
    byte[] imageInByte;
    String catimgtext;
    // private AddressResultReceiver mResultReceiver;
    Context mContext;
    ArrayList<Integer> totalcartvalues = new ArrayList<>();
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    private int LOCATION_PERMISSION_CODE = 3;
    private Location mCurrentLocation;
    private String lang1, lat1;
    int j;
    EditText searchet;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    DrawerLayout drawer;
    LinearLayout lin;
    NavigationView navigationView;
    TextView way;
    ImageView smiley;
    String cusid;
    ImageView profilepic;
    TextView profnameid;
    Spinner droprange;
    List<String> list;
    String rangeval;
    seekbarsession seekbarsession;
    DbHelpertopcat helpercat;
    Boolean isInternetPresent = false;
    private List<String> lstid = null;
    private List<String> lstname = null;
    private List<String> lstimg = null;
    private List<String> lstlat = null;
    private List<String> lstlang = null;
    ImageView imgs,imgs1;
    String base=null;
    private LruCache<String, Bitmap> mMemoryCache;
    Bitmap theBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navbtn = (ImageView) findViewById(R.id.menubar);
        helpercat = new DbHelpertopcat(this);
        recyclerviewhome = (RecyclerView) findViewById(R.id.recyclecategory);
        recyclerviewproduct = (RecyclerView) findViewById(R.id.recycleproduct);
        recyleviewadd = (RecyclerView) findViewById(R.id.advertrecycle);
        recycleviewshops = (RecyclerView) findViewById(R.id.recycleshop);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        LayoutInflater layoutInflater = LayoutInflater.from(Homepage.this);
        View promptView = layoutInflater.inflate(R.layout.progressbar, null);
        progress1 = (CircleProgressBar) promptView.findViewById(R.id.progress1);
        progress1.setShowArrow(true);
        // progress1.setVisibility(View.VISIBLE);
        lin = (LinearLayout) findViewById(R.id.draw);
        way = (TextView) findViewById(R.id.way);
        smiley = (ImageView) findViewById(R.id.smiley);
        smiley.setVisibility(View.GONE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        profilepic = (ImageView) findViewById(R.id.nav_prof_img_id);
        profnameid = (TextView) findViewById(R.id.prof_name_id);
        imgs = (ImageView) findViewById(R.id.imagesql);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingaction();
        searchet = (EditText) findViewById(R.id.search);
        mContext = this;
        try {
            loginSessionManager = new LoginSessionManager(getApplicationContext());
            HashMap<String, String> user = loginSessionManager.getUserDetails();

            if (loginSessionManager.isLoggedIn()) {
                materialDesignFAM.setVisibility(View.INVISIBLE);
                cusid = user.get(LoginSessionManager.KEY_ID);
                navbardetails nav = new navbardetails();
                nav.execute("http://quotecp.com:444/api/NavBarProfile/" + cusid);


            } else {

                navbtn.setVisibility(View.INVISIBLE);
                //   navigationView.setVisibility(View.GONE);
                lin.setVisibility(View.GONE);
                way.setVisibility(View.VISIBLE);
                smiley.setVisibility(View.VISIBLE);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            seekbarsession = new seekbarsession(getApplicationContext());
            HashMap<String, String> user1 = seekbarsession.gettseekdetail();
            String val = user1.get(seekbarsession.KEY_RANGE);
            if (val == null) {

                range = "15";
            } else {
                range = user1.get(seekbarsession.KEY_RANGE);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
        j = 0;
        // final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //menubar rotation
                Animation rotation = AnimationUtils.loadAnimation(Homepage.this, R.anim.menurotate);
                rotation.setRepeatCount(Animation.ABSOLUTE);
                navbtn.startAnimation(rotation);
                drawer.openDrawer(Gravity.LEFT);
                setUpNavigationView();
            }
        });
        searchet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSearchactivity = new Intent(Homepage.this, SearchActivity.class);
                gotoSearchactivity.putExtra("lat", lat1);
                gotoSearchactivity.putExtra("lan", lang1);
                startActivity(gotoSearchactivity);
            }
        });


        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {

            sqlcat cat = new sqlcat();
            cat.execute();
        }

    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                //Check to see which item was being clicked and perform appropriate action
                switch (id) {
                    case R.id.nav_profile_id:
                        Intent i1 = new Intent(Homepage.this, Profiledetails.class);
                        startActivity(i1);
                        break;


                    case R.id.nav_yourcart:
                        Intent i2 = new Intent(Homepage.this, Yourcart.class);
                        startActivity(i2);
                        break;


                    case R.id.nav_order_id:
                        Intent i3 = new Intent(Homepage.this, Yourorders.class);
                        startActivity(i3);
                        break;


                    case R.id.nav_range:
                        Intent i4 = new Intent(Homepage.this, pickrange1.class);
                        startActivity(i4);
                        break;
          /*  case R.id.nav_edit_id:
                nav_edit_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.gkshopshare1);
                        File filepath = Environment.getExternalStorageDirectory();
                        File dir = new File(filepath.getAbsolutePath()
                                + "/Gk_Shop_Pic/");
                        dir.mkdirs();
                        File file = new File(dir, "invite_gk.png");
                        OutputStream outStream = null;
                        try {
                            outStream = new FileOutputStream(file);
                            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // easytracker.send(MapBuilder.createEvent("BloodForSure_Invite", "Invite", "Blood_For_Sure_Invite_Page", null).build());
                        asynchinvite pars22 = new asynchinvite();
                        pars22.execute();
                    }
                });*/

                    // Intent i2=new Intent();
                    // startActivity(i2);
                    //break;

           /* case R.id.nav_privacy_id:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.gkshope.gkcustomer"));
                startActivity(intent);
                this.finish();
                break;*/


                    case R.id.signout_id:

                        loginSessionManager.logoutUser();
                        finish();
                        break;

          /*  case R.id.terms_id:
                Intent i5 = new Intent(Homepage.this, Termsandcondition.class);
                startActivity(i5);
                break;*/

           /* case R.id.nav_about1_id:
                Intent i4 = new Intent(Homepage.this, aboutus.class);
                startActivity(i4);
                break;*/

                }

                return true; //Checking if the item is in checked state or not, if not make it in checked state
            }
        });
    }

    public void floatingaction() {

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sign = new Intent(Homepage.this, Registration.class);
                startActivity(sign);


            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent log = new Intent(Homepage.this, LoginPage.class);
                startActivity(log);
            }
        });
    }


    public void webapis() {


        categoryvalues cat = new categoryvalues();
        cat.execute("http://quotecp.com:444/api/topcategory");

        productvalues prd = new productvalues();
        prd.execute("http://quotecp.com:444/api/topproducts");

        advert advt = new advert();
        advt.execute("http://quotecp.com:444/api/Ads");

        topshops shops = new topshops();
        shops.execute("http://quotecp.com:444/api/topshop");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(a);
                }
            }, 1000);

        }
    }

    /* @Override
     public boolean onNavigationItemSelected(MenuItem item) {
         int id = item.getItemId();


      *//*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);*//*
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nav_profile_id:
                Intent i1 = new Intent(Homepage.this, Profiledetails.class);
                startActivity(i1);
                break;


            case R.id.nav_yourcart:
                Intent i2 = new Intent(Homepage.this, Yourcart.class);
                startActivity(i2);
                break;

            case R.id.nav_order_id:
                Intent i3 = new Intent(Homepage.this, Yourorders.class);
                startActivity(i3);
                break;

            case R.id.nav_range:
                Intent i4 = new Intent(Homepage.this, pickrange1.class);
                startActivity(i4);
                break;
          /*  case R.id.nav_edit_id:
                nav_edit_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.gkshopshare1);
                        File filepath = Environment.getExternalStorageDirectory();
                        File dir = new File(filepath.getAbsolutePath()
                                + "/Gk_Shop_Pic/");
                        dir.mkdirs();
                        File file = new File(dir, "invite_gk.png");
                        OutputStream outStream = null;
                        try {
                            outStream = new FileOutputStream(file);
                            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // easytracker.send(MapBuilder.createEvent("BloodForSure_Invite", "Invite", "Blood_For_Sure_Invite_Page", null).build());
                        asynchinvite pars22 = new asynchinvite();
                        pars22.execute();
                    }
                });*/

            // Intent i2=new Intent();
            // startActivity(i2);
            //break;

           /* case R.id.nav_privacy_id:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.gkshope.gkcustomer"));
                startActivity(intent);
                this.finish();
                break;*/


            case R.id.signout_id:

                loginSessionManager.logoutUser();
                finish();
                break;

          /*  case R.id.terms_id:
                Intent i5 = new Intent(Homepage.this, Termsandcondition.class);
                startActivity(i5);
                break;*/

           /* case R.id.nav_about1_id:
                Intent i4 = new Intent(Homepage.this, aboutus.class);
                startActivity(i4);
                break;*/

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat1 = String.valueOf(location.getLatitude());
        lang1 = String.valueOf(location.getLongitude());
        System.out.println("lat=" + lat1 + "lang=" + lang1);
        if (j < 1) {
            j++;
            webapis();
        }


    }


    private class categoryvalues extends AsyncTask<String, String, List<category>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //  dialog =  ProgressDialog.show(Homepage.this, "Loading" , "Please wait...", true);
            //  dialog.show();
        }

        @Override
        protected List<category> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {


                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");



                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                String finalJson = buffer.toString();

                System.out.println("final--" + finalJson);
                List<category> itemModelList = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    final JSONObject obj = arr.getJSONObject(i);
                    category model = new category();
                    if (obj != null && arr.length() > 0) {


                        int catid1 = Integer.parseInt(obj.getString("CatogeryID"));
                        String catname1 = obj.getString("CategoryName");
                        catimgtext = obj.getString("Image");
                        String latit1 = lat1;
                        String langtit = lang1;
                        model.setId(Integer.parseInt(obj.getString("CatogeryID")));
                        model.setName(obj.getString("CategoryName"));
                        model.setThumbnailUrl(obj.getString("Image"));


                        model.setlat(lat1);
                        model.setlang(lang1);
                        itemModelList.add(model);


                      /*  runOnUiThread (new Thread(new Runnable() {
                            public void run() {


                                    Picasso.with(getApplicationContext())
                                            .load(catimgtext).into(imgs);

                               *//* theBitmap=Glide.with(getApplicationContext())
                                        .load(catimgtext)
                                        .asBitmap()
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                imgs.setImageBitmap(resource);
                                            }
                                        });*//*

                            }
                        }));*/

                      /* runOnUiThread (new Thread(new Runnable() {
                            public void run() {
                                Picasso.with(Homepage.this)
                                        .load(catimgtext)
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                *//* Save the bitmap or do something with it here *//*

                                                //Set it in the ImageView
                                                imgs.setImageBitmap(bitmap);
                                                //  Bitmap bitmap1 = ((BitmapDrawable) imgs.getDrawable()).getBitmap();
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                imageInByte = baos.toByteArray();
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {

                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {



                                            }







                                        });

                            }

                        }));*/
                     //  String imageval= ConvertImgString(imgs);
                       /* BitmapDrawable drawable = (BitmapDrawable) imgs.getDrawable();
                        Bitmap bitmap1= drawable.getBitmap();
                        addBitmapToMemoryCache(catname1,bitmap1);*/

                        String bitsql=bitm(catimgtext);
                        boolean value = helpercat.CheckIsDataAlreadyInDBorNotcat(obj.getString("CatogeryID"));
                        if (value != true) {


                            helpercat.insertcat(catid1, catname1,bitsql , latit1, langtit);
                        }

                }
                }
                return itemModelList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }/*catch (IllegalArgumentException e){
                e.printStackTrace();
            }*/

            return null;
        }
        protected String bitm(String path) {

            try {
                theBitmap = Glide.
                        with(Homepage.this).
                        load(path).
                        asBitmap().
                        into(-1,-1).
                        get();

              /*  runOnUiThread (new Thread(new Runnable() {
                    public void run() {
                        imgs1.setImageBitmap(theBitmap);
                    }
                }));*/
              //  Bitmap bitmap1 = ((BitmapDrawable) imgs1.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                theBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                base= Base64.encodeToString(b,Base64.DEFAULT);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("vvvv-"+theBitmap.toString());


            return base;
        }
        @Override
        protected void onPostExecute(List<category> detailsModels) {

            super.onPostExecute(detailsModels);
//
            if (detailsModels != null && detailsModels.size() > 0) {
                System.out.println("det1-" + detailsModels);
                recyclerviewhome = (RecyclerView) findViewById(R.id.recyclecategory);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerviewhome.setLayoutManager(linearLayoutManager);
                recyclerviewhome.setHasFixedSize(true);
                Topcategories rcAdapter = new Topcategories(getApplicationContext(), detailsModels);
                recyclerviewhome.setAdapter(rcAdapter);
                // dialog.dismiss();
            } else {


            }

            //   dialog.dismiss();
        }


    }


   /* public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }*/
  /*  public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    String ConvertImgString(ImageView imgs)  {
//Bitmap bitmap1 = ((BitmapDrawable) imgs.getDrawable()).getBitmap();
        BitmapDrawable drawable = (BitmapDrawable) imgs.getDrawable();
        Bitmap bitmap1= drawable.getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        String image64 =Base64.encodeToString(b,Base64.DEFAULT);
     /*  *//**//*//**//*//* Bitmap bm = BitmapFactory.decodeFile(url);
        Bitmap bitmap1 = ((BitmapDrawable) imgs.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b,Base64.DEFAULT);*//**//*
        return  image64;
    }*/

    private class productvalues extends AsyncTask<String, String, List<products>> {


        @Override
        protected void onPreExecute() {
            //          super.onPreExecute();
//            dialog.show();
        }

        @Override
        protected List<products> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {


                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");


                InputStream stream = connection.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                String finalJson = buffer.toString();


                List<products> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    products model1 = new products();
                    if (obj != null && arr.length() > 0) {

                        model1.setId(obj.getString("ShopProductID"));
                        model1.setName(obj.getString("ProductName"));
                        model1.setThumbnailUrl(obj.getString("Image"));
                        model1.setprice("₹" + obj.getString("SellingPrice"));
                        itemModelList1.add(model1);
                    }
                }


                return itemModelList1;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<products> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                recyclerviewproduct = (RecyclerView) findViewById(R.id.recycleproduct);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerviewproduct.setLayoutManager(linearLayoutManager);
                recyclerviewproduct.setHasFixedSize(true);
                Topproducts rcAda = new Topproducts(getApplicationContext(), detailsModels1);
                recyclerviewproduct.setAdapter(rcAda);

                //  dialog.dismiss();
            }

//dialog.dismiss();
        }


    }

    private class advert extends AsyncTask<String, String, List<advertisment>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   dialog = new ProgressDialog(Homepage.this);
            //   dialog.show();
        }

        @Override
        protected List<advertisment> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {


                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                /*connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");*/

                // connection.setUseCaches(false);
                // connection.setDoInput(true);
                // connection.setDoOutput(true);


                // DataOutputStream wr = new DataOutputStream(
                //       connection.getOutputStream());
                //  wr.writeBytes(urlParameters);
                // wr.flush();
                // wr.close();


                InputStream stream = connection.getInputStream();
                // InputStreamReader rdr=new InputStreamReader(stream);
               /* int tmp=rdr.read();
                while(tmp!=-1) {
char curr=(char)tmp;
                    tmp=rdr.read();
                    System.out.println(curr);

                }*/

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                String finalJson2 = buffer.toString();

                System.out.println("final2--" + finalJson2);
                List<advertisment> itemModelList2 = new ArrayList<>();

                arr2 = new JSONArray(finalJson2);
                for (int i = 0; i < arr2.length(); i++) {
                    JSONObject obj2 = arr2.getJSONObject(i);
                    advertisment model2 = new advertisment();
                    if (obj2 != null && arr.length() > 0) {

                        model2.setid(obj2.getString("ADID"));
                        model2.setThumbnailUrl(obj2.getString("Image"));
                        itemModelList2.add(model2);
                    }
                }


                return itemModelList2;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<advertisment> detailsModels2) {

            super.onPostExecute(detailsModels2);
//
            if (detailsModels2 != null && detailsModels2.size() > 0) {
                System.out.println("det1-" + detailsModels2);
                recyleviewadd = (RecyclerView) findViewById(R.id.advertrecycle);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyleviewadd.setLayoutManager(linearLayoutManager2);
                recyleviewadd.setHasFixedSize(true);
                Topdadd rcAdapter2 = new Topdadd(getApplicationContext(), detailsModels2);
                recyleviewadd.setAdapter(rcAdapter2);
                //            dialog.dismiss();
            }

            //   dialog.dismiss();
        }


    }

    private class topshops extends AsyncTask<String, String, List<shops>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            dialog = new ProgressDialog(Homepage.this);
            //          dialog.show();
        }

        @Override
        protected List<shops> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                urlParameters = "&lati=" + URLEncoder.encode(lat1, "UTF-8") +
                        "&longi=" + URLEncoder.encode(lang1, "UTF-8") +
                        "&range=" + URLEncoder.encode(range, "UTF-8");

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                InputStream stream = connection.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                String finalJson3 = buffer.toString();

                System.out.println("final3--" + finalJson3);
                List<shops> itemModelList3 = new ArrayList<>();

                arr3 = new JSONArray(finalJson3);
                for (int i = 0; i < arr3.length(); i++) {
                    JSONObject obj = arr3.getJSONObject(i);
                    shops model3 = new shops();
                    if (obj != null && arr.length() > 0) {
                        model3.setId(obj.getString("ShopID"));
                        model3.setName(obj.getString("ShopName"));
                        model3.setThumbnailUrl(obj.getString("Image"));
                        model3.setdistance(obj.getString("Distance"));
                        itemModelList3.add(model3);
                    }
                }


                return itemModelList3;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<shops> detailsModels3) {

            super.onPostExecute(detailsModels3);
//
            if (detailsModels3 != null && detailsModels3.size() > 0) {
                System.out.println("det1-" + detailsModels3);
                recycleviewshops = (RecyclerView) findViewById(R.id.recycleshop);
                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
                recycleviewshops.setLayoutManager(linearLayoutManager3);
                recycleviewshops.setHasFixedSize(true);
                Topshops rcAdapter = new Topshops(getApplicationContext(), detailsModels3);
                recycleviewshops.setAdapter(rcAdapter);

                //   dialog.dismiss();
            } else {
                recycleviewshops.setAdapter(null);
                //   dialog.dismiss();
            }

            progress1.setVisibility(View.GONE);
            //   dialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                rangeval = data.getStringExtra("rangeval");
                System.out.println("rangeval===" + rangeval);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }


        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        if (isReadStorageAllowed()) {
                            //If permission is already having then showing the toast

                            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
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
            System.out.println("lat=" + lat1 + "lang=" + lang1);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
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

                    status.startResolutionForResult(Homepage.this, REQUEST_CHECK_SETTINGS);
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


    private boolean isReadStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);


        if (result == PackageManager.PERMISSION_GRANTED)
            return true;


        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

        }


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == LOCATION_PERMISSION_CODE) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);


            } else {

                Toast.makeText(this, "location is needed to get the product", Toast.LENGTH_LONG).show();
            }
        }
    }


    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }*/


    private class navbardetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*  dialog = ProgressDialog.show(Homepage.this, "Loading",
                    "Please wait...", true);
            dialog.show();*/
        }

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection = null;
            try {


                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
               /* connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);*/

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

                        profnameid.setText(obj.getString("Name"));
                        String im1 = obj.getString("Image");
                        Picasso.with(Homepage.this)
                                .load(im1)
                                .into(profilepic);


                    } else {


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

//dialog.dismiss();
        }
    }

    private class sqlcat extends AsyncTask<String, String, List<category>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Homepage.this, "Loading", "Please Wait...", true);
            dialog.show();
        }

        @Override
        protected List<category> doInBackground(String... urls) {


            try {
                helpercat = new DbHelpertopcat(Homepage.this);
                mcatlist = helpercat.getResultscat();
                List<category> itemModelList1 = new ArrayList<>();

                if (mcatlist.size() != 0) {



                    for (topcatresults cat : mcatlist)

                    {
                        category model = new category();
                        model.setId(cat.getcatid());
                        model.setName(cat.getcatname());
                        model.setimg(cat.getcatimg());
                        model.setlat(cat.gelat());
                        model.setlang(cat.getLang());
                        itemModelList1.add(model);
                        Log.d("val.. ", cat.getcatid()+"");
                    }


                    return itemModelList1;
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<category> detailsModels1) {

            super.onPostExecute(detailsModels1);
            if (detailsModels1 != null && detailsModels1.size() > 0)
            {
                recyclerviewhome = (RecyclerView) findViewById(R.id.recyclecategory);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerviewhome.setLayoutManager(linearLayoutManager1);
                recyclerviewhome.setHasFixedSize(true);
                Topcategories rcAdapter1 = new Topcategories(getApplicationContext(), detailsModels1);
                recyclerviewhome.setAdapter(rcAdapter1);

            }
            dialog.dismiss();
        }

    }
}
