package com.example.devel.timesapiproject.Model;

/**
 * Created by Devel on 9/20/2015.
 */
public class Article {

    protected String Title;

    protected String Abstract;

    protected String Url;

    protected Image multimedia;

    public Article() {
    }

    public Article(String title, String anAbstract, String url, Image multimedia) {
        Title = title;
        Abstract = anAbstract;
        Url = url;
        this.multimedia = multimedia;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Image getMultimedia() {
        return multimedia;
    }

    @Override
    public String toString() {
        return Url;
    }
}
