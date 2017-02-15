package sample.com.closebuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;

import Adapters.DbHelper;
import Adapters.Topproducts;
import SessionManager.seekbarsession;
import pojo.ShoppingCartResults;
import pojo.getsubcategory;
import pojo.prodlist;
import pojo.products;

public class Subcategory extends AppCompatActivity {
    JSONArray arr = null, arr2 = null, arr3 = null;
    private Boolean Isinternetpresent = false;
    ConnectionDetector cd;
    private String urlParameters;
    ProgressDialog dialog, dialog1;
    String id,lat,lang;
    RecyclerView recylcesubcat, recylceprodlist;
    String subcat;
    ArrayList<Integer> totalcartvalues = new ArrayList<>();
    private List<ShoppingCartResults> mCartList;
    DbHelper helper;
    Double subTotal = 0.0;
    TextView totalval;
    LinearLayout bottomLayout;
    String qty = "1";
    Button continu;
    int tot, totalvalue = 0, totalqty;
     ImageView noprod;
    String range;
    seekbarsession seekbarsession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });
      //  totalval = (TextView) findViewById(R.id.tot);
     //   continu = (Button) findViewById(R.id.cont);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("categoryid");
        lat=extras.getString("lat");
        lang=extras.getString("lang");
        recylcesubcat = (RecyclerView) findViewById(R.id.subcat);
        recylceprodlist = (RecyclerView) findViewById(R.id.prodlist);
        bottomLayout = (LinearLayout) findViewById(R.id.bot);
        noprod=(ImageView)findViewById(R.id.noprod);
        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();

        if (Isinternetpresent) {

            subcatvalues getsub = new subcatvalues();
            getsub.execute("http://quotecp.com:444/api/topcategory/getsubcategorybyid/" + id);
            subcatwebapis();


        } else {

            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
      /*  mCartList = helper.getResults();
        for (ShoppingCartResults p : mCartList) {
            int pricesql = p.getprice();
            int qtysql = p.getqty();
            subTotal += pricesql * qtysql;
            totalcartvalues.add(qtysql);
        }*/
        if (subTotal > 1) {


            bottomLayout.setVisibility(View.VISIBLE);
            totalval.setVisibility(View.VISIBLE);
        }

        try{
            seekbarsession = new seekbarsession(getApplicationContext());
            HashMap<String, String> user1 = seekbarsession.gettseekdetail();
            String  val = user1.get(seekbarsession.KEY_RANGE);
            if (val == null)
            {

                range="15";
            }else{
                range = user1.get(seekbarsession.KEY_RANGE);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //  total.setText(String.valueOf(subTotal));
/*
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent con = new Intent(Subcategory.this, Yourcart.class);
                startActivity(con);
            }
        });*/
    }


    public void subcatwebapis() {

        subcat = "0";
        productvalues prdsub = new productvalues();
        prdsub.execute("http://quotecp.com:444/api/topcategory/getproductlistcategory");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuprodlist, menu);

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

            Intent in1 = new Intent(Subcategory.this, Yourcart.class);
            startActivity(in1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private class subcatvalues extends AsyncTask<String, String, List<getsubcategory>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Subcategory.this, "Loading", "Please Wait.....");
            dialog.show();
        }

        @Override
        protected List<getsubcategory> doInBackground(String... urls) {
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


                List<getsubcategory> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    getsubcategory model1 = new getsubcategory();
                    if (obj != null && arr.length() > 0) {

                        model1.setId(obj.getString("SubCategoryID"));
                        model1.setName(obj.getString("Name"));
                        model1.setThumbnailUrl(obj.getString("Image"));
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
        protected void onPostExecute(List<getsubcategory> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                recylcesubcat = (RecyclerView) findViewById(R.id.subcat);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                recylcesubcat.setLayoutManager(linearLayoutManager1);
                recylcesubcat.setHasFixedSize(true);
                Subcategory_adapter rcAda = new Subcategory_adapter(getApplicationContext(), detailsModels1);
                recylcesubcat.setAdapter(rcAda);
                dialog.dismiss();
            }else{

                dialog.dismiss();
            }

//dialog.dismiss();
        }


    }

    private class productvalues extends AsyncTask<String, String, List<prodlist>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = ProgressDialog.show(Subcategory.this, "Loading", "Please Wait.....");
            dialog1.show();
        }

        @Override
        protected List<prodlist> doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                urlParameters = "&lati=" + URLEncoder.encode(lat, "UTF-8") +
                        "&longi=" + URLEncoder.encode(lang, "UTF-8") +
                        "&range=" + URLEncoder.encode(range, "UTF-8") +
                        "&categoryid=" + URLEncoder.encode(id, "UTF-8") +
                        "&subcategoryid=" + URLEncoder.encode(subcat, "UTF-8");
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
                    prodlist model1 = new prodlist();
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
        protected void onPostExecute(List<prodlist> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                System.out.println("det1-" + detailsModels1);
                recylceprodlist = (RecyclerView) findViewById(R.id.prodlist);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recylceprodlist.setLayoutManager(linearLayoutManager);
                recylceprodlist.setHasFixedSize(true);
                noprod.setVisibility(View.GONE);
                prodlist_adapter rcAda = new prodlist_adapter(getApplicationContext(), detailsModels1);
                recylceprodlist.setAdapter(rcAda);
                dialog1.dismiss();
            } else {


                recylceprodlist.setAdapter(null);
                noprod.setVisibility(View.VISIBLE);
                dialog1.dismiss();
            }

