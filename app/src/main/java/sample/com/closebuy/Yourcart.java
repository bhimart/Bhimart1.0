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
import android.view.LayoutInflater;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.Cart;
import Adapters.DbHelper;
import SessionManager.LoginSessionManager;
import pojo.ShoppingCartResults;
import pojo.cartvalues;

public class Yourcart extends AppCompatActivity implements View.OnClickListener{
    private List<ShoppingCartResults> mCartList;
    DbHelper dbHelper;
    RecyclerView recyclecart;
    LoginSessionManager loginSessionManager;
    int fulltotalprice1 = 0, fulltotalprice2=0;

    TextView totalpriceTv;

    ProgressDialog dialog;
    JSONArray arr = null;
    ConnectionDetector cd;

    private List<String> listshopProductid   = null;
    private List<String> listUnitPrice =  null;
    private List<String> listQuantity  =  null;
    private List<String> lstprdtprice  =  null;

    String  overalltotaltext;
    Button orderbt;
    LinearLayout lin;
    ImageView noprod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourcart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclecart = (RecyclerView) findViewById(R.id.cartrecycle);
        totalpriceTv = (TextView) findViewById(R.id.tot);
        orderbt=(Button)findViewById(R.id.order);
        lin=(LinearLayout) findViewById(R.id.linval);
        noprod=(ImageView)findViewById(R.id.novalues);

        orderbt.setOnClickListener(this);


