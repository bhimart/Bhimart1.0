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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginPage extends AppCompatActivity {
EditText phone,password;
    String uphone,passw;
    Button submit;
    TextView forgot;
    ProgressDialog dialog;
    private String urlParameters;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    JSONArray arr = null;
    String customerid,userphone,passwordva;
    LoginSessionManager loginSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone=(EditText)findViewById(R.id.phonenumber);
        password=(EditText)findViewById(R.id.pass);
        submit=(Button) findViewById(R.id.submit);
        forgot=(TextView) findViewById(R.id.forgot);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (phone.getText().toString().length() > 10 ) {
                    phone.setError("Please Enter valid phone");
                } else if (password.getText().toString().length() ==0) {
                    password.setError("Please Enter Your Valid password");

                }else{
                    cd = new ConnectionDetector(getApplicationContext());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (!isInternetPresent) {
                        showAlertDialog(LoginPage.this, "No Internet Connection", "You don't have internet connection.", false);
                    } else {



                        uphone=phone.getText().toString();
                        passw=password.getText().toString();




                       loginval tasklog = new loginval();

                        tasklog.execute("http://quotecp.com:444/api/Login");
                    }




                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent frgt=new Intent(LoginPage.this,Forgotpass.class);
                startActivity(frgt);
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


    private class loginval extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginPage.this, "Loading",
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
                                "&Password=" + URLEncoder.encode(passw ,"UTF-8");





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

                        Intent fp=new Intent(getApplicationContext(),Homepage.class);
                        startActivity(fp);
                        //   finish();
                        customerid=obj.getString("CustomerID");
                        userphone= obj.getString("UserName");
                        passwordva= obj.getString("Password");
                        loginSessionManager = new LoginSessionManager(getApplicationContext());
                        loginSessionManager.createLoginSession(userphone, customerid,passwordva);



                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Registration was not successfully done, please try again", Toast.LENGTH_LONG).show();

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

        Intent in1 = new Intent(LoginPage.this, Homepage.class);
        startActivity(in1);


    }


}
