package com.example.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lostarkapplication.ui.gallery.Stone;

import static android.content.ContentValues.TAG;

public class Earring2DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_GRADE = "GRADE";
    public static final String KEY_STAMP1 = "STAMP1";
    public static final String KEY_STAMP2 = "STAMP2";
    public static final String KEY_STAMP3 = "STAMP3";
    public static final String KEY_CNT1 = "CNT1";
    public static final String KEY_CNT2 = "CNT2";
    public static final String KEY_CNT3 = "CNT3";

    private static final String DATABASE_CREATE = "create table EARRING2 (_id integer primary key, " +
            "GRADE text not null, STAMP1 text not null, STAMP2 text not null, STAMP3 text not null, " +
            "CNT1 interger not null, CNT2 interger not null, CNT3 interger not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_EARRING2";
    private static final String DATABASE_TABLE = "EARRING2";
    private static final int DATABASE_VERSION = 2;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public Earring2DBAdapter(Context mCtx) {
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
            db.execSQL("DROP TABLE IF EXISTS EARRING2");
            onCreate(db);
        }
    }

    public Earring2DBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }
    
    public long insertData(Stone stone) {
        String[] stamps = stone.getStamp();
        int[] cnts = stone.getCnt();
        ContentValues values = new ContentValues();
        values.put(KEY_GRADE, stone.getGrade());
        values.put(KEY_STAMP1, stamps[0]);
        values.put(KEY_STAMP2, stamps[1]);
        values.put(KEY_STAMP3, stamps[2]);
        values.put(KEY_CNT1, cnts[0]);
        values.put(KEY_CNT2, cnts[1]);
        values.put(KEY_CNT3, cnts[2]);
        return sqlDB.insert(DATABASE_TABLE, null, values);
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_GRADE, KEY_STAMP1, KEY_STAMP2, KEY_STAMP3, KEY_CNT1, KEY_CNT2, KEY_CNT3}, null, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }
}
