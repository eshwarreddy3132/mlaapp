package main.com.dvb.sqliteAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import main.com.dvb.pojos.EventBean;
import main.com.dvb.pojos.NotificationsBean;

/**
 * Created by AIA on 12/9/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "dashyam";

    // Table Names
    private static final String TABLE_Notifications = "notifications";
    private static final String TABLE_Events = "events";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_CREATED_SUBJECT = "subject";
    private static final String KEY_CREATED_MESSAGE = "message";

    private static final String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_Notifications + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CREATED_AT
            + " TEXT," + KEY_CREATED_SUBJECT + " TEXT," + KEY_CREATED_MESSAGE + " TEXT" + ")";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_Events + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CREATED_AT
            + " TEXT," + KEY_CREATED_SUBJECT + " TEXT," + KEY_CREATED_MESSAGE + " TEXT" + ")";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public long insertNotification(NotificationsBean notificationsBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, notificationsBean.getDate_notification());
        values.put(KEY_CREATED_SUBJECT, notificationsBean.getSubject_notification());
        values.put(KEY_CREATED_MESSAGE, notificationsBean.getMessage_notification());

        // insert row
        long todo_id = db.insert(TABLE_Notifications, null, values);
        db.close();
        return todo_id;
    }

    public long insertEvent(EventBean eventBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, eventBean.getDate_event());
        values.put(KEY_CREATED_SUBJECT, eventBean.getTitle_event());
        values.put(KEY_CREATED_MESSAGE, eventBean.getMessage_event());

        // insert row
        long todo_id = db.insert(TABLE_Events, null, values);
        db.close();
        return todo_id;
    }

    public EventBean getEvent(int notificationID) {
        SQLiteDatabase db = this.getReadableDatabase();
        EventBean eventBean = new EventBean();
        String selectQuery = "SELECT  * FROM " + TABLE_Events + " WHERE id  = " + notificationID;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
//            do {


            eventBean.id = c.getInt(c.getColumnIndex(KEY_ID));
            eventBean.message_event = c.getString(c.getColumnIndex(KEY_CREATED_MESSAGE));
            eventBean.date_event = c.getString(c.getColumnIndex(KEY_CREATED_AT));
            eventBean.title_event = c.getString(c.getColumnIndex(KEY_CREATED_SUBJECT));

//            } while (c.moveToNext());
        }
        db.close();
        return eventBean;
    }

    public ArrayList<EventBean> getAllEvent() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<EventBean> eventBeanArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_Events;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            do {

                EventBean eventBean = new EventBean();
                eventBean.id = c.getInt(c.getColumnIndex(KEY_ID));
                eventBean.message_event = c.getString(c.getColumnIndex(KEY_CREATED_MESSAGE));
                eventBean.date_event = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                eventBean.title_event = c.getString(c.getColumnIndex(KEY_CREATED_SUBJECT));

                eventBeanArrayList.add(eventBean);
            } while (c.moveToNext());
        }
        db.close();
        return eventBeanArrayList;
    }

    public ArrayList<NotificationsBean> getNotification() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<NotificationsBean> notificationsBeenList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_Notifications;// + " WHERE + KEY_ID + " = " + notificationID"


        Cursor c = db.rawQuery(selectQuery, null);
//        Cursor c = db.query(CREATE_TABLE_NOTIFICATIONS, new String[] { KEY_ID,
//                        KEY_CREATED_AT, KEY_CREATED_SUBJECT, KEY_CREATED_MESSAGE}, null,
//                null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            do {

                NotificationsBean notificationsBean = new NotificationsBean();
                notificationsBean.id = c.getInt(c.getColumnIndex(KEY_ID));
                notificationsBean.message_notification = c.getString(c.getColumnIndex(KEY_CREATED_MESSAGE));
                notificationsBean.date_notification = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                notificationsBean.subject_notification = c.getString(c.getColumnIndex(KEY_CREATED_SUBJECT));

                notificationsBeenList.add(notificationsBean);
            } while (c.moveToNext());
        }
        db.close();
        return notificationsBeenList;
    }

    public int deleteEvent(String message){
        SQLiteDatabase db = this.getWritableDatabase();
        int id = db.delete(TABLE_Events, KEY_CREATED_MESSAGE + " = ?", new String[] { message });
        db.close();
        return id;
    }

    public int getPrimaryKey(String message){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_Events + " WHERE created_at = " + message;


        Cursor c = db.rawQuery(selectQuery, null);

        return c.getInt(c.getColumnIndex(KEY_ID));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_EVENTS);

        onCreate(db);
    }
}
