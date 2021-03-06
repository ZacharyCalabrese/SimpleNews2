package com.zacharycalabrese.doughboy.simplenews.Data;

import java.util.ArrayList;
import java.util.List;

public class Source {

    public Source() {

    }

    public String[] getCategoriesWithoutSubscribed() {
        String[] resultsToReturn;

        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.findWithQuery(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "Select distinct(category) from Source;");

        resultsToReturn = new String[results.size()];
        for (com.zacharycalabrese.doughboy.simplenews.Model.Source item : results)
            resultsToReturn[results.indexOf(item)] = item.category;

        return resultsToReturn;
    }

    public String[] getCategories() {
        String[] resultsToReturn;

        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.findWithQuery(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "Select distinct(category) from Source where subscribed = 1;");

        resultsToReturn = new String[results.size()];
        for (com.zacharycalabrese.doughboy.simplenews.Model.Source item : results)
            resultsToReturn[results.indexOf(item)] = item.category;

        return resultsToReturn;
    }

    public ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source>
    getSourceByCategory(String category) {


        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "category = ?", category);

        ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source> returningList =
                new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews.Model.Source item : results) {
            com.zacharycalabrese.doughboy.simplenews.Helper.Source newItem =
                    new com.zacharycalabrese.doughboy.simplenews.Helper.Source(
                            item.name, item.rssUrl, item.category, item.subscribed, item);

            returningList.add(newItem);
        }

        return returningList;
    }

    public ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source> getAllSources() {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.findWithQuery(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "Select * from Source order by category;");

        ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source> returningList =
                new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews.Model.Source item : results) {
            com.zacharycalabrese.doughboy.simplenews.Helper.Source newItem =
                    new com.zacharycalabrese.doughboy.simplenews.Helper.Source(
                            item.name, item.rssUrl, item.category, item.subscribed, item);

            returningList.add(newItem);
        }

        return returningList;
    }

    public ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source> getSubscriptions() {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "subscribed = ?", "1");

        ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source> returningList =
                new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews.Model.Source item : results) {
            com.zacharycalabrese.doughboy.simplenews.Helper.Source newItem =
                    new com.zacharycalabrese.doughboy.simplenews.Helper.Source(
                            item.name, item.rssUrl, item.category, item.subscribed, item);

            returningList.add(newItem);
        }

        return returningList;
    }

    public com.zacharycalabrese.doughboy.simplenews.Helper.Source
    editSource(com.zacharycalabrese.doughboy.simplenews.Helper.Source source,
               String newName, String newUrl, String category) {

        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "name = ?", source.name);

        if (results.size() < 1)
            return source;

        if (!doesSourceWithNameExist(newName) && newName.length() > 4) {
            results.get(0).name = newName;
        }

        if (!doesSourceWithUrlExist(newUrl) && newUrl.length() > 9) {
            results.get(0).rssUrl = newUrl;
        }

        results.get(0).category = category;

        results.get(0).save();

        return new com.zacharycalabrese.doughboy.simplenews.Helper.Source(results.get(0));
    }

    public void unsubscribe(String name) {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> source =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "name = ?", name);

        if (source.size() == 1) {
            source.get(0).subscribed = false;
            source.get(0).save();
        }
    }

    public void subscribe(String name) {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> source =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class,
                        "name = ?", name);

        if (source.size() == 1) {
            source.get(0).subscribed = true;
            source.get(0).save();
        }
    }

    public void deleteAll() {
        com.zacharycalabrese.doughboy.simplenews.Model.Source.deleteAll(com.zacharycalabrese.doughboy.simplenews.Model.Source.class);
    }

    public Boolean addSource(com.zacharycalabrese.doughboy.simplenews.Helper.Source source) {
        if (!doesSourceWithNameExist(source.name)) {
            com.zacharycalabrese.doughboy.simplenews.Model.Source newSource =
                    new com.zacharycalabrese.doughboy.simplenews.Model.Source(source);

            newSource.save();
            return true;
        }

        return false;
    }

    public Boolean addNewSource(String name, String url, String category) {

        if (!doesSourceWithNameExist(name) && name.length() > 4
                && !doesSourceWithUrlExist(url) && url.length() > 9) {

            com.zacharycalabrese.doughboy.simplenews.Model.Source newSource =
                    new com.zacharycalabrese.doughboy.simplenews.Model.Source(name, url, category, true);

            newSource.save();
            return true;
        }

        return false;
    }

    private Boolean doesSourceWithNameExist(String name) {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class, "name = ?",
                        name);

        return results.size() > 0;
    }

    private Boolean doesSourceWithUrlExist(String url) {
        List<com.zacharycalabrese.doughboy.simplenews.Model.Source> results =
                com.zacharycalabrese.doughboy.simplenews.Model.Source.find(
                        com.zacharycalabrese.doughboy.simplenews.Model.Source.class, "RSS_URL = ?",
                        url);

        return results.size() > 0;
    }
}
