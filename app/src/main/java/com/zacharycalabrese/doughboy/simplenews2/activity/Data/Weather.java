package com.zacharycalabrese.doughboy.simplenews2.activity.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.zacharycalabrese.doughboy.simplenews2.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public Weather(Context context){
        this.context = context;

        initializeWeatherArrays();
    }

    public Weather(String[] arrayOfJsonResults){
        this.arrayOfJsonResults = arrayOfJsonResults;
        splitArrayOfJsonResults = new String[arrayOfJsonResults.length][];
        splitJsonResults();
        deleteOldData();
        writeToDatabase();
    }

    private void initializeWeatherArrays(){
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

    public List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> getWeekData(){
        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> results =
                com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather
                        .listAll(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather
                                .class);

        results = processTemperatures(results);
        results = processConditions(results);

        return results;
    }

    private List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> processTemperatures
            (List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> results){

        for(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather res: results){
            res.temperatureCurrent = getCelsiusOrFahrenheit(roundOff(res.temperatureCurrent));
            res.temperatureHi = getCelsiusOrFahrenheit(roundOff(res.temperatureHi));
            res.temperatureLow = getCelsiusOrFahrenheit(roundOff(res.temperatureLow));
        }

        return results;
    }

    private String roundOff(String value){
        Double val = Double.parseDouble(value);
        BigDecimal bd = new BigDecimal(val);
        bd = bd.setScale(0, RoundingMode.HALF_UP);
        val = bd.doubleValue();
        return Integer.toString(val.intValue());
    }

    private String getCelsiusOrFahrenheit(String value){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        if (SP.getBoolean("fahrenheit_or_celsius", true)) {
            double celsiusValue = Double.parseDouble(value);
            double fahrenheitValue = (9.0 / 5.0) * celsiusValue + 32;
            return Integer.toString((int) fahrenheitValue);
        }else {
            return value;
        }
    }

    private List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> processConditions
            (List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> results){

        for(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather res: results){
            res.conditions = Integer.toString(getImageResource(res.conditions));
        }

        return results;
    }

    private int getImageResource(String condition){
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
        }else {
            return R.mipmap.ic_tornado;
        }
    }

    private static boolean stringContainsItemFromList(String inputString, String[] items) {
        for (String item : items) {
            if (inputString.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void splitJsonResults(){
        for(int i = 0; i < arrayOfJsonResults.length; i++){
            Pattern pattern = Pattern.compile(Pattern.quote("/"));
            splitArrayOfJsonResults[i] = pattern.split(arrayOfJsonResults[i]);
        }
    }

    private void deleteOldData(){
        com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather
                .deleteAll(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather.class);
    }

    private void writeToDatabase(){
        for(String[] workingDay : splitArrayOfJsonResults){
            Pattern pattern = Pattern.compile(Pattern.quote(" "));
            String[] dayDateMonth = pattern.split(workingDay[0]);

            com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Weather weatherObject =
                    new com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Weather(
                            workingDay[6], workingDay[7], workingDay[8], workingDay[2],
                            workingDay[1], workingDay[4], workingDay[5], dayDateMonth[0],
                            dayDateMonth[2], dayDateMonth[1], workingDay[9], workingDay[10],
                            workingDay[3]);

            com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather session;

            session = new com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather(
                    weatherObject);

            session.save();
        }
    }
}
