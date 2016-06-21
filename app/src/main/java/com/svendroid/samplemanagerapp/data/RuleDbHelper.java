package com.svendroid.samplemanagerapp.data;

/**
 * Created by svhe on 11.04.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class RuleDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sampleManagerApp.db";
    public static final String TABLE_NAME = "rules";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RULE_ID = "rule_id";
    public static final String COLUMN_BEGIN = "begin";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_REPEATS = "repeats";
    public static final String COLUMN_MEASURE = "measure";

    private HashMap hp;

    public RuleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(" + COLUMN_ID +" integer primary key autoincrement, " +
                        COLUMN_RULE_ID + " text," +
                        COLUMN_BEGIN + " integer," +
                        COLUMN_END + " integer," +
                        COLUMN_REPEATS + " integer," +
                        COLUMN_MEASURE + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact(String ruleId, int begin, int end, int repeats, String measure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RULE_ID, ruleId);
        contentValues.put(COLUMN_BEGIN, begin);
        contentValues.put(COLUMN_END, end);
        contentValues.put(COLUMN_REPEATS, repeats);
        contentValues.put(COLUMN_MEASURE, measure);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + id, null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(Integer id, String ruleId, int begin, int end, int repeats, String measure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RULE_ID, ruleId);
        contentValues.put(COLUMN_BEGIN, begin);
        contentValues.put(COLUMN_END, end);
        contentValues.put(COLUMN_REPEATS, repeats);
        contentValues.put(COLUMN_MEASURE, measure);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAll() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(COLUMN_BEGIN)));
            res.moveToNext();
        }
        return array_list;
    }
}