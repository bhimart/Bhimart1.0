package sample.com.closebuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class signlogin extends AppCompatActivity {
Button signupBtn,loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signlogin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        signupBtn=(Button)findViewById(R.id.signup);
        loginBtn=(Button)findViewById(R.id.login);

        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent sign=new Intent(signlogin.this,Registration.class);
                startActivity(sign);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent sign=new Intent(signlogin.this,LoginPage.class);
               startActivity(sign);
           }
       });

    }

}
