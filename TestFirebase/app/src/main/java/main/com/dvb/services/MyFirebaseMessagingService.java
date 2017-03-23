package main.com.dvb.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.homePageFragments.NewsFragment;
import main.com.dvb.sqliteAdapter.NotificationSQLiteHelper;

/**
 * Created by artre on 12/28/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationSQLiteHelper db;
    private Dashboard_main main;

    Handler handler;
    @Override
    public void onCreate() {
        // Handler will get associated with the current thread,
        // which is the main thread.
        super.onCreate();
        handler = new Handler();
        main= new Dashboard_main();
        db=new NotificationSQLiteHelper(getApplicationContext());
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            handleDataMessage(json);
        } catch (Exception e) {
            Log.e("mahesh fcm", "Exception: " + e.getMessage());
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String type = data.getString("type");
            //method call to store the message details into sqlite db

            insert(title,message);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    new Dashboard_main().checkForNotifications();
//                }
//            });
            Intent intent = new Intent(this, Dashboard_main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("TYPE",type);
//            Intent intent = new Intent(this, Dashboard_main.class);
//            intent.putExtra("TYPE",type);
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification noti = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.dasyam)
                    .setContentIntent(pIntent)
                    .setSound(soundUri)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, noti);
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    public void insert(final String sub, final String detail) throws Exception{
        handler.post(new Runnable(){
            public void run(){
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date d = new Date();
                String subject = sub;
                String date = dateFormat.format(d);
                String datails =detail;
                db.addNotification(subject,date,datails);
            }
        });
    }

}