package com.egon.elweather.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.egon.elweather.R;
import com.egon.elweather.adapter.MainWeatherAdapter;
import com.egon.elweather.fragment.AboutFragment;
import com.egon.elweather.fragment.NoInternetFragment;
import com.egon.elweather.model.DailyWeatherReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String URL_BASE = "https://api.openweathermap.org/data/2.5/forecast";
    final String URL_UNITS = "?units=imperial";
    final String URL_PHIL_COORDINATES = "&lat=12.8797&lon=121.7740";
    final String API_KEY = "&appid=58a6416b2fc55b29f277ba7f90f05cf7";

    private final int PERMISSION_LOCATION = 111;
    private ArrayList<DailyWeatherReport> weatherReportList;
    private DailyWeatherReport dailyReport;
    private RecyclerView weatherRv;
    private TextView currentDate, currentMaxTemp, currentMinTemp, currentCountryName, currentWeatherType, currentWeatherDesc;
    private ImageView currentIcon, mainMenu;
    private String mWeatherType;
    private MainWeatherAdapter mAdapter;
    private ProgressDialog dialog;
    private Handler handler;
    private PopupMenu popupMenu;
    private static int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        castValues();
        setListeners();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    private void setListeners() {
        AppCompatActivity activity = (AppCompatActivity) this;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        dialog.setMessage("Fetching recent weather reports.");
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isConnectedToInternet()) {
                    getWeatherData();
                } else {
                    dialog.dismiss();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    NoInternetFragment noInternetFragment = new NoInternetFragment();
                    fragmentTransaction.replace(R.id.main_framelayout, noInternetFragment);
                    fragmentTransaction.commit();
                }
            }
        }, DELAY);

        mainMenu.setOnClickListener(v -> {
            popupMenu = new PopupMenu(MainActivity.this, mainMenu);
            popupMenu.inflate(R.menu.main_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // switch main_menu items
                    switch (item.getItemId()) {
                        case R.id.about_menu:
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            AboutFragment aboutFragment = new AboutFragment();
                            fragmentTransaction.replace(R.id.main_framelayout, aboutFragment);
                            fragmentTransaction.commit();
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        });
    }

    private void castValues() {
        currentDate = findViewById(R.id.main_current_date);
        currentMaxTemp = findViewById(R.id.main_current_maxTemp);
        currentMinTemp = findViewById(R.id.main_current_minTemp);
        currentIcon = findViewById(R.id.main_current_icon);
        currentCountryName = findViewById(R.id.main_country_name);
        currentWeatherType = findViewById(R.id.main_weather_type);
        currentWeatherDesc = findViewById(R.id.main_weather_description);
        mainMenu = findViewById(R.id.main_menu);
        weatherRv = findViewById(R.id.main_weather_rv);
        weatherReportList = new ArrayList<>();

        dialog = new ProgressDialog(this);
        handler = new Handler();
    }

    private void getWeatherData() {
        final String url = URL_BASE + URL_UNITS + URL_PHIL_COORDINATES + API_KEY;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // get city object
                            JSONObject city = response.getJSONObject("city");
                            String cityName = city.getString("name");
                            String cityCountry = city.getString("country");

                            // list of data
                            JSONArray list = response.getJSONArray("list");

                            for (int x = 0; x < list.length(); x++) {
                                JSONObject obj = list.getJSONObject(x);

                                // main data
                                JSONObject main = obj.getJSONObject("main");
                                Double currentTemp = main.getDouble("temp");
                                Double maxTemp = main.getDouble("temp_max");
                                Double minTemp = main.getDouble("temp_min");

                                // weather obj
                                JSONArray weatherArr = obj.getJSONArray("weather");
                                JSONObject weather = weatherArr.getJSONObject(0);
                                String weatherType = weather.getString("main");
                                String weatherDesc = weather.getString("description");

                                // date data
                                String rawDate = obj.getString("dt_txt");

                                // add to model
                                dailyReport = new DailyWeatherReport(cityName, cityCountry, currentTemp.intValue(), maxTemp.intValue(), minTemp.intValue(), weatherType, weatherDesc, rawDate);

                                // add to arrayList
                                weatherReportList.add(dailyReport);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "jsonRequestError: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        updateUI();
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ELWEATHER", "ERROR: " + error.getLocalizedMessage());
            }
        });

        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
    }

    public void updateUI() {

        DailyWeatherReport reportOfTheDay = weatherReportList.get(0);
        if (weatherReportList.size() > 0) {
            dialog.dismiss();
            mWeatherType = "";
            switch (reportOfTheDay.getWeatherType()) {
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    currentIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                    mWeatherType = "Cloudy";
                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    currentIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                    mWeatherType = "Rainy";
                    break;
                case DailyWeatherReport.WEATHER_TYPE_WIND:
                    currentIcon.setImageDrawable(getResources().getDrawable(R.drawable.windy));
                    mWeatherType = "Windy";
                    break;
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    currentIcon.setImageDrawable(getResources().getDrawable(R.drawable.snowy));
                    mWeatherType = "Snowy";
                    break;
                case DailyWeatherReport.WEATHER_TYPE_CLEAR:
                    currentIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
                    mWeatherType = "Sunny";
                    break;
            }

            currentDate.setText("Today " + reportOfTheDay.getFormattedDate());
            currentMaxTemp.setText(reportOfTheDay.getCurrentTemp()+ "°");
            currentMinTemp.setText(reportOfTheDay.getMinTemp() + "°");
            currentCountryName.setText("" + reportOfTheDay.getCityName() + ", " + reportOfTheDay.getCityCountry());
            currentWeatherType.setText(mWeatherType);
            currentWeatherDesc.setText("There will be " + reportOfTheDay.getWeatherDesc() + " within the day");
        }

        // remove first object
        // load to list all other object datas
        weatherReportList.remove(0);
        mAdapter = new MainWeatherAdapter(weatherReportList);
        weatherRv.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        weatherRv.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("ElWeather")
                .setMessage("Do you want to exit ElWeather App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}