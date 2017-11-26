package com.example.jason.lab5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;

/**
 * Created by joey on 11/23/17.
 */

public class Character extends SQLiteOpenHelper {
    //The image I use the int type for the image id to store in the table
    public final static String NAME = "Characters";
    public final static int VERSION = 1;
    //The following String variables are the name of the columns of the table
    //Storing in the database is the id of the image
    public final static String IMAGE = "IMAGE";
    public final static String CHARACTER_NAME = "CHARACTER_NAME";
    public final static String BIRTHDAY = "BIRTHDAY";
    public final static String DEATHDAY = "DEATHDAY";
    //0 means male while 1 means female
    public final static String SEX = "SEX";
    //"wei", "shu", "wu", "qun" is represented 0, 1, 2, 3 respectively
    public final static String KINGDOM = "KINGDOM";

    public Character(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + NAME + " (" + CHARACTER_NAME + " TEXT PRIMARY KEY, " + IMAGE + " TEXT, " + BIRTHDAY + " TEXT, " + DEATHDAY + " TEXT, " + SEX + " INTEGER, " + KINGDOM + " INTEGER)");
        //Initialize the database with my simple data.
        data_init(db);
    }

    public void data_init(SQLiteDatabase db) {
        String[] character_name = new String[] { "曹操", "刘备", "孙权",
                "董卓", "袁绍","张角", "周瑜", "诸葛亮", "司马懿" , "华佗"  };
        int[] kingdom = new int[] { 0, 1, 2, 3, 3,
                3, 2, 1, 0, 3};
        String birthday = "year/month/day";
        String deathday = "year/month/day";
        String[] image =new String[] { "caocao", "liubei", "sunquan",
                "dongzhuo", "yuanshao", "zhangjiao", "zhouyu", "zhugeliang",
                "simayi", "huatuo"};
        int sex = 0;
        for (int i = 0; i < 10; i++) {
            db.execSQL("INSERT INTO " + NAME + " VALUES('" + character_name[i] + "', '" + image[i] + "', '"
                    + birthday + "', '" + deathday + "', '" + Integer.toString(sex) + "', '" + Integer.toString(kingdom[i]) + "');");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    public boolean insert (String character_name, int image_id, String birthday, String deathday, int sex, int kingdom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHARACTER_NAME, character_name);
        contentValues.put(IMAGE_ID, image_id);
        contentValues.put(BIRTHDAY, birthday);
        contentValues.put(DEATHDAY, deathday);
        contentValues.put(SEX, sex);
        contentValues.put(KINGDOM, kingdom);
        return (db.insert(NAME, null, contentValues) != -1);
    }

    //Get the data from the table by the name of the character
    //You can also with the method Cursor.isNull() to determind whether there exists any data you wish
    public Cursor getCharacterData(String character_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + NAME + " where CHARACTER_NAME="+ character_name +"", null);
        return res;
    }
    //If succeed then return true else return false
    public boolean delete(String character_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(NAME, CHARACTER_NAME + " = ?", new String[] {character_name}) != 0);
    }

    public boolean update(String character_name, int image_id, String birthday, String deathday, int sex, int kingdom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHARACTER_NAME, character_name);
        contentValues.put(IMAGE_ID, image_id);
        contentValues.put(BIRTHDAY, birthday);
        contentValues.put(DEATHDAY, deathday);
        contentValues.put(SEX, sex);
        contentValues.put(KINGDOM, kingdom);
        return (db.update(NAME, contentValues, CHARACTER_NAME + " = ? ", new String[] {character_name}) != 0);
    }

    //Return the character name in chinese and you can get the data from the database
    public ArrayList<String> getAllCharactersName() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> result = new ArrayList<String>();
        Cursor res = db.rawQuery("select * from " + NAME, null);
        if (res != null && res.moveToFirst()) {
            do {
                result.add(res.getString(res.getColumnIndex(CHARACTER_NAME)));
            } while (res.moveToNext());
        }
        return result;
    }
}
