package sample.com.closebuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Thankupage extends AppCompatActivity {
TextView ordercodeTv;
    String ordercodetext;
    Button continueBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankupage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        continueBtn=(Button)findViewById(R.id.cont);
        ordercodeTv =(TextView)findViewById(R.id.ordercode);
        Bundle extras = getIntent().getExtras();
        ordercodetext = extras.getString("ocode");

        ordercodeTv.setText("Your Order Code is "+ordercodetext);
        continueBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent seti = new Intent(Thankupage.this,Homepage.class);
                startActivity(seti);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent setIntent = new Intent(Thankupage.this,Homepage.class);
        startActivity(setIntent);

    }
}
