package com.example.devel.timesapiproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devel.timesapiproject.Model.Article;

import java.util.List;

/**
 * Created by Devel on 9/23/2015.
 */
public class StoriesListAdapter extends ArrayAdapter<Article> {

    public StoriesListAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_story, parent, false);
        }


        ImageView ArticleImage = (ImageView) convertView.findViewById(R.id.ArticleImage);
        if(article.getMultimedia() != null){
            ArticleImage.setImageBitmap(article.getMultimedia().getBmp());
        }
        else {
            ArticleImage.setImageResource(R.drawable.nytico);
        }

        ArticleImage.getLayoutParams().height = 200;
        ArticleImage.getLayoutParams().width = 200;


        TextView ArticleTitle = (TextView) convertView.findViewById(R.id.ArticleTitle);
        ArticleTitle.setText(article.getTitle());

        TextView ArticleAbstract = (TextView) convertView.findViewById(R.id.ArticleAbstract);
        ArticleAbstract.setText(article.getAbstract());


        return convertView;
    }


}
