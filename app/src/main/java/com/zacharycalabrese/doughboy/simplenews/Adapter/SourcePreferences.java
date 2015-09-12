package com.zacharycalabrese.doughboy.simplenews.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zacharycalabrese.doughboy.simplenews.Data.*;
import com.zacharycalabrese.doughboy.simplenews.Data.News;
import com.zacharycalabrese.doughboy.simplenews.Data.Weather;
import com.zacharycalabrese.doughboy.simplenews.Helper.*;
import com.zacharycalabrese.doughboy.simplenews.Helper.Source;
import com.zacharycalabrese.doughboy.simplenews.R;

import org.w3c.dom.Text;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by zcalabrese on 9/10/15.
 */
public class SourcePreferences extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = SourcePreferences.class.getName();
    private Hashtable<String, String> titleToCategory;
    private Context context;
    private List<Source> sourceList;

    public SourcePreferences(Context context, List<Source> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
        initializeHashtable(sourceList);
    }

    private void initializeHashtable(List<Source> sourceList){
        titleToCategory = new Hashtable<>();
        String currentCategory;
        String lastCategory = "";
        for(Source item : sourceList){
            currentCategory = item.category;
            if(!currentCategory.equals(lastCategory)){
                titleToCategory.put(item.name, item.category);
            }
            lastCategory = currentCategory;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater;
        View view;

        switch (i) {
            default:
                layoutInflater = LayoutInflater.from(viewGroup.getContext());
                view = layoutInflater.inflate(R.layout.viewholder_source_preferences, viewGroup, false);
                return new SourceViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(titleToCategory.get(sourceList.get(i).name) != null){
            bindWithCategory(viewHolder, i);
        }else{
            bindWithoutCategory(viewHolder, i);
        }
    }

    private void bindWithCategory(RecyclerView.ViewHolder viewHolder, final int i){
        final SourceViewHolder viewHolder1 = (SourceViewHolder) viewHolder;
        viewHolder1.title.setText(sourceList.get(i).name);
        viewHolder1.url.setText(sourceList.get(i).rssUrl);
        viewHolder1.checkBox.setChecked(sourceList.get(i).subscribed);
        viewHolder1.category.setText(sourceList.get(i).category);
        setListeners(viewHolder1, i);
    }

    private void bindWithoutCategory(RecyclerView.ViewHolder viewHolder, final int i){
        final SourceViewHolder viewHolder1 = (SourceViewHolder) viewHolder;

        viewHolder1.title.setText(sourceList.get(i).name);
        viewHolder1.url.setText(sourceList.get(i).rssUrl);
        viewHolder1.checkBox.setChecked(sourceList.get(i).subscribed);
        viewHolder1.category.setVisibility(View.GONE);
        setListeners(viewHolder1, i);
    }

    private void setListeners(final SourceViewHolder viewHolder, final int i){
        final com.zacharycalabrese.doughboy.simplenews.Data.Source sourceDataObject =
                new com.zacharycalabrese.doughboy.simplenews.Data.Source();

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.v(LOG_TAG, "SHOWWWING");
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.dialog_input_edit_source, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);

                final EditText source = (EditText)
                        promptView.findViewById(R.id.dialog_input_edit_source_name_edit_text);
                source.setText(sourceList.get(i).name);

                final EditText rss = (EditText)
                        promptView.findViewById(R.id.dialog_input_edit_source_rss_edit_text);
                rss.setText(sourceList.get(i).rssUrl);

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Source result = sourceDataObject.editSource(sourceList.get(i),
                                        source.getText().toString(), rss.getText().toString());

                                viewHolder.title.setText(result.name);
                                viewHolder.url.setText(result.rssUrl);
                                viewHolder.checkBox.setChecked(result.subscribed);

                                sourceList.set(i, result);
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                // Create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checked = sourceList.get(i).subscribed;
                if (checked) {
                    Log.v(LOG_TAG, "unsubscribe");
                    sourceDataObject.unsubscribe(sourceList.get(i).name);
                    sourceList.get(i).subscribed = false;

                } else {
                    Log.v(LOG_TAG, "subscribe");
                    sourceDataObject.subscribe(sourceList.get(i).name);
                    sourceList.get(i).subscribed = true;
                }
                viewHolder.checkBox.setChecked(sourceList.get(i).subscribed);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position % sourceList.size();
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    public class SourceViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView url;
        protected CheckBox checkBox;
        protected TextView category;
        protected CardView cardView;

        public SourceViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.viewholder_source_preferences_title);
            url = (TextView) v.findViewById(R.id.viewholder_source_preferences_url);
            checkBox = (CheckBox) v.findViewById(R.id.viewholder_source_preferences_checkbox);
            category = (TextView) v.findViewById(R.id.viewholder_source_preferences_category);
            cardView = (CardView) v.findViewById(R.id.viewholder_source_preferences_card_view);
        }
    }
}
