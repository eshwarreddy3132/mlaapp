package main.com.dvb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.com.dvb.sqliteAdapter.NotificationSQLiteHelper;

/**
 * Created by AIA on 8/16/16.
 */
public class SplashScreen extends AppCompatActivity{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rs2jjzShvGV47MidZZVlNjzAO";
    private static final String TWITTER_SECRET = "WUcR1u1AeLhC0KRAxf3Sc3jJoPSGAaUUTvfWPJQOgLY3t9YOCS";

    
    private static int SPLASH_TIME_OUT = 4000;
    Handler handler;
    Runnable runnable;
    private SharedPreferences sharedPreferences;

    private NotificationSQLiteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String langg="Engilsh";
        editor.putString("LANGUAGE", langg);
        editor.commit();
        db = new NotificationSQLiteHelper(this);


//insert();
        handler = new Handler();


        runnable = new Runnable() {
            @Override
            public void run() {
//                    Intent intent = new Intent(SplashScreen.this, Dashboard_main.class);
//                    finishAffinity();
//                    startActivity(intent);
//                    finish();
                checkSession();

            }
        };

        handler.postDelayed(runnable, SPLASH_TIME_OUT);

    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    public void checkSession(){
        sharedPreferences =getSharedPreferences(Constants.LOGIN_SESSION,0);
        String session  = sharedPreferences.getString("SESSION","");
        if (session.equals("TRUE")){
            Intent intent = new Intent(SplashScreen.this,Dashboard_main.class);
            finishAffinity();
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(SplashScreen.this,UserLogin.class);
            finishAffinity();
            startActivity(intent);
            finish();
        }
    }
//sqlite dummy
    private void insert(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = new Date();
        String subject = " the notificatio";
        String date = dateFormat.format(d);
        String datails =" Telangana to Shilothri Devi and Dasyam Rangaiah, former Engineer in the Electricity Department. He received his early education from Kakatiya High School and St.Gabriel’s High School, Warangal and went on to do his Post graduation in Political Science from BR Ambedkar University. He took up politics after the demise of his brother and contested in the 1999 assembly elections as an Independent candidate and secured 37,000 votes. He contested again in the year 2004 as an Independent candidate and lost with a narrow margin of 1,500 votes.</string>\n";
        db.addNotification(subject,date,datails);
        Toast.makeText(this,"Inserted Successfully",Toast.LENGTH_LONG).show();
    }

}
