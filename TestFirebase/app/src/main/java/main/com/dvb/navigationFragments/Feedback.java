package main.com.dvb.navigationFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.services.WebServices;

/**
 * Created by AIA on 11/6/16.
 */

public class Feedback extends Fragment {

SharedPreferences sharedPreferences;
    String FCM_RegisterID="";
    WebServices webServices;
    EditText feedback;
    ProgressBar progressBar;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.feedback, container, false);

        feedback = (EditText) view.findViewById(R.id.feedback_edit);
        Button submit_feedback = (Button) view.findViewById(R.id.submit_feedback);
        textView= (TextView) view.findViewById(R.id.feedbackText);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        sharedPreferences = Dashboard_main.context.getSharedPreferences(Constants.SHARED_PREF,0);
        FCM_RegisterID = sharedPreferences.getString("regId","");
        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            textView.setText(getString(R.string.feedback_headding));
        }else {
            textView.setText(getString(R.string.telugu_feedbakheadding));
        }
        webServices = new WebServices();


        submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedback.getText() == null){
                    Toast.makeText(Dashboard_main.context, "Please enter your feedback", Toast.LENGTH_LONG).show();
                    return;
                }
                String feedBackText = feedback.getText().toString();
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

    private String formPostData(String feedBackText) throws UnsupportedEncodingException {

//        if(FCM_RegisterID.equalsIgnoreCase("")){
//            FCM_RegisterID = "12345";
//        }
        String data = URLEncoder.encode("fcm_regid", "UTF-8") + "="
                + URLEncoder.encode(FCM_RegisterID, "UTF-8");
        data += "&" + URLEncoder.encode("feedback", "UTF-8") + "="
                + URLEncoder.encode(feedBackText, "UTF-8");

        return  data;
    }

    public class SendFeedback extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String result="";
            try {
                result = webServices.postRequest(params[0], Constants.FEEDBACK);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                progressBar.setVisibility(View.GONE);

                JSONObject object = new JSONObject(s);
                boolean result = object.getBoolean("result");
                if(result){
                    feedback.setText("");
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
