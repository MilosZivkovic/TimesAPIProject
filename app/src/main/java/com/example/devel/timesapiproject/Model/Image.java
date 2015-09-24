package com.example.devel.timesapiproject.Model;

import android.graphics.Bitmap;

/**
 * Created by Devel on 9/20/2015.
 */
public class Image {

    protected String url;

    protected double height;

    protected double width;

    protected Bitmap bmp;


    public Image() {
    }

    public Image(String url, double height, double width, Bitmap bmp) {
        this.url = url;
        this.height = height;
        this.width = width;
        this.bmp = bmp;
    }

    public String getUrl() {
        return url;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
