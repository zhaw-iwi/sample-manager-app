package com.svendroid.samplemanagerapp.data;

/**
 * Created by svhe on 11.04.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.svendroid.samplemanagerapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sampleManagerApp.db";
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_TOKEN = "token";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(" + COLUMN_ID +" integer primary key autoincrement, " +
                        COLUMN_EMAIL + " text," +
                        COLUMN_TOKEN + " text," +
                        COLUMN_USER_ID + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String email, String userId, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_USER_ID, userId);
        contentValues.put(COLUMN_TOKEN, token);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + id, null);
        return res;
    }

    public Cursor getData(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where user_id='" + userId + "'", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(String userId, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TOKEN, token);
        db.update(TABLE_NAME, contentValues, "user_id = ? ", new String[]{userId});
        return true;
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void tabulaRasa() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public ArrayList<User> getAll() {
        ArrayList<User> array_list = new ArrayList<User>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            User user = new User();
            user.set_id(res.getString(res.getColumnIndex(COLUMN_USER_ID)));
            user.setEmail(res.getString(res.getColumnIndex(COLUMN_EMAIL)));
            user.setGcmToken(res.getString(res.getColumnIndex(COLUMN_TOKEN)));
            array_list.add(user);
            res.moveToNext();
        }
        return array_list;
    }
}