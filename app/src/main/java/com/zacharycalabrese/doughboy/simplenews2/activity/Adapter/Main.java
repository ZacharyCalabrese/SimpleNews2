package com.zacharycalabrese.doughboy.simplenews2.activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zacharycalabrese.doughboy.simplenews2.R;
import com.zacharycalabrese.doughboy.simplenews2.activity.Data.Weather;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by zcalabrese on 8/24/15.
 */
public class Main extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int CARD_COUNT = 2;

    public Main(){

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.viewholder_main_weather, viewGroup, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        Weather weather = new Weather();
        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> results
                = weather.getWeekData();

        WeatherViewHolder weatherViewHolder = (WeatherViewHolder)viewHolder ;
        weatherViewHolder.currentTemperature.setText(results.get(0).location);
    }

    @Override
    public int getItemViewType(int position){
        return position % CARD_COUNT;
    }

    @Override
    public int getItemCount(){
        return CARD_COUNT;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        protected TextView currentDate;
        protected TextView currentLocation;
        protected ImageView currentCondition;
        protected TextView currentTemperature;

        public WeatherViewHolder(View v){
            super(v);
            currentDate = (TextView) v.findViewById(R.id.viewholder_main_weather_date);
            currentLocation = (TextView) v.findViewById(R.id.viewholder_main_weather_location);
            currentCondition = (ImageView) v.findViewById(R.id.viewholder_main_weather_current_condition_graphic);
            currentTemperature = (TextView) v.findViewById(R.id.viewholder_main_weather_current_temperature);
        }

    }
}
