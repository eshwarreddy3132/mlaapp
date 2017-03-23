package main.com.dvb.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.pojos.EventBean;
import main.com.dvb.sqliteAdapter.DatabaseHelper;

public class AlarmService extends IntentService {

	public AlarmService(String name) {
		super(name);
	}

	public AlarmService() {
		super("AlarmService");
	}

	public static String TAG = AlarmService.class.getSimpleName();


	@Override
	public void onHandleIntent(Intent intent) {
		try {
			long p_id = intent.getLongExtra("primaryKey", 0);

			DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
			EventBean eventBean =databaseHelper.getEvent((int) p_id);

			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Intent intent_alarm = new Intent(this,Dashboard_main.class);
			intent_alarm.putExtra("isFromNotification", "alarm");
			intent_alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pendingIntent = PendingIntent.getActivity(this,(int)p_id,intent_alarm,PendingIntent.FLAG_ONE_SHOT);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentTitle("Event Reminder");
			builder.setContentText(eventBean.getMessage_event());
			builder.setSmallIcon(R.mipmap.ic_launcher);
			builder.setSound(soundUri);
			builder.setAutoCancel(true);
			builder.setContentIntent(pendingIntent);
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.notify((int)p_id,builder.build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
