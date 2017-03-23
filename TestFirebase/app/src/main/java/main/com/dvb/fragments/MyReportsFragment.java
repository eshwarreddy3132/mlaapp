package main.com.dvb.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.MyReportDetails;
import main.com.dvb.R;
import main.com.dvb.adapters.MyReportsAdapter;
import main.com.dvb.pojos.MyReportsBean;
import main.com.dvb.pojos.User;
import main.com.dvb.services.WebServices;

/**
 * Created by AIA on 11/6/16.
 */

public class MyReportsFragment extends Fragment{

//    ArrayList<MyReportsBean> items;
    public Handler myHandlerCode;

    public static MyReportsFragment myReportFragment;
    MyReportsAdapter myReportsAdapter;
    WebServices webServices;
    SharedPreferences sharedPreferences;
    String FCM_RegisterID="";
    ListView listView;
    private TextView textView;

    public MyReportsFragment()
    {
        myReportFragment = MyReportsFragment.this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.myreports, container, false);
        textView = (TextView) view.findViewById(R.id.headding_text);
        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
                textView.setText(getString(R.string.myreport_headding));
        }else {
            textView.setText(getString(R.string.telugumyreport_headding));
        }
        listView = (ListView) view.findViewById(R.id.listItems);
//        items = new ArrayList<MyReportsBean>();
        webServices = new WebServices();

//        items.addAll(((Dashboard_main)Dashboard_main.context).myReports);



       if(Constants.myReportsList != null && Constants.myReportsList.size() == 0){
            sharedPreferences = Dashboard_main.context.getSharedPreferences(Constants.SHARED_PREF,0);
            FCM_RegisterID = sharedPreferences.getString("regId","");
            String formPostData ="";
            try {
                formPostData = formPostData();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            new FetchReports().execute(formPostData);
        }else{
            myReportsAdapter = new MyReportsAdapter(Constants.myReportsList, Dashboard_main.context);
            listView.setAdapter(myReportsAdapter);
        }


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(Dashboard_main.context, MyReportDetails.class);
//                intent.putExtra("details", Constants.myReportsList.get(i));
//                startActivity(intent);
//            }
//        });


        return view;
    }

    private String formPostData() throws UnsupportedEncodingException {

        String data = "&" + URLEncoder.encode("fcm_regid", "UTF-8") + "="
                + URLEncoder.encode(FCM_RegisterID, "UTF-8");//FCM_RegisterID

        return  data;
    }

    public class FetchReports extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result="";
            try {
                result = webServices.postRequest(params[0], Constants.MY_REPORTS);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((Dashboard_main)Dashboard_main.context).card_view_report.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("",s);
            ((Dashboard_main)Dashboard_main.context).card_view_report.setVisibility(View.GONE);

            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    MyReportsBean myReportsBean = new MyReportsBean();
                    myReportsBean.date = object.getString("created_at");
                    myReportsBean.mobile_number = Long.parseLong(object.getString("mobile"));
                    myReportsBean.name = object.getString("name");
                    myReportsBean.place = object.getString("place");
                    myReportsBean.subject = object.getString("subject");
                    myReportsBean.problem = object.getString("complaint_details");
                    myReportsBean.FCM_ID = object.getString("fcm_regestration_id");
                    myReportsBean.status = object.getString("report_status");
                    myReportsBean.id = object.getString("id");

                    Constants.myReportsList.add(myReportsBean);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            myReportsAdapter = new MyReportsAdapter(Constants.myReportsList, Dashboard_main.context);
            listView.setAdapter(myReportsAdapter);
        }
    }


}
