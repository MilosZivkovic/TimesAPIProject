package com.example.devel.timesapiproject.Model;


import android.graphics.Bitmap;

/**
 * Created by Devel on 9/22/2015.
 */
public class Builder {


    public static Image buildImage(String jsonUrl, Double jsonHeight, Double jsonWidth, Bitmap bmp) {
        final String url = jsonUrl != null ? jsonUrl : null;
        final Double height = jsonHeight != null ? jsonHeight : 0;
        final Double width = jsonWidth != null ? jsonWidth : 0;
        return new Image(url, height, width, bmp);

    }

    public static Article buildArticle(String jsonTitle, String jsonAbstractText, String jsonUrl, Image jsonImage) {
        final String title = jsonTitle != null ? jsonTitle : null;
        final String abstractText = jsonAbstractText != null ? jsonAbstractText : null;
        final String url = jsonUrl != null ? jsonUrl : null;

        return new Article(title, abstractText, url, jsonImage);
    }


}