        cart cartval = new cart();
        cartval.execute();



    }

    @Override
    public void onClick(View v) {
        loginSessionManager = new LoginSessionManager(getApplicationContext());
        HashMap<String, String> user = loginSessionManager.getUserDetails();

        if (loginSessionManager.checkLogin())
        {
            Intent in=new Intent(Yourcart.this,signlogin.class);
            startActivity(in);

        }else
        {


            Intent in=new Intent(Yourcart.this,Orderpage.class);
            in.putExtra("lstshpid",listshopProductid .toString());
            in.putExtra("lstuniprice",listUnitPrice.toString());
            in.putExtra("lstqty",listQuantity.toString());
            in.putExtra("lsteachptotal",lstprdtprice.toString());
            in.putExtra("overalltotal",overalltotaltext);
            startActivity(in);



        }
    }


    private class cart extends AsyncTask<String, String, List<cartvalues>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Yourcart.this, "Loading", "Please Wait...", true);
            dialog.show();
        }

        @Override
        protected List<cartvalues> doInBackground(String... urls) {


            try {
                dbHelper = new DbHelper(Yourcart.this);
                mCartList = dbHelper.getResults();

                listshopProductid  = new ArrayList<>();
                lstprdtprice = new ArrayList<>();
                listUnitPrice = new ArrayList<>();
                listQuantity = new ArrayList<>();


                List<cartvalues> itemModelList = new ArrayList<>();
                for (ShoppingCartResults p : mCartList) {
                    cartvalues model = new cartvalues();

                    int totalprice = p.getprice() * p.getqty();
                    fulltotalprice1 = fulltotalprice1 + totalprice;
                    fulltotalprice2=fulltotalprice1;
                    listshopProductid .add(p.getid());
                    listUnitPrice.add(String.valueOf(p.getprice()));
                    listQuantity.add(String.valueOf(p.getqty()));
                    lstprdtprice.add(String.valueOf(totalprice));




                    System.out.println("total" + fulltotalprice1);
                    model.setProdname(p.getprdname());
                    model.setShopname(p.getshopname());
                    model.setThumbnailUrl(p.getprdimg());
                    model.setPrice(p.getprice());
                    model.setId(p.getid());
                    model.setqty(p.getqty());
                    model.settotalprice(totalprice);
                    model.setVendorQty(p.getvendorqty());
                    itemModelList.add(model);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            totalpriceTv.setText(fulltotalprice1 + "");
                            overalltotaltext=totalpriceTv.getText().toString();
                        }
                    });

                }
                return itemModelList;


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<cartvalues> detailsModels) {

            super.onPostExecute(detailsModels);
            dialog.dismiss();
            if (detailsModels != null && detailsModels.size() > 0)
            {
                noprod.setVisibility(View.GONE);
                lin.setVisibility(View.VISIBLE);
                orderbt.setVisibility(View.VISIBLE);
                recyclecart = (RecyclerView) findViewById(R.id.cartrecycle);
                recyclecart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclecart.setHasFixedSize(true);
                cartval rcAdapter = new cartval(Yourcart.this, detailsModels);
                recyclecart.setAdapter(rcAdapter);

            }else{

                noprod.setVisibility(View.VISIBLE);
                lin.setVisibility(View.GONE);
                orderbt.setVisibility(View.GONE);
            }
        }







    }

    public class cartval extends RecyclerView.Adapter<cartval.ViewHolder> {
        private List<cartvalues> itemList;
        private Context context;
        DbHelper helper;
        int fullprdtotal = 0;

        public cartval(Context context, List<cartvalues> itemList) {
            this.context = context;
            this.itemList = itemList;
            helper = new DbHelper(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cart, null);
            ViewHolder cart = new ViewHolder(layoutview);
            return cart;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.prdid.setText(itemList.get(position).getid());
            holder.prdname.setText(itemList.get(position).getProdname());
            holder.price.setText(itemList.get(position).getprice() + "");
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
            holder.qty.setText(itemList.get(position).getqty() + "");
            holder.prdtotal.setText(itemList.get(position).gettotalprice() + "");
            holder.vendorqty.setText(itemList.get(position).getVendorQty() + "");
            System.out.println("holder---" + holder.prdname.getText().toString());
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView prdname, prdid, price, qty, prdtotal,vendorqty;
            ImageView icon, subtractQty, addQty;
            private int itmQty = 1;
            private int SELECTED_POS;
            String prodid;
            Button remove;
            int tot,tot1;


            public ViewHolder(final View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.prdimg);
                prdname = (TextView) itemView.findViewById(R.id.prdname);
                prdid   = (TextView) itemView.findViewById(R.id.pid);
                price   = (TextView) itemView.findViewById(R.id.prdprice);
                qty     =  (TextView) itemView.findViewById(R.id.qtyval);
                addQty=(ImageView)itemView.findViewById(R.id.add);
                vendorqty=(TextView) itemView.findViewById(R.id.vendorqty);
                remove=(Button) itemView.findViewById(R.id.remove);
                subtractQty=(ImageView)itemView.findViewById(R.id.sub);
                prdtotal=(TextView) itemView.findViewById(R.id.prdtotal);
                itemView.setOnClickListener(this);
                addQty.setOnClickListener(this);
                subtractQty.setOnClickListener(this);
                remove.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v == addQty) {
                    int vend=Integer.parseInt(vendorqty.getText().toString());
                   if(itmQty<vend) {
                       ++itmQty;
                       cartvalues item = itemList.get(SELECTED_POS);
                       item.setqty(itmQty);
                       qty.setText(String.valueOf(itmQty));
                       prodid = prdid.getText().toString();
                       helper.updateqty(prodid, itmQty);


                       tot = (Integer.parseInt(price.getText().toString()) * itmQty);
                       tot1 = (Integer.parseInt(price.getText().toString()) * 1);

                       prdtotal.setText(tot + "");

                       fulltotalprice2 += tot1;
                       System.out.println("full---" + fulltotalprice1);

                       totalpriceTv.setText(fulltotalprice2 + "");
                       overalltotaltext = totalpriceTv.getText().toString();
                   }else{

                       Toast.makeText(v.getContext(),"Exceeds Stock Limit", Toast.LENGTH_SHORT).show();
                   }
                }

                if (v == subtractQty) {
                    if (itmQty > 1) {
                        --itmQty;
                        cartvalues item = itemList.get(SELECTED_POS);
                        item.setqty(itmQty);
                        qty.setText(String.valueOf(itmQty));
                        prodid = prdid.getText().toString();
                        helper.updateqty(prodid, itmQty);
                        tot = (Integer.parseInt(price.getText().toString()) * itmQty);
                        tot1 =  (Integer.parseInt(price.getText().toString()) * 1);
                        prdtotal.setText(tot + "");
                        fulltotalprice2 -= tot1;
                        System.out.println("full---" + fulltotalprice1);
                        totalpriceTv.setText(fulltotalprice2+"");
                        overalltotaltext=totalpriceTv.getText().toString();
                    } else {

                        qty.setText("1");
                    }
                }
                if (v == remove) {
                    prodid = prdid.getText().toString();
                    helper.delete(prodid);
                    Intent in = new Intent(v.getContext(), Yourcart.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(in);

                }
            }

        }

    }


    @Override
    public void onBackPressed() {

            finish();
        }




}