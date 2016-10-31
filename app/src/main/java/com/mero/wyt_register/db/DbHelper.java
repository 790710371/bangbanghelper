package com.mero.wyt_register.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenlei on 2016/10/28.
 */

public class DbHelper extends SQLiteOpenHelper {
    private Context context;
    private  static final String dbName = "bbzs.db";
    public DbHelper(Context context){
        super(context,dbName,null,1);
        this.context = context;
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table if not exists device_model_info(id integer primary key AUTOINCREMENT,model varchar(20),brand varchar(20),manufacturer varchar(20))";
        db.execSQL(sql1);
    }
    //删除数据库
    public void deleteDb(){
        context.deleteDatabase(dbName);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
