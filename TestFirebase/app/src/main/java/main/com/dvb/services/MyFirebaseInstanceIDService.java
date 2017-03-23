package main.com.dvb.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import main.com.dvb.Constants;

/**
 * Created by artre on 12/28/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG+"FCM REGID",""+refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_EVENTS);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_NEWS);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

    }


    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
