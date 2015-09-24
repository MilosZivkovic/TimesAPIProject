package com.example.devel.timesapiproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devel.timesapiproject.Model.Article;
import com.example.devel.timesapiproject.Model.Builder;
import com.example.devel.timesapiproject.Model.Image;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;
import java.lang.Override;import java.lang.String;import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Devel on 9/23/2015.
 */
public class SearchResultActivity extends Activity {

    List<Article> Results = null;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        final Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/NewYorkTimes.ttf");
        TextView Title = (TextView) findViewById(R.id.SearchResultsTitle);
        Title.setTypeface(font);

        Intent i = getIntent();
        String jsonResult = i.getStringExtra("jsonResult");
        try {
            Results = SearchResultActivity.DecodeJSON(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
            Results = Collections.emptyList();
        }

        adapter = new StoriesListAdapter(this, R.layout.listitem_story, Results);
        ListView list = (ListView) findViewById(R.id.SearchResults);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = parent.getItemAtPosition(position).toString();
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }

    private static List<Article> DecodeJSON(String jsonResult) throws JSONException {
        List<Article> result = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        String base_url = "http://www.nytimes.com/";

        try{
            JsonNode root = mapper.readTree(jsonResult);
            JsonNode docsNode = root.get("response").get("docs");
            for(JsonNode doc : docsNode){
                Image image = null;
                JsonNode multimediaNode = doc.get("multimedia");
                if(multimediaNode.size() != 0){
                    for (JsonNode imageNode : multimediaNode){
                        String url = base_url + imageNode.get("url").asText();
                        URL link = new URL(url);
                        Bitmap bmp = BitmapFactory.decodeStream(link.openConnection().getInputStream());
                        image = Builder.buildImage(url, imageNode.get("height").asDouble(), imageNode.get("width").asDouble(), bmp);
                        break;
                    }
                }

                Article article = Builder.buildArticle(doc.get("headline").get("main").asText(), doc.get("snippet").asText(), doc.get("web_url").asText(), image);
                result.add(article);
            }

        } catch (IOException e) {
            e.printStackTrace();
            result = Collections.emptyList();
        }


        return result;
    }
}
