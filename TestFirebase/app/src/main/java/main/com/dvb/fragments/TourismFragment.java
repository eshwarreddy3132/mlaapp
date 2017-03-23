package main.com.dvb.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.ViewPagerAdapter;
import main.com.dvb.navigationFragments.Feedback;
import main.com.dvb.services.WebServices;

public class TourismFragment extends Fragment {

    public static TourismFragment simpleFragment;
    private WebView text;
    SharedPreferences sharedPreferences;
    String FCM_RegisterID="";
    WebServices webServices;
    EditText tourism_edit;
    TextView textView;
    ProgressBar progressBar;

    public TourismFragment() {
        simpleFragment = TourismFragment.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tourism, container, false);
        ImageView warangel = (ImageView) view.findViewById(R.id.warangal);
        Button submit_feedback = (Button) view.findViewById(R.id.submit_tourism);
        tourism_edit = (EditText) view.findViewById(R.id.tourism_edit);
        text = (WebView) view.findViewById(R.id.webviewtourism_data);
        textView = (TextView) view.findViewById(R.id.textviewtourism_data);
        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            String tourism=getResources().getString(R.string.tourism);
            text.loadData("<p style=\"text-align: justify\">"+ tourism +"</p>", "text/html", "UTF-8");
        }else{
            text.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        warangel.getLayoutParams().width = Dashboard_main.width/3;
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        sharedPreferences = Dashboard_main.context.getSharedPreferences(Constants.SHARED_PREF,0);
        FCM_RegisterID = sharedPreferences.getString("regId","");
        webServices = new WebServices();


        submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tourism_edit.getText() == null){
                    Toast.makeText(Dashboard_main.context, "Please enter your feedback", Toast.LENGTH_LONG).show();
                    return;
                }
                String feedBackText = tourism_edit.getText().toString();
                InputMethodManager imm = (InputMethodManager)Dashboard_main.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                progressBar.setVisibility(View.VISIBLE);

                String formPostData ="";
                try {
                    formPostData = formPostData(feedBackText);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                new SendFeedback().execute(formPostData);
            }
        });

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }


    private String formPostData(String feedBackText) throws UnsupportedEncodingException {

//        if(FCM_RegisterID.equalsIgnoreCase("")){
//            FCM_RegisterID = "12345";
//        }
        String data = URLEncoder.encode("fcm_regid", "UTF-8") + "="
                + URLEncoder.encode(FCM_RegisterID, "UTF-8");
        data += "&" + URLEncoder.encode("suggestion", "UTF-8") + "="
                + URLEncoder.encode(feedBackText, "UTF-8");

        return  data;
    }

    public class SendFeedback extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result="";
            try {
                result = webServices.postRequest(params[0], Constants.TOURISM_FEEDBACK);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(s);
                boolean result = object.getBoolean("result");
                if(result){

                    tourism_edit.setText("");
                    Toast.makeText(Dashboard_main.context, "Thank you for your valuable feedback", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Dashboard_main.context, "Something went wrong while sending message to service", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(Dashboard_main.context, "Error in request. Please try again", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

        }
    }


}
