package main.com.dvb.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.Events_ExpandableListAdapter;
import main.com.dvb.homePageFragments.FacebookFragment;
import main.com.dvb.pojos.EventBean;
import main.com.dvb.services.AlarmBroadcast;
import main.com.dvb.services.WebServices;
import main.com.dvb.sqliteAdapter.DatabaseHelper;

/**
 * Created by SREEVATSAVA on 30-07-2016.
 */
public class EvnetsFragment extends Fragment{


    Events_ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ProgressBar progressBar;
    ArrayList<EventBean> listOfBeans;
    DatabaseHelper databaseHelper;
    AlarmManager alarmManager;

    public static EvnetsFragment bioFragment;
    public EvnetsFragment() {
        // Required empty public constructor
        bioFragment = EvnetsFragment.this;
        alarmManager = (AlarmManager) Dashboard_main.context.getSystemService(Context.ALARM_SERVICE);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.events_layout, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.eventslist_progressbar);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        progressBar.setVisibility(View.GONE);
        databaseHelper = new DatabaseHelper(Dashboard_main.context);
        prepareListData();
        listAdapter = new Events_ExpandableListAdapter(Dashboard_main.context,listDataHeader,listDataChild,expListView);
        expListView.setAdapter(listAdapter);
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> listchild = new ArrayList<String>();
        listOfBeans = new ArrayList<>();

/*
for (int i =0;i<10;i++){
    listDataHeader.add("Android Application Launch");
    listchild = new ArrayList<>();
    listchild.add(
            "if you wanted to do it with just layouts you could make a frame layout which layers layouts and then have two layouts for vertical and horizontal and then add your layout in. i think this might be what you are looking for as this will put the lines overtop over your other views.");
    listDataChild.put(listDataHeader.get(i),listchild);

}
*/

        setAlarm(Dashboard_main.context,10,"2017-01-01 13:19:00");
        if (checkIsNetworkAvailable()){
        new FechEvents().execute();
        }else{
            errorMessage();
        }
    }

    private class FechEvents extends AsyncTask<Void, Void, String> {
        String response;
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            WebServices webServices = new WebServices();

            try {
                response = webServices.getRequest(Constants.EVENTS);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.e("events response",""+response);
            progressBar.setVisibility(View.GONE);
            ArrayList<EventBean> eventBeanArrayList = databaseHelper.getAllEvent();
            if(response != null){
                List<String> listchild = new ArrayList<String>();
                JSONArray arr = null;
                try {
                    arr =new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Parsing json
                for (int i = 0; i < arr.length(); i++) {
                    try {

                        JSONObject obj =  arr.getJSONObject(i);

                        String title = obj.getString("title");
                        String message = obj.getString("message");
                        String date = obj.getString("create_at");
                        String content = title+"\nEvent Date: "+date;
                        listDataHeader.add(content);
                        listchild = new ArrayList<>();
                        listchild.add(message);
                        listDataChild.put(listDataHeader.get(i),listchild);

                        EventBean eventBean = new EventBean();
                        eventBean.title_event = title;
                        eventBean.message_event = message;
                        eventBean.date_event = date;

                        for(EventBean eventBean1 : eventBeanArrayList){
                            if(eventBean1.message_event.equalsIgnoreCase(message)){
                                eventBean.isActive = true;
                                break;
                            }
                        }

                        listOfBeans.add(eventBean);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                listAdapter.resetData(listDataHeader,listDataChild,expListView,listOfBeans);
                ((BaseExpandableListAdapter)listAdapter).notifyDataSetChanged();

            }else{
                Toast.makeText(getActivity(), "Server error please try again", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void errorMessage(){
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
                progressBar.setVisibility(View.VISIBLE);
                new FechEvents().execute();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }

    public Calendar dateConvertor(String dateValue){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        try {
            date = format.parse(dateValue);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;

    }

    public void setAlarm(Context context, long id, String d){
//        String eventDate = eventBean.getDate_event();
//        Calendar calendar = dateConvertor(eventDate);
        Calendar calendar =  dateConvertor(d);

        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.MONTH, 1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        Intent mintent = new Intent(Dashboard_main.context, AlarmBroadcast.class);
        mintent.putExtra("primaryKey", id);

        mintent.setAction("com.alarm.ActionSetter");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int)id, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }else{
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }
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
