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
import Adapters.ProductlistAdapter;
import pojo.getshopproduct;
import pojo.getshopsubcat;
import pojo.getsubcategory;
import pojo.prodlist;

public class Shopsubcat extends AppCompatActivity {
    String id;
    RecyclerView recylceshopsubcat,shoprecylceprodlist;
    JSONArray arr = null;
    private Boolean Isinternetpresent = false;
    ConnectionDetector cd;
    private String urlParameters;
    ProgressDialog dialog, dialog1;
    String subcattext;
    ImageView noproductImg;
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
        noproductImg = (ImageView)findViewById(R.id.noprod);

        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();

        if (Isinternetpresent)
        {

            shopsubcatvalues getsub = new shopsubcatvalues();
            /* To get shop subcategory */
            getsub.execute("http://quotecp.com:444/api/ShopProductDetails/GetShopSubCategory/" + id);

            subcattext = "0";
            shopproductvalues prdsub = new shopproductvalues();
            /*TO get productdetails*/
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
        protected void onPostExecute(List<getshopsubcat> shopsubcategory) {

            super.onPostExecute(shopsubcategory);
//
            if (shopsubcategory != null && shopsubcategory.size() > 0) {
                System.out.println("det1-" + shopsubcategory);
                recylceshopsubcat = (RecyclerView) findViewById(R.id.shopsubcat);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                recylceshopsubcat.setLayoutManager(linearLayoutManager1);
                recylceshopsubcat.setHasFixedSize(true);
                Shopsubcategory_adapter rcAda = new Shopsubcategory_adapter(getApplicationContext(), shopsubcategory);
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


        public Shopsubcategory_adapter(Context context, List<getshopsubcat> itemList)
        {
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

                          subcattext = subcatid1.getText().toString();
                         shopproductvalues prdsub = new shopproductvalues();
                          prdsub.execute("http://quotecp.com:444/api/ShopProductDetails");

                    }
                });

            }
        }

    }

    private class shopproductvalues extends AsyncTask<String, String, List<prodlist>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = ProgressDialog.show(Shopsubcat.this, "Loading", "Please Wait.....");
            dialog1.show();
        }

        @Override
        protected List<prodlist> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                urlParameters = "&ShopID=" + URLEncoder.encode(id, "UTF-8") +
                          "&SubCategoryID=" + URLEncoder.encode(subcattext, "UTF-8");
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


                List<prodlist> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    prodlist shoproduct = new prodlist();
                    if (obj != null && arr.length() > 0) {

                        shoproduct.setId(obj.getString("ShopProductID"));
                        shoproduct.setName(obj.getString("ProductName"));
                        shoproduct.setShopname(obj.getString("ShopName"));
                        shoproduct.setPrice(obj.getString("Price"));
                        shoproduct.setThumbnailUrl(obj.getString("Image"));
                        shoproduct.setVendorQty(obj.getString("VendorQty"));
                        itemModelList1.add(shoproduct);
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
        protected void onPostExecute(List<prodlist> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                shoprecylceprodlist= (RecyclerView) findViewById(R.id.shopprodlist);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                shoprecylceprodlist.setLayoutManager(linearLayoutManager);
                shoprecylceprodlist.setHasFixedSize(true);
                noproductImg.setVisibility(View.GONE);
                ProductlistAdapter rcAda = new ProductlistAdapter(getApplicationContext(), detailsModels1);
                shoprecylceprodlist.setAdapter(rcAda);
                dialog1.dismiss();
            } else {


                shoprecylceprodlist.setAdapter(null);
                noproductImg.setVisibility(View.VISIBLE);
                dialog1.dismiss();
            }

//dialog.dismiss();
        }
    }

    }




