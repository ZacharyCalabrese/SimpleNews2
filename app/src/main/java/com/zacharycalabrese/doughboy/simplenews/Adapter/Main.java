package com.zacharycalabrese.doughboy.simplenews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zacharycalabrese.doughboy.simplenews.Data.Weather;
import com.zacharycalabrese.doughboy.simplenews.R;

import java.util.List;

/**
 * Created by zcalabrese on 8/24/15.
 */
public class Main extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = Main.class.getName();
    private int cardCount;
    private int currentPosition;
    private Context context;
    private List<com.zacharycalabrese.doughboy.simplenews.Helper.News> newsHeadlines;
    private com.zacharycalabrese.doughboy.simplenews.Data.News news;

    public Main(Context context, List<com.zacharycalabrese.doughboy.simplenews.Helper.News> newsHeadlines) {
        this.context = context;

        this.newsHeadlines = newsHeadlines;
        cardCount = this.newsHeadlines.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater;
        View view;

        switch (i) {
            case 0:
                layoutInflater = LayoutInflater.from(viewGroup.getContext());
                view = layoutInflater.inflate(R.layout.viewholder_main_weather, viewGroup, false);
                return new WeatherViewHolder(view);
            default:
                layoutInflater = LayoutInflater.from(viewGroup.getContext());
                view = layoutInflater.inflate(R.layout.viewholder_main_news_2, viewGroup, false);
                return new NewsViewHolder2(view);
        }

    }

    public void updateList(List<com.zacharycalabrese.doughboy.simplenews.Helper.News> data) {
        newsHeadlines = data;
        cardCount = newsHeadlines.size() + 1;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                updateWeatherViewHolder(viewHolder);
                break;
            default:
                try {
                    updateNewsViewHolder2(viewHolder);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(LOG_TAG, "Index out of bound exception");
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        currentPosition = position % cardCount - 1;
        return position % cardCount;
    }

    @Override
    public int getItemCount() {
        return cardCount;
    }

    private void updateWeatherViewHolder(RecyclerView.ViewHolder viewHolder) {
        final String DEGREE = "\u00b0";
        Weather weather = new Weather(context);
        List<com.zacharycalabrese.doughboy.simplenews.Helper.Weather> results
                = weather.getWeekData();

        WeatherViewHolder weatherViewHolder = (WeatherViewHolder) viewHolder;

        try {
            String dateFormatted = results.get(0).day + " " + results.get(0).month
                    + " " + results.get(0).date;

            weatherViewHolder.currentDate.setText(dateFormatted);
            weatherViewHolder.currentLocation.setText(results.get(0).location);
            weatherViewHolder.currentCondition.setImageResource(results.get(0).conditionImageResourceId);
            weatherViewHolder.currentTemperature.setText(results.get(0).temperatureCurrent + DEGREE);
            weatherViewHolder.day1Name.setText(results.get(0).day);
            weatherViewHolder.day1Hi.setText(results.get(0).temperatureHi);
            weatherViewHolder.day1Low.setText(results.get(0).temperatureLow);
            weatherViewHolder.day1Condition.setImageResource(results.get(0).conditionImageResourceId);
            weatherViewHolder.day2Name.setText(results.get(1).day);
            weatherViewHolder.day2Hi.setText(results.get(1).temperatureHi);
            weatherViewHolder.day2Low.setText(results.get(1).temperatureLow);
            weatherViewHolder.day2Condition.setImageResource(results.get(1).conditionImageResourceId);
            weatherViewHolder.day3Name.setText(results.get(2).day);
            weatherViewHolder.day3Hi.setText(results.get(2).temperatureHi);
            weatherViewHolder.day3Low.setText(results.get(2).temperatureLow);
            weatherViewHolder.day3Condition.setImageResource(results.get(2).conditionImageResourceId);
            weatherViewHolder.day4Name.setText(results.get(3).day);
            weatherViewHolder.day4Hi.setText(results.get(3).temperatureHi);
            weatherViewHolder.day4Low.setText(results.get(3).temperatureLow);
            weatherViewHolder.day4Condition.setImageResource(results.get(3).conditionImageResourceId);
            weatherViewHolder.day5Name.setText(results.get(4).day);
            weatherViewHolder.day5Hi.setText(results.get(4).temperatureHi);
            weatherViewHolder.day5Low.setText(results.get(4).temperatureLow);
            weatherViewHolder.day5Condition.setImageResource(results.get(4).conditionImageResourceId);
            weatherViewHolder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            com.zacharycalabrese.doughboy.simplenews.Activity.Weather.class);
                    context.startActivity(intent);
                }
            });
        } catch (IndexOutOfBoundsException e) {

        }
    }


    private void updateNewsViewHolder2(RecyclerView.ViewHolder viewHolder) {
        NewsViewHolder2 newsViewHolder2 = (NewsViewHolder2) viewHolder;

        newsViewHolder2.card.setVisibility(View.VISIBLE);
        newsViewHolder2.card.setOnClickListener(new View.OnClickListener() {
            final String url = newsHeadlines.get(currentPosition).link;

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        com.zacharycalabrese.doughboy.simplenews.Activity.Web.class);

                intent.putExtra(context.getResources().getString(R.string.url_to_load), url);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        String newSource = newsHeadlines.get(currentPosition).source.name.trim();


        String timeAgo =
                DateUtils.getRelativeTimeSpanString(newsHeadlines.get(currentPosition).timestamp).toString();

        if(timeAgo.contains("in "))
            newsViewHolder2.source.setText("Now" + " - " + newSource);
        else
            newsViewHolder2.source.setText(timeAgo + " - " + newSource);
        newsViewHolder2.headline.setText(newsHeadlines.get(currentPosition).title);

        String descriptionToDisplay = Html.fromHtml(newsHeadlines.get(currentPosition).description).toString().replace('\n', (char) 32)
                .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim();

        if (!isAlphaNumeric(descriptionToDisplay) && stringDifferentThanTitle(newsHeadlines.get
                (currentPosition).title, descriptionToDisplay) && stringIsClear(descriptionToDisplay))
            newsViewHolder2.description.setText(descriptionToDisplay);
        else {
            newsViewHolder2.description.setVisibility(View.GONE);
        }
    }

    public boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public boolean stringDifferentThanTitle(String string1, String string2) {
        string1 = string1.toLowerCase();
        string2 = string2.toLowerCase();
        if (string1.equals(string2))
            return false;

        return true;
    }

    public boolean stringIsClear(String string) {
        if (string.contains("??"))
            return false;

        return true;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
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
        protected FloatingActionButton floatingActionButton;

        public WeatherViewHolder(View v) {
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
            floatingActionButton = (FloatingActionButton) v.findViewById(R.id.viewholder_main_weather_fab);
        }

    }

    public class NewsViewHolder2 extends RecyclerView.ViewHolder {
        protected CardView card;
        protected TextView source;
        protected TextView headline;
        protected TextView description;

        public NewsViewHolder2(View v) {
            super(v);
            card = (CardView) v.findViewById(R.id.viewholder_main_news_2_card_view);
            source = (TextView) v.findViewById(R.id.viewholder_main_news_2_source);
            headline = (TextView) v.findViewById(R.id.viewholder_main_news_2_headline);
            description = (TextView) v.findViewById(R.id.viewholder_main_news_2_description);
        }
    }
}
