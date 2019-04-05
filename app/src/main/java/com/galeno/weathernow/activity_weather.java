package com.galeno.weathernow;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class activity_weather extends Activity {
    private List<Weather> weatherList =
            new ArrayList<>();
    private ArrayAdapter<Weather> weatherArrayAdapter;
    private ListView weatherListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherListView =
                findViewById(R.id.weatherListView);
        weatherArrayAdapter =
                new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        Intent it = getIntent();
        String cidade = it.getStringExtra("cida");

        try {
            URL url = montarURL(cidade);
            new GetWeatherTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    getString(R.string.read_error),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private class GetWeatherTask extends
            AsyncTask<URL, Void, Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            weatherArrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(URL... urls) {
            URL endereco = urls[0];
            try {
                HttpURLConnection conn =
                        (HttpURLConnection)
                                endereco.openConnection();
                try(BufferedReader reader =
                            new BufferedReader
                                    (new InputStreamReader(
                                            conn.getInputStream()));){
                    String linha = null;
                    StringBuilder resultado = new StringBuilder("");
                    while ((linha = reader.readLine()) != null){
                        resultado.append(linha);
                    }
                    JSONObject resultadoPrincipal =
                            new JSONObject(resultado.toString());
                    JSONArray list =
                            resultadoPrincipal.getJSONArray("list");
                    weatherList.clear();
                    for (int i = 0; i < list.length(); i++){
                        JSONObject iesimo = list.getJSONObject(i);
                        long dt = iesimo.getLong("dt");
                        JSONObject main =
                                iesimo.getJSONObject("main");
                        double temp_min =
                                main.getDouble("temp_min");
                        double temp_max =
                                main.getDouble("temp_max");
                        int humidity = main.getInt("humidity");
                        JSONArray weather =
                                iesimo.getJSONArray("weather");
                        JSONObject unicoNoWeather =
                                weather.getJSONObject(0);
                        String description =
                                unicoNoWeather.getString("description");
                        String icon =
                                unicoNoWeather.getString("icon");
                        Weather w =
                                new Weather(dt, temp_min, temp_max,
                                        humidity, description, icon);
                        weatherList.add(w);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private URL montarURL (String cidade)
            throws MalformedURLException {
        String endereco =
                getString(
                        R.string.web_service_url,
                        getString(R.string.lang),
                        cidade,
                        getString(R.string.api_key)
                );
        return new URL(endereco);
    }
}
