package com.zacharycalabrese.doughboy.simplenews2.activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zcalabrese on 8/31/15.
 */
public class Weather extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final int CARD_COUNT = 0;

    public Weather(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LayoutInflater layoutInflater;
        View view;

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){

    }

    @Override
    public int getItemViewType(int position){
        return position % CARD_COUNT;
    }

    @Override
    public int getItemCount(){
        return CARD_COUNT;
    }
}
