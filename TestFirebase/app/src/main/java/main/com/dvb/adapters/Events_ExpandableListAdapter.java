package main.com.dvb.adapters;

/**
 * Created by artre on 12/22/2016.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.pojos.EventBean;
import main.com.dvb.services.AlarmBroadcast;
import main.com.dvb.sqliteAdapter.DatabaseHelper;


public class Events_ExpandableListAdapter extends BaseExpandableListAdapter {
    private ExpandableListView exp;
    private Context _context;
    public List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    ImageView imageView,left_image;
    private TextView subtext;
    ToggleButton remindMe;
    DatabaseHelper databaseHelper;
    public ArrayList<EventBean> listOfBeans;
    AlarmManager alarmManager;

    public Events_ExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<String>> listChildData, ExpandableListView exp) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.exp=exp;
        databaseHelper = new DatabaseHelper(Dashboard_main.context);
        alarmManager = (AlarmManager) Dashboard_main.context.getSystemService(Context.ALARM_SERVICE);


    }

    public void resetData(List<String> listDataHeader,
                          HashMap<String, List<String>> listChildData, ExpandableListView exp, ArrayList<EventBean> list){
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.exp=exp;
        this.listOfBeans = list;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        subtext.setVisibility(View.GONE);
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.events_listitem, null);
        }
        exp.setDividerHeight(0);
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        imageView.setImageResource(R.mipmap.arrow_down);
        if(childText.equalsIgnoreCase("null")){
            txtListChild.setText("");
        }else{
            txtListChild.setText(childText);
        }
        remindMe = (ToggleButton) convertView.findViewById(R.id.remind_me);
        // lblListHeader.setTypeface(null, Typeface.BOLD);
//        left_image.setBackgroundResource(R.mipmap.ic_insert_invitation_black_48dp);
//        imageView.setBackgroundResource(R.mipmap.ic_expand_less_black_48dp);

        EventBean eventBean = listOfBeans.get(groupPosition);
        if(eventBean.isActive){
            remindMe.setChecked(true);
        }else{
            remindMe.setChecked(false);
        }
        remindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleclick(v,groupPosition);

            }
        });
//        left_image.setBackgroundResource(R.mipmap.ic_event_available_black_48dp);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.events_listgroup, null);
        }
        exp.setDividerHeight(10);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);

        subtext = (TextView) convertView.findViewById(R.id.subtitle);
        subtext.setVisibility(View.VISIBLE);
        imageView = (ImageView) convertView.findViewById(R.id.imageview_arrow);
        left_image = (ImageView) convertView.findViewById(R.id.left_imageview);
        imageView.setImageResource(R.mipmap.arrow_up);
        lblListHeader.setText(headerTitle);
        String subtexttitle ="";
        List<String> sub =_listDataChild.get(this._listDataHeader.get(groupPosition));
        for (String s : sub)
        {
            subtexttitle += s ;
        }
        if(subtexttitle.equalsIgnoreCase("null")){
            subtext.setText("");
        }else{
            subtext.setText(subtexttitle);


        }


        return convertView;
    }

    public void toggleclick(View v, int groupPosition){
        boolean isChecked = ((ToggleButton) v).isChecked();
        EventBean eventBean = listOfBeans.get(groupPosition);

        if(isChecked){
            long id = (int) databaseHelper.insertEvent(eventBean);
            setAlarm(Dashboard_main.context, id, eventBean);
            Toast.makeText(Dashboard_main.context,"You have set reminder for this Event.",Toast.LENGTH_LONG).show();
        }else{
//            int pId = databaseHelper.getPrimaryKey(eventBean.getDate_event());
//            cancelAlarm(pId);
            int id = databaseHelper.deleteEvent(eventBean.getMessage_event());

            Toast.makeText(Dashboard_main.context,"You have removed reminder for this Event.",Toast.LENGTH_LONG).show();
        }
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

    protected void cancelAlarm(long primaryKey) {

        Intent mintent = new Intent(Dashboard_main.context, AlarmBroadcast.class);
        mintent.putExtra("primaryKey", primaryKey);
        mintent.setAction("com.alarm.ActionSetter");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Dashboard_main.context,
                (int)primaryKey, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(pendingIntent != null)
            alarmManager.cancel(pendingIntent);
    }

    public void setAlarm(Context context, long id, EventBean eventBean){
        String eventDate = eventBean.getDate_event();
//        Calendar calendar = dateConvertor(eventDate);
        Calendar calendar =  dateConvertor(eventDate);

        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.MONTH, 1);

//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar.get(Calendar.HOUR);
//        int min = calendar.get(Calendar.MINUTE);
//        int sec = calendar.get(Calendar.SECOND);

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

//    public void setAlarm(Context context, long id, EventBean eventBean){
//        String eventDate = eventBean.getDate_event();
////        Calendar calendar = dateConvertor(eventDate);
//        Calendar calendar =  dateConvertor(eventDate);
//
//        calendar.add(Calendar.DATE, -1);
////        calendar.set(Calendar.SECOND, 0);
////        calendar.set(Calendar.MILLISECOND, 0);
//
////        int hr = calendar.get(Calendar.HOUR_OF_DAY);
////        int min = calendar.get(Calendar.MINUTE);
////        min = min +2;
////        calendar.set(Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY);
////        calendar.set(Calendar.MINUTE, min);
////        calendar.add(Calendar.SECOND, 15);
//
//        Intent mintent = new Intent(Dashboard_main.context, AlarmBroadcast.class);
//        mintent.putExtra("primaryKey", id);
//
//        mintent.setAction("com.alarm.ActionSetter");
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                (int)id, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
////            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
////        }else{
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
////        }
//    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}