package com.lostark.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.ui.gallery.Preset;

import static android.content.ContentValues.TAG;

public class StonePresetDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_GRADE = "GRADE";
    public static final String KEY_STAMPS = "STAMPS";

    private static final String DATABASE_CREATE = "create table STONEPRESET (_id integer primary key, " +
            "NAME text not null, GRADE text not null, STAMPS text not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_STONEPRESET";
    private static final String DATABASE_TABLE = "STONEPRESET";
    private static final int DATABASE_VERSION = 2;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public StonePresetDBAdapter(Context mCtx) {
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

    public StonePresetDBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public int getCount() {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_GRADE, KEY_STAMPS}, null, null, null, null, null);
        return cursor.getCount();
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertData(Preset preset) {
        String stamps = "";
        for (int i = 0; i < preset.getStamps().length; i++) {
            stamps += preset.getStamps()[i];
            if (i < preset.getStamps().length-1) {
                stamps += ",";
            }
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, preset.getName());
        values.put(KEY_GRADE, preset.getGrade());
        values.put(KEY_STAMPS, stamps);
        return sqlDB.insert(DATABASE_TABLE, null, values);
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_GRADE, KEY_STAMPS}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_GRADE, KEY_STAMPS}, "_id = "+rowID, null, null, null, null);
    }

    public int getNextID() {
        int id = 0;
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_GRADE, KEY_STAMPS}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (id < cursor.getInt(0)) {
                id = cursor.getInt(0);
            }
            cursor.moveToNext();
        }
        id++;
        return id;
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(DATABASE_TABLE, "_id = "+rowID, null) > 0;
    }
}
