package main.com.dvb.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.MyReportsAdapter;
import main.com.dvb.adapters.NotificationsAdapter;
import main.com.dvb.adapters.ViewPagerAdapter;
import main.com.dvb.pojos.NotificationsBean;
import main.com.dvb.sqliteAdapter.DatabaseHelper;

//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;

//import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by arjun on 30-07-2016.
 */
public class NotificationsFragment extends Fragment {

    ArrayList<String> resultList = new ArrayList<>();
    public static NotificationsFragment simpleFragment;
//    ViewPager viewPagerBanner;
    ViewPagerAdapter adapter;

    ViewPager viewPager;
    TabLayout tabLayout;
    ListView listView;
    NotificationsAdapter notificationsFragment;
    ArrayList<NotificationsBean> notificationsBeanArrayList = new ArrayList<>();

    DatabaseHelper databaseHelper;
    public NotificationsFragment() {
        simpleFragment = NotificationsFragment.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fcmnotification, container, false);
        listView = (ListView) view.findViewById(R.id.notifications);
        TextView notifications_msg = (TextView) view.findViewById(R.id.notifications_msg);

//        for(int i =0;i<10;i++){
//            NotificationsBean notificationsBean = new NotificationsBean();
//            notificationsBean.date_notification = "10-10-2016";
//            notificationsBean.message_notification = "Message notifications";
//            notificationsBean.subject_notification = "Subject";
//            notificationsBeanArrayList.add(notificationsBean);
//        }
        databaseHelper = new DatabaseHelper(Dashboard_main.context);
        notificationsBeanArrayList = databaseHelper.getNotification();
        if(notificationsBeanArrayList.size() == 0){
            notifications_msg.setVisibility(View.VISIBLE);
        }else{
            notifications_msg.setVisibility(View.GONE);
        }

        notificationsFragment = new NotificationsAdapter(notificationsBeanArrayList,getContext());
        listView.setAdapter(notificationsFragment);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }


}
