package com.zacharycalabrese.doughboy.simplenews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zacharycalabrese.doughboy.simplenews.R;

import java.util.List;

/**
 * Created by zcalabrese on 9/3/15.
 */
public class News extends BaseAdapter{
    private List<com.zacharycalabrese.doughboy.simplenews.Helper.News> articles;
    private LayoutInflater layoutInflater;
    private Context context;

    public News(Context context, List<
                com.zacharycalabrese.doughboy.simplenews.Helper.News> articles) {

        this.context = context;
        this.articles = articles;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.viewholder_news, null);
        final ViewHolder newsViewHolder = new ViewHolder(view);
        view.setTag(newsViewHolder);

        if (articles.get(position).link.contains("http://")) {
            setTextViewTitle(newsViewHolder, articles.get(position).title, articles.get(position).description);
        }

        setCardOnCLickListener(newsViewHolder);
        setShareOnClickListener(newsViewHolder, position);
        setPocketOnClickListener(newsViewHolder, position);
        setReadOnClickListener(newsViewHolder, position);

        return view;
    }

    public void setCardOnCLickListener(final ViewHolder viewholder){
        viewholder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewholder.share_button.getVisibility() == View.GONE) {
                    viewholder.share_button.setVisibility(View.VISIBLE);
                    viewholder.pocket_button.setVisibility(View.VISIBLE);
                    viewholder.read_button.setVisibility(View.VISIBLE);
                } else {
                    viewholder.share_button.setVisibility(View.GONE);
                    viewholder.pocket_button.setVisibility(View.GONE);
                    viewholder.read_button.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Set the share button onClickListener so that the link and title can be shared
     * to supported applications such as email, messaging apps, etc...
     * @param viewHolder
     * @param position
     */
    private void setShareOnClickListener(final ViewHolder viewHolder, final int position){
        viewHolder.share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, articles.get((int) getItemId(position)).title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, articles.get((int)getItemId(position)).link);
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(sharingIntent);
            }
        });
    }

    /**
     * Set the pocket button onClickListener so that the link can be shared to the
     * pocket application for reading later
     *
     * Throw an exception if the user does not have pocket installed on their device
     *
     * @param viewHolder
     * @param position
     */
    private void setPocketOnClickListener(final ViewHolder viewHolder, final int position){
        viewHolder.pocket_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.setClassName(context.getString(R.string.pocket_class_name), context.getString(R.string.pocket_save_activity_name));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, articles.get((int) getItemId(position)).title);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, articles.get((int) getItemId(position)).link);
                    sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(sharingIntent);
                } catch (Exception e) {
                    Toast.makeText(context, "Pocket not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Set the read button onClickListener so that the link can be opened in our
     * custom webBrowser so that the user does not have to leave our application
     * to read the article
     *
     * @param viewHolder
     * @param position
     */
    private void setReadOnClickListener(final ViewHolder viewHolder, final int position){

        viewHolder.read_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        com.zacharycalabrese.doughboy.simplenews.Activity.Web.class);

                intent.putExtra(context.getResources().getString(R.string.url_to_load),
                        articles.get((int) getItemId(position)).link);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void setTextViewTitle(ViewHolder viewHolder, String title, String description) {
        String titleToDisplay = Html.fromHtml(title).toString();

        if(!isAlphaNumeric(titleToDisplay)) {
            viewHolder.txtTitle.setText(titleToDisplay);

            String descriptionToDisplay = Html.fromHtml(description).toString().replace('\n', (char) 32)
                    .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim();

            if (!isAlphaNumeric(descriptionToDisplay) && stringDifferentThanTitle(titleToDisplay, descriptionToDisplay) && stringIsClear(descriptionToDisplay))
                viewHolder.txtDesc.setText(descriptionToDisplay);
            else
                viewHolder.txtDesc.setVisibility(View.GONE);
        }
    }

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    public boolean stringDifferentThanTitle(String string1, String string2){
        string1 = string1.toLowerCase();
        string2 = string2.toLowerCase();
        if(string1.equals(string2))
            return false;

        return true;
    }

    public boolean stringIsClear(String string){
        if(string.contains("??"))
            return false;

        return true;
    }

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
        CardView cardLayout;
        Button pocket_button;
        Button share_button;
        Button read_button;

        public ViewHolder(View v){
            txtTitle = (TextView) v.findViewById(R.id.viewholder_news_title);
            txtDesc = (TextView) v.findViewById(R.id.viewholder_news_description);
            share_button = (Button) v.findViewById(R.id.viewholder_news_share_button);
            pocket_button = (Button) v.findViewById(R.id.viewholder_news_pocket_button);
            read_button = (Button) v.findViewById(R.id.viewholder_news_read_button);
            cardLayout = (CardView) v.findViewById(R.id.viewholder_news_card_view);
        }
    }
}
