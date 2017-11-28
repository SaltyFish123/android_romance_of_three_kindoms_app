package com.example.jason.lab5;

/**
 * Created by Jason on 2017/11/28.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "character.db";
    private final static String TABLE_NAME = "Character";

    //SQLiteOpenHelper子类必须要的一个构造函数
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        //必须通过super 调用父类的构造函数
        super(context, name, factory, version);
    }

    //数据库的构造函数，传递三个参数的
    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
    public DBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    // 回调函数，第一次创建时才会调用此函数，创建一个数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("RUNNING", "DATABASE CREATED");
        String CREATE_TBL =
                "create table Character(_id integer primary key autoincrement,"
                + " name varchar(16), kingdom varchar(4), picture varchar(16),"
                + " birthDate varchar(20), deathDate varchar(20), sex integer,"
                + "bornPlace varchar(32))";
        db.execSQL(CREATE_TBL);
    }

    //回调函数，当你构造DBHelper的传递的Version与之前的Version调用此函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RUNNING", "DATABASE update to:" + newVersion);

    }

    //插入方法
    public void insert(ContentValues values) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        //插入数据库中
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //查询方法
    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        //获取Cursor
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return c;

    }

    //根据唯一标识_id  来删除数据
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }


    //更新数据库的内容
    public void update(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public void init() {
        SQLiteDatabase db = getReadableDatabase();
        String[] charactor_name = new String[] { "曹操", "刘备", "孙权",
                "董卓", "袁绍","张角", "周瑜", "诸葛亮", "司马懿" , "华佗"  };
        String[] key = new String[] { "caocao", "liubei", "sunquan",
                "dongzhuo", "yuanshao", "zhangjiao", "zhouyu", "zhugeliang",
                "simayi", "huatuo"};
        String[] kingdom = new String[] { "wei", "shu", "wu", "qun", "qun",
                "qun", "wu", "shu", "wei", "qun"};
        // initial the data
        ContentValues values = new ContentValues();
        for (int i = 0; i < 10; i++) {
            values.put("name", charactor_name[i]);
            values.put("kingdom", kingdom[i]);
            values.put("picture", key[i]);
            db.insert(TABLE_NAME, null, values);
        }
        Log.i("RUNNING", "DATABASE INIT");
    }

}
