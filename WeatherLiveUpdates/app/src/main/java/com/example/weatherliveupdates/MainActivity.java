package com.example.weatherliveupdates;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText etCity, etCountry;
    TextView tvResults;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appId = "057994b1fc99d3a46a70e02cb4f0c420";
    DecimalFormat df = new DecimalFormat("#.##"); // to get two decimal places


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResults = findViewById(R.id.tvResult);

    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.equals("")) {
            tvResults.setText("City field can not be empty");
        }else{
            if(!country.equals("")) {
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appId;
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appId;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("GDResponse", response);
                    //String outPut = "";
                    String mainOutPut = "";

                    StringBuilder ss = new StringBuilder();
                    try {
                        JSONObject jsonObjectResponse = new JSONObject(response); // convert ouput into Json object
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonObjectResponse.getJSONObject("main");

                        double temperature = jsonObjectMain.getDouble("temp")  - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");


                        JSONObject jsonObjectWind = jsonObjectResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");

                        JSONObject jsonObjectClouds = jsonObjectResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        Log.i("GDU", "test");

                        JSONObject jsonObjectSys = jsonObjectResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonObjectResponse.getString("name");

                        tvResults.setTextColor(Color.rgb(68,134,199));
                        // concatenate the necessary variables properly
                        mainOutPut += "Current Weather of " + cityName + "(" + countryName + ")"
                                + "\nTemperature: " + df.format(temperature) + " °C"
                                + "\nFeels like: " + feelsLike + "%"
                                + "\nDescription: " + description
                                + "\nWind speed: " + wind + "m/s (meters per second)"
                                + "\nCloudiness: " + clouds + "%"
                                + "\nHumidity: " + humidity
                                + "\nPressure: " + pressure + "hpa";

                        tvResults.setText(mainOutPut);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            // add the request to the request queue
            requestQueue.add(stringRequest);

        }

    }
}