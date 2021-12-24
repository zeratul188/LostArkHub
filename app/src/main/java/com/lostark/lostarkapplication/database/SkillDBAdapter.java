package com.lostark.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.ui.commander.Checklist;
import com.lostark.lostarkapplication.ui.skill.SkillPreset;

import static android.content.ContentValues.TAG;

public class SkillDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_NOW = "NOW";
    public static final String KEY_RUNE = "RUNE";
    public static final String KEY_TRIPOD1 = "TRIPOD1";
    public static final String KEY_TRIPOD2 = "TRIPOD2";
    public static final String KEY_TRIPOD3 = "TRIPOD3";

    private String databaseName;
    private String databaseTable;

    private String databaseCreate = "create table "+databaseTable+" (_id integer primary key, " +
            "NAME text not null, NOW integer not null, RUNE integer not null, "+
            "TRIPOD1 integer not null, TRIPOD2 integer not null, TRIPOD3 integer not null);";

    private static final int DATABASE_VERSION = 2;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public SkillDBAdapter(Context mCtx, String name) {
        this.mCtx = mCtx;
        databaseName = "LOSTARKHUB_SKILL"+name;
        databaseTable = "SKILL"+name;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        Context mCtx = null;
        String table = null, create = null;

        DatabaseHelper(Context context, String table, String database) {
            super(context, database, null, DATABASE_VERSION);
            this.mCtx = context;
            this.table = table;
            create = "create table "+table+" (_id integer primary key, " +
                    "NAME text not null, NOW integer not null, RUNE integer not null, "+
                    "TRIPOD1 integer not null, TRIPOD2 integer not null, TRIPOD3 integer not null);";
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+table);
            onCreate(db);
        }
    }

    public SkillDBAdapter open() throws SQLException {
        System.out.println("Opened");
        myDBHelper = new DatabaseHelper(mCtx, databaseTable, databaseName);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void close() {
        myDBHelper.close();
    }
    
    public long insertData(SkillPreset skillPreset) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, skillPreset.getName());
        values.put(KEY_NOW, skillPreset.getNow());
        values.put(KEY_RUNE, skillPreset.getRune());
        values.put(KEY_TRIPOD1, skillPreset.getTripods()[0]);
        values.put(KEY_TRIPOD2, skillPreset.getTripods()[1]);
        values.put(KEY_TRIPOD3, skillPreset.getTripods()[2]);
        return sqlDB.insert(databaseTable, null, values);
    }

    public boolean changeData(String name, SkillPreset skillPreset) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, skillPreset.getName());
        values.put(KEY_NOW, skillPreset.getNow());
        values.put(KEY_RUNE, skillPreset.getRune());
        values.put(KEY_TRIPOD1, skillPreset.getTripods()[0]);
        values.put(KEY_TRIPOD2, skillPreset.getTripods()[1]);
        values.put(KEY_TRIPOD3, skillPreset.getTripods()[2]);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public Cursor fetchAllData() {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_NOW, KEY_RUNE, KEY_TRIPOD1, KEY_TRIPOD2, KEY_TRIPOD3}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_NOW, KEY_RUNE, KEY_TRIPOD1, KEY_TRIPOD2, KEY_TRIPOD3}, "_id = "+rowID, null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(databaseTable, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(databaseTable, "_id = "+rowID, null) > 0;
    }

    public boolean deleteData(String name) {
        return sqlDB.delete(databaseTable, "NAME = '"+name+"'", null) > 0;
    }

    public void dropTable() {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+databaseTable);
    }
}
