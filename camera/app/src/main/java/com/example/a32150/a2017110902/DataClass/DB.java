package com.example.a32150.a2017110902.DataClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DB {
    private static final String DATABASE_NAME = "project.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "mytable";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
        + TABLE_NAME + "(`_id` INTEGER PRIMARY KEY , `CityName` VARCHAR, `RegionName` VARCHAR, `Address` VARCHAR, `DeptNm` VARCHAR, `BranchNm` VARCHAR, `Longitude` VARCHAR, `Latitude` VARCHAR, `direct` VARCHAR, `limit` VARCHAR);";

    private Context mCtx;
    private DataBaseOpenHelper helper;
    private SQLiteDatabase db;

    public DB(Context context) {
        mCtx = context;
    }

    public DB open() {
        helper = new DataBaseOpenHelper(mCtx);
        db = helper.getReadableDatabase();
        return this;
    }

    private class DataBaseOpenHelper extends SQLiteOpenHelper {

        public DataBaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    private static final String KEY_ROWID = "_id";
    private static final String KEY_CITYNAME = "CityName";
    private static final String KEY_REGIONNAME = "RegionName";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_DEPTNM = "DeptNm";
    private static final String KEY_BRANCHNM = "BranchNm";
    private static final String KEY_LONGITUDE = "Longitude";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_DIRECT = "direct";
    private static final String KEY_LIMIT = "`limit`";

    String[] strCols = {KEY_ROWID, KEY_CITYNAME, KEY_REGIONNAME, KEY_ADDRESS, KEY_DEPTNM, KEY_BRANCHNM, KEY_LONGITUDE, KEY_LATITUDE, KEY_DIRECT, KEY_LIMIT};

    public Cursor getAll() {
        return db.query(TABLE_NAME, strCols, null, null, null, null, null);
    }

    public Cursor get(long rowId) throws SQLException {
        Cursor mCursor = db.query(true,
            TABLE_NAME,
            strCols,
            KEY_ROWID + "=" + rowId,
            null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public long create(String noteName) {
        Date now = new Date();
        ContentValues args = new ContentValues();
        //args.put(KEY_NOTE, noteName);
        //args.put(KEY_CITY, now.getTime());
        return db.insert(TABLE_NAME, null, args);
    }

    public boolean delete(long rowId) {
        return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean update(long rowId, String noteName) {
        ContentValues args = new ContentValues();
        //args.put(KEY_NOTE, noteName);
        return db.update(TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
