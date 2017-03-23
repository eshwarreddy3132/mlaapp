package main.com.dvb.homePageFragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.FacebookAdapter;
import main.com.dvb.adapters.NewsAdapter;
import main.com.dvb.fragments.NoNetworkFragment;
import main.com.dvb.pojos.NewsBean;
import main.com.dvb.pojos.User;
import main.com.dvb.services.WebServices;

import static main.com.dvb.Dashboard_main.context;

/**
 * Created by AIA on 12/8/16.
 */

public class NewsFragment extends Fragment implements View.OnClickListener{
    public static NewsFragment newsFragment;
    WebServices webServices;
    ProgressBar progressBar;
    RecyclerView newsRecycle;
    private FragmentManager manager;
    private FloatingActionButton fab;
    private LinearLayout emailLinear,messagesLinear,whatsAppLinear;
    private RelativeLayout floatMenuOptions;
    public   int flag = 0;
//    ArrayList<NewsBean> newList = new ArrayList<>();
    NewsAdapter newsAdapter;

    User user;
    public NewsFragment(){
        newsFragment = NewsFragment.this;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        newsRecycle = (RecyclerView) view.findViewById(R.id.newsList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarCenter);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        floatMenuOptions= (RelativeLayout) view.findViewById(R.id.floatmenu_options);
        emailLinear = (LinearLayout) view.findViewById(R.id.email_linear);
        messagesLinear= (LinearLayout) view.findViewById(R.id.message_linear);
        whatsAppLinear = (LinearLayout) view.findViewById(R.id.whatsapp_linear);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        newsRecycle.setLayoutManager(mLayoutManager);
        newsRecycle.setItemAnimator(new DefaultItemAnimator());
        fab.setOnClickListener(this);
        emailLinear.setOnClickListener(this);
        messagesLinear.setOnClickListener(this);
        whatsAppLinear.setOnClickListener(this);
       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.floatbutton_into));

                Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
                PopupMenu popup = new PopupMenu(wrapper, fab);
                popup.getMenuInflater().inflate(R.menu.float_menu,popup.getMenu());
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
                    }
                });
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Email")){
                            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto","dasyamofficial@gmail.com", null));
                            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                        }else if (item.getTitle().equals("Message")){
                            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.setData(Uri.parse("sms:"));
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            sendIntent.putExtra("address", "8886111258");
                            startActivity(sendIntent);
                        }else if (item.getTitle().equals("WhatsApp")) {
                            String phoneNumber="8105903298";
                            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
                                    Uri.parse("smsto:" + "" + phoneNumber));
                            sendIntent.setPackage("com.whatsapp");
                            startActivity(sendIntent);
                        }else {
                            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });*/
        webServices = new WebServices();
//        new FetchNews().execute(Constants.NEWS_FEEDS);
//        progressBar.setVisibility(View.GONE);
//        newsAdapter = new NewsAdapter(Constants.newsBeanArrayList);
//        newsRecycle.setAdapter(newsAdapter);


        if(Constants.newsBeanArrayList == null || Constants.newsBeanArrayList.size() ==0){
//            NewsBean newsBean = new NewsBean();
//            Constants.newsBeanArrayList.add(newsBean);
            /*if (checkIsNetworkAvailable()){
                new FetchNews().execute(Constants.NEWS_FEEDS);
            }else {
                errorMessage();
            }*/
            new FetchNews().execute(Constants.NEWS_FEEDS);
        }else{
            progressBar.setVisibility(View.GONE);
            newsAdapter = new NewsAdapter(Constants.newsBeanArrayList);
            newsRecycle.setAdapter(newsAdapter);
        }



        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.email_linear){
            flag=0;
            floatMenuOptions.setVisibility(View.GONE);
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","dasyamofficial@gmail.com", null));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }else if(v.getId() == R.id.message_linear){
            flag=0;
            floatMenuOptions.setVisibility(View.GONE);
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.putExtra("address", "8886111258");
            startActivity(sendIntent);
        }if(v.getId() == R.id.whatsapp_linear){
            flag=0;
            floatMenuOptions.setVisibility(View.GONE);
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
            String phoneNumber="8105903298";
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + "" + phoneNumber));
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }if(v.getId() == R.id.fab){
            if (flag== 0){
                flag=1;
                floatMenuOptions.setVisibility(View.VISIBLE);
                fab.setTag(R.mipmap.floatbutton_into);
                fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.floatbutton_into));
            }else {
                flag=0;
                floatMenuOptions.setVisibility(View.GONE);
                fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.float_button));
            }
        }
    }


    private class FetchNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                return webServices.getRequest(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);

            try {

                formatData(result);

                if(newsAdapter == null) {
                   newsAdapter = new NewsAdapter(Constants.newsBeanArrayList);
                    newsRecycle.setAdapter(newsAdapter);
                }else{
                    newsAdapter.resetData(Constants.newsBeanArrayList);
                    newsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void formatData(String result) throws JSONException {
            JSONArray jsonArray = new JSONArray(result);

            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                NewsBean newsBean = new NewsBean();
                newsBean.id = object.getString("id");
                newsBean.content = object.getString("message");
                newsBean.date = object.getString("inserted");
                newsBean.subject = object.getString("title");
                //newsBean.imageURL = object.getString("fileToUpload");
                String url = object.getString("fileToUpload");
                newsBean.setImageURL(url);

                Constants.newsBeanArrayList.add(newsBean);
                Log.e("mahesh",""+newsBean.getImageURL());
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
               // manager = getSupportFragmentManager();
                manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_fragment, new NoNetworkFragment());
                transaction.commit();

            }
        });
        builder.setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (checkIsNetworkAvailable()){
                    new FetchNews().execute(Constants.NEWS_FEEDS);
                }else {errorMessage();}

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
