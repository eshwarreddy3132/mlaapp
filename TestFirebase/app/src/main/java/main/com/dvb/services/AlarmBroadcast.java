package main.com.dvb.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;
import java.util.Date;


public class AlarmBroadcast extends WakefulBroadcastReceiver {

	AlarmManager alarmManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
//			SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(context);
//
//			alarmManager = (AlarmManager) context
//					.getSystemService(Context.ALARM_SERVICE);
//
//			ArrayList<AlarmBean> alarmBeans = new ArrayList<>();
//			alarmBeans = sqLiteAdapter.select_alarm();
//
//			for(int i =0;i< alarmBeans.size();i++){
//				AlarmBean alarmBean = alarmBeans.get(i);
//				int hour = alarmBean.getHour();
//				int min = alarmBean.getMinute();
//				String isMon = alarmBean.getMonday();
//				String isTue = alarmBean.getTueday();
//				String isWed = alarmBean.getWedday();
//				String isThu = alarmBean.getThurday();
//				String isFri = alarmBean.getFriday();
//				String isSat = alarmBean.getSatday();
//				String isSun = alarmBean.getSunday();
//				long pId = alarmBean.getPrimaryKey();
//
//				boolean isRepeatChecked = false;
//
//				Calendar calendar = setCalendarTime(hour, min, i);
//
//				if(isMon.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 2, context);
//				}
//				if(isTue.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 3, context);
//				}
//				if(isWed.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 4, context);
//				}
//				if(isThu.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 5, context);
//				}
//				if(isFri.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 6, context);
//				}
//				if(isSat.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 7, context);
//				}
//				if(isSun.equalsIgnoreCase("true")){
//					isRepeatChecked = true;
//					setAlarm(calendar, pId, 1, context);
//				}
//
//				if(!isRepeatChecked){
//					Calendar currentCal = Calendar.getInstance();
//					currentCal.set(Calendar.SECOND, 0);
//					currentCal.set(Calendar.MILLISECOND, 0);
//
//        	        Date currentDate = calendar.getTime();
//
//        	        currentCal.set(Calendar.HOUR_OF_DAY, alarmBean.getHour());
//        	        currentCal.set(Calendar.MINUTE, alarmBean.getMinute());
//
//        	        Date setDate = currentCal.getTime();
//
//        	        if(setDate.after(currentDate) || setDate.equals(currentDate)){
//    					Intent mintent = new Intent(context.getApplicationContext(), AlarmBroadcast.class);
//    					mintent.putExtra("primaryKey", alarmBean.getPrimaryKey());
//    					mintent.setAction("com.alarm.ActionSetter");
//                        String str = alarmBean.getPrimaryKey() +"08";
//                        pId = Integer.parseInt(str);
//    					PendingIntent pi = PendingIntent.getBroadcast(context,
//    							(int)pId, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                        	alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//                        }else{
//                        	alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//                        }
//        	        }else{
//    					if (alarmBean.getWeekAlarm().equalsIgnoreCase("not yet")
//    							|| alarmBean.getWeekAlarm().equalsIgnoreCase(
//    									"not set")) {
//    						alarmBean.alarmOn = false;
//    						int id = sqLiteAdapter.update_alarm(alarmBean);
//    						Log.e("", "" + id);
////    						AlarmsListActivity.isAlarmFire = true;
//    					}
//        	        }
//				}
//
//			}
//
//		}else{
			long p_id = intent.getLongExtra("primaryKey", 0);
			int weekId = intent.getIntExtra("weekDayKey", 0);
			Intent mintent = new Intent(context, AlarmService.class);
	        mintent.putExtra("primaryKey", p_id);
	        mintent.putExtra("weekid", weekId);
	        startWakefulService(context, mintent);
	        setResultCode(Activity.RESULT_OK);
//		}
	}
	
	protected Calendar setCalendarTime(int hour, int minute, int weekDay) {
        Calendar calendar = Calendar.getInstance();
        
        switch (weekDay) {
		case 1:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		case 2:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			break;
		case 3:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			break;
		case 4:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			break;
		case 5:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			break;
		case 6:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			break;
		case 7:
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;

		default:
			break;
		}
        
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        
        Date targetDate = calendar.getTime();
        
        Calendar currentTime = Calendar.getInstance();
        
        Date currentDate = currentTime.getTime();
        
        if(currentDate.after(targetDate)){
        	calendar.add(Calendar.DATE, 7);
        }
        
        return calendar;
	}
	
	protected void setAlarm(Calendar calendar, long primaryKey, int weekDay, Context context) {
        String str = primaryKey +"0"+weekDay;
        int id = Integer.parseInt(str);
        
        Intent mintent = new Intent(context.getApplicationContext(), AlarmBroadcast.class);
        mintent.putExtra("primaryKey", primaryKey);
        mintent.putExtra("weekDayKey", id);
        mintent.setAction("com.alarm.ActionSetter");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
        		(int)id, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, pendingIntent);
	}

}
