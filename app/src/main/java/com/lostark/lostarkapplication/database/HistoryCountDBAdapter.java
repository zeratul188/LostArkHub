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
import com.lostark.lostarkapplication.objects.HistoryCount;

public class HistoryCountDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_COUNT = "COUNT";

    private static final String DATABASE_CREATE = "create table HISTORYCOUNT (_id integer primary key, " +
            "NAME text not null, COUNT integer not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_HISTORYCOUNT";
    private static final String DATABASE_TABLE = "HISTORYCOUNT";
    private static final int DATABASE_VERSION = 3;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public HistoryCountDBAdapter(Context mCtx) {
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
            String[] homeworks = {"카오스 던전", "가디언 토벌", "에포나 일일 의뢰", "접속"};
            for (String name : homeworks) {
                db.execSQL("INSERT INTO "+DATABASE_TABLE+"(NAME, COUNT) VALUES (\""+name+"\", "+0+");");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS HISTORYCOUNT");
            onCreate(db);

        }
    }

    public HistoryCountDBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public boolean resetData() {
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, 0);
        sqlDB.update(DATABASE_TABLE, values, null, null);
        return true;
    }

    public void close() {
        myDBHelper.close();
    }
    
    public long insertData(HistoryCount historyCount) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, historyCount.getName());
        values.put(KEY_COUNT, historyCount.getCount());
        return sqlDB.insert(DATABASE_TABLE, null, values);
    }

    public boolean changeValue(String name, int count) {
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, count);
        sqlDB.update(DATABASE_TABLE, values, "NAME = ?", new String[] {name});
        return true;
    }

    public int getQueryCount(String name) {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_COUNT}, "NAME = '"+name+"'", null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(2);
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_COUNT}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_COUNT}, "_id = "+rowID, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(DATABASE_TABLE, "_id = "+rowID, null) > 0;
    }
}
