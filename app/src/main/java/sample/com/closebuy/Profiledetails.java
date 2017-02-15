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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

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
import java.util.HashMap;

import SessionManager.LoginSessionManager;

public class Profiledetails extends AppCompatActivity {
    ConnectionDetector cd;
    final Context context = this;
    private Boolean isInternetPresent = false;
    private String urlParameters;
    ProgressDialog dialog;
    JSONArray arr = null;
    String uid;
    LoginSessionManager loginSessionManager;
    TextView name,mobile,mailid,dob,gender;
    ImageView profilepic;
    Button edit;
    String img;
    String state;
    String customerid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name=(TextView)findViewById(R.id.uname);
        profilepic=(ImageView)findViewById(R.id.nav_prof_img_id);
        mobile=(TextView)findViewById(R.id.pno);
        mailid=(TextView)findViewById(R.id.mailid1);
        dob=(TextView)findViewById(R.id.dob);
        edit=(Button)findViewById(R.id.edit);
        gender=(TextView)findViewById(R.id.gender);
        try {
            loginSessionManager = new LoginSessionManager(getApplicationContext());
            HashMap<String, String> user = loginSessionManager.getUserDetails();
            customerid = user.get(LoginSessionManager.KEY_ID);
            if(loginSessionManager.checkLogin()){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Your Details Are  Not Yet Registered");
                alertDialogBuilder
                        .setMessage("Press Ok To Register your Details")
                        .setCancelable(false);
                alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent ix = new Intent(getApplicationContext(), signlogin.class);

                        ix.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ix.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ix.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ix);
                        finish();
                    }
                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }else{

                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    profiledet tasku = new profiledet();
                    tasku.execute("http://quotecp.com:444/api/Myprofile/Profile/" + customerid);



                } else {
                    // Toast.makeText(UserProfileActivity.this,"No Internet connection",Toast.LENGTH_SHORT).show();
                    showAlertDialog(Profiledetails.this, "No Internet Connection", "You don't have internet connection.", false);
                }


            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ed=new Intent(Profiledetails.this,Edit_profile.class);
                ed.putExtra("name",name.getText().toString());
                ed.putExtra("ph",mobile.getText().toString());
                ed.putExtra("dob",dob.getText().toString());
                ed.putExtra("mail",mailid.getText().toString());
                ed.putExtra("gender",gender.getText().toString());
                ed.putExtra("image",img);
                startActivity(ed);
            }
        });
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //         alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
    public class profiledet extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Profiledetails.this, "Loading",
                    "Please wait...", true);
            dialog.show();


        }

        @Override
        protected String doInBackground(String... urls) {


            URL url;
            HttpURLConnection connection = null;
            try {

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
              /*  connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);*/

                //Send request
                /*DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();*/

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

                    if(obj!=null) {
                        name.setText(obj.getString("Name"));
                        mobile.setText(obj.getString("PhoneNumber"));
                        mailid.setText(obj.getString("EmailID"));
                        dob.setText(obj.getString("DOB"));
                        gender.setText(obj.getString("Gender"));
                        img = obj.getString("Image");
                         Glide.with(Profiledetails.this)
                                .load(img)
                                .into(profilepic);


                        // String state=obj.getString("status");
                      /*  if(state.equalsIgnoreCase("0")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            alertDialogBuilder.setTitle("Your Details Are  Not Yet Registered");
                            alertDialogBuilder
                                    .setMessage("Press Ok To Register your Details")
                                    .setCancelable(false);
                            alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent ix = new Intent(getApplicationContext(), signlogin.class);

                                    ix.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ix.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    ix.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(ix);
                                    finish();
                                }
                            }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }*/

                    }





                    else {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                        //       Toast.makeText(getApplicationContext(),"Please Try Again After Somtime",Toast.LENGTH_LONG).show();
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
           dialog.dismiss();
        }

    }
    @Override
    public void onBackPressed()
    {
        Intent back=new Intent(this,Homepage.class);
        startActivity(back);
        finish();
    }
}
