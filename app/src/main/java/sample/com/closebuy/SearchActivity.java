package sample.com.closebuy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import Adapters.DbHelper;
import pojo.ProductModel;
import pojo.ShoppingCartResults;


public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView searchtv;
    String search1 = "";
    String searchRange,searchTearm;
    String baseurl ="http://quotecp.com:444";
    private List<ProductModel> list;
    private String urlParameters;
    JSONArray arr = null;
    private RecyclerView recyclarviewProduct;
    // CrystalSeekbar seekbar;
    TextView seekbarMinvalue;
    String range;
    String seekbarValue;
    private SeekBar seekbar = null;
    private MenuItem searchItem;
    ImageView closeBtn;
    //  private ProductAdapter mAdapter;
    ProgressDialog dialog,dialog1;
    private List<ShoppingCartResults> mCartList;
    DbHelper helper;
    String lat,lan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        lat=extras.getString("lat");
        lan=extras.getString("lan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent homepage = new Intent(SearchActivity.this,Homepage.class);
                startActivity(homepage);*/
                onBackPressed();
            }
        });



        searchtv = (AutoCompleteTextView) findViewById(R.id.autoserachitem_tv);
        recyclarviewProduct = (RecyclerView) findViewById(R.id.productlistrv);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // searchtv.setDropDownBackgroundResource(R.color.grdgreen);
        searchtv.setHint("search");

        search1 = searchtv.getText().toString();
        searchtv.addTextChangedListener(searchvalue);
        seekBarCall();
        closeBtn = (ImageView)findViewById(R.id.tb_closebutton);
        closeBtn.setVisibility(View.GONE);
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                searchtv.getText().clear();

            }
        });


    }
 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

       *//* searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchtv.addTextChangedListener(searchvalue);
        //searchView.setQueryHint("searchtext");

        //searchView.setBackgroundColor(R.color.white);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchActivity.class)));

        searchView.setIconifiedByDefault(false);*//*


        return true;
    }*/



   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

       *//* int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            return true;
        }*//*
        return super.onOptionsItemSelected(item);
    }*/

    private void seekBarCall(){

        seekbar = (SeekBar) findViewById(R.id.rangeSeekbar1);
        seekbarMinvalue = (TextView) findViewById(R.id.textMin1);
        seekbar.setVisibility(View.GONE);
        seekbarMinvalue.setVisibility(View.GONE);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 15;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                seekbarMinvalue.setText(String.valueOf(progressChanged));
                autosuggest autos = new autosuggest();
                range = seekbarMinvalue.getText().toString();
                autos.execute("http://quotecp.com:444/api/ProductSearch");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






      /*  // get seekbar from view
        seekbar = (CrystalSeekbar) findViewById(R.id.rangeSeekbar1);
        seekbarMinvalue = (TextView) findViewById(R.id.textMin1);
        seekbar.setVisibility(View.GONE);
        seekbarMinvalue.setVisibility(View.GONE);
        seekbar.setMinValue(7);
        seekbar.setMaxValue(20);


        // get min and max text view


        // set listener
        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                seekbarMinvalue.setText(String.valueOf(minValue));

                 autosuggest autos = new autosuggest();
                 range = seekbarMinvalue.getText().toString();
                 autos.execute("http://quotecp.com:444/api/ProductSearch");

            }
        });

        // set final value listener
        seekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                Log.d("CRS=>", String.valueOf(value));
            }
        });*/
    }

    private final TextWatcher searchvalue = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(s.length() >= 3)
            {
                searchTearm= searchtv.getText().toString();
                autosuggest autos = new autosuggest();
                range = "7";
                autos.execute("http://quotecp.com:444/api/ProductSearch");
                seekbar.setVisibility(View.VISIBLE);
                seekbarMinvalue.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    private class autosuggest extends AsyncTask<String, String, List<ProductModel>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /*dialog1 =  ProgressDialog.show(SearchActivity.this,"Loading","Please Wait.....");
            dialog1.show();*/
        }

        @Override
        protected List<ProductModel> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                urlParameters = "&lati=" + URLEncoder.encode(lat, "UTF-8") +
                        "&longi=" + URLEncoder.encode(lan, "UTF-8")+
                        "&range=" + URLEncoder.encode(range, "UTF-8")+
                        "&Term=" + URLEncoder.encode(searchTearm, "UTF-8");

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
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

                String finalJson = buffer.toString();

                List<ProductModel> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    ProductModel model1 = new ProductModel();
                    if (obj != null && arr.length() > 0) {
                        model1.setId(obj.getString("ShopProductID"));
                        model1.setThumbnailUrl(obj.getString("Image"));
                        model1.setShopname(obj.getString("ShopName"));
                        model1.setPrice(obj.getString("Price"));
                        model1.setName(obj.getString("ProductName"));
                        model1.setVendorQty(obj.getString("VendorQty"));

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
        protected void onPostExecute(List<ProductModel> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                recyclarviewProduct = (RecyclerView) findViewById(R.id.productlistrv);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclarviewProduct.setLayoutManager(linearLayoutManager);
                recyclarviewProduct.setHasFixedSize(true);
                ProductAdapter rcAda = new ProductAdapter(getApplicationContext(), detailsModels1);
                recyclarviewProduct.setAdapter(rcAda);

                // dialog1.dismiss();
            }else{

                recyclarviewProduct.setAdapter(null);


            }

//dialog.dismiss();
        }

    }


    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>

    {
        private List<ProductModel> itemList;
        private Context context;
        DbHelper helper;

        public ProductAdapter(Context context, List<ProductModel> itemList) {
            this.context = context;
            this.itemList = itemList;
            helper = new DbHelper(context);
        }


        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_prodctlist, parent, false);
            ProductAdapter.ViewHolder prod = new ProductAdapter.ViewHolder(layoutview);
            return prod;
        }

        @Override
        public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
            holder.id.setText(itemList.get(position).getid());
            holder.name.setText(itemList.get(position).getName());
            holder.shopname.setText(itemList.get(position).getShopname());
            holder.price.setText(itemList.get(position).getprice());
            holder.pimagetext.setText(itemList.get(position).getThumbnailUrl());
            holder.vendorqty.setText(itemList.get(position).getVendorQty());
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.prodimg);

        }


        @Override
        public int getItemCount() {
            return this.itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, id, price, shopname,pimagetext,vendorqty;
            ImageView prodimg, checkin, checkout;
            Integer pricevalue,vendorqant;
            public ViewHolder(View itemView) {

                super(itemView);

                prodimg = (ImageView) itemView.findViewById(R.id.prodimg);
                name = (TextView) itemView.findViewById(R.id.prodname);
                id = (TextView) itemView.findViewById(R.id.pid);
                shopname = (TextView) itemView.findViewById(R.id.shopname);
                price = (TextView) itemView.findViewById(R.id.sellprice);
                checkin = (ImageView) itemView.findViewById(R.id.checkin);
                checkout = (ImageView) itemView.findViewById(R.id.checkout);
                pimagetext = (TextView) itemView.findViewById(R.id.prdimage);
                vendorqty = (TextView) itemView.findViewById(R.id.vendorqty);

                mCartList = helper.getResults();
                if (mCartList != null) {

                    for (ShoppingCartResults p : mCartList) {
                        int pricesql = p.getprice();
                        int qtysql = p.getqty();
                        // subTotal += pricesql * 1;
                        // totalqty += subTotal;
                        //totalval.setText(totalqty + "");
                    }

                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shopprdid = id.getText().toString();
                        Intent in = new Intent(view.getContext(), ProductDetails.class);
                        in.putExtra("shopprdid", shopprdid);
                        in.putExtra("qty", "1");
                        in.putExtra("pimage", pimagetext.getText().toString());

                        view.getContext().startActivity(in);


                    }
                });
                checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                        Intent incheck = new Intent(v.getContext(), Checkin.class);
                        incheck.putExtra("shoppid", id.getText().toString());
                        incheck.putExtra("shopid","0");
                        v.getContext().startActivity(incheck);

                    }
                });
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                        boolean value = helper.CheckIsDataAlreadyInDBorNot(id.getText().toString());
                        if (value != true) {
                            pricevalue = Integer.parseInt(price.getText().toString());
                            vendorqant=Integer.parseInt(vendorqty.getText().toString());
                            System.out.println("vendq--"+vendorqant);
                            String pimageval = pimagetext.getText().toString();
                            helper.insert(id.getText().toString(), 1, pricevalue,vendorqant, name.getText().toString(), pimageval, shopname.getText().toString());
                            Toast.makeText(SearchActivity.this, "success", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(SearchActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(Subcategory.this,"already exists",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}








/*autoCompView.addTextChangedListener(new TextWatcher() {

          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int a, int b, int c) {
              if (a == 3) {
                  Toast.makeText(getApplicationContext(), "Maximum Limit Reached" + searchTerm + "", Toast.LENGTH_SHORT).show();
              }
          }

          @Override
          public void afterTextChanged(Editable editable) {


          }
      });*/