package com.example.devel.timesapiproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.devel.timesapiproject.Model.Article;
import com.example.devel.timesapiproject.Model.Builder;
import com.example.devel.timesapiproject.Model.Image;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Override;import java.lang.String;import java.lang.StringBuilder;import java.lang.Void;import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Devel on 9/18/2015.
 */
public class TopArticlesFragment extends Fragment {

    private ArrayList<Article> Articles;
    private ListView Items;
    private ArrayAdapter<Article> adapter;
    private Section selectedSection;
    TopArticlesTask stories;
    public Section section;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        section = Section.world;
        super.onCreate(savedInstanceState);
        updateTopStories();
    }

    private void updateTopStories() {
        Articles = new ArrayList<>();
        stories = new TopArticlesTask();
        stories.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liststories, container, false);

        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NewYorkTimes.ttf");
        TextView Title = (TextView) view.findViewById(R.id.ArticlesFragmentTitle);
        Title.setTypeface(font);

        adapter = new StoriesListAdapter(this.getActivity(), R.layout.listitem_story, Articles);
        Items = (ListView) view.findViewById(R.id.Items);
        Items.setAdapter(adapter);
        Items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = parent.getItemAtPosition(position).toString();
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        List<String> sections = getSections();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sections);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner categories = (Spinner) view.findViewById(R.id.categories);
        categories.setAdapter(spinnerAdapter);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSection(Section.valueOf(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return view;
    }

    private void setSection(Section section) {
        this.section = section;
        Log.d("NewSection", section.toString());
        updateTopStories();
        adapter.notifyDataSetChanged();
    }

    protected enum Section {
        home, world, national, politics, nyregion, business,
        opinion, technology, science, health, sports, arts,
        fashion, dining, travel, magazine, realestate;


        @Override
        public String toString() {
            return super.toString();
        }
    }



    public static ArrayList<String> getSections(){
        ArrayList<String> list = new ArrayList<>();
        for(Section s : Section.values()){
            list.add(s.name());
        }
        return list;
    }

    /***
     * AsyncTask that fetches all articles from API
     */
    public class TopArticlesTask extends AsyncTask<String, Void, ArrayList<Article>> {

        private static final String ApiKey = "3a21b57fbdd45d931d9f2597f109e85d:17:72990716";
        private static final String TopStoriesUrl = "http://api.nytimes.com/svc/topstories/v1/";

        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            String jsonResult;
            HttpURLConnection urlConnection;
            ArrayList<Article> result = new ArrayList<>();

            try {
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.encodedPath(TopStoriesUrl + section.name() + ".json");
                uriBuilder.appendQueryParameter("api-key", ApiKey);

                URL url = new URL(uriBuilder.build().toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseString = new StringBuilder();

                String input;
                while((input = streamReader.readLine()) != null){
                    responseString.append(input);
                }

                jsonResult = responseString.toString();

                try{
                    result = DecodeJSON(jsonResult);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        private ArrayList<Article> DecodeJSON(String jsonResult) throws JSONException{

            ArrayList<Article> result = null;
            try {
                result = new ArrayList<>();
                final JsonNode rootNode = JSON_MAPPER.readTree(jsonResult);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ObjectMapper mapper = new ObjectMapper();

            try {
                JsonNode node = mapper.readTree(jsonResult);
                JsonNode resultNode = node.get("results");
                for ( JsonNode articleNode : resultNode) {
                    Image image = null;

                    JsonNode multimediaNode = articleNode.get("multimedia");
                    for ( JsonNode multimediaNodeIntern : multimediaNode) {
                        String url = multimediaNodeIntern.get("url").asText();
                        URL link = new URL(url);
                        Bitmap bmp = BitmapFactory.decodeStream(link.openConnection().getInputStream());

                        image = Builder.buildImage(url,
                                multimediaNodeIntern.get("height").asDouble(),
                                multimediaNodeIntern.get("width").asDouble(), bmp);
                        break;
                    }

                    Article article = Builder.buildArticle(articleNode.get("title").asText(), articleNode.get("abstract").asText(), articleNode.get("url").asText(), image);
                    result.add(article);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Collections.emptyList();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            super.onPostExecute(articles);
            if(articles != null){
                adapter.clear();
                for(Article article : articles){
                    adapter.add(article);
                }
            }

        }
    }
}
