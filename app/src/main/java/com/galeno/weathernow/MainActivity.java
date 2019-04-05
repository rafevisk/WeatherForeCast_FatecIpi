package com.galeno.weathernow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<City> cityList = new ArrayList<>();
    private CityArrayAdapter cityArrayAdapter;
    private ListView cityListView;
    private EditText locationEditText;

    private AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEditText = findViewById(R.id.locationEditText);
        cityListView = (ListView) findViewById(R.id.cityListView);
        cityArrayAdapter = new CityArrayAdapter(this,cityList);
        cityListView.setAdapter(cityArrayAdapter);

        FloatingActionButton btAdd = findViewById(R.id.btAdd);
        btAdd.setOnClickListener((v) -> {
            String textCity = locationEditText.getEditableText().toString();
            if(textCity.isEmpty()){
                Snackbar.make (findViewById(R.id.coordinatorLayout), R.string.emptyText, Snackbar.LENGTH_LONG).show();
            }
            else{
                City c = new City(textCity);
                //cityList.clear();
                cityList.add(c);
                cityArrayAdapter.notifyDataSetChanged();
                locationEditText.setText("");
            }
        });

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City c = cityList.get(position);
                String cidade = c.getCity();
                Intent it = new Intent(view.getContext(), activity_weather.class);
                it.putExtra("cida",cidade);
                startActivity(it);
            }
        });
        cityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                criaBotao(position);
                return false;
            }
        });
    }
    public void criaBotao(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.notice));
        builder.setMessage(getText(R.string.messageDialog));
        builder.setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                cityList.remove(position);
                cityArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Faz Nada
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
