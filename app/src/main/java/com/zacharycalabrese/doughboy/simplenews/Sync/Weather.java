package com.zacharycalabrese.doughboy.simplenews.Sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by zcalabrese on 8/25/15.
 */
public class Weather {
    private final int DAYS_TO_FETCH = 5;
    private Boolean updatedWeather;
    private String zipCode;
    private String countryCode;

    public Weather(String zipCode, String countryCode) {
        updatedWeather = false;
        this.zipCode = zipCode;
        this.countryCode = countryCode;
    }

    public void updateWeather() {
        new FetchWeatherTask().execute(zipCode + "," + countryCode);
    }

    private void finishedProcessing() {
        updatedWeather = true;
    }

    public Boolean getUpdatedWeather() {
        return updatedWeather;
    }

    private void processJsonResult(String jsonResult) {
        try {
            String[] results = getWeatherDataFromJson(jsonResult);

            new com.zacharycalabrese.doughboy.simplenews.Data.Weather(results);
        } catch (JSONException e) {
            Log.e("Error: ", "Json exception");
        }
    }

    private String[] getWeatherDataFromJson(String json)
            throws JSONException {
        String OWM_CITY = "city";
        String OWM_CITYNAME = "name";
        String OWM_LIST = "list";
        String OWM_WEATHER = "weather";
        String OWM_TEMPERATURE = "temp";
        String OWM_MAX = "max";
        String OWM_MIN = "min";
        String OWM_DESCRIPTION = "description";
        String OWM_CURRENT = "day";
        String OWM_PRESSURE = "pressure";
        String OWM_HUMIDITY = "humidity";
        String OWM_SPEED = "speed";
        String OWM_CLOUDS = "clouds";
        String OWM_WINDDIRECTION = "deg";

        JSONObject forecastJson = new JSONObject(json);
        JSONObject cityInformation = forecastJson.getJSONObject(OWM_CITY);
        String city = cityInformation.getString(OWM_CITYNAME);

        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        dayTime = new Time();

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        String[] resultStringInArray = new String[DAYS_TO_FETCH];

        for (int i = 0; i < weatherArray.length(); i++) {
            String day;
            String description;
            String pressure;
            String humidity;
            String speed;
            String clouds;
            String wind;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime = dayTime.setJulianDay(julianStartDay + i);

            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            day = shortenedDateFormat.format(dateTime);

            pressure = dayForecast.getString(OWM_PRESSURE);
            humidity = dayForecast.getString(OWM_HUMIDITY);
            speed = dayForecast.getString(OWM_SPEED);
            clouds = dayForecast.getString(OWM_CLOUDS);
            wind = dayForecast.getString(OWM_WINDDIRECTION);

            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);
            double tempToday = temperatureObject.getDouble(OWM_CURRENT);

            resultStringInArray[i] = day + "/" + description + "/" + tempToday + "/" + speed + "/"
                    + clouds + "/" + wind + "/" + city + "/" + Double.toString(high) + "/"
                    + Double.toString(low) + "/" + pressure + "/" + humidity;

        }

        return resultStringInArray;

    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String> {
        public FetchWeatherTask() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String forecastJsonStr = null;
            String format = "json";
            String units = "metric";
            String apiKey = "8e14959919f19af3a054955bfe2a3edc";

            URL url = null;

            try {
                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String API_PARAM = "APPID";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(DAYS_TO_FETCH))
                        .appendQueryParameter(API_PARAM, apiKey)
                        .build();


                try {
                    url = new URL(builtUri.toString());
                    Log.v("Weather API string: ", builtUri.toString());
                } catch (MalformedURLException e) {
                    Log.e("Error: ", "url built incorrectly");
                    return null;
                }

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }

                forecastJsonStr = stringBuffer.toString();
            } catch (IOException e) {
                Log.e("Error: ", "IO exception");
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e("Error: ", "couldn't close buffered reader");
                    }
                }
            }

            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            processJsonResult(result);
            finishedProcessing();
        }
    }
}
