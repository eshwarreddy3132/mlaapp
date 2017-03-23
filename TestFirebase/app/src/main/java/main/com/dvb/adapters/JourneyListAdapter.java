package main.com.dvb.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.pojos.FB.FBCommentBean;
import main.com.dvb.pojos.JourneyBean;

/**
 * Created by AIA on 11/29/16.
 */

public class JourneyListAdapter extends BaseAdapter {
    SharedPreferences sharedPreferences;
    private ArrayList<JourneyBean> beanArrayList;
    private static LayoutInflater inflater=null;

    public JourneyListAdapter(ArrayList<JourneyBean> list, Context context){
        this.beanArrayList = list;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return beanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return beanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        sharedPreferences = Dashboard_main.context.getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");

        ViewHolder viewHolder;

        JourneyBean journeyBean = beanArrayList.get(position);
        if(convertView == null){
            view = inflater.inflate(R.layout.journey_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.journeyDate = (TextView) view.findViewById(R.id.journeyDate);
            viewHolder.journeyValue=(WebView)view.findViewById(R.id.journeyValue);
            viewHolder.textviewjourneyValue = (TextView) view.findViewById(R.id.textviewjourneyValue);
            viewHolder.webview_linearlayout = (LinearLayout) view.findViewById(R.id.webview_linearlayout);
            viewHolder.textview_linearlayout= (LinearLayout) view.findViewById(R.id.textview_linearlayout);

            view.setTag( viewHolder );
        }else
            viewHolder=(ViewHolder)view.getTag();

        viewHolder.journeyDate.setText(journeyBean.getJourneyDate());
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            String text=journeyBean.getJourneyValue();
            viewHolder.journeyValue.loadData("<p style=\"text-align: justify\">"+ text +"</p>", "text/html", "UTF-8");
        }else{
            viewHolder.webview_linearlayout.setVisibility(View.GONE);
            viewHolder.textview_linearlayout.setVisibility(View.VISIBLE);
            viewHolder.textviewjourneyValue.setText(journeyBean.getJourneyValue());
        }


        return view;
    }

    public static class ViewHolder{
        public LinearLayout textview_linearlayout,webview_linearlayout;
        public TextView journeyDate,textviewjourneyValue;
        public WebView journeyValue;


    }

}
