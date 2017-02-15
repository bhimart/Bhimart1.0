package sample.com.closebuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class thankupage extends AppCompatActivity {
TextView ordercode;
    String orderc;
    Button contin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankupage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contin=(Button)findViewById(R.id.cont);
        ordercode=(TextView)findViewById(R.id.ordercode);
        Bundle extras = getIntent().getExtras();
        orderc = extras.getString("ocode");
        ordercode.setText("Your Order Code is "+orderc);
        contin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seti = new Intent(thankupage.this,Homepage.class);
                startActivity(seti);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent setIntent = new Intent(thankupage.this,Homepage.class);
        startActivity(setIntent);

    }
}
