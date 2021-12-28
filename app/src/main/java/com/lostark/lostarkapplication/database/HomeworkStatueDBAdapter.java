package com.lostark.lostarkapplication.database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.objects.HistoryCount;
import com.lostark.lostarkapplication.objects.HomeworkStatue;

import java.util.ArrayList;

public class HomeworkStatueDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NUMBER = "NUMBER";
    public static final String KEY_COUNT = "COUNT";

    private static final String DATABASE_CREATE = "create table HOMEWORKSTATUE (_id integer primary key, " +
            "NUMBER integer not null, COUNT integer not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_HOMEWORKSTATUE";
    private static final String DATABASE_TABLE = "HOMEWORKSTATUE";
    private static final int DATABASE_VERSION = 2;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public HomeworkStatueDBAdapter(Context mCtx) {
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
            for (int i = 1; i <= 5; i++) {
                db.execSQL("INSERT INTO "+DATABASE_TABLE+"(NUMBER, COUNT) VALUES (\""+i+"\", "+0+");");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS HOMEWORKSTATUE");
            onCreate(db);

        }
    }

    public HomeworkStatueDBAdapter open() throws SQLException {
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

    public void nextDay() {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NUMBER, KEY_COUNT}, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<HomeworkStatue> statues = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int number = cursor.getInt(1);
            int count = cursor.getInt(2);
            statues.add(new HomeworkStatue(number, count));
            cursor.moveToNext();
        }
        for (int i = 1; i < statues.size(); i++) {
            statues.get(i-1).setValue(statues.get(i).getValue());
        }
        statues.get(4).setValue(0);
        for (int i = 0; i < statues.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_COUNT, statues.get(i).getValue());
            sqlDB.update(DATABASE_TABLE, values, "NUMBER = "+statues.get(i).getNumber(), null);
        }
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public void close() {
        myDBHelper.close();
    }

    public boolean changeLastValue(int value) {
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, value);
        sqlDB.update(DATABASE_TABLE, values, "NUMBER = 5", null);
        return true;
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NUMBER, KEY_COUNT}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NUMBER, KEY_COUNT}, "_id = "+rowID, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(DATABASE_TABLE, "_id = "+rowID, null) > 0;
    }
}
