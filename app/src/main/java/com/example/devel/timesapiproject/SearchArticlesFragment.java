package com.example.devel.timesapiproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.devel.timesapiproject.Model.Article;
import com.example.devel.timesapiproject.Model.Builder;
import com.example.devel.timesapiproject.Model.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Override;import java.lang.String;import java.lang.StringBuilder;import java.lang.Void;import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Devel on 9/22/2015.
 */
public class SearchArticlesFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener, View.OnClickListener {

    InputMethodManager im;

    private EditText StartDate;
    private EditText EndDate;
    private EditText SearchParams;
    private Button Search;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        Search = (Button) rootView.findViewById(R.id.Search);
        Search.setOnClickListener(this);

        im.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        SearchParams = (EditText) rootView.findViewById(R.id.SearchParams);
        SearchParams.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) im.showSoftInput(v, 0);
                else im.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        StartDate = (EditText) rootView.findViewById(R.id.startDate);
        StartDate.setOnFocusChangeListener(this);

        EndDate = (EditText) rootView.findViewById(R.id.endDate);
        EndDate.setOnFocusChangeListener(this);

        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NewYorkTimes.ttf");
        TextView Title = (TextView) rootView.findViewById(R.id.SearchFragmentTitle);
        Title.setTypeface(font);

        return rootView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear++;

        if (StartDate.isFocused())
            StartDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);

        if (EndDate.isFocused())
            EndDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            android.support.v4.app.DialogFragment dialog = new DatePickerFragment();
            dialog.setTargetFragment(SearchArticlesFragment.this, 0);
            dialog.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public void onClick(View v) {
        String query = SearchParams.getText().toString();
        String startDate = StartDate.getText().toString();
        String endDate = EndDate.getText().toString();

        SearchStoriesTask searchResults = new SearchStoriesTask(this);
        searchResults.execute(query, startDate, endDate);
    }

    public static class SearchStoriesTask extends AsyncTask<String, Void, String> {

        private static final String ApiKey = "e5d3c6e648895286038a9e6f21e00018:17:72990716";
        private static final String SearchBaseUrl = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        private final SearchArticlesFragment fragment;

        public SearchStoriesTask(SearchArticlesFragment searchArticlesFragment) {
            this.fragment = searchArticlesFragment;
        }

        private String getDateFormat(String s) {
            String[] res = s.split("/");

            if (res[0].length() == 1) res[0] = "0" + res[0];
            if (res[1].length() == 1) res[1] = "0" + res[1];

            s = res[2] + res[0] + res[1];

            return s;

        }

        @Override
        protected void onPostExecute(String s) {

            Intent intent = new Intent( fragment.getActivity(), SearchResultActivity.class);
            intent.putExtra("jsonResult", s);
            fragment.startActivity(intent);
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<Article> result = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            String jsonResult = null;

            try {
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.encodedPath(SearchBaseUrl);

                String startDate = params[1];
                String endDate = params[2];

                if (!params[0].equals("")) uriBuilder.appendQueryParameter("q", params[0]);
                if (!params[1].equals("")) {
                    startDate = getDateFormat(startDate);
                    uriBuilder.appendQueryParameter("begin_date", startDate);
                }
                if (!params[2].equals("")) {
                    endDate = getDateFormat(endDate);
                    uriBuilder.appendQueryParameter("end_date", endDate);
                }
                uriBuilder.appendQueryParameter("api-key", ApiKey);

                URL url = new URL(uriBuilder.build().toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder responseString = new StringBuilder();

                String input;
                while ((input = streamReader.readLine()) != null) {
                    responseString.append(input);
                }

                jsonResult = responseString.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return jsonResult;
        }
    }
}
