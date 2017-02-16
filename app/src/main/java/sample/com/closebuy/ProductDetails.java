package sample.com.closebuy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import Adapters.DbHelper;

public class ProductDetails extends AppCompatActivity  implements View.OnClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    DbHelper helper;
    private SliderLayout mDemoSlider;
    JSONArray array = null;
    ConnectionDetector cd;
    private Boolean Isinternetpresent = false;
    ProgressDialog dialog;
    String idtext;
    TextView mrpTv,priceTv,shopnameTv,descTv,prdnameTv,vendorqtyTv;
    String pricesql;
    int vendorquant;
    String productimage;
    Integer itmQty=1;
     Button addtoBt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               onBackPressed();
            }
        });

        shopnameTv=(TextView)findViewById(R.id.shopname);
        prdnameTv=(TextView)findViewById(R.id.prdname);
        helper = new DbHelper(this);
        mrpTv=(TextView)findViewById(R.id.mrp);
        priceTv=(TextView)findViewById(R.id.sellprice);
        vendorqtyTv=(TextView)findViewById(R.id.vendorqty);
        descTv=(TextView)findViewById(R.id.description);
        mrpTv.setPaintFlags(mrpTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        addtoBt= (Button) findViewById(R.id.addcart);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        Bundle extras = getIntent().getExtras();
        idtext = extras.getString("shopprdid");
        productimage=extras.getString("pimage");

        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();
        if (Isinternetpresent)
        {

            getimgs img=new getimgs();

            /*api to get the imges for slider*/
            img.execute("http://quotecp.com:444/api/product/GetProductDetailsimages/"+idtext);

            getdetails tasku = new getdetails();

           /* api to get product details*/
            tasku.execute("http://quotecp.com:444/api/product/GetProductDetails/"+idtext);

        }
        else
        {

            showAlertDialog(getApplicationContext(), "No Internet Connection", "You don't have internet connection.", false);

        }
        addtoBt.setOnClickListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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

/* adding the values to sqlite by checking the id is exist or not*/

        boolean value=helper.CheckIsDataAlreadyInDBorNot(idtext);
        if(value!=true)
        {

            pricesql=priceTv.getText().toString();
            vendorquant=Integer.parseInt(vendorqtyTv.getText().toString());
            helper.insert(idtext, itmQty, Integer.parseInt(pricesql),vendorquant, prdnameTv.getText().toString(), productimage, shopnameTv.getText().toString());

            Toast.makeText(ProductDetails.this,"Added to cart",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ProductDetails.this,"Already Exists",Toast.LENGTH_SHORT).show();
        }

    }

    public void showAlertDialog(Context context, String title, String message, Boolean status)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
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
    public void onPageScrollStateChanged(int state)
    {

    }

    public class getimgs extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ProductDetails.this, "Loading",
                    "Please wait...", true);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls)
        {


            URL url;
            HttpURLConnection connection = null;
            try {


                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

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
                array = new JSONArray(result);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (obj != null)
                    {



                        url_maps.put("", obj.getString("ImageName"));


                        for(String name : url_maps.keySet())
                        {

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
                    }



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public class getdetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... urls)
        {


            URL url;
            HttpURLConnection connection = null;
            try {



                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");


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

            }
            finally
            {

                if (connection != null)
                {
                    connection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(String result)
        {

            try {


                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    if (obj != null)
                    {

                        prdnameTv.setText(obj.getString("ProductName").toString());
                        shopnameTv.setText(obj.getString("ShopName").toString());
                        descTv.setText(obj.getString("Description").toString());
                        mrpTv.setText(obj.getString("MRP").toString());
                        priceTv.setText(obj.getString("SellingPrice").toString());
                        vendorqtyTv.setText(obj.getString("VendorQty").toString());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
          dialog.dismiss();
        }

    }



}
