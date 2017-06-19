package com.example.android.sunshine;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // TODO (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.
        // TODO (3) Delete the for loop that populates the TextView with dummy data
        // TODO (9) Call loadWeatherData to perform the network request to get the weather
        weatherDataLoading();
    }


    // TODO (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    private void weatherDataLoading() {
        String locationOfData = SunshinePreferences.getPreferredWeatherLocation(this);
        new weatherTaskFetching().execute(locationOfData);
    }

    // TODO (5) Create a class that extends AsyncTask to perform network requests
    public class weatherTaskFetching extends AsyncTask<String, Void, String[]> {

        // TODO (6) Override the doInBackground method to perform your network requests
        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherURLRequest = NetworkUtils.buildUrl(location);

            try {
                String weatherJsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherURLRequest);

                String[] weatherDataofJson = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, weatherJsonResponse);

                return weatherDataofJson;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // TODO (7) Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(String[] dataOfWeather) {
            if (dataOfWeather != null) {
                for (String weatherData : dataOfWeather) {
                    mWeatherTextView.append((weatherData) + "\n\n\n");
                }
            }
        }

    }
}