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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import SessionManager.LoginSessionManager;

public class ProfileDetails extends AppCompatActivity
{

    ConnectionDetector cd;
    final Context context = this;
    private Boolean isInternetPresent = false;
    ProgressDialog dialog;
    JSONArray array = null;

    LoginSessionManager loginSessionManager;

    TextView usernameTv,mobileTv,mailidTv,dobTv,genderTv;
    ImageView profilepicImg;

    Button editBt;
    String userimgtext,customeridtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        usernameTv = (TextView)findViewById(R.id.username);
        profilepicImg = (ImageView)findViewById(R.id.nav_prof_img_id);
        mobileTv   = (TextView)findViewById(R.id.phone);
        mailidTv   = (TextView)findViewById(R.id.mailid);
        genderTv   = (TextView)findViewById(R.id.gender);
        dobTv      = (TextView) findViewById(R.id.dob);

        editBt      = (Button)findViewById(R.id.edit);

        try
        {
            loginSessionManager = new LoginSessionManager(getApplicationContext());
            HashMap<String, String> user = loginSessionManager.getUserDetails();
            customeridtext = user.get(LoginSessionManager.KEY_ID);
            
            if(loginSessionManager.checkLogin())
            {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Your Details Are  Not Yet Registered");
                alertDialogBuilder
                        .setMessage("Press Ok To Register your Details")
                        .setCancelable(false);

                alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
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

            }
            else
            {

                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) 
                {

                    ProfileData Detailstask = new ProfileData();
                   
                    /*api to get profile details*/
                    Detailstask.execute("http://quotecp.com:444/api/Myprofile/Profile/" + customeridtext);
                    
                } 
                else 
                {
                    showAlertDialog(ProfileDetails.this, "No Internet Connection", "You don't have internet connection.", false);
                }


            }

        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        editBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               
                Intent editvalue=new Intent(ProfileDetails.this,Edit_profile.class);
                editvalue.putExtra("name",usernameTv.getText().toString());
                editvalue.putExtra("ph",mobileTv.getText().toString());
                editvalue.putExtra("dob",dobTv.getText().toString());
                editvalue.putExtra("mail",mailidTv.getText().toString());
                editvalue.putExtra("gender",genderTv.getText().toString());
                editvalue.putExtra("image",userimgtext);
                
                startActivity(editvalue);
            }
        });
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        alertDialog.show();
    }

    public class ProfileData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = ProgressDialog.show(ProfileDetails.this, "Loading",
                    "Please wait...", true);
            dialog.show();


        }

        @Override
        protected String doInBackground(String... urls)
        {


            URL url;
            HttpURLConnection connection = null;
            try {

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
             

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
               
                while ((line = rd.readLine()) != null)
                
                {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {

                e.printStackTrace();
                return null;

            } finally
            {

                if (connection != null)
                {
                    connection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {

            try {


                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    if(obj!=null) 
                    {
                        usernameTv.setText(obj.getString("Name"));
                        mobileTv.setText(obj.getString("PhoneNumber"));
                        mailidTv.setText(obj.getString("EmailID"));
                        dobTv.setText(obj.getString("DOB"));
                        genderTv.setText(obj.getString("Gender"));


                        userimgtext = obj.getString("Image");

                        Glide.with(ProfileDetails.this)
                                .load(userimgtext)
                                .into(profilepicImg);
                        
                    } 
                    else
                    {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                    }
                }
            }catch(JSONException e)
            {
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
