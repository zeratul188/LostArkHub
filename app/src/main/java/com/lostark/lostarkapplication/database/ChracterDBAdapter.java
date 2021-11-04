package com.lostark.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.ui.commander.Checklist;
import com.lostark.lostarkapplication.ui.commander.Chracter;

import static android.content.ContentValues.TAG;

public class ChracterDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_TYPE = "TYPE";
    public static final String KEY_NOW = "NOW";
    public static final String KEY_MAX = "MAX";
    public static final String KEY_ALARM = "ALARM";

    private static final String DATABASE_CREATE = "create table CHRACTERLIST (_id integer primary key, " +
            "NAME text not null, TYPE text not null, NOW integer not null, MAX integer not null, ALARM text not null);";

    private String databaseName;
    private String databaseTable;
    private static final int DATABASE_VERSION = 2;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public ChracterDBAdapter(Context mCtx, String name) {
        this.mCtx = mCtx;
        databaseName = "LOSTARKHUB_"+name;
        databaseTable = name;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        Context mCtx = null;
        String table = null;

        DatabaseHelper(Context context, String table, String database) {
            super(context, database, null, DATABASE_VERSION);
            this.mCtx = context;
            this.table = table;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+table);
            onCreate(db);
        }
    }

    public ChracterDBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx, databaseTable, databaseName);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }
    
    public long insertData(Checklist checklist) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, checklist.getName());
        values.put(KEY_TYPE, checklist.getType());
        values.put(KEY_NOW, checklist.getNow());
        values.put(KEY_MAX, checklist.getMax());
        values.put(KEY_ALARM, Boolean.toString(checklist.isAlarm()));
        return sqlDB.insert(databaseTable, null, values);
    }

    public Cursor fetchAllData() {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM}, "_id = "+rowID, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(databaseTable, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(databaseTable, "_id = "+rowID, null) > 0;
    }
}