//dialog.dismiss();
        }


    }

    public class Subcategory_adapter extends RecyclerView.Adapter<Subcategory_adapter.ViewHolder>

    {
        private List<getsubcategory> itemList;
        private Context context;


        public Subcategory_adapter(Context context, List<getsubcategory> itemList) {
            this.context = context;
            this.itemList = itemList;
        }


        @Override
        public Subcategory_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_subcategory, parent, false);
            Subcategory_adapter.ViewHolder sub = new Subcategory_adapter.ViewHolder(layoutview);
            return sub;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.subcatid1.setText(itemList.get(position).getid());
            holder.name.setText(itemList.get(position).getName());
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
        }


        @Override
        public int getItemCount() {
            return this.itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, subcatid1;
            ImageView icon;

            public ViewHolder(View itemView) {

                super(itemView);

                icon = (ImageView) itemView.findViewById(R.id.subcategory);
                name = (TextView) itemView.findViewById(R.id.subnametext);
                subcatid1 = (TextView) itemView.findViewById(R.id.subidvalue);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        subcat = subcatid1.getText().toString();
                        productvalues prdsub = new productvalues();
                        prdsub.execute("http://quotecp.com:444/api/topcategory/getproductlistcategory");

                    }
                });

            }
        }

    }

    public class prodlist_adapter extends RecyclerView.Adapter<prodlist_adapter.ViewHolder>

    {
        private List<prodlist> itemList;
        private Context context;
        DbHelper helper;
        int total;

        public prodlist_adapter(Context context, List<prodlist> itemList) {
            this.context = context;
            this.itemList = itemList;
            helper = new DbHelper(context);
        }


        @Override
        public prodlist_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_prodctlist, parent, false);
            prodlist_adapter.ViewHolder prod = new prodlist_adapter.ViewHolder(layoutview);
            return prod;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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

               /* mCartList = helper.getResults();
                if (mCartList != null) {

                    for (ShoppingCartResults p : mCartList) {
                        int pricesql = p.getprice();
                        int qtysql = p.getqty();
                       // subTotal += pricesql * 1;
                       // totalqty += subTotal;
                        //totalval.setText(totalqty + "");
                    }

                }*/
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


                          /*  mCartList = helper.getResults();
                            for (ShoppingCartResults p : mCartList) {
                                int pricesql = p.getprice();
                                int qtysql = p.getqty();
                                subTotal += pricesql * qtysql;
                                totalqty+=qtysql;
                                totalcartvalues.add(qtysql);*/


                        //   }


                        Intent incheck = new Intent(v.getContext(), Checkin.class);
                        incheck.putExtra("shoppid", id.getText().toString());
                        incheck.putExtra("shopid","0");
                        v.getContext().startActivity(incheck);

                    }
                });
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                     /*   {
                            mCartList = helper.getResults();
                            if (mCartList != null) {
                                for (ShoppingCartResults p : mCartList) {
                                    int pricesql = p.getprice();
                                    int qtysql = p.getqty();
                                    subTotal += pricesql * 1;

                                    totalqty += subTotal;
                                    totalval.setText(totalqty + "");
                                }
                            }else{
                                int priceval = Integer.parseInt(price.getText().toString());
                                tot = priceval * 1;
                                totalvalue = totalvalue + tot;

                                totalval.setText(String.valueOf(totalvalue + ""));


                            }

                           *//* int priceval = Integer.parseInt(price.getText().toString());
                            tot = priceval * 1;
                            totalvalue = totalvalue + tot;

                            totalval.setText(String.valueOf(totalvalue + ""));*//*
                        }
*/

                        boolean value = helper.CheckIsDataAlreadyInDBorNot(id.getText().toString());
                        if (value != true) {
                            pricevalue = Integer.parseInt(price.getText().toString());
                            String pimageval = pimagetext.getText().toString();
                            vendorquant= Integer.parseInt(vendorqty.getText().toString());
                            System.out.println("subvendq--"+vendorquant);
                            helper.insert(id.getText().toString(), 1, pricevalue,vendorquant, name.getText().toString(), pimageval, shopname.getText().toString());
                            Toast.makeText(Subcategory.this, "success", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(Subcategory.this, "Already Exists", Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(Subcategory.this,"already exists",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

}
