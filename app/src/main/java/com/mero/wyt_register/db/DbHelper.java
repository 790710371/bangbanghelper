package com.mero.wyt_register.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mero.wyt_register.bean.DeviceModelBean;

import java.util.List;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by chenlei on 2016/10/28.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    private Context context;
    private  static final String dbName = "bbzs.db";
    public DbHelper(Context context){
        super(context,dbName,null,1);
        this.context = context;
    }

    /**
     * 创建表
     * @param db
     */
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

    /**
     *
     * @param ls                 要插入的对象
     * @param databaseName      表名
     * @param b1                  键
     * @param b2                  键值
     * @param <T>                 泛型对象T
     */
    public <T> void inserDataToDb( List<T> ls,String databaseName,Object[] b1,Object[] b2) {
        SQLiteDatabase db = null;
        try {
             db = new DbHelper(context).getWritableDatabase();
            //开始事务
            db.beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("insert into ").append(databaseName).append("(");
            for (Object ob : b1) {
                sb.append((String) ob).append(",");
            }
            sb.deleteCharAt(sb.length() - 1).append(") values (");
            for (Object ob : b2) {
                sb.append("?").append(",");
            }
            sb.deleteCharAt(sb.length() - 1).append(")");
            String sql = sb.toString();
            Log.e(TAG, "sql" + sql);
            for(T t:ls){
                db.execSQL(sql,b2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //提交
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }
}
