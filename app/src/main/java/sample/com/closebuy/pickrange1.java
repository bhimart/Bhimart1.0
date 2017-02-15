package sample.com.closebuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;

import SessionManager.LoginSessionManager;
import SessionManager.seekbarsession;

public class pickrange1 extends AppCompatActivity {
    private SeekBar seekbar = null;
    TextView seekbarMinvalue;
    String range;
    seekbarsession seekbarsession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickrange1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seekBarCall();
    }

    private void seekBarCall() {

        seekbar = (SeekBar) findViewById(R.id.rangeSeekbar1);
        seekbarMinvalue = (TextView) findViewById(R.id.textMin1);

        try {
            seekbarsession = new seekbarsession(getApplicationContext());
            HashMap<String, String> user = seekbarsession.gettseekdetail();
            String val=user.get(seekbarsession.KEY_RANGE);

            if (val != null) {
                range = user.get(seekbarsession.KEY_RANGE);
                seekbar.setProgress(Integer.parseInt(range));
                seekbarMinvalue.setText(String.valueOf(range));
            }else{
                seekbar.setProgress(15);
                seekbarMinvalue.setText("15");

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 15;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    seekbarsession = new seekbarsession(getApplicationContext());
                    HashMap<String, String> user = seekbarsession.gettseekdetail();

                    if (user != null) {

                        seekbarsession.clear();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                progressChanged = progress;
                seekbarMinvalue.setText(String.valueOf(progressChanged));
                range = seekbarMinvalue.getText().toString();
                System.out.println("seekbar--"+range);
                seekbarsession.createsession(range);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onBackPressed(){
       Intent in =new Intent(pickrange1.this,Homepage.class);
        startActivity(in);
        finish();

    }
}
