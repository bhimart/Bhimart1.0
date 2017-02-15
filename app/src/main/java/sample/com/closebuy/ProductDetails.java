package sample.com.closebuy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.DbHelper;
import pojo.ShoppingCartResults;

public class ProductDetails extends AppCompatActivity  implements View.OnClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private ImageView selectedImageView;
    DbHelper helper;
    private SliderLayout mDemoSlider;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    JSONArray arr = null;
    ConnectionDetector cd;
    private Boolean Isinternetpresent = false;
    private String urlParameters;
    ProgressDialog dialog,dialog1;
    String idtext;
    ArrayList<String> list = new ArrayList<String>();
    TextView mrp,price,shopname,qty,desc,prdname,prdimg,vendorqty;
    ImageView addQty,subtractQty;
    private LinearLayout checkOutLayout;
    String pricev,qtyv;
    int vendorquant;
    String pqty,pimage;
     Integer itmQty=1;
    Integer producttotal;
    String totalvalue;
     Button addto;

    ArrayList<Integer> totalcartvalues=new ArrayList<>();
    private List<ShoppingCartResults> mCartList;

    String image1,image2,image3,image4,image5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        shopname=(TextView)findViewById(R.id.shopname);
        prdname=(TextView)findViewById(R.id.prdname);
        helper = new DbHelper(this);
        mrp=(TextView)findViewById(R.id.mrp);
        price=(TextView)findViewById(R.id.sellprice);
        vendorqty=(TextView)findViewById(R.id.vendorqty);

        //  qty=(TextView)findViewById(R.id.qtyvalue);
        desc=(TextView)findViewById(R.id.description);
        //addQty=(ImageView) findViewById(R.id.add);
       // subtractQty=(ImageView) findViewById(R.id.sub);
        addto = (Button) findViewById(R.id.addcart1);
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
       // mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();
        Bundle extras = getIntent().getExtras();
        idtext = extras.getString("shopprdid");
       // pqty=extras.getString("qty");
       // itmQty=Integer.parseInt(pqty);
        pimage=extras.getString("pimage");

        if (Isinternetpresent)
        {

            getimgs img=new getimgs();

            img.execute("http://quotecp.com:444/api/product/GetProductDetailsimages/"+idtext);

            getdetails tasku = new getdetails();

            tasku.execute("http://quotecp.com:444/api/product/GetProductDetails/"+idtext);

        }
        else
        {
            // Toast.makeText(UserProfileActivity.this,"No Internet connection",Toast.LENGTH_SHORT).show();
            showAlertDialog(getApplicationContext(), "No Internet Connection", "You don't have internet connection.", false);
        }
       /* addQty.setOnClickListener(this);
        subtractQty.setOnClickListener(this);
        if (itmQty <= 0) {
            checkOutLayout.setVisibility(View.GONE);
            subtractQty.setEnabled(false);
            subtractQty.setOnClickListener(this);
        }*/

        addto.setOnClickListener(this);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuprodetails, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addcat)
        {

            Intent in1 = new Intent(ProductDetails.this, Yourcart.class);
            startActivity(in1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {


        boolean value=helper.CheckIsDataAlreadyInDBorNot(idtext);
        if(value!=true){


            pricev=price.getText().toString();
            vendorquant=Integer.parseInt(vendorqty.getText().toString());
            helper.insert(idtext, itmQty, Integer.parseInt(pricev),vendorquant, prdname.getText().toString(), pimage, shopname.getText().toString());
            Toast.makeText(ProductDetails.this,"success",Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(ProductDetails.this,"Already Exists",Toast.LENGTH_SHORT).show();
        }

    }
    /* @Override
    public void onClick(View v) {

        if (v == addQty) {
            int qtyVal = ++itmQty;
            qty.setText(qtyVal + "");


            if (qtyVal > 0) {
          //      checkOutLayout.setVisibility(View.VISIBLE);
                subtractQty.setEnabled(true);
                subtractQty.setOnClickListener(this);
            }

        }

        if (v == subtractQty) {
            int qtyVal = itmQty;
            if (--qtyVal >= 0) {
                if (qtyVal == 0)
                    checkOutLayout.setVisibility(View.GONE);
                itmQty = qtyVal;
                qty.setText(qtyVal + "");


            } else {
                qty.setText("1");
                checkOutLayout.setVisibility(View.GONE);
                subtractQty.setEnabled(false);
                subtractQty.setOnClickListener(this);
                return;
            }
        }

        if (v == addto) {


            pricev = price.getText().toString();
            qtyv = qty.getText().toString();
            helper.insert(idtext, Integer.parseInt(qtyv), Integer.parseInt(pricev), prdname.getText().toString(), pimage, shopname.getText().toString());
            mCartList = helper.getResults();
            for (ShoppingCartResults p : mCartList) {
                int pricesql = p.getprice();
                int qtysql = p.getqty();
                totalcartvalues.add(qtysql);

            }
            producttotal = (Integer.parseInt(qtyv)) * (Integer.parseInt(pricev));
            totalvalue = String.valueOf(producttotal);
            System.out.println("prdtot--"+totalvalue);

            cd = new ConnectionDetector(getApplicationContext());
            Isinternetpresent = cd.isConnectingToInternet();


        }
    }*/
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //         alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }



    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mDemoSlider.startAutoCycle();


    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();

    }
    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class getimgs extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = ProgressDialog.show(ProductDetails.this, "Loading",
                    "Please wait...", true);
            dialog1.show();

        }

        @Override
        protected String doInBackground(String... urls) {


            URL url;
            HttpURLConnection connection = null;
            try {


               // urlParameters = "&id=" + URLEncoder.encode(idtext, "UTF-8");
             //   System.out.println("url--"+urlParameters);
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
              /*  connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
*/
               /* DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();*/

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
                HashMap<String, String> url_maps = new HashMap<String, String>();
                arr = new JSONArray(result);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    if (obj != null)
                    {



                        url_maps.put("", obj.getString("ImageName"));


                        for(String name : url_maps.keySet()){

                            DefaultSliderView textSliderView = new DefaultSliderView(ProductDetails.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                                    .setOnSliderClickListener(ProductDetails.this);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            mDemoSlider.addSlider(textSliderView);
                            mDemoSlider.stopAutoCycle();
                        }
                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                        mDemoSlider.setDuration(0);
                        mDemoSlider.addOnPageChangeListener(ProductDetails.this);





                    }



                    else
                    {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                        //       Toast.makeText(getApplicationContext(),"Please Try Again After Somtime",Toast.LENGTH_LONG).show();
                        //dialog1.dismiss();
                    }



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//          dialog.dismiss();
        }

    }

    public class getdetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog1 = ProgressDialog.show(ProductDetails.this, "Loading",
                    "Please wait...", true);
            dialog1.show();*/

        }

        @Override
        protected String doInBackground(String... urls) {


            URL url;
            HttpURLConnection connection = null;
            try {


                // urlParameters = "&id=" + URLEncoder.encode(idtext, "UTF-8");
                //   System.out.println("url--"+urlParameters);
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
              /*  connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
*/
               /* DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();*/

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

                //  HashMap<String, String> url_maps = new HashMap<String, String>();
                arr = new JSONArray(result);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    //  String state = obj.getString("status");
                    if (obj != null)
                    {

                       prdname.setText(obj.getString("ProductName").toString());
                       shopname.setText(obj.getString("ShopName").toString());
                        desc.setText(obj.getString("Description").toString());
                        mrp.setText(obj.getString("MRP").toString());
                        price.setText(obj.getString("SellingPrice").toString());
                        vendorqty.setText(obj.getString("VendorQty").toString());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                        //       Toast.makeText(getApplicationContext(),"Please Try Again After Somtime",Toast.LENGTH_LONG).show();
                        //dialog1.dismiss();
                    }
                    //dialog1.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
          dialog1.dismiss();
        }

    }



}
