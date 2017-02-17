package sample.com.closebuy;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import Adapters.ProductlistAdapter;
import pojo.ProductModel;
import pojo.ShoppingCartResults;
import pojo.prodlist;


public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView searchtv;
    String searchtext,rangetext;
    private List<ProductModel> list;
    private String urlParameters;
    JSONArray arr = null;
    private RecyclerView recyclarviewProduct;
    TextView seekbarMinvalueTv;
    private SeekBar seekbar = null;
    ImageView closeBtn;
   
    ProgressDialog dialog;
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
            public void onClick(View v)
            {

                onBackPressed();
            }
        });



        searchtv = (AutoCompleteTextView) findViewById(R.id.autoserachitem_tv);
        recyclarviewProduct = (RecyclerView) findViewById(R.id.productlistrv);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        searchtv.setHint("search");

        searchtext = searchtv.getText().toString();
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


    private void seekBarCall(){

        seekbar = (SeekBar) findViewById(R.id.rangeSeekbar1);
        seekbarMinvalueTv = (TextView) findViewById(R.id.textMin1);
        seekbar.setVisibility(View.GONE);
        seekbarMinvalueTv.setVisibility(View.GONE);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 15;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progressChanged = progress;
                seekbarMinvalueTv.setText(String.valueOf(progressChanged));
                Autosuggestproduct autos = new Autosuggestproduct();
                rangetext = seekbarMinvalueTv.getText().toString();
                autos.execute("http://quotecp.com:444/api/ProductSearch");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





        
    }

    private final TextWatcher searchvalue = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(s.length() >= 3)
            {
                searchtext= searchtv.getText().toString();
                Autosuggestproduct autos = new Autosuggestproduct();
                rangetext = "7";
                autos.execute("http://quotecp.com:444/api/ProductSearch");
                seekbar.setVisibility(View.VISIBLE);
                seekbarMinvalueTv.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    private class Autosuggestproduct extends AsyncTask<String, String, List<prodlist>>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<prodlist> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                urlParameters = "&lati=" + URLEncoder.encode(lat, "UTF-8") +
                        "&longi=" + URLEncoder.encode(lan, "UTF-8")+
                        "&range=" + URLEncoder.encode(rangetext, "UTF-8")+
                        "&Term=" + URLEncoder.encode(searchtext, "UTF-8");

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

                List<prodlist> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    prodlist modelproduct = new prodlist();
                    if (obj != null && arr.length() > 0)
                    {
                        modelproduct.setId(obj.getString("ShopProductID"));
                        modelproduct.setThumbnailUrl(obj.getString("Image"));
                        modelproduct.setShopname(obj.getString("ShopName"));
                        modelproduct.setPrice(obj.getString("Price"));
                        modelproduct.setName(obj.getString("ProductName"));
                        modelproduct.setVendorQty(obj.getString("VendorQty"));
                        itemModelList1.add(modelproduct);
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
        protected void onPostExecute(List<prodlist> suggestproduct) {

            super.onPostExecute(suggestproduct);

            if (suggestproduct != null && suggestproduct.size() > 0)
            {

                recyclarviewProduct = (RecyclerView) findViewById(R.id.productlistrv);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclarviewProduct.setLayoutManager(linearLayoutManager);
                recyclarviewProduct.setHasFixedSize(true);
                ProductlistAdapter productvalues = new ProductlistAdapter(getApplicationContext(), suggestproduct);
                recyclarviewProduct.setAdapter(productvalues);
                
            }
            else
            {

                recyclarviewProduct.setAdapter(null);


            }


        }

    }


   /* private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>

    {
        private List<ProductModel> itemList;
        private Context context;
        DbHelper helper;

        public ProductAdapter(Context context, List<prodlist> itemList) {
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
                        } else 
                        {

                            Toast.makeText(SearchActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });
            }
        }
    }*/
}







