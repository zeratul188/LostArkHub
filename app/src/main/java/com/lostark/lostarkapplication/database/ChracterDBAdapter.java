package com.lostark.lostarkapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lostark.lostarkapplication.ui.commander.Checklist;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class ChracterDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_TYPE = "TYPE";
    public static final String KEY_NOW = "NOW";
    public static final String KEY_MAX = "MAX";
    public static final String KEY_ALARM = "ALARM";
    public static final String KEY_CONTENT = "CONTENT";
    public static final String KEY_HISTORY = "HISTORY";
    public static final String KEY_RESTCOUNT = "RESTCOUNT";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_ICON = "ICON";

    private String databaseName;
    private String databaseTable;

    private String databaseCreate = "create table "+databaseTable+" (_id integer primary key, " +
            "NAME text not null, TYPE text not null, NOW integer not null, MAX integer not null, ALARM text not null, "+
            "CONTENT text not null, HISTORY integer not null, RESTCOUNT integer not null, POSITION integer not null, ICON integer not null);";

    private static final int DATABASE_VERSION = 8;
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
        String table = null, create = null;

        DatabaseHelper(Context context, String table, String database) {
            super(context, database, null, DATABASE_VERSION);
            this.mCtx = context;
            this.table = table;
            create = "create table "+table+" (_id integer primary key, " +
                    "NAME text not null, TYPE text not null, NOW integer not null, MAX integer not null, ALARM text not null, CONTENT text not null, HISTORY integer not null, RESTCOUNT integer not null, POSITION integer not null, ICON integer not null);";
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            try {
                if (oldVersion < 3) {
                    updateContentColumn(db);
                }
                if (oldVersion < 4) {
                    updateHistoryColumn(db);
                }
                if (oldVersion < 5) {
                    updateRestcountColumn(db);
                }
                if (oldVersion < 6) {
                    updatePositionColumn(db);
                }
                if (oldVersion < 7) {
                    resortPosition(db);
                }
                if (oldVersion < 8) {
                    updateIconColumn(db);
                }
            } catch (SQLException e) {
                db.execSQL("DROP TABLE IF EXISTS "+table);
                onCreate(db);
            }
            
        }
        
        /*
         * ICON 칼럼 추가
         */
        private void updateIconColumn(SQLiteDatabase a_db) {
            a_db.execSQL("ALTER TABLE " + table + " " + "ADD COLUMN " + "ICON" + " " + "integer not null DEFAULT " + "0");
        }

        /*
         * Position 재배열
         */
        private void resortPosition(SQLiteDatabase a_db) {
            Cursor cursor = a_db.query(table, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, null, null, null, null, null);
            cursor.moveToFirst();
            int index = 0;
            while (!cursor.isAfterLast()) {
                int rowID = cursor.getInt(0);
                String type = cursor.getString(2);
                ContentValues values = new ContentValues();
                if (type.equals("휴식게이지")) {
                    values.put(KEY_POSITION, 9998);
                } else {
                    values.put(KEY_POSITION, index);
                }
                a_db.update(table, values, "_id = ?", new String[] {Integer.toString(rowID)});
                cursor.moveToNext();
                index++;
            }
        }

        /*
         * Position 칼럼 추가
         */
        private void updatePositionColumn(SQLiteDatabase a_db) {
            a_db.execSQL("ALTER TABLE " + table + " " + "ADD COLUMN " + "POSITION" + " " + "integer not null DEFAULT " + "9999");
            Cursor cursor = a_db.query(table, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, null, null, null, null, null);
            cursor.moveToFirst();
            int index = 0;
            while (!cursor.isAfterLast()) {
                int rowID = cursor.getInt(0);
                String type = cursor.getString(2);
                ContentValues values = new ContentValues();
                if (type.equals("휴식게이지")) {
                    values.put(KEY_POSITION, 9998);
                } else {
                    values.put(KEY_POSITION, index);
                }
                a_db.update(table, values, "_id = ?", new String[] {Integer.toString(rowID)});
                cursor.moveToNext();
                index++;
            }
        }

        /*
         * Restcount 칼럼 추가
         */
        private void updateRestcountColumn(SQLiteDatabase a_db) {
            a_db.execSQL("ALTER TABLE " + table + " " + "ADD COLUMN " + "RESTCOUNT" + " " + "integer not null DEFAULT " + "0");
        }

        /*
         * History 칼럼 추가
         */
        private void updateHistoryColumn(SQLiteDatabase a_db) {
            a_db.execSQL("ALTER TABLE " + table + " " + "ADD COLUMN " + "HISTORY" + " " + "integer DEFAULT " + "0");
        }

        /*
         * Content 칼럼 추가
         */
        private void updateContentColumn(SQLiteDatabase a_db) {
            a_db.execSQL("ALTER TABLE " + table + " " + "ADD COLUMN " + "CONTENT" + " " + "text DEFAULT " + "''");
        }
    }

    public ChracterDBAdapter open() throws SQLException {
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

    public boolean resetRestCount() {
        String[] args = {"카던 휴식", "가디언 휴식", "에포나 휴식"};
        for (String arg : args) {
            ContentValues values = new ContentValues();
            values.put(KEY_RESTCOUNT, 0);
            sqlDB.update(databaseTable, values, "NAME = ?", new String[] {arg});
        }
        return true;
    }

    public int getRestCount(String name) {
        Cursor cursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "NAME = '"+name+"'", null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) return cursor.getInt(8);
        else return -1;
    }

    public int getRest(String name) {
        Cursor cursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "NAME = '"+name+"'", null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) return cursor.getInt(3);
        else return -1;
    }

    public boolean changeRestCount(String name, int value) {
        ContentValues values = new ContentValues();
        values.put(KEY_RESTCOUNT, value);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean changePosition(String name, int value) {
        ContentValues values = new ContentValues();
        values.put(KEY_POSITION, value);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public int getLastRowID() {
        Cursor cursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, null, null, null, null, null);
        cursor.moveToFirst();
        int result = 0;
        while (!cursor.isAfterLast()) {
            int rowID = cursor.getInt(0);
            if (result < rowID) {
                result = rowID;
            }
            cursor.moveToNext();
        }
        result++;
        return result;
    }

    public boolean changeRest(String name, int value) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOW, value);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean changeIcon(String name, int value) {
        ContentValues values = new ContentValues();
        values.put(KEY_ICON, value);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }
    
    public long insertData(Checklist checklist) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, checklist.getName());
        values.put(KEY_TYPE, checklist.getType());
        values.put(KEY_NOW, checklist.getNow());
        values.put(KEY_MAX, checklist.getMax());
        values.put(KEY_ALARM, Boolean.toString(checklist.isAlarm()));
        values.put(KEY_CONTENT, checklist.getContent());
        values.put(KEY_HISTORY, checklist.getHistory());
        values.put(KEY_RESTCOUNT, 0);
        values.put(KEY_POSITION, checklist.getPosition());
        return sqlDB.insert(databaseTable, null, values);
    }

    public boolean changeAlarm(String name, boolean isAlarm) {
        ContentValues values = new ContentValues();
        values.put(KEY_ALARM, Boolean.toString(isAlarm));
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean resetData(String type, int day) {
        day--;
        Cursor cursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            int now = cursor.getInt(3);
            int max = cursor.getInt(4);
            if (name.equals("카오스 던전")) {
                if (max-now > 0) {
                    Cursor dungeonCursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "NAME='카던 휴식'", null, null, null, null);
                    dungeonCursor.moveToFirst();
                    if (dungeonCursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        values.put(KEY_NAME, "카던 휴식");
                        values.put(KEY_TYPE, "휴식게이지");
                        values.put(KEY_NOW, 0);
                        values.put(KEY_MAX, 10);
                        values.put(KEY_ALARM, false);
                        values.put(KEY_CONTENT, "");
                        values.put(KEY_HISTORY, 0);
                        sqlDB.insert(databaseTable, null, values);
                    } else {
                        int undo_now = dungeonCursor.getInt(3);
                        int result = undo_now+(max-now);
                        if (day != 0) result += 2*day;
                        if (result > 10) result = 10;
                        ContentValues values = new ContentValues();
                        values.put(KEY_NOW, result);
                        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {"카던 휴식"});
                    }
                }
            } else if (name.equals("가디언 토벌")) {
                if (max-now > 0) {
                    Cursor bossCursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "NAME='가디언 휴식'", null, null, null, null);
                    bossCursor.moveToFirst();
                    if (bossCursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        values.put(KEY_NAME, "가디언 휴식");
                        values.put(KEY_TYPE, "휴식게이지");
                        values.put(KEY_NOW, 0);
                        values.put(KEY_MAX, 10);
                        values.put(KEY_ALARM, false);
                        values.put(KEY_CONTENT, "");
                        values.put(KEY_HISTORY, 0);
                        sqlDB.insert(databaseTable, null, values);
                    } else {
                        int undo_now = bossCursor.getInt(3);
                        int result = undo_now+(max-now);
                        if (day != 0) result += 2*day;
                        if (result > 10) result = 10;
                        ContentValues values = new ContentValues();
                        values.put(KEY_NOW, result);
                        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {"가디언 휴식"});
                    }
                }
            } else if (name.equals("에포나 일일 의뢰")) {
                if (max-now > 0) {
                    Cursor questCursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "NAME='에포나 휴식'", null, null, null, null);
                    questCursor.moveToFirst();
                    if (questCursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        values.put(KEY_NAME, "에포나 휴식");
                        values.put(KEY_TYPE, "휴식게이지");
                        values.put(KEY_NOW, 0);
                        values.put(KEY_MAX, 10);
                        values.put(KEY_ALARM, false);
                        values.put(KEY_CONTENT, "");
                        values.put(KEY_HISTORY, 0);
                        sqlDB.insert(databaseTable, null, values);
                    } else {
                        int undo_now = questCursor.getInt(3);
                        int result = undo_now+(max-now);
                        if (day != 0) result += 3*day;
                        if (result > 10) result = 10;
                        ContentValues values = new ContentValues();
                        values.put(KEY_NOW, result);
                        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {"에포나 휴식"});
                    }
                }
            }
            cursor.moveToNext();
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NOW, 0);
        sqlDB.update(databaseTable, values, "TYPE = ?", new String[] {type});
        return true;
    }

    public boolean resetWeek(String type) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOW, 0);
        sqlDB.update(databaseTable, values, "TYPE = ?", new String[] {type});
        return true;
    }

    public boolean isSame(String name, String type) {
        Cursor cursor = sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "TYPE='"+type+"'", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String find = cursor.getString(1);
            if (find.equals(name)) return true;
            cursor.moveToNext();
        }
        return false;
    }

    public boolean changeHistory(String name, int history) {
        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY, history);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean changeNow(String name, int now) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOW, now);
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public boolean changeData(String name, Checklist checklist) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, checklist.getName());
        values.put(KEY_TYPE, checklist.getType());
        values.put(KEY_NOW, checklist.getNow());
        values.put(KEY_MAX, checklist.getMax());
        values.put(KEY_ALARM, Boolean.toString(checklist.isAlarm()));
        values.put(KEY_CONTENT, checklist.getContent());
        values.put(KEY_HISTORY, checklist.getHistory());
        sqlDB.update(databaseTable, values, "NAME = ?", new String[] {name});
        return true;
    }

    public Cursor fetchData(String name) {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "NAME = '"+name+"'", null, null, null, null);
    }

    public boolean resetHistory() {
        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY, 0);
        sqlDB.update(databaseTable, values, null, null);
        return true;
    }

    public Cursor fetchAllData() {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, null, null, null, null, null);
    }

        public Cursor fetchData(int rowID) {
        return sqlDB.query(databaseTable, new String[] {KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_NOW, KEY_MAX, KEY_ALARM, KEY_CONTENT, KEY_HISTORY, KEY_RESTCOUNT, KEY_POSITION, KEY_ICON}, "_id = "+rowID, null, null, null, null);
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
