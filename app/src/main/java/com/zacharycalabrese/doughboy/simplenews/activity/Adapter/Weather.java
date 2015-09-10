package com.zacharycalabrese.doughboy.simplenews.activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zacharycalabrese.doughboy.simplenews.R;

import java.util.List;

/**
 * Created by zcalabrese on 8/31/15.
 */
public class Weather extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<com.zacharycalabrese.doughboy.simplenews.activity.Helper.Weather> weatherList;

    public Weather(Context context,
                   List<com.zacharycalabrese.doughboy.simplenews.activity.Helper.Weather>
                           weatherList) {

        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.viewholder_weather, viewGroup, false);

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final String DEGREE = "\u00b0";
        final String PERCENTAGE = "%";
        final String PRESSURE = " hPa";
        WeatherViewHolder weatherViewHolder = (WeatherViewHolder) viewHolder;

        String dateFormatted = weatherList.get(i).day + " " + weatherList.get(i).month
                + " " + weatherList.get(i).date;

        weatherViewHolder.currentDate.setText(dateFormatted);
        weatherViewHolder.currentConditions.setText(weatherList.get(i).conditions);
        weatherViewHolder.temperatureHi.setText(weatherList.get(i).temperatureHi + DEGREE);
        weatherViewHolder.temperatureLow.setText(weatherList.get(i).temperatureLow + DEGREE);
        weatherViewHolder.humidity.setText(weatherList.get(i).humidity + PERCENTAGE);
        weatherViewHolder.pressure.setText(weatherList.get(i).pressure + PRESSURE);
        weatherViewHolder.wind.setText(weatherList.get(i).windSpeedAndDirection);
        weatherViewHolder.cloudiness.setText(weatherList.get(i).cloudiness + PERCENTAGE);
        weatherViewHolder.conditions.setImageResource(weatherList.get(i).conditionImageResourceId);
    }

    @Override
    public int getItemViewType(int position) {
        return position % weatherList.size();
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        protected TextView currentDate;
        protected TextView currentConditions;
        protected TextView temperatureHi;
        protected TextView temperatureLow;
        protected TextView humidity;
        protected TextView pressure;
        protected TextView wind;
        protected TextView cloudiness;
        protected ImageView conditions;

        public WeatherViewHolder(View v) {
            super(v);

            currentDate = (TextView) v.findViewById(R.id.viewholder_weather_date);
            currentConditions = (TextView) v.findViewById(R.id.viewholder_weather_conditions);
            temperatureHi = (TextView) v.findViewById(R.id.viewholder_weather_temperature_high);
            temperatureLow = (TextView) v.findViewById(R.id.viewholder_weather_temperature_low);
            humidity = (TextView) v.findViewById(R.id.viewholder_weather_humidity);
            pressure = (TextView) v.findViewById(R.id.viewholder_weather_pressure);
            wind = (TextView) v.findViewById(R.id.viewholder_weather_wind);
            cloudiness = (TextView) v.findViewById(R.id.viewholder_weather_cloudiness);
            conditions = (ImageView) v.findViewById(R.id.viewholder_weather_current_condition_graphic);
        }
    }
}
