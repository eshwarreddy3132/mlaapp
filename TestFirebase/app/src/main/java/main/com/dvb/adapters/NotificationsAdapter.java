package main.com.dvb.adapters;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import main.com.dvb.R;
import main.com.dvb.pojos.MyReportsBean;
import main.com.dvb.pojos.NotificationsBean;

/**
 * Created by AIA on 11/6/16.
 */


public class NotificationsAdapter extends BaseAdapter {

    private List<NotificationsBean> list;
    LayoutInflater inflater;

    public NotificationsAdapter( List<NotificationsBean> notificationsBeen,Context context) {
                this.list=notificationsBeen;
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){
/****** Inflate tabitem.xml file for each row ( Defined below ) *******/

            vi = inflater.inflate(R.layout.notifications_item, null);


/****** View Holder Object to contain tabitem.xml file elements ******/


            holder = new ViewHolder();
            holder.subject_notification = (TextView) vi.findViewById(R.id.textviewSubject);
            holder.date_notification=(TextView)vi.findViewById(R.id.textviewDate);
            holder.details_notification=(TextView)vi.findViewById(R.id.textviewDetails);
            holder.card_view = (CardView) vi.findViewById(R.id.card_view);
            holder.arrow_imageup = (ImageView) vi.findViewById(R.id.imagearrowup);
            holder.arrow_down = (ImageView) vi.findViewById(R.id.imagearrowdown);
            holder.expandtextdetails = (WebView) vi.findViewById(R.id.textviewexpandDetails);
            holder.details_linearlayout = (LinearLayout) vi.findViewById(R.id.details_linearlayout);


/************  Set holder with LayoutInflater ************/

            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        NotificationsBean notificationsBean = list.get(position);
        holder.subject_notification.setText(notificationsBean.getSubject_notification());
        holder.date_notification.setText(notificationsBean.getDate_notification());
        holder.details_notification.setText(notificationsBean.getMessage_notification());
        //holder.expandtextdetails.setText(notificationsBean.getMessage_notification());
        String myData=notificationsBean.getMessage_notification();
        holder.expandtextdetails.loadData("<p style=\"text-align: justify\">"+ myData +"</p>", "text/html", "UTF-8");


        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.arrow_imageup.getVisibility() == View.VISIBLE) {
                    holder.details_notification.setVisibility(View.GONE);
                    holder.details_linearlayout.setVisibility(View.VISIBLE);
                    holder.arrow_imageup.setVisibility(View.GONE);
                    holder.arrow_down.setVisibility(View.VISIBLE);
                }
                else {
                    holder.details_notification.setVisibility(View.VISIBLE);
                    holder.details_linearlayout.setVisibility(View.GONE);
                    holder.arrow_imageup.setVisibility(View.VISIBLE);
                    holder.arrow_down.setVisibility(View.GONE);
                }
            }
        });

        return vi;
    }

    public static class ViewHolder{

        public TextView subject_notification,details_notification,date_notification;//expandtextdetails;
        public CardView card_view;
        public WebView expandtextdetails;
        public ImageView arrow_imageup,arrow_down;
        public LinearLayout details_linearlayout;
    }
}

