package sample.com.closebuy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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

import SessionManager.LoginSessionManager;

public class Forgotpass extends AppCompatActivity {
EditText phone,pass,conpass;
    Button submit;
    ProgressDialog dialog;
    private String urlParameters;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    JSONArray arr = null;
    String fpass1,uphone;
    LoginSessionManager loginSessionManager;
    String customerid,userphone,passwordva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone=(EditText)findViewById(R.id.phno);
        pass=(EditText)findViewById(R.id.password);
        conpass=(EditText)findViewById(R.id.confirmpassword);
        submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String con=conpass.getText().toString();
                String pas=pass.getText().toString();
                if (phone.getText().toString().length() < 10) {
                    phone.setError("Please Enter Your Valid Mobile Number");




                }else if(!con.equals(pas)){

                    conpass.setError("Please Enter correct password");

                }

                else{

                    cd = new ConnectionDetector(getApplicationContext());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (!isInternetPresent) {
                        showAlertDialog(Forgotpass.this, "No Internet Connection", "You don't have internet connection.", false);
                    } else {



                        uphone=phone.getText().toString();
                        fpass1=conpass.getText().toString();

                        forgotpassval taskforgot = new forgotpassval();

                        taskforgot.execute(new String[]{"http://quotecp.com:444/api/ForgotPassword"});
                    }

                }
            }
        });
    }

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
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

    private class forgotpassval extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Forgotpass.this, "Loading",
                    "Please wait...", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection = null;
            try {
                urlParameters =
                        "&UserName=" + URLEncoder.encode(uphone, "UTF-8") +
                                "&Password=" + URLEncoder.encode(fpass1 ,"UTF-8");





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


                System.out.println("url===="+url);

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

                arr = new JSONArray(result);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String state = obj.getString("Status");
                    if (state.equals("Success")) {
                        dialog.dismiss();


                        customerid=obj.getString("CustomerID");
                        userphone= obj.getString("UserName");
                        passwordva= obj.getString("Password");
                        loginSessionManager = new LoginSessionManager(getApplicationContext());
                        loginSessionManager.createLoginSession(userphone, customerid,passwordva);
                        Toast.makeText(getApplicationContext(),"password successfully changed", Toast.LENGTH_LONG).show();
                        Intent fp=new Intent(getApplicationContext(),Homepage.class);
                        startActivity(fp);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"password not changed", Toast.LENGTH_LONG).show();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
            dialog.dismiss();

        }
    }
    @Override
    public void onBackPressed() {

            super.onBackPressed();
        }


}
