package com.zacharycalabrese.doughboy.simplenews.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zacharycalabrese.doughboy.simplenews.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zcalabrese on 8/25/15.
 */
public class Weather {
    private String[] arrayOfJsonResults;
    private String[][] splitArrayOfJsonResults;
    private Context context;
    private String[] icFreeze;
    private String[] icCloudy;
    private String[] icLightCloud;
    private String[] icSunCloudRain;
    private String[] icRain;
    private String[] icLightRainNoSun;
    private String[] icMildThunderstorm;
    private String[] icRainyThunderstormSun;
    private String[] icThunderWithSun;
    private String[] icThunder;
    private String[] icLightSnow;
    private String[] icSnowWithSun;
    private String[] icSunny;
    private String[] icTornado;
    private String[] icWindy;

    public Weather(Context context) {
        this.context = context;

        initializeWeatherArrays();
    }

    public Weather(String[] arrayOfJsonResults) {
        this.arrayOfJsonResults = arrayOfJsonResults;
        splitArrayOfJsonResults = new String[arrayOfJsonResults.length][];
        splitJsonResults();
        deleteOldData();
        writeToDatabase();
    }

    private static boolean stringContainsItemFromList(String inputString, String[] items) {
        for (String item : items) {
            if (inputString.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void initializeWeatherArrays() {
        icCloudy = context.getResources().getStringArray(R.array.ic_cloudy);
        icFreeze = context.getResources().getStringArray(R.array.ic_freeze);
        icLightCloud = context.getResources().getStringArray(R.array.ic_light_cloud);
        icSunCloudRain = context.getResources().getStringArray(R.array.ic_sun_cloud_rain);
        icRain = context.getResources().getStringArray(R.array.ic_rain);
        icLightRainNoSun = context.getResources().getStringArray(R.array.ic_light_rain_no_sun);
        icMildThunderstorm = context.getResources().getStringArray(R.array.ic_mild_thunderstorm);
        icRainyThunderstormSun = context.getResources().getStringArray(R.array.ic_rainy_thunderstorm_sun);
        icThunderWithSun = context.getResources().getStringArray(R.array.ic_thunder_with_sun);
        icThunder = context.getResources().getStringArray(R.array.ic_thunder);
        icLightSnow = context.getResources().getStringArray(R.array.ic_light_snow);
        icSnowWithSun = context.getResources().getStringArray(R.array.ic_snow_with_sun);
        icSunny = context.getResources().getStringArray(R.array.ic_sunny);
        icTornado = context.getResources().getStringArray(R.array.ic_tornado);
        icWindy = context.getResources().getStringArray(R.array.ic_windy);
    }

    public List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> getWeekData() {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Weather> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Weather
                        .listAll(com.zacharycalabrese.doughboy.simplenews.Model.Weather
                                .class);
        List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> helperWeatherList;
        helperWeatherList = initilizeHelperWeatherList(results);
        helperWeatherList = processTemperatures(helperWeatherList);
        helperWeatherList = processConditionImages(helperWeatherList);
        helperWeatherList = processConditions(helperWeatherList);
        helperWeatherList = processWind(helperWeatherList);

        return helperWeatherList;
    }

    private List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather>
    initilizeHelperWeatherList(
            List<com.zacharycalabrese.doughboy.simplenews.Model.Weather> data) {

        List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> returningList;
        returningList = new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews.Model.Weather item : data) {
            com.zacharycalabrese.doughboy.simplenews.Helper.Weather temp;
            temp = new com.zacharycalabrese.doughboy.simplenews.Helper.Weather(item);
            returningList.add(temp);
        }

        return returningList;
    }

    private List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> processTemperatures
            (List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> results) {

        for (com.zacharycalabrese.doughboy.simplenews.Helper.Weather res : results) {
            res.temperatureCurrent = getCelsiusOrFahrenheit(roundOff(res.temperatureCurrent));
            res.temperatureHi = getCelsiusOrFahrenheit(roundOff(res.temperatureHi));
            res.temperatureLow = getCelsiusOrFahrenheit(roundOff(res.temperatureLow));
        }

        return results;
    }

    private String roundOff(String value) {
        Double val = Double.parseDouble(value);
        BigDecimal bd = new BigDecimal(val);
        bd = bd.setScale(0, RoundingMode.HALF_UP);
        val = bd.doubleValue();
        return Integer.toString(val.intValue());
    }

    private String getCelsiusOrFahrenheit(String value) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        if (SP.getBoolean("fahrenheit_or_celsius", true)) {
            double celsiusValue = Double.parseDouble(value);
            double fahrenheitValue = (9.0 / 5.0) * celsiusValue + 32;
            return Integer.toString((int) fahrenheitValue);
        } else {
            return value;
        }
    }

    private List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> processConditionImages
            (List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> results) {

        for (com.zacharycalabrese.doughboy.simplenews.Helper.Weather res : results) {
            res.conditionImageResourceId = getImageResource(res.conditions);
        }

        return results;
    }

    private List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> processConditions
            (List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> results) {

        for (com.zacharycalabrese.doughboy.simplenews.Helper.Weather res : results) {
            res.conditions = res.conditions.substring(0, 1).toUpperCase() + res.conditions.substring(1);
        }

        return results;
    }

    private List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> processWind
            (List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> results) {

        for (com.zacharycalabrese.doughboy.simplenews.Helper.Weather res : results) {
            res.direction = getWindSpeed(res.direction);
            res.windSpeedAndDirection = res.direction + " " + getWindDirection(res.windiness);
        }

        return results;
    }

    private int getImageResource(String condition) {
        if (stringContainsItemFromList(condition, icCloudy)) {
            return R.mipmap.ic_cloudy;
        } else if (stringContainsItemFromList(condition, icFreeze)) {
            return R.mipmap.ic_freeze;
        } else if (stringContainsItemFromList(condition, icLightCloud)) {
            return R.mipmap.ic_light_cloud;
        } else if (stringContainsItemFromList(condition, icLightRainNoSun)) {
            return R.mipmap.ic_light_rain_no_sun;
        } else if (stringContainsItemFromList(condition, icLightSnow)) {
            return R.mipmap.ic_light_snow;
        } else if (stringContainsItemFromList(condition, icMildThunderstorm)) {
            return R.mipmap.ic_mild_thunderstorm;
        } else if (stringContainsItemFromList(condition, icRain)) {
            return R.mipmap.ic_rain;
        } else if (stringContainsItemFromList(condition, icRainyThunderstormSun)) {
            return R.mipmap.ic_rainy_thunderstorm_sun;
        } else if (stringContainsItemFromList(condition, icSnowWithSun)) {
            return R.mipmap.ic_snow_with_sun;
        } else if (stringContainsItemFromList(condition, icSunCloudRain)) {
            return R.mipmap.ic_sun_cloud_rain;
        } else if (stringContainsItemFromList(condition, icSunny)) {
            return R.mipmap.ic_sunny;
        } else if (stringContainsItemFromList(condition, icThunder)) {
            return R.mipmap.ic_thunder;
        } else if (stringContainsItemFromList(condition, icThunderWithSun)) {
            return R.mipmap.ic_thunder_with_sun;
        } else if (stringContainsItemFromList(condition, icTornado)) {
            return R.mipmap.ic_tornado;
        } else if (stringContainsItemFromList(condition, icWindy)) {
            return R.mipmap.ic_windy;
        } else if (stringContainsItemFromList(condition, icMildThunderstorm)) {
            return R.mipmap.ic_mild_thunderstorm;
        } else {
            return R.mipmap.ic_tornado;
        }
    }

    private String getWindSpeed(String speed) {
        Double speedInIntMetersPerSecond;
        speedInIntMetersPerSecond = Double.parseDouble(speed);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        Double metersToKmConversion = 3.6;
        Double metersToMphConversion = 2.23694;


        if (!SP.getBoolean("metric_or_imperial", true)) {
            speedInIntMetersPerSecond = speedInIntMetersPerSecond * metersToKmConversion;
            speedInIntMetersPerSecond = (double)Math.round(speedInIntMetersPerSecond * 100) / 100;
            String speedInKilometersPerHour = Double.toString(speedInIntMetersPerSecond);
            return speedInKilometersPerHour + " km/h";
        } else {
            speedInIntMetersPerSecond = speedInIntMetersPerSecond * metersToMphConversion;
            speedInIntMetersPerSecond = (double)Math.round(speedInIntMetersPerSecond * 100) / 100;
            String speedInMilersPerHour = Double.toString(speedInIntMetersPerSecond);
            return speedInMilersPerHour + " mph";
        }
    }

    private String getWindDirection(String degrees) {
        int degreesInInt;
        degreesInInt = Integer.parseInt(degrees);

        if (degreesInInt < 15)
            return "E";
        else if (degreesInInt < 75)
            return "NE";
        else if (degreesInInt < 105)
            return "N";
        else if (degreesInInt < 165)
            return "NW";
        else if (degreesInInt < 195)
            return "E";
        else if (degreesInInt < 255)
            return "SW";
        else if (degreesInInt < 285)
            return "S";
        else if (degreesInInt < 345)
            return "SE";
        else
            return "E";

    }

    private void splitJsonResults() {
        for (int i = 0; i < arrayOfJsonResults.length; i++) {
            Pattern pattern = Pattern.compile(Pattern.quote("/"));
            splitArrayOfJsonResults[i] = pattern.split(arrayOfJsonResults[i]);
        }
    }

    private void deleteOldData() {
        com.zacharycalabrese.doughboy.simplenews.Model.Weather
                .deleteAll(com.zacharycalabrese.doughboy.simplenews.Model.Weather.class);
    }

    private void writeToDatabase() {
        for (String[] workingDay : splitArrayOfJsonResults) {
            Pattern pattern = Pattern.compile(Pattern.quote(" "));
            String[] dayDateMonth = pattern.split(workingDay[0]);

            com.zacharycalabrese.doughboy.simplenews.Helper.Weather weatherObject =
                    new com.zacharycalabrese.doughboy.simplenews.Helper.Weather(
                            workingDay[6], workingDay[7], workingDay[8], workingDay[2],
                            workingDay[1], workingDay[4], workingDay[5], dayDateMonth[0],
                            dayDateMonth[2], dayDateMonth[1], workingDay[9], workingDay[10],
                            workingDay[3]);

            com.zacharycalabrese.doughboy.simplenews.Model.Weather session;

            session = new com.zacharycalabrese.doughboy.simplenews.Model.Weather(
                    weatherObject);

            session.save();
        }
    }
}
