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
    private SQLiteDatabase database;

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
        database = db;
        String CREATE_TBL =
                "create table Character(_id integer primary key autoincrement,"
                + " name varchar(16), kingdom varchar(4), picture varchar(16),"
                + " birthDate varchar(20), anotherName varchar(16), sex integer,"
                + "bornPlace varchar(32))";
        db.execSQL(CREATE_TBL);
        Log.i("RUNNING", "DATABASE CREATED");
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
    public Cursor query_name(String[] condition) {
        SQLiteDatabase db = getReadableDatabase();
        //获取Cursor
        Cursor c = db.query(TABLE_NAME, null, "name=?", condition, null, null, null, null);
        return c;
    }

    //查询方法
    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        //获取Cursor
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return c;
    }

    //查询方法
    public Cursor query_kingdom(String[] condition) {
        SQLiteDatabase db = getReadableDatabase();
        //获取Cursor
        Cursor c = db.query(TABLE_NAME, null, "kingdom=?", condition, null, null, null, null);
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
        Log.i("RUNNING", "DATABASE INIT");
        String[] charactor_name = new String[] { "曹操", "刘备", "孙权",
                "董卓", "袁绍","张角", "周瑜", "诸葛亮", "司马懿" , "华佗",
                "关羽", "吕布", "赵云", "陆逊", "黄月英", "甄宓", "于吉",
                "孙策", "吕蒙", "马超", "黄忠", "郭嘉", "典韦", "曹冲"};
        String[] key = new String[] { "caocao", "liubei", "sunquan",
                "dongzhuo", "yuanshao", "zhangjiao", "zhouyu", "zhugeliang",
                "simayi", "huatuo", "guanyu", "lvbu", "zhaoyun", "luxun",
                "huangyueying", "zhenmi", "yuji", "sunce", "lvmeng", "machao",
                "huangzhong", "guojia", "dianwei", "caochong"};
        String[] kingdom = new String[] { "wei", "shu", "wu", "qun", "qun",
                "qun", "wu", "shu", "wei", "qun", "shu", "qun", "shu", "wu",
                "shu", "wei", "qun", "wu", "wu", "shu", "shu", "wei", "wei", "wei"};
        int [] sex = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1,
                1, 1, 1, 1, 1, 1, 1};
        String[] birthDate = new String[] { "155年-220年3月15日", "161年-223年6月10日",
                "182年-252年5月21日", "?-192年5月22日", "?-202年6月28日", "?-184年",
                "175年-210年", "181年-234年10月8日", "179年—251年9月7日", "约公元145年－公元208年",
                "?-220年", "？-198年", "？-229年", "183年-245年3月19日", "?-?", "183年1月26日—221年8月4日",
                "?-200年", "175年-200年5月5日", "179年—220年", "176年－222年", "?-－220年", "170年－207年",
                "？-197年", "196年－208年5月甲戌"};
        String[] anotherName = new String[] { "孟德", "玄德", "仲谋", "仲颖", "本初", "不明",
                "公瑾", "孔明", "仲达", "元化", "云长", "奉先", "子龙", "伯言", "不明", "不明", "不明",
                "伯符", "子明", "孟起", "汉升", "奉孝", "不明", "仓舒"};
        String[] bornPlace = new String[] { "沛国谯县（今安徽亳州)", "幽州涿郡涿县（今河北省涿州市）",
                "吴郡富春（今浙江杭州富阳区）", "陇西临洮（今甘肃省岷县）", "汝南汝阳（今河南省周口市商水县袁老乡袁老村)",
                "钜鹿（治今河北省邢台市巨鹿县）", "庐江舒（今合肥庐江舒县）", "徐州琅琊阳都（今山东临沂市沂南县）",
                "河内郡温县孝敬里（今河南省焦作市温县）", "沛国谯县（今安徽亳州)", "河东郡解县（今山西运城）",
                "五原郡九原县（在今内蒙古包头市九原区麻池镇西北）", "常山真定（今河北省正定）", "吴郡吴县（今江苏苏州）",
                "荆州沔南白水（今湖北襄阳）", "中山无极（今河北省无极县）", "徐州琅邪国(山东临沂市北)",
                "吴郡富春（今浙江富阳）", "汝南富陂人（今安徽阜南吕家岗）", "司隶扶风茂陵(陕西咸阳市兴平东)",
                "南阳（今河南南阳）", "颍川阳翟（今河南禹州）", "陈留己吾（今河南商丘市宁陵县己吾城村）",
                "豫州刺史部谯（今亳州）"};
        // initial the data
        ContentValues values = new ContentValues();
        for (int i = 0; i < bornPlace.length; i++) {
            values.put("name", charactor_name[i]);
            values.put("kingdom", kingdom[i]);
            values.put("picture", key[i]);
            values.put("sex", sex[i]);
            values.put("birthDate", birthDate[i]);
            values.put("anotherName", anotherName[i]);
            values.put("bornPlace", bornPlace[i]);
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

}
