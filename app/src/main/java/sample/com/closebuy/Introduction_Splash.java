package sample.com.closebuy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class Introduction_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    CircleProgressBar progress1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introduction__splash);
        progress1 = (CircleProgressBar) findViewById(R.id.progress1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress1.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(Introduction_Splash.this, Homepage.class);
                    startActivity(i);
                    progress1.setVisibility(View.GONE);
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }



