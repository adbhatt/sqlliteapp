package com.example.adarshbhatt.sqlliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adarshbhatt on 5/23/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "user_table";
    public static final String TABLE2 = "votes_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "HIGHSCORE";
    public static final String V_COL_1 = "vote_id";
    public static final String V_COL_2 = "vote_count";
    public static final String V_COL_3 = "vote_type";
    public static final String V_COL_4 = "P_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, HIGHSCORE INTEGER)");
        db.execSQL("create table " + TABLE2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT, HIGHSCORE INTEGER)");
       // db.execSQL("CREATE TABLE " + TABLE2 + " (vote_id int PRIMARY KEY AUTOINCREMENT, vote_count int NOT NULL, vote_type varChar(50), " +
         //       "FOREIGN KEY P_id REFERENCES " + TABLE_NAME + "(ID))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE2);
        onCreate(db);
    }

    public boolean existsUser(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);
        while(res.moveToNext()) {
            if (name.equals(res.getString(1))) {
                return true;
            }
            else continue;
        }
        return false;
    }

    public boolean updateScore(String user, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
       // Cursor res = db.rawQuery("UPDATE " + DATABASE_NAME);
        ContentValues cv = new ContentValues();
        cv.put(COL_4, newScore);
        db.update(TABLE_NAME, cv, COL_2+"=?", new String[]{user});
        return false;
    }

    public int insertData (String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);
        while(res.moveToNext()) {
            if (username.equals(res.getString(1))) {
                return 1;
            }
            else continue;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        contentValues.put(COL_4, "0");
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return -1;
        }
        return 0;
    }
    public Cursor getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);
        return res;
    }

    public Cursor getVote() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE2, null);
        System.out.print(res.toString());
        return res;
    }

    public void updateVote(int newVote) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Cursor res = db.rawQuery("UPDATE " + DATABASE_NAME);
        ContentValues cv = new ContentValues();
        cv.put(V_COL_2, newVote);
        db.rawQuery("UPDATE " + TABLE2 + " SET VOTES=" + newVote +"", null);
    }

    public void insert (int votes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(V_COL_2, votes);
        db.insert(TABLE2, null, contentValues);
    }


}
