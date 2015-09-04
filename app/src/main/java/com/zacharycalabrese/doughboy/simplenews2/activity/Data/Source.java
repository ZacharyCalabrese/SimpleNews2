package com.zacharycalabrese.doughboy.simplenews2.activity.Data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcalabrese on 9/1/15.
 */
public class Source {

    public Source() {

    }

    public String[] getCategories(){
        String[] resultsToReturn;

        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.findWithQuery(
                        com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.class,
                        "Select distinct(category) from Source where subscribed = 1;");

        resultsToReturn = new String[results.size()];
        for(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source item : results)
            resultsToReturn[results.indexOf(item)] = item.category;

        return resultsToReturn;
    }

    public ArrayList<com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source> getSubscriptions() {
        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.class,
                        "subscribed = ?", "1");

        ArrayList<com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source> returningList =
                new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source item : results) {
            com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source newItem =
                    new com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source(
                            item.name, item.rssUrl, item.category, item.subscribed, item);

            returningList.add(newItem);
        }

        return returningList;
    }

    public void deleteAll() {
        com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.deleteAll(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.class);
    }

    public Boolean addSource(com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source source) {
        if (!doesSourceExist(source.name)) {
            com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source newSource =
                    new com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source(source);

            newSource.save();
            return true;
        }

        return false;
    }

    private Boolean doesSourceExist(String name) {
        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.class, "name = ?",
                        name);

        if (results.size() > 0)
            return true;
        else
            return false;
    }
}
