package main.com.dvb.homePageFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.EndlessRecyclerViewScrollListener;
import main.com.dvb.R;
import main.com.dvb.adapters.TwitterAdapter;
import main.com.dvb.pojos.twitter.Authenticated;
import main.com.dvb.pojos.twitter.Tweet;
import main.com.dvb.pojos.twitter.Twitter;
import main.com.dvb.services.WebServices;

import static main.com.dvb.Constants.TwitterStreamURL;

/**
 * Created by AIA on 11/11/16.
 */

public class TwitterFragment extends Fragment {

    public static TwitterFragment twitterFragment;
    RecyclerView twitterRecyclerView;
    ProgressBar loadMoreProgress;
    ProgressBar progressBarCenter;

    private static final int SEARCH_COUNT = 20;
    private boolean isNotFirst;

    WebServices webServices;
    private EndlessRecyclerViewScrollListener scrollListener;
    TwitterAdapter twitterAdapter;

    public TwitterFragment() {
        // Required empty public constructor
        twitterFragment = TwitterFragment.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.twitter_layout, container, false);
        loadMoreProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        twitterRecyclerView = (RecyclerView) view.findViewById(R.id.twitterList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(((Dashboard_main)Dashboard_main.context).getApplicationContext());
        twitterRecyclerView.setLayoutManager(mLayoutManager);
        twitterRecyclerView.setItemAnimator(new DefaultItemAnimator());

        webServices = new WebServices();
        if (checkIsNetworkAvailable()){
            if(Constants.twitter == null || Constants.twitter.size() == 0){
                new FetchTweetsAsync().execute();
            }else{
                progressBarCenter.setVisibility(View.GONE);
                isNotFirst = true;
                twitterAdapter = new TwitterAdapter(Constants.twitter);
                twitterRecyclerView.setAdapter(twitterAdapter);
            }
            Log.e("if twitt network check","available");
        }else { Log.e("else twt network check","not available");
            errorMessage();
        }



        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreProgress.setVisibility(View.VISIBLE);
                new LoadMoreTweets().execute(Constants.twitter.get(Constants.twitter.size()-1).getId());
            }
        };
        // Adds the scroll listener to RecyclerView
        twitterRecyclerView.addOnScrollListener(scrollListener);

        return view;
    }


    public class FetchTweetsAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String result = null;

            result = getTwitterStream();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarCenter.setVisibility(View.GONE);
            Twitter twits = jsonToTwitter(result);

            isNotFirst = true;
            twitterAdapter = new TwitterAdapter(Constants.twitter);
            twitterRecyclerView.setAdapter(twitterAdapter);
            // send the tweets to the adapter for rendering
//            ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(Dashboard_main.context, android.R.layout.simple_list_item_1, twits);
//            twitterListView.setAdapter(adapter);
        }
    }

    public void noInternetConnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert.!");
        builder.setMessage("Unable to connect server. Please check your internet connection.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                progressBarCenter.setVisibility(View.VISIBLE);
                if(isNotFirst){
                    new LoadMoreTweets().execute(Constants.twitter.get(Constants.twitter.size()-1).getId());
                }else{
                    new FetchTweetsAsync().execute();
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }

    // converts a string of JSON data into a Twitter object
    private Twitter jsonToTwitter(String result) {
        Twitter twits = null;

        if (result != null && result.length() > 0) {
            try {
                Gson gson = new Gson();
                twits = gson.fromJson(result, Twitter.class);
            } catch (IllegalStateException ex) {
                // just eat the exception
            }
        }

        if(isNotFirst){
            twits.remove(0);
        }

        Constants.twitter.addAll(twits);
        return twits;
    }

    private String getTwitterStream() {
        String resultValue = "";

        try {
            String data = URLEncoder.encode("grant_type", "UTF-8")
                    + "=" + URLEncoder.encode("client_credentials", "UTF-8");


            webServices.postRequest_Twitter(data, Constants.TwitterTokenURL);

            if (Constants.authenticated != null && Constants.authenticated.token_type.equals("bearer")) {

                resultValue = webServices.getRequest_twitter(TwitterStreamURL + Constants.twitterPageOwner+"&count=10");

            }

        } catch (UnsupportedEncodingException ex) {
        } catch (IllegalStateException ex1) {
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultValue;
    }

    public class LoadMoreTweets extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String resultValue="";
            try {
                resultValue = webServices.getRequest_twitter(TwitterStreamURL + Constants.twitterPageOwner+"&max_id="+params[0]+"&count=10");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultValue;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equalsIgnoreCase("")){
                noInternetConnection();
            }else{
                loadMoreProgress.setVisibility(View.GONE);
                jsonToTwitter(result);
                twitterAdapter.resetData(Constants.twitter);
                twitterAdapter.notifyDataSetChanged();
            }

        }
    }
    //Diolog for showing network information
    public void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert.!");
        builder.setMessage("Unable to connect server. Please check your internet connection.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                progressBarCenter.setVisibility(View.GONE);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }
    public boolean checkIsNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
