package sample.com.closebuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class pickrange extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<String> list;
    Spinner droprange;
    String range;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickrange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        droprange=(Spinner)findViewById(R.id.droprange);
        list = new ArrayList<String>();
        list.add("20km");
        list.add("40km");
        list.add("60km");
        list.add("80km");
        list.add("100km");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        droprange.setAdapter(dataAdapter);
        droprange.setOnItemSelectedListener(this);
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        range= (String) droprange.getSelectedItem();
        Intent intent=new Intent(pickrange.this,Homepage.class);
        intent.putExtra("rangeval", range);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
