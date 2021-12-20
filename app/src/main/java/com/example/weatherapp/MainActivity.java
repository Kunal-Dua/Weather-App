package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycleView;
    ArrayList<weatherModel> data;
    weatherModel weatherModel_obj;
    adapter adapter_obj;
    EditText curr_location_ip;
    TextView curr_location_op,temp,overcast,wind_speed;
    ImageView search_icon,icon_weather,background;
    String curr_location,url,cityName;
//    LocationManager locationManager;
//    int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remove tittle bar from activity
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        recycleView=findViewById(R.id.recycleView);
        data=new ArrayList();
        adapter_obj=new adapter(this,data);
        background=findViewById(R.id.backBK);
        curr_location_ip = findViewById(R.id.current_location_ip);
        curr_location_op = findViewById(R.id.current_location_op);
        temp = findViewById(R.id.temp);
        search_icon=findViewById(R.id.search_icon);
        icon_weather=findViewById(R.id.icon_weather);
        overcast=findViewById(R.id.overcast);
        wind_speed=findViewById(R.id.wind_speed);
        recycleView.setAdapter(adapter_obj);
        recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        //At start runs this //Default
        weather_info("https://api.weatherapi.com/v1/forecast.json?key=5f8026c6bad74834b43110528211812&q=Delhi&days=1&aqi=yes&alerts=yes");

        // TODO accessing phone location
        //For accessing phone location
        // NOT WORKING
        // THROWING ERROR
        //

//        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ){
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
//        }
//        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        cityName=getCityName(location.getLongitude(),location.getLatitude());
//        url="https://api.weatherapi.com/v1/current.json?key=5f8026c6bad74834b43110528211812&q="+cityName+"&aqi=yes";
//        weather_info(url);



        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr_location = curr_location_ip.getText().toString();
                url="https://api.weatherapi.com/v1/forecast.json?key=5f8026c6bad74834b43110528211812&q="+curr_location+"&days=1&aqi=yes&alerts=yes";
                weather_info(url);
            }
        });
    }
    public void weather_info(String url){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //clearing array for multiple requests
                        data.clear();
                        try {
                            Log.d("myapp", "running request");
                            curr_location_op.setText(response.getJSONObject("location").getString("name"));
                            temp.setText((response.getJSONObject("current").getString("temp_c")).concat(" °C"));
                            String condition_icon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.with(MainActivity.this).load("https:".concat(condition_icon)).into(icon_weather);
                            overcast.setText(response.getJSONObject("current").getJSONObject("condition").getString("text"));
                            wind_speed.setText("Wind Speed "+response.getJSONObject("current").getString("wind_kph"));

                            int is_day=response.getJSONObject("current").getInt("is_day");
                            if(is_day==1){
                                background.setImageResource(R.drawable.day_bg);
                            }
                            else{
                                background.setImageResource(R.drawable.night_bg);
                            }
                            //Forcast
                            JSONObject forecast_obj=response.getJSONObject("forecast");
                            JSONObject forcast=forecast_obj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourarray=forcast.getJSONArray("hour");

                            for(int i=0;i<hourarray.length();i++){
                                Log.d("myapp","hourarrary");
                                JSONObject hour_obj=hourarray.getJSONObject(i);
                                String time=hour_obj.getString("time");
                                String temper=hour_obj.getString("temp_c");
                                Log.d("myapp",temper);
                                String icon_curr_img= hour_obj.getJSONObject("condition").getString("icon");
                                String wind =hour_obj.getString("wind_kph");
                                Log.d("myapp",wind);
                                Log.d("myapp","before add");
                                data.add(new weatherModel(time,temper,icon_curr_img,wind));
                                Log.d("myapp","end of try block");
                            }
                            adapter_obj.notifyDataSetChanged();
                            Log.d("myapp","after changed");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

//    private String getCityName(double longitude,double latitude){
//        String cityname="Not Found";
//        Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());
//        try {
//            List<Address> addresses=gcd.getFromLocation(latitude,longitude,10);
//            for(Address adr:addresses){
//                if(adr!=null){
//                    String city=adr.getLocality();
//                    if(city!=null && !city.equals("")){
//                        cityname=city;
//                    }
//                    else{
//                        Toast.makeText(MainActivity.this, "city name not found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return cityname;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==PERMISSION_CODE){
//            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(MainActivity.this, "Permission NOT Granted", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
}