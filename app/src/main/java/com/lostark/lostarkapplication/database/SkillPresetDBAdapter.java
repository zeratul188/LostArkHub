package com.lostark.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.ui.slideshow.objects.Stat;

import static android.content.ContentValues.TAG;

public class SkillPresetDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_JOB = "JOB";
    public static final String KEY_SKILLPOINT = "SKILLPOINT";
    public static final String KEY_MAX = "MAX";

    private static final String DATABASE_CREATE = "create table SKILLPRESET (_id integer primary key, " +
            "NAME text not null, JOB text not null, SKILLPOINT int not null, MAX int not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_SKILLPRESET";
    private static final String DATABASE_TABLE = "SKILLPRESET";
    private static final int DATABASE_VERSION = 2;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public SkillPresetDBAdapter(Context mCtx) {
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
            db.execSQL("DROP TABLE IF EXISTS SKILLPRESET");
            onCreate(db);
        }
    }

    public SkillPresetDBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public int getCount() {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_SKILLPOINT, KEY_MAX}, null, null, null, null, null);
        return cursor.getCount();
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertData(String name, String job, int skill_point, int max) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_JOB, job);
        values.put(KEY_SKILLPOINT, skill_point);
        values.put(KEY_MAX, max);
        return sqlDB.insert(DATABASE_TABLE, null, values);
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_SKILLPOINT, KEY_MAX}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_SKILLPOINT, KEY_MAX}, "_id = "+rowID, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(DATABASE_TABLE, "_id = "+rowID, null) > 0;
    }
}
