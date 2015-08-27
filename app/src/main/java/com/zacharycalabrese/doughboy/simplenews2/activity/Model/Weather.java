package com.zacharycalabrese.doughboy.simplenews2.activity.Model;

import com.orm.SugarRecord;

/**
 * Created by zcalabrese on 8/25/15.
 */
public class Weather extends SugarRecord<Weather> {
    public String location;
    public String timestamp;
    public String temperatureHi;
    public String temperatureLow;
    public String temperatureCurrent;
    public String conditions;
    public String cloudiness;
    public String windiness;
    public String day;
    public String date;
    public String month;
    public String pressure;
    public String humidity;
    public String direction;

    public Weather(){

    }

    public Weather(com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Weather weatherObject){
        this.location = weatherObject.location;
        this.temperatureHi = weatherObject.temperatureHi;
        this.temperatureLow = weatherObject.temperatureLow;
        this.temperatureCurrent = weatherObject.temperatureCurrent;
        this.conditions = weatherObject.conditions;
        this.cloudiness = weatherObject.cloudiness;
        this.windiness = weatherObject.windiness;
        this.day = weatherObject.day;
        this.date = weatherObject.date;
        this.month = weatherObject.month;
        this.pressure = weatherObject.pressure;
        this.humidity = weatherObject.humidity;
        this.direction = weatherObject.direction;
        timestamp = ((Long) System.currentTimeMillis()).toString();

    }
}
