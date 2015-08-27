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
        //deleteOldData();
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
