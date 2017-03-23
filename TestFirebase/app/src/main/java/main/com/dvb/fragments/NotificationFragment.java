package main.com.dvb.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import main.com.dvb.Constants;
import main.com.dvb.R;
import main.com.dvb.adapters.NotificationsAdapter;
import main.com.dvb.pojos.NotificationsBean;
import main.com.dvb.sqliteAdapter.NotificationSQLiteHelper;

import static com.facebook.FacebookSdk.getApplicationContext;
import static main.com.dvb.Dashboard_main.context;

/**
 * Created by eshwar reddy on 2/22/2017.
 */
//this is used for fcm notifications sqlite
public class NotificationFragment extends Fragment{
private ListView notificationlist;
    private NotificationSQLiteHelper db;
    private TextView textView;
    SharedPreferences sharedPreferences;
    NotificationsAdapter notificationsAdapter;
    private Button clearall;
    String preff_lang;
    int i=0;
    private List<NotificationsBean> notificationsarraylist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.notification_fragment, container, false);
        textView = (TextView)view.findViewById(R.id.notificationheadding_text);
        notificationlist = (ListView) view.findViewById(R.id.notificationlistItems);
        clearall= (Button) view.findViewById(R.id.clearall_notification);
        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
         preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            textView.setText(getString(R.string.notificationheadding));
            clearall.setText("Clear All");
        }else {
            clearall.setText("తొలగించు");
            textView.setText(getString(R.string.telugu_notificationheadding));
        }
        notificationsarraylist = new ArrayList<NotificationsBean>();
        db = new NotificationSQLiteHelper(getActivity());
        getAllNotifications();

        notificationsAdapter = new NotificationsAdapter(notificationsarraylist,getContext());
        notificationlist.setAdapter(notificationsAdapter);
        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearalldate();

            }
        });

        return view;
    }

    private void clearalldate() {
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            if (!notificationsarraylist.isEmpty()){
                new AlertDialog.Builder(context)
                        .setTitle("Clear All")
                        .setMessage("Are you sure want to clear all notifications?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteAllNotifications();
                                notificationsarraylist.clear();
                                notificationsAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }else {
            if (!notificationsarraylist.isEmpty()){
                new AlertDialog.Builder(context)
                        .setTitle("తొలగించు")
                        .setMessage(getString(R.string.notification_cleartext))
                        .setPositiveButton("అవును", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteAllNotifications();
                                notificationsarraylist.clear();
                                notificationsAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("కాదు", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }


    }


    private void getAllNotifications(){
        Cursor c = db.getAllNotification();

        if (c != null) {
            try {
                c.moveToFirst();
                i=Integer.parseInt(c.getString(c.getColumnIndex(NotificationSQLiteHelper.COLUMN_ID)));
                saveIdIntoPreferences(i);
                if (c.moveToFirst()) {
                    do {

                        NotificationsBean notifi = new NotificationsBean();
                        notifi.setNotifi_id(c.getString(c.getColumnIndex(NotificationSQLiteHelper.COLUMN_ID)));
                        notifi.setSubject_notification(c.getString(c.getColumnIndex(NotificationSQLiteHelper.COLUMN_SUBJECT)));
                        notifi.setDate_notification(c.getString(c.getColumnIndex(NotificationSQLiteHelper.COLUMN_DATE)));
                        notifi.setMessage_notification(c.getString(c.getColumnIndex(NotificationSQLiteHelper.COLUMN_DETAILS)));
                        notificationsarraylist.add(notifi);

                    } while (c.moveToNext());

                }
            }catch (Exception e){
                return;
            }

        }

    }
    public void saveIdIntoPreferences(int id){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF_NOTI_SQLITE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("SQLITEID", id);
        editor.commit();
    }

}
