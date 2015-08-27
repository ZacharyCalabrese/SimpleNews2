package com.zacharycalabrese.doughboy.simplenews2.activity.Helper;

/**
 * Created by zcalabrese on 8/25/15.
 */
public class Weather {
    public String location;
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

    public Weather(String location, String temperatureHi, String temperatureLow,
                   String temperatureCurrent, String conditions, String cloudiness,
                   String windiness, String day, String date, String month,
                   String pressure, String humidity, String direction){

        this.location = location;
        this.temperatureHi = temperatureHi;
        this.temperatureLow = temperatureLow;
        this.temperatureCurrent = temperatureCurrent;
        this.conditions = conditions;
        this.cloudiness = cloudiness;
        this.windiness = windiness;
        this.day = day;
        this.date = date;
        this.month = month;
        this.pressure = pressure;
        this.humidity = humidity;
        this.direction = direction;

    }
}
