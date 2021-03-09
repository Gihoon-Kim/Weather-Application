package com.example.weatherviewer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YahooApiManager {

    private String city;

    public YahooApiManager(String city) {
        this.city = city;
    }

    public WeatherData getWeather() throws ApiException {

        JSONObject json = makeRequest();
        WeatherData data = parseJson(json);

        return data;
    }

    private JSONObject makeRequest() throws ApiException {

        final String appId = "suq6bL36";
        final String baseUrl = "https://weather-ydn-yql.media.yahoo.com/forecastrss";

        String urlString = baseUrl + "?location=" + city + "&format=json&u=c";

        HttpURLConnection urlConnection;
        JSONObject json = null;

        try {
            URL url = new URL(urlString);

            String authorizationLine = OAuthManager.GetAuthorization(baseUrl, city);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Yahoo-App-Id", appId);
            urlConnection.setRequestProperty("Authorization", authorizationLine);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inStream)
            );

            String temp;
            StringBuilder response = new StringBuilder();

            while ((temp = bReader.readLine()) != null) {

                response.append(temp);
            }

            json = (JSONObject) new JSONTokener(response.toString()).nextValue();

        } catch (IOException| JSONException e) {
            Log.e(MainActivity.LOG_KEY, e.getClass().getName() + ": " + e.getMessage());
            throw new ApiException("Unable to process request");
        }

        return json;
    }

    private WeatherData parseJson(JSONObject json) throws ApiException {

        WeatherData data = null;

        if (json == null) {
            Log.e(MainActivity.LOG_KEY, "parseJson: parameter is null");
            return null;
        }

        try {
            JSONObject condition = json.getJSONObject("current_observation")
                    .getJSONObject("condition");

            int currentTemperature = condition.getInt("temperature");
            String description = condition.getString("text");

            JSONArray forecasts = json.getJSONArray("forecasts");

            int highTemperature = forecasts.getJSONObject(0).getInt("high");
            int lowTemperature = forecasts.getJSONObject(0).getInt("low");

            data = new WeatherData(city, currentTemperature, highTemperature, lowTemperature, description);

        } catch(JSONException e) {
            Log.e(MainActivity.LOG_KEY, e.getClass().getName() + ": " + e.getMessage());
            throw new ApiException("Error parsing JSON");
        }

        return data;
    }
}
