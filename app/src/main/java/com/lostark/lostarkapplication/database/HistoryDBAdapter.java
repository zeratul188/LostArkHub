package com.lostark.lostarkapplication.database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.objects.History;
import com.lostark.lostarkapplication.ui.gallery.Stone;

public class HistoryDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_CONTENT = "CONTENT";

    private static final String DATABASE_CREATE = "create table HISTORYS (_id integer primary key, " +
            "NAME text not null, DATE text not null, CONTENT text not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_HISTORYS";
    private static final String DATABASE_TABLE = "HISTORYS";
    private static final int DATABASE_VERSION = 3;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public HistoryDBAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        Context mCtx = null;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mCtx = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS HISTORYS");
            onCreate(db);

        }
    }

    public HistoryDBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }
    
    public long insertData(History history) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, history.getName());
        values.put(KEY_DATE, history.getDate());
        values.put(KEY_CONTENT, history.getContent());
        return sqlDB.insert(DATABASE_TABLE, null, values);
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DATE, KEY_CONTENT}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DATE, KEY_CONTENT}, "_id = "+rowID, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(DATABASE_TABLE, "_id = "+rowID, null) > 0;
    }

    public void limitDelete(int limit) {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DATE, KEY_CONTENT}, null, null, null, null, null);;
        cursor.moveToFirst();
        int count = cursor.getCount() - limit;
        if (count > 0) {
            while (!cursor.isAfterLast() && count > 0) {
                int rowID = cursor.getInt(0);
                deleteData(rowID);
                cursor.moveToNext();
                count--;
            }
        }
    }
}
