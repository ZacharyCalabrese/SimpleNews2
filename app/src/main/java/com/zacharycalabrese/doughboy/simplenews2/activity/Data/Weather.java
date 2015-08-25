package com.zacharycalabrese.doughboy.simplenews2.activity.Data;

import android.util.Log;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zcalabrese on 8/25/15.
 */
public class Weather {
    String[] arrayOfJsonResults;
    String[][] splitArrayOfJsonResults;

    public Weather(){

    }

    public Weather(String[] arrayOfJsonResults){
        this.arrayOfJsonResults = arrayOfJsonResults;
        splitArrayOfJsonResults = new String[arrayOfJsonResults.length][];
        splitJsonResults();
        writeToDatabase();
    }

    public List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> getWeekData(){
        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> results =
                com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather
                        .listAll(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather
                                .class);

        return results;
    }

    private void splitJsonResults(){
        for(int i = 0; i < arrayOfJsonResults.length; i++){
            Pattern pattern = Pattern.compile(Pattern.quote("/"));
            splitArrayOfJsonResults[i] = pattern.split(arrayOfJsonResults[i]);
        }
    }

    private void writeToDatabase(){
        for(String[] workingDay : splitArrayOfJsonResults){
            Pattern pattern = Pattern.compile(Pattern.quote(" "));
            String[] dayDateMonth = pattern.split(workingDay[0]);

            com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Weather weather =
                    new com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Weather();

            weather.location = workingDay[6];
            weather.temperatureHi = workingDay[7];
            weather.temperatureLow = workingDay[8];
            weather.temperatureCurrent = workingDay[2];
            weather.conditions = workingDay[1];
            weather.cloudiness = workingDay[4];
            weather.windiness = workingDay[5];
            weather.day = dayDateMonth[0];
            weather.date = dayDateMonth[2];
            weather.month = dayDateMonth[1];
            weather.pressure = workingDay[9];
            weather.humidity = workingDay[10];
            weather.direction = workingDay[3];


            com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather session;

            session = new com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather(
                    weather.location, weather.temperatureHi, weather.temperatureLow,
                    weather.temperatureCurrent, weather.conditions, weather.cloudiness,
                    weather.windiness, weather.day, weather.date, weather.month, weather.pressure,
                    weather.humidity, weather.direction);

            session.save();
        }
    }
}
