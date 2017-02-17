package sample.com.closebuy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.HashMap;

import Adapters.DbHelper;
import SessionManager.LoginSessionManager;

public class Orderpage extends AppCompatActivity {
String lsprid,lstuprice,lsqty,lstotprdprice,lsoverallprice;
    ProgressDialog dialog;
    JSONArray arr = null;
    ConnectionDetector cd;
    private Boolean Isinternetpresent = false;
    private String urlParameters;
    LoginSessionManager loginSessionManager;
    String customerid,phonev;
    Button order;
    EditText name1,ph1,email,address1;
    RadioGroup Radiopay;
    private RadioButton radiopayButton;
    String pay_string,address2;
    String fph,fname,email2;
    String orderid;
    DbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        lsprid = extras.getString("lstshpid");
        System.out.print("lstshop--"+lsprid);
        lstuprice = extras.getString("lstuniprice");
        lsqty = extras.getString("lstqty");
        lstotprdprice = extras.getString("lsteachptotal");
        lsoverallprice = extras.getString("overalltotal");
        Radiopay=(RadioGroup)findViewById(R.id.RadioPay);
        helper = new DbHelper(this);
        loginSessionManager = new LoginSessionManager(getApplicationContext());
        HashMap<String, String> user = loginSessionManager.getUserDetails();
        customerid = user.get(LoginSessionManager.KEY_ID);
        phonev = user.get(LoginSessionManager.KEY_PHONE);
        name1 = (EditText) findViewById(R.id.name1);
        ph1 = (EditText) findViewById(R.id.Phno);
        email = (EditText) findViewById(R.id.email);
        order = (Button) findViewById(R.id.plac);
        address1 = (EditText) findViewById(R.id.address1);
        ph1.setText(phonev);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address1.getText().toString().length() == 0) {
                    address1.setError("Please Enter Your Valid Address");
                } else if (Radiopay.getCheckedRadioButtonId() == -1) {

                    Toast.makeText(Orderpage.this, "please check cash on delivery", Toast.LENGTH_SHORT).show();


                } else if (name1.getText().toString().length() == 0) {

                    name1.setError("Please Enter Your Name");


                } else if (ph1.getText().toString().length() == 0) {
                    ph1.setError("Please Enter Your Phone Number");
                } else if (email.getText().toString().length() == 0) {
                    email.setError("Please Enter Your Valid email");

                } else {
                    cd = new ConnectionDetector(getApplicationContext());
                    Isinternetpresent = cd.isConnectingToInternet();
                    if (!Isinternetpresent) {
                        showAlertDialog(Orderpage.this,
                                "No Internet Connection", "You don't have internet connection.", false);
                    } else {


                        int selectedId1 = Radiopay
                                .getCheckedRadioButtonId();

                        radiopayButton = (RadioButton) findViewById(selectedId1);

                        pay_string = radiopayButton.getText().toString();
                        address2 = address1.getText().toString();

                        fph = ph1.toString();
                        //address = addressText.getText().toString();
                        fname = name1.getText().toString();
                        fph = ph1.getText().toString();
                        email2 = email.getText().toString();


                        sendcartvalues cat = new sendcartvalues();
                        cat.execute("http://quotecp.com:444/api/OrderCart");


                    }


                }
            }
        });
    }

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status)  {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //    alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }


    private class sendcartvalues extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Orderpage.this, "Loading", "Please Wait...", true);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection = null;

            try {



                urlParameters = "&ShopProductID=" + URLEncoder.encode(lsprid, "UTF-8")+
                        "&UnitPrice=" + URLEncoder.encode(lstuprice, "UTF-8")+
                        "&Qty=" + URLEncoder.encode(lsqty, "UTF-8")+
                        "&ProductTotalPrice=" + URLEncoder.encode(lstotprdprice, "UTF-8")+
                        "&CustomerID=" + URLEncoder.encode(customerid, "UTF-8")+
                        "&OverAllPrice=" + URLEncoder.encode(lsoverallprice, "UTF-8");
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

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
            } catch (IOException e) {

                e.printStackTrace();


                return null;

            }


            finally {

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

                    if (state.equals("SUCCESS")) {


                        String ordercode=obj.getString("OrderCode");
                        orderid=obj.getString("OrderID");
                        Toast.makeText(Orderpage.this,ordercode,Toast.LENGTH_SHORT).show();


                        confirmdetails cat = new confirmdetails();
                        cat.execute("http://quotecp.com:444/api/OrderDetails");



                        Intent ord=new Intent(Orderpage.this,Thankupage.class);
                        ord.putExtra("ocode",ordercode);
                        startActivity(ord);
                    }else{

                        Toast.makeText(Orderpage.this,"Not successfull try again",Toast.LENGTH_SHORT).show();


                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }


    }


    private class confirmdetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Orderpage.this, "Loading", "Please Wait...", true);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection = null;

            try {



                urlParameters = "&CustomerID=" + URLEncoder.encode(customerid, "UTF-8")+
                        "&OrderID=" + URLEncoder.encode(orderid, "UTF-8")+
                        "&Name=" + URLEncoder.encode(fname, "UTF-8")+
                        "&EmailID=" + URLEncoder.encode(email2, "UTF-8")+
                        "&PhoneNumber=" + URLEncoder.encode(fph, "UTF-8")+
                        "&Address=" + URLEncoder.encode(address2, "UTF-8")+
                        "&PaymentMethod=" + URLEncoder.encode(pay_string, "UTF-8");
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

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
            } catch (IOException e) {

                e.printStackTrace();


                return null;

            }


            finally {

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


                        helper.deleteAll();
                        Toast.makeText(Orderpage.this,"success",Toast.LENGTH_SHORT).show();







                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }


    }



}
