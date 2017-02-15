package sample.com.closebuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import pojo.getshopproduct;
import pojo.getshopsubcat;
import pojo.getsubcategory;
import pojo.prodlist;

public class Shopsubcat extends AppCompatActivity {
    String id;
    RecyclerView recylceshopsubcat,shoprecylceprodlist;
    JSONArray arr = null, arr2 = null, arr3 = null;
    private Boolean Isinternetpresent = false;
    ConnectionDetector cd;
    private String urlParameters;
    ProgressDialog dialog, dialog1;
    String subcat;
    ImageView noprod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopsubcat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("shopid");
        recylceshopsubcat = (RecyclerView) findViewById(R.id.shopsubcat);
        shoprecylceprodlist= (RecyclerView) findViewById(R.id.shopprodlist);
        noprod=(ImageView)findViewById(R.id.noprod);
        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();

        if (Isinternetpresent) {

            shopsubcatvalues getsub = new shopsubcatvalues();
            getsub.execute("http://quotecp.com:444/api/ShopProductDetails/GetShopSubCategory/" + id);
            // subcatwebapis();
            subcat = "0";
            shopproductvalues prdsub = new shopproductvalues();
            prdsub.execute("http://quotecp.com:444/api/ShopProductDetails");

        } else {

            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class shopsubcatvalues extends AsyncTask<String, String, List<getshopsubcat>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Shopsubcat.this, "Loading", "Please Wait.....");
            dialog.show();
        }

        @Override
        protected List<getshopsubcat> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                //  urlParameters = id;
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");


                //   DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                // wr.writeBytes(urlParameters);
                //  wr.flush();
                //   wr.close();
                InputStream stream = connection.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                String finalJson = buffer.toString();


                List<getshopsubcat> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    getshopsubcat model1 = new getshopsubcat();
                    if (obj != null && arr.length() > 0) {

                        model1.setId(obj.getString("SubCategoryID"));
                        model1.setName(obj.getString("Name"));
                        model1.setThumbnailUrl(obj.getString("Image"));
                        model1.setcategoryid(obj.getString("CatogeryID"));
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
        protected void onPostExecute(List<getshopsubcat> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                recylceshopsubcat = (RecyclerView) findViewById(R.id.shopsubcat);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                recylceshopsubcat.setLayoutManager(linearLayoutManager1);
                recylceshopsubcat.setHasFixedSize(true);
                Shopsubcategory_adapter rcAda = new Shopsubcategory_adapter(getApplicationContext(), detailsModels1);
                recylceshopsubcat.setAdapter(rcAda);
                dialog.dismiss();
            } else {

                dialog.dismiss();
            }

//dialog.dismiss();
        }


    }


    public class Shopsubcategory_adapter extends RecyclerView.Adapter<Shopsubcategory_adapter.ViewHolder> {
        private List<getshopsubcat> itemList;
        private Context context;


        public Shopsubcategory_adapter(Context context, List<getshopsubcat> itemList) {
            this.context = context;
            this.itemList = itemList;
        }


        @Override
        public Shopsubcategory_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewshopsubcategory, parent, false);
            Shopsubcategory_adapter.ViewHolder sub = new Shopsubcategory_adapter.ViewHolder(layoutview);
            return sub;
        }

        @Override
        public void onBindViewHolder(Shopsubcategory_adapter.ViewHolder holder, int position) {
            holder.subcatid1.setText(itemList.get(position).getid());
            holder.name.setText(itemList.get(position).getName());
            holder.catid.setText(itemList.get(position).getcategoryid());
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
        }


        @Override
        public int getItemCount() {
            return this.itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, subcatid1, catid;
            ImageView icon;

            public ViewHolder(View itemView) {

                super(itemView);

                icon = (ImageView) itemView.findViewById(R.id.shopsubcategory);
                name = (TextView) itemView.findViewById(R.id.subnametext);
                subcatid1 = (TextView) itemView.findViewById(R.id.shopsubidvalue);
                catid = (TextView) itemView.findViewById(R.id.catid);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                          subcat = subcatid1.getText().toString();
                         shopproductvalues prdsub = new shopproductvalues();
                          prdsub.execute("http://quotecp.com:444/api/ShopProductDetails");

                    }
                });

            }
        }

    }

    private class shopproductvalues extends AsyncTask<String, String, List<getshopproduct>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = ProgressDialog.show(Shopsubcat.this, "Loading", "Please Wait.....");
            dialog1.show();
        }

        @Override
        protected List<getshopproduct> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                urlParameters = "&ShopID=" + URLEncoder.encode(id, "UTF-8") +
                          "&SubCategoryID=" + URLEncoder.encode(subcat, "UTF-8");
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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


                List<getshopproduct> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    getshopproduct model1 = new getshopproduct();
                    if (obj != null && arr.length() > 0) {

                        model1.setId(obj.getString("ShopProductID"));
                        model1.setName(obj.getString("ProductName"));
                        model1.setShopname(obj.getString("ShopName"));
                        model1.setPrice(obj.getString("Price"));
                        model1.setThumbnailUrl(obj.getString("Image"));
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
        protected void onPostExecute(List<getshopproduct> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                shoprecylceprodlist= (RecyclerView) findViewById(R.id.shopprodlist);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                shoprecylceprodlist.setLayoutManager(linearLayoutManager);
                shoprecylceprodlist.setHasFixedSize(true);
                noprod.setVisibility(View.GONE);
                shopprodlist_adapter rcAda = new shopprodlist_adapter(getApplicationContext(), detailsModels1);
                shoprecylceprodlist.setAdapter(rcAda);
                dialog1.dismiss();
            } else {


                shoprecylceprodlist.setAdapter(null);
                noprod.setVisibility(View.VISIBLE);
                dialog1.dismiss();
            }

//dialog.dismiss();
        }
    }
    public class shopprodlist_adapter extends RecyclerView.Adapter<shopprodlist_adapter.ViewHolder>

    {
        private List<getshopproduct> itemList;
        private Context context;
        DbHelper helper;
        int total;

        public shopprodlist_adapter(Context context, List<getshopproduct> itemList) {
            this.context = context;
            this.itemList = itemList;
            helper = new DbHelper(context);
        }


        @Override
        public shopprodlist_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_shopprodlist, parent, false);
            ViewHolder prod = new ViewHolder(layoutview);
            return prod;
        }

        @Override
        public void onBindViewHolder(shopprodlist_adapter.ViewHolder holder, int position) {
            holder.id.setText(itemList.get(position).getid());
            holder.name.setText(itemList.get(position).getName());
            holder.shopname.setText(itemList.get(position).getShopname());
            holder.price.setText(itemList.get(position).getprice() + "");
            holder.pimagetext.setText(itemList.get(position).getThumbnailUrl());
            holder.vendorqty.setText(itemList.get(position).getVendorQty());
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.prodimg);

        }


        @Override
        public int getItemCount() {
            return this.itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, id, price, shopname, pimagetext,vendorqty;
            ImageView prodimg, checkin, checkout;
            Integer pricevalue,vendorquant;


            public ViewHolder(View itemView) {

                super(itemView);

                prodimg = (ImageView) itemView.findViewById(R.id.prodimg);
                name = (TextView) itemView.findViewById(R.id.prodname);
                id = (TextView) itemView.findViewById(R.id.pid);
                shopname = (TextView) itemView.findViewById(R.id.shopname);
                price = (TextView) itemView.findViewById(R.id.sellprice);
                checkin = (ImageView) itemView.findViewById(R.id.checkin);
                checkout = (ImageView) itemView.findViewById(R.id.checkout);
                vendorqty = (TextView) itemView.findViewById(R.id.vendorqty);
                pimagetext = (TextView) itemView.findViewById(R.id.prdimage);


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
                            String pimageval = pimagetext.getText().toString();
                            vendorquant= Integer.parseInt(vendorqty.getText().toString());
                            System.out.println("subvendq--"+vendorquant);
                            helper.insert(id.getText().toString(), 1, pricevalue,vendorquant, name.getText().toString(), pimageval, shopname.getText().toString());
                            Toast.makeText(Shopsubcat.this, "success", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(Shopsubcat.this, "Already Exists", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        }

    }



}
