package com.lostark.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.ui.commander.Chracter;

import static android.content.ContentValues.TAG;

public class ChracterListDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_JOB = "JOB";
    public static final String KEY_LEVEL = "LEVEL";
    public static final String KEY_ALARM = "ALARM";
    public static final String KEY_SERVER = "SERVER";
    public static final String KEY_FAVORITE = "FAVORITE";

    private static final String DATABASE_CREATE = "create table CHRACTERLIST (_id integer primary key, " +
            "NAME text not null, JOB text not null, LEVEL integer not null, ALARM text not null, SERVER text not null, FAVORITE integer not null);";

    private static final String DATABASE_NAME = "LOSTARKHUB_CHRACTERLIST";
    private static final String DATABASE_TABLE = "CHRACTERLIST";
    private static final int DATABASE_VERSION = 4;
    private Context mCtx;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    public ChracterListDBAdapter(Context mCtx) {
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
            try {
                if (oldVersion < 4) {
                    updateFavoriteColumn(db);
                }
                if (oldVersion < 3) {
                    updateServerColumn(db);
                }
            } catch (SQLException e) {
                db.execSQL("DROP TABLE IF EXISTS CHRACTERLIST");
                onCreate(db);
            }
        }

        /*
         * Server 칼럼 추가
         */
        private void updateServerColumn(SQLiteDatabase a_db) {
            a_db.execSQL("ALTER TABLE " + DATABASE_TABLE + " " + "ADD COLUMN " + KEY_SERVER + " " + "text DEFAULT " + "'니나브'");
        }

        /*
         * Favorite 칼럼 추가
         */
        private void updateFavoriteColumn(SQLiteDatabase sd) {
            sd.execSQL("ALTER TABLE " + DATABASE_TABLE + " " + "ADD COLUMN " + KEY_FAVORITE + " " + "integer DEFAULT " + "0");
        }
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public ChracterListDBAdapter open() throws SQLException {
        myDBHelper = new DatabaseHelper(mCtx);
        sqlDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }
    
    public long insertData(Chracter chracter) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, chracter.getName());
        values.put(KEY_JOB, chracter.getJob());
        values.put(KEY_LEVEL, chracter.getLevel());
        values.put(KEY_SERVER, chracter.getServer());
        values.put(KEY_ALARM, Boolean.toString(chracter.isAlarm()));
        values.put(KEY_FAVORITE, chracter.getFavorite());
        return sqlDB.insert(DATABASE_TABLE, null, values);
    }

    public boolean changeAlarm(String name, boolean isAlarm) {
        String sql = "UPDATE "+DATABASE_TABLE+" SET ALARM='"+Boolean.toString(isAlarm)+"' WHERE NAME='"+name+"'";
        sqlDB.execSQL(sql);
        /*ContentValues values = new ContentValues();
        values.put(KEY_ALARM, isAlarm);
        sqlDB.update(DATABASE_TABLE, values, "NAME = '?'", new String[] {name});*/
        return true;
    }

    public boolean checkFavorite(String name, boolean isFavorite) {
        int favorite;
        if (isFavorite) favorite = 1;
        else favorite = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITE, favorite);
        sqlDB.update(DATABASE_TABLE, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean changeInfo(String name, String new_name, String job, int level, String server) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, new_name);
        values.put(KEY_JOB, job);
        values.put(KEY_LEVEL, level);
        values.put(KEY_SERVER, server);
        sqlDB.update(DATABASE_TABLE, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean changeLevel(String name, int level) {
        ContentValues values = new ContentValues();
        values.put(KEY_LEVEL, level);
        sqlDB.update(DATABASE_TABLE, values, "NAME = ?", new String[] {name});
        return true;
    }

    public int getRowID(String name) {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_LEVEL, KEY_ALARM, KEY_SERVER, KEY_FAVORITE}, "NAME = '"+name+"'", null, null, null, null);
        cursor.moveToFirst();
        int result = 0;
        while (!cursor.isAfterLast()) {
            result = cursor.getInt(0);
            cursor.moveToNext();
        }
        return result;
    }

    public Cursor fetchAllData() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_LEVEL, KEY_ALARM, KEY_SERVER, KEY_FAVORITE}, null, null, null, null, null);
    }

    public Cursor fetchData(int rowID) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_LEVEL, KEY_ALARM, KEY_SERVER, KEY_FAVORITE}, "_id = "+rowID, null, null, null, null);
    }

    public Cursor fetchData(String name) {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_LEVEL, KEY_ALARM, KEY_SERVER, KEY_FAVORITE}, "NAME = '"+name+"'", null, null, null, null);
    }

    public Cursor findFavoriteChracter() {
        return sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_LEVEL, KEY_ALARM, KEY_SERVER, KEY_FAVORITE}, "FAVORITE = 1", null, null, null, null);
    }

    public boolean deleteAllData() {
        return sqlDB.delete(DATABASE_TABLE, null, null) > 0;
    }

    public boolean deleteData(int rowID) {
        return sqlDB.delete(DATABASE_TABLE, "_id = "+rowID, null) > 0;
    }

    public boolean deleteData(String name) {
        return sqlDB.delete(DATABASE_TABLE, "NAME = '"+name+"'", null) > 0;
    }

    public boolean emptyFavorite() {
        Cursor cursor = sqlDB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_JOB, KEY_LEVEL, KEY_ALARM, KEY_SERVER, KEY_FAVORITE}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(6) == 1) return false;
            cursor.moveToNext();
        }
        return true;
    }

}
