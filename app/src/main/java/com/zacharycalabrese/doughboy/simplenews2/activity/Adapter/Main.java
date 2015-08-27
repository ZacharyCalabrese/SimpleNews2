package com.zacharycalabrese.doughboy.simplenews2.activity.Adapter;

import android.content.Context;
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
    private Context context;

    public Main(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LayoutInflater layoutInflater;
        View view;

        switch (i){
            case 0:
                layoutInflater = LayoutInflater.from(viewGroup.getContext());
                view = layoutInflater.inflate(R.layout.viewholder_main_weather, viewGroup, false);
                return new WeatherViewHolder(view);
            case 1:
                layoutInflater = LayoutInflater.from(viewGroup.getContext());
                view = layoutInflater.inflate(R.layout.viewholder_main_weather, viewGroup, false);
                return new WeatherViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        switch(i){
            case 0:
                updateWeatherViewHolder(viewHolder);
                break;
            case 1:
                updateWeatherViewHolder(viewHolder);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position){
        return position % CARD_COUNT;
    }

    @Override
    public int getItemCount(){
        return CARD_COUNT;
    }

    private void updateWeatherViewHolder(RecyclerView.ViewHolder viewHolder){
        Weather weather = new Weather(context);
        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Weather> results
                = weather.getWeekData();

        WeatherViewHolder weatherViewHolder = (WeatherViewHolder)viewHolder;

        try {
            String dateFormatted = results.get(0).day + " " + results.get(0).month
                    + " " + results.get(0).date;

            weatherViewHolder.currentDate.setText(dateFormatted);
            weatherViewHolder.currentLocation.setText(results.get(0).location);
            weatherViewHolder.currentCondition.setImageResource(Integer.parseInt(results.get(0).conditions));
            weatherViewHolder.currentTemperature.setText(results.get(0).temperatureCurrent);
            weatherViewHolder.day1Name.setText(results.get(0).day);
            weatherViewHolder.day1Hi.setText(results.get(0).temperatureHi);
            weatherViewHolder.day1Low.setText(results.get(0).temperatureLow);
            weatherViewHolder.day1Condition.setImageResource(Integer.parseInt(results.get(0).conditions));
            weatherViewHolder.day2Name.setText(results.get(1).day);
            weatherViewHolder.day2Hi.setText(results.get(1).temperatureHi);
            weatherViewHolder.day2Low.setText(results.get(1).temperatureLow);
            weatherViewHolder.day2Condition.setImageResource(Integer.parseInt(results.get(1).conditions));
            weatherViewHolder.day3Name.setText(results.get(2).day);
            weatherViewHolder.day3Hi.setText(results.get(2).temperatureHi);
            weatherViewHolder.day3Low.setText(results.get(2).temperatureLow);
            weatherViewHolder.day3Condition.setImageResource(Integer.parseInt(results.get(2).conditions));
            weatherViewHolder.day4Name.setText(results.get(3).day);
            weatherViewHolder.day4Hi.setText(results.get(3).temperatureHi);
            weatherViewHolder.day4Low.setText(results.get(3).temperatureLow);
            weatherViewHolder.day4Condition.setImageResource(Integer.parseInt(results.get(3).conditions));
            weatherViewHolder.day5Name.setText(results.get(4).day);
            weatherViewHolder.day5Hi.setText(results.get(4).temperatureHi);
            weatherViewHolder.day5Low.setText(results.get(4).temperatureLow);
            weatherViewHolder.day5Condition.setImageResource(Integer.parseInt(results.get(4).conditions));
        }catch (IndexOutOfBoundsException e){

        }
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        protected TextView currentDate;
        protected TextView currentLocation;
        protected ImageView currentCondition;
        protected TextView currentTemperature;
        protected TextView day1Name;
        protected TextView day2Name;
        protected TextView day3Name;
        protected TextView day4Name;
        protected TextView day5Name;
        protected TextView day1Hi;
        protected TextView day2Hi;
        protected TextView day3Hi;
        protected TextView day4Hi;
        protected TextView day5Hi;
        protected TextView day1Low;
        protected TextView day2Low;
        protected TextView day3Low;
        protected TextView day4Low;
        protected TextView day5Low;
        protected ImageView day1Condition;
        protected ImageView day2Condition;
        protected ImageView day3Condition;
        protected ImageView day4Condition;
        protected ImageView day5Condition;

        public WeatherViewHolder(View v){
            super(v);
            currentDate = (TextView) v.findViewById(R.id.viewholder_main_weather_date);
            currentLocation = (TextView) v.findViewById(R.id.viewholder_main_weather_location);
            currentCondition = (ImageView) v.findViewById(R.id.viewholder_main_weather_current_condition_graphic);
            currentTemperature = (TextView) v.findViewById(R.id.viewholder_main_weather_current_temperature);
            day1Name = (TextView) v.findViewById(R.id.viewholder_main_weather_day_1);
            day2Name = (TextView) v.findViewById(R.id.viewholder_main_weather_day_2);
            day3Name = (TextView) v.findViewById(R.id.viewholder_main_weather_day_3);
            day4Name = (TextView) v.findViewById(R.id.viewholder_main_weather_day_4);
            day5Name = (TextView) v.findViewById(R.id.viewholder_main_weather_day_5);
            day1Hi = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_1_high);
            day2Hi = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_2_high);
            day3Hi = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_3_high);
            day4Hi = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_4_high);
            day5Hi = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_5_high);
            day1Low = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_1_low);
            day2Low = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_2_low);
            day3Low = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_3_low);
            day4Low = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_4_low);
            day5Low = (TextView) v.findViewById(R.id.viewholder_main_weather_temperature_day_5_low);
            day1Condition = (ImageView) v.findViewById(R.id.viewholder_main_weather_image_view_day_1);
            day2Condition = (ImageView) v.findViewById(R.id.viewholder_main_weather_image_view_day_2);
            day3Condition = (ImageView) v.findViewById(R.id.viewholder_main_weather_image_view_day_3);
            day4Condition = (ImageView) v.findViewById(R.id.viewholder_main_weather_image_view_day_4);
            day5Condition = (ImageView) v.findViewById(R.id.viewholder_main_weather_image_view_day_5);
        }

    }
}
