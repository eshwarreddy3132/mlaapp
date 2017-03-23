package main.com.dvb.homePageFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.EndlessRecyclerViewScrollListener;
import main.com.dvb.R;
import main.com.dvb.fragments.NoNetworkFragment;
import main.com.dvb.services.WebServices;
import main.com.dvb.adapters.FacebookAdapter;
import main.com.dvb.pojos.FB.FBCommentBean;
import main.com.dvb.pojos.FB.FBCommentBeanParent;
import main.com.dvb.pojos.FB.FBLikeBean;
import main.com.dvb.pojos.FB.FBLikesBeanParent;
import main.com.dvb.pojos.FB.FacebookBean;
import main.com.dvb.pojos.FB.FacebookBeanParent;

/**
 * Created by AIA on 11/11/16.
 */

public class FacebookFragment extends Fragment{

    public static FacebookFragment facebookFragment;

    public AccessToken accessToken;
    public WebServices webServices;

    CallbackManager callbackManager;
//    FacebookBeanParent facebookBeanParent;
    RecyclerView facebookRecycle;

    private EndlessRecyclerViewScrollListener scrollListener;
    ArrayList<FacebookBean> facebookBeanArrayList;
    FacebookAdapter facebookAdapter;
    ProgressBar progressBar,progressBarCenter;
    LoginButton loginButton;
//    public Bitmap profileImage;
    String DIRECTORY_PATH = Environment.getExternalStorageDirectory().toString();
    FileWriter writer = null;

    public FacebookFragment() {
        // Required empty public constructor
        facebookFragment = FacebookFragment.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.facebook_layout, container, false);
        if (checkIsNetworkAvailable()){
            Log.e("if fb network check","available");
        }else { Log.e("else fb network check","not available");
            errorMessage();
        }

        webServices = new WebServices();
        facebookBeanArrayList = new ArrayList<>();

        File root = new File(DIRECTORY_PATH);
        File gpxfile = new File(root, "WarangalLogs.txt");

        try {
            writer = new FileWriter(gpxfile);

        } catch (IOException e) {
            e.printStackTrace();
        }


        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarCenter = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        facebookRecycle = (RecyclerView) view.findViewById(R.id.facebookList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(((Dashboard_main)Dashboard_main.context).getApplicationContext());
        facebookRecycle.setLayoutManager(mLayoutManager);
        facebookRecycle.setItemAnimator(new DefaultItemAnimator());
        loginButton.setReadPermissions(Arrays.asList("email"));//,"user_likes","publish_pages","public_profile","publish_actions"

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi(page);
                Log.e("","");
                progressBar.setVisibility(View.VISIBLE);
                new DownloadWebpageTask().execute(Constants.facebookBeanParent.getNext());
            }
        };
        // Adds the scroll listener to RecyclerView
        facebookRecycle.addOnScrollListener(scrollListener);
        // If using in a fragment
        loginButton.setFragment(this);
