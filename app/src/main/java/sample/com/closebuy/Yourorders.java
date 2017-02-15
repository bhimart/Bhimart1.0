package sample.com.closebuy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.yourorderlist;
import SessionManager.LoginSessionManager;
import pojo.getsubcategory;
import pojo.urorder;

public class Yourorders extends AppCompatActivity {
RecyclerView orderlist;
    ProgressDialog dialog;
    JSONArray arr = null;
    private Boolean Isinternetpresent = false;
    ConnectionDetector cd;
    private String urlParameters;
    LoginSessionManager loginSessionManager;
    String cusid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourorders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderlist = (RecyclerView) findViewById(R.id.yourorderls);
        loginSessionManager = new LoginSessionManager(getApplicationContext());
        HashMap<String, String> user = loginSessionManager.getUserDetails();
        cusid = user.get(LoginSessionManager.KEY_ID);
        cd = new ConnectionDetector(getApplicationContext());
        Isinternetpresent = cd.isConnectingToInternet();
        if (Isinternetpresent)
        {

            orderlist order=new orderlist();
          order.execute("http://quotecp.com:444/api/YourOrder/GetYourOrder/" +cusid);


        } else
        {

            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }



    }
    private class orderlist extends AsyncTask<String, String, List<urorder>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Yourorders.this, "Loading", "Please Wait.....");
            dialog.show();
        }

        @Override
        protected List<urorder> doInBackground(String... urls) {
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


                List<urorder> itemModelList1 = new ArrayList<>();

                arr = new JSONArray(finalJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    urorder model1 = new urorder();
                    if (obj != null && arr.length() > 0) {

                        model1.setProdname(obj.getString("ProductName"));
                        model1.setorderid(obj.getString("OrderID"));
                        model1.setqty(obj.getString("Quantity"));
                        model1.settotalprice(obj.getString("TotalPrice"));
                        model1.setShopname(obj.getString("SellerName"));
                        model1.setThumbnailUrl(obj.getString("ProductImage"));
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
        protected void onPostExecute(List<urorder> detailsModels1) {

            super.onPostExecute(detailsModels1);
//
            if (detailsModels1 != null && detailsModels1.size() > 0) {
                orderlist = (RecyclerView) findViewById(R.id.yourorderls);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext()/*, LinearLayoutManager.VERTICAL, false*/);
                linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
                orderlist.setLayoutManager(linearLayoutManager1);
                orderlist.setHasFixedSize(true);
                yourorderlist rcAda = new yourorderlist(getApplicationContext(), detailsModels1);
                orderlist.setAdapter(rcAda);

            }

         dialog.dismiss();
        }


    }

}
