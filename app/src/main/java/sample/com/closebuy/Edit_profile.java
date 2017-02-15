package sample.com.closebuy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;

import SessionManager.LoginSessionManager;

public class Edit_profile extends AppCompatActivity {
    Button submit,Phno;

    LoginSessionManager loginSessionManager;
    RadioButton radio;
    static final int DATE_PICKER_ID = 0;
    String idString,namestring,phstring;
    Button edit;
    RadioGroup vacciRadioGroup, breedRadioGroup;
    RadioButton vacciRadiomale, vacciRadiofemale, breedRadioYes, breedRadioNo, vacciRadioButton, breedRadioButton;
    String name,phno,dob1,email1;
    EditText uname,uphone,dob,email,address;
    private int day, month, year;
    String formattedMonth;
    ProgressDialog dialog;
    private String urlParameters;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    JSONArray arr = null;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String tgender_string;
    ImageView profileimg;
    private int CAMERA_PERMISSION_CODE = 4;
    private int GALLERY_PERMISSION_CODE = 5;
    static String encodedImage;
    private int PICK_IMAGE_REQUEST = 3;
    private Bitmap bitmap;
    private Uri filePath;
    RadioButton rmale,rfemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loginSessionManager = new LoginSessionManager(getApplicationContext());
        HashMap<String, String> user = loginSessionManager.getUserDetails();
        uname = (EditText) findViewById(R.id.name);
        uphone = (EditText) findViewById(R.id.phno);
        idString = user.get(LoginSessionManager.KEY_ID);

        phstring = user.get(LoginSessionManager.KEY_PHONE);

        email = (EditText) findViewById(R.id.email);
        dob = (EditText) findViewById(R.id.dob);
        submit = (Button) findViewById(R.id.submit);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        profileimg = (ImageView) findViewById(R.id.profileimg);
        rmale=(RadioButton)findViewById(R.id.rmale);
        rfemale=(RadioButton)findViewById(R.id.rfemale);
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(0);
        cd = new ConnectionDetector(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        String nameValue = extras.getString("name");
        String mobiValue = extras.getString("ph");
        String mailValue = extras.getString("mail");
        String dobValue=extras.getString("dob");
        final String imgValue = extras.getString("image");
       String genderValue=extras.getString("gender");
        //   String addrValue = extras.getString("addr");
        uname.setText(nameValue);
        uphone.setText(mobiValue);
        email.setText(mailValue);
        dob.setText(dobValue);
        Picasso.with(Edit_profile.this)
                .load(imgValue)
                .into(profileimg);
        // im1 = obj.getString("image");.
        try {
           /* profileimg.buildDrawingCache();
            Bitmap bm = profileimg.getDrawingCache();*/
            Bitmap   bm = ((BitmapDrawable) profileimg.getDrawable()).getBitmap();
            int nh = (int) (bm.getHeight() * (256.0 / bm.getWidth()));
            Bitmap scaled1 = Bitmap.createScaledBitmap(bm, 256, nh, true);
            System.out.println("ser---" + bm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            scaled1.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }catch(RuntimeException e){
            e.printStackTrace();
        }

        if (genderValue.equalsIgnoreCase("MALE")) {
            rmale.setChecked(true);
        } else if (genderValue.equalsIgnoreCase("FEMALE")) {
            rfemale.setChecked(true);
        } else {
            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
        }

        //  uname.setText(namestring);
        //  uphone.setText(phstring);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent sign = new Intent(UpdateUserProfile.this, Homepage.class);
                startActivity(sign);*/
                if (uname.getText().toString().length() == 0) {
                    uname.setError("Please Enter Your Name");
                } else if (uphone.getText().toString().length() < 10) {
                    uphone.setError("Please Enter Your Valid Mobile Number");

                }else if(dob.getText().toString().length() == 0){
                    dob.setError("Please Enter Your Valid date of birth");

                }else if(email.getText().toString().length() == 0) {
                    email.setError("Please Enter Your Valid email");

                }  else if (profileimg.getDrawable() == null ) {

                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
                }else {
                    cd = new ConnectionDetector(getApplicationContext());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (!isInternetPresent) {
                        showAlertDialog(Edit_profile.this, "No Internet Connection", "You don't have internet connection.", false);
                    } else {


                        email1 = email.getText().toString().trim();
                        namestring=uname.getText().toString();
                        dob1 = dob.getText().toString();
                        phstring=uphone.getText().toString();
                        int selectedId1 = radioSexGroup
                                .getCheckedRadioButtonId();

                        radioSexButton = (RadioButton) findViewById(selectedId1);
                        tgender_string = radioSexButton.getText().toString();


                        editprofile task = new editprofile();

                        task.execute(new String[]{"http://quotecp.com:444/api/UpdateProfile"});
                    }

                }
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });


        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Edit_profile.this);
                alertDialog.setTitle("Select option");
                alertDialog.setMessage("Please upload your  photo..");
/*********Camera********/
                // alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (isCameraAllowed()) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, 2);
//                            Toast.makeText(mContext, "got permision already", Toast.LENGTH_SHORT).show();
                        } else {

                            //If the app has not the permission then asking for the permission
                            requestCameraPermission();
                        }
                    }
                });
/*********Gallery********/
                alertDialog.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (isReadStorageAllowed()) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                        } else {
                            //If the app has not the permission then asking for the permission
                            requestStoragePermission();
                        }
                    }
                });
                alertDialog.show();
            }
        });


    }

    public void showAlertDialog(Context context, String title, String message, Boolean status)
    {
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                int nh = (int) (bitmap.getHeight() * (256.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 256, nh, true);
                profileimg.setImageBitmap(scaled);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.PNG, 90, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");

            profileimg.setImageBitmap(photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
    }
    private boolean isReadStorageAllowed() {
        //Getting the permission status

        int result1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result1 == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
    }

    private boolean isCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == GALLERY_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Sorry we cannot Take Image without permission", Toast.LENGTH_LONG).show();
            }
        }


        if (requestCode == CAMERA_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 2);
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Sorry we cannot Take Image without permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // set date picker as current date
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth+1;
            day = selectedDay;

            if (month< 10)
            {
                formattedMonth = "0"+month;
            }
            else
            {
                formattedMonth=String.valueOf(month);
            }
            dob.setText(new StringBuilder().append(day)
                    .append("-").append(formattedMonth).append("-").append(year)
                    .append(" "));

        }
    };

    private class editprofile extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Edit_profile.this, "Loading",
                    "Please wait...", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("s---"+idString);
            URL url;
            HttpURLConnection connection = null;
            try {
                urlParameters = "&CustomerID=" + URLEncoder.encode(idString, "UTF-8") +
                        "&CustomerName=" + URLEncoder.encode(namestring, "UTF-8") +
                        "&PhoneNumber=" + URLEncoder.encode(phstring, "UTF-8") +
                        "&EmailID=" + URLEncoder.encode(email1, "UTF-8") +
                        "&DOB="  + URLEncoder.encode(dob1, "UTF-8") +
                        "&Gender=" + URLEncoder.encode(tgender_string ,"UTF-8")+
                        "&ImageBase64=" +   URLEncoder.encode(encodedImage ,"UTF-8");                                                   ;



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

                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();

                        Intent fp=new Intent(getApplicationContext(),Profiledetails.class);
                        startActivity(fp);
                        //   finish();
                        Log.d("result++++", result);



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




}