//        if(!isFBLoggedIn()) {
//            loginButton.performClick();
//        }else {
//            String url = "https://graph.facebook.com/"+Constants.facebookPageOwner+"/posts?access_token="+accessToken.getToken();
//            new DownloadWebpageTask().execute(url);
//        }


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = AccessToken.getCurrentAccessToken();
                new LoadProfilePicture().execute("https://graph.facebook.com/"+ Constants.facebookPageOwner+"/picture?type=small");
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("","");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("","");
            }});

                        if(!isFBLoggedIn()) {
                    loginButton.performClick();
                }else {
                            if(Constants.facebookBeanParent.facebookBeanArrayList == null || Constants.facebookBeanParent.facebookBeanArrayList.size() == 0){
                                new LoadProfilePicture().execute("https://graph.facebook.com/"+Constants.facebookPageOwner+"/picture?type=small");
                            }else{
                                progressBarCenter.setVisibility(View.GONE);
                                facebookBeanArrayList.addAll(Constants.facebookBeanParent.facebookBeanArrayList);
                                facebookAdapter = new FacebookAdapter(Constants.facebookBeanParent.facebookBeanArrayList);
                                facebookRecycle.setAdapter(facebookAdapter);
                            }


                }


        return view;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isFBLoggedIn(){
        accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null){
            return false;
        }else{
            Log.e("","ACCESS TOKET    "+accessToken.getToken());
            return true;
        }
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            //probably orientation change
//            facebookBeanArrayList = (ArrayList<FacebookBean>) savedInstanceState.getSerializable("list");
//            facebookAdapter.notifyDataSetChanged();
//        } else {
//
//            try {
//                formatData(new JSONObject(Constants.SAMPLE_DATA));
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
////                if(!isFBLoggedIn()) {
////                    loginButton.performClick();
////                }else {
//////                        new LoadProfilePicture().execute("https://graph.facebook.com/"+Constants.facebookPageOwner+"/picture?type=small");
////                    try {
////                        formatData(new JSONObject(Constants.SAMPLE_DATA));
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("","");
//        if(!isFBLoggedIn()) {
//            loginButton.performClick();
//        }else {
////            String url = "https://graph.facebook.com/"+Constants.facebookPageOwner+"/posts?access_token="+accessToken.getToken();
////            new DownloadWebpageTask().execute(url);
//            if(facebookBeanParent == null)
//                new LoadProfilePicture().execute("https://graph.facebook.com/"+Constants.facebookPageOwner+"/picture?type=small");
//            else{
//                facebookAdapter.resetData(facebookBeanParent.facebookBeanArrayList);
//                facebookAdapter.notifyDataSetChanged();
//            }
//
//        }
    }


    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return webServices.getRequest(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            progressBarCenter.setVisibility(View.GONE);
            if(!result.equalsIgnoreCase("Unable to retrieve web page. URL may be invalid.")){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    try {
                        formatData(jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                noInternetConnection();
            }


        }
    }

    private class LoadProfilePicture extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Constants.profileImage = getBitmapFromURL(urls[0]);

            return "";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(Constants.profileImage == null){
                noInternetConnection();

            }
            Bundle params = new Bundle();
            params.putString("fields", "message,created_time,id,full_picture,status_type,source,comments.summary(true),likes.summary(true),link");//full_picture
            params.putString("limit", "10");


            /* make the API call */
            new GraphRequest(accessToken, "/dasyam.vinayabhaskar/posts", params, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                        /* handle the result */
                            progressBarCenter.setVisibility(View.GONE);
                            System.out.println("Festival Page response::" + String.valueOf(response.getJSONObject()));

                            try {
//                                JSONObject jObjResponse = new JSONObject(Constants.SAMPLE_DATA);
                                JSONObject jObjResponse = new JSONObject(String.valueOf(response.getJSONObject()));
                                formatData(jObjResponse);
                                Log.e("","");
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
            ).executeAsync();

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
                new LoadProfilePicture().execute("https://graph.facebook.com/"+Constants.facebookPageOwner+"/picture?type=small");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }
    private void formatData(JSONObject jsonObject) throws IOException {
        try {

//            facebookBeanParent = new FacebookBeanParent();


            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0 ; i<jsonArray.length() ; i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String story = "";
                String message = "";
                String picture = "";
//                ArrayList<FBLikeBean> fbLikeList = new ArrayList<>();
//                ArrayList<FBCommentBean> fbCommentList = new ArrayList<>();


                if (object.has("message"))
                    message = object.getString("message");
                String createdTime = object.getString("created_time");
                String id = object.getString("id");

                if (object.has("status_type"))
                    story = object.getString("status_type");
                if (object.has("full_picture"))
                    picture = object.getString("full_picture");

                FBLikesBeanParent fbLikesBeanParent = new FBLikesBeanParent();
                if (object.has("likes")) {
                    JSONObject likesObj = object.getJSONObject("likes");
//                    JSONArray likeArray = likesObj.getJSONArray("data");
//                    if(likeArray != null){
//                        for (int j = 0; j < likeArray.length(); j++) {
//                            JSONObject likeObj2 = likeArray.getJSONObject(i);
//                            FBLikeBean fbLikeBean = new FBLikeBean();
//                            fbLikeBean.likeId = likeObj2.getString("id");
//                            fbLikeBean.likeName = likeObj2.getString("name");
//                            fbLikeList.add(fbLikeBean);
//                        }
//                    }

//                    fbLikesBeanParent.fbLikeBeen = fbLikeList;
                    fbLikesBeanParent.fbLikeBeen = new ArrayList<FBLikeBean>();
//                    if(likesObj.has("paging")){
//                        JSONObject likePagingObj = likesObj.getJSONObject("paging");
//
//                        if(likePagingObj.has("cursors")){
//                            JSONObject likeCursorObj = likePagingObj.getJSONObject("cursors");
//                            fbLikesBeanParent.beforeCursor = likeCursorObj.getString("before");
//                            fbLikesBeanParent.afterCursor = likeCursorObj.getString("after");
//                        }else{
//                            fbLikesBeanParent.beforeCursor = "";
//                            fbLikesBeanParent.afterCursor = "";
//                        }
//
//                        if(likePagingObj.has("next")){
//                            fbLikesBeanParent.likeNext = likePagingObj.getString("next");
//                        }else{
//                            fbLikesBeanParent.likeNext = "";
//                        }
//
//                    }else{
                        fbLikesBeanParent.beforeCursor = "";
                        fbLikesBeanParent.afterCursor = "";
//                        fbLikesBeanParent.fbLikeBeen = new ArrayList<FBLikeBean>();
//                    }
                    if(likesObj.has("summary")){
                        JSONObject likeSummaryObj = likesObj.getJSONObject("summary");
                        fbLikesBeanParent.totalCount = likeSummaryObj.getInt("total_count");
                        fbLikesBeanParent.canLike = likeSummaryObj.getBoolean("can_like");
                        fbLikesBeanParent.hasLiked = likeSummaryObj.getBoolean("has_liked");
                    }else{
                        fbLikesBeanParent.totalCount = 0;
                        fbLikesBeanParent.canLike = true;
                        fbLikesBeanParent.hasLiked = false;
                    }


                } else {
                    fbLikesBeanParent.beforeCursor = "";
                    fbLikesBeanParent.afterCursor = "";

                    fbLikesBeanParent.likeNext = "";
                    fbLikesBeanParent.fbLikeBeen = new ArrayList<FBLikeBean>();

                    fbLikesBeanParent.totalCount = 0;
                    fbLikesBeanParent.canLike = true;
                    fbLikesBeanParent.hasLiked = false;

                    writer.append(jsonObject.toString());
                    writer.flush();
                    writer.close();
                }

                FBCommentBeanParent fbCommentBeanParent = new FBCommentBeanParent();
                if(object.has("comments")){
                JSONObject commentObj = object.getJSONObject("comments");
//                JSONArray commentArray = commentObj.getJSONArray("data");
//                for (int j = 0; j < commentArray.length(); j++) {
//                    JSONObject commentObj2 = commentArray.getJSONObject(j);
//                    FBCommentBean fbCommentBean = new FBCommentBean();
//                    fbCommentBean.createdTime = commentObj2.getString("created_time");
//                    fbCommentBean.message = commentObj2.getString("message");
//                    fbCommentBean.commentId = commentObj2.getString("id");
//
//                    JSONObject obj = commentObj2.getJSONObject("from");
//                    fbCommentBean.userNameId = obj.getString("id");
//                    fbCommentBean.userName = obj.getString("name");
//
//                    fbCommentList.add(fbCommentBean);
//                }
//                    fbCommentBeanParent.commentsList = fbCommentList;
                    fbCommentBeanParent.commentsList = new ArrayList<FBCommentBean>();
//                    if(commentObj.has("paging")){
//                        JSONObject commentPagingObj = commentObj.getJSONObject("paging");
//                        if(commentPagingObj.has("cursors")){
//                            JSONObject commentCursorObj = commentPagingObj.getJSONObject("cursors");
//                            fbCommentBeanParent.after = commentCursorObj.getString("after");
//                            fbCommentBeanParent.before = commentCursorObj.getString("before");
//                        }else{
//                            fbCommentBeanParent.after ="";
//                            fbCommentBeanParent.before = "";
//                        }
//
//
//                        if(commentPagingObj.has("next")){
//                            fbCommentBeanParent.next = commentPagingObj.getString("next");
//                        }else{
//                            fbCommentBeanParent.next = "";
//                        }
//                    }else{
                        fbCommentBeanParent.after = "";
                        fbCommentBeanParent.before = "";
                        fbCommentBeanParent.next = "";
//                    }
                    if(commentObj.has("summary")){
                        JSONObject commentSummaryObj = commentObj.getJSONObject("summary");
                        fbCommentBeanParent.order = commentSummaryObj.getString("order");
                        fbCommentBeanParent.totalCount = commentSummaryObj.getInt("total_count");
                        fbCommentBeanParent.canComment = commentSummaryObj.getBoolean("can_comment");
                    }else{
                        fbCommentBeanParent.order = "";
                        fbCommentBeanParent.totalCount = 0;
                        fbCommentBeanParent.canComment = true;
                    }

            }else{
                    fbCommentBeanParent.after = "";
                    fbCommentBeanParent.before = "";
                    fbCommentBeanParent.next = "";
                    fbCommentBeanParent.commentsList = new ArrayList<FBCommentBean>();
                    fbCommentBeanParent.order = "";
                    fbCommentBeanParent.totalCount = 0;
                    fbCommentBeanParent.canComment = true;
                }
                FacebookBean facebookBean = new FacebookBean();
                facebookBean.userMessage = message;
                facebookBean.userCreated_time = createdTime;
                facebookBean.userId = id;
                facebookBean.userStory = story;
                facebookBean.fbLikesBeanParent = fbLikesBeanParent;
                facebookBean.fbCommentBeanParent = fbCommentBeanParent;
                facebookBean.picture = picture;

                facebookBeanArrayList.add(facebookBean);
            }

            JSONObject object = jsonObject.getJSONObject("paging");
            if(object.has("previous")) {
                String previousPage = object.getString("previous");
                Constants.facebookBeanParent.previous = previousPage;
            }
            if(object.has("next")){
                String nextPage = object.getString("next");
                Constants.facebookBeanParent.next = nextPage;
            }else{
                Constants.facebookBeanParent.next = "";
            }

            Constants.facebookBeanParent.facebookBeanArrayList = facebookBeanArrayList;


        } catch (JSONException e) {
            writer.append(jsonObject.toString());
            writer.flush();
            writer.close();
            e.printStackTrace();
        }
        if(facebookAdapter == null) {
            facebookAdapter = new FacebookAdapter(Constants.facebookBeanParent.facebookBeanArrayList);
            facebookRecycle.setAdapter(facebookAdapter);
        }else{
            facebookAdapter.resetData(Constants.facebookBeanParent.facebookBeanArrayList);
            facebookAdapter.notifyDataSetChanged();
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
