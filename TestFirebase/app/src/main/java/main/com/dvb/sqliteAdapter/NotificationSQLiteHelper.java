package main.com.dvb.sqliteAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by artre on 2/22/2017.
 */

public class NotificationSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Notificationsdb";
    public static final String TABLE_NAME = "Notification";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SUBJECT ="subject";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DETAILS = "details";


    private static final int DB_VERSION = 1;

    public NotificationSQLiteHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql = "CREATE TABLE " +TABLE_NAME
//                +"(" +COLUMN_ID+
//                " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUMN_NOTIID+
//                " INTEGER " +COLUMN_SUBJECT+
//                " VARCHAR, " +COLUMN_DATE+
//                " VARCHAR, " +COLUMN_DETAILS+
//                " VARCHAR);";
        String sql = "CREATE TABLE " +TABLE_NAME
                +"(" +COLUMN_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUMN_SUBJECT+
                " VARCHAR, " +COLUMN_DATE+
                " VARCHAR, " +COLUMN_DETAILS+
                " VARCHAR);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Notification";
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addNotification(String subject, String date, String details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SUBJECT,subject);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_DETAILS, details);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getAllNotification(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM Notification ORDER BY id DESC;";
        Cursor c = db.rawQuery(sql,null);
        return c;
    }
    public void deleteAllNotifications(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql ="DELETE FROM Notification";
        db.delete(TABLE_NAME,null,null);
        db.execSQL(sql);
        db.close();

    }
}
