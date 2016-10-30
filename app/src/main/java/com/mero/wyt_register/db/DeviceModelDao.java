package com.mero.wyt_register.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mero.wyt_register.bean.DeviceModelBean;

import java.util.Iterator;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by chenlei on 2016/10/28.
 */

public class DeviceModelDao {
    public static final String TAG = "DeviceModelDao";
    Context context;
    SQLiteDatabase db;
    List<DeviceModelBean> listDeviceModel = null;
    public  DeviceModelDao(Context context){
        this.context = context;
    }
    //Gson解析数组到对象中去
    public List<DeviceModelBean>  addObjectToList(){
        String jsonString = "[{\"brand\":\"华为\",\"model\":\"c8818\"},{\"brand\":\"华为\",\"model\":\"Y635\"}," +
                "{\"brand\":\"华为\",\"model\":\"Y635-CL00\"},{\"brand\":\"华为\",\"model\":\"P8 Lite\"},{\"brand\":\"华为\",\"model\":\"荣耀X2\"}," +
                "{\"brand\":\"华为\",\"model\":\"荣耀Hol-T00\"},{\"brand\":\"华为\",\"model\":\"荣耀3X畅玩版\"}," +
                "{\"brand\":\"华为\",\"model\":\"荣耀6\"},{\"brand\":\"华为\",\"model\":\"荣耀4C\"},{\"brand\":\"华为\",\"model\":\"荣耀X3升级版\"}," +
                "{\"brand\":\"华为\",\"model\":\"C8816\"},{\"brand\":\"华为\",\"model\":\"C8816D\"},{\"brand\":\"华为\",\"model\":\"Mate 7\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4C\"}," +
                "{\"brand\":\"华为\",\"model\":\"荣耀7\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4C\"},{\"brand\":\"华为\",\"model\":\"荣耀7\"},{\"brand\":\"华为\",\"model\":\"荣耀4A\"}," +
                "{\"brand\":\"华为\",\"model\":\"P8\"},{\"brand\":\"华为\",\"model\":\"C2900\"},{\"brand\":\"华为\",\"model\":\"Y320\"}," +
                "{\"brand\":\"华为\",\"model\":\"C8815\"},{\"brand\":\"华为\",\"model\":\"Mate\"},{\"brand\":\"华为\",\"model\":\"Y600\"}," +
                "{\"brand\":\"华为\",\"model\":\"荣耀6 Plus\"},{\"brand\":\"华为\",\"model\":\"C8817L\"},{\"brand\":\"华为\",\"model\":\"G5000\"}," +
                "{\"brand\":\"华为\",\"model\":\"C8817E\"},{\"brand:\":\"华为\",\"model\":\"荣耀6X\"},{\"brand\":\"华为\",\"model\":\"P8 Lite\"}," +
                "{\"brand\":\"华为\",\"model\":\"Ascend P8\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4X\"},{\"brand\":\"华为\",\"model\":\"G629\"},{\"brand\":\"华为\",\"model\":\"G620\"},{\"brand\":\"华为\",\"model\":\"荣耀X2\"}," +
                "{\"brand\":\"华为\",\"model\":\"荣耀3C\"},{\"brand\":\"华为\",\"model\":\"荣耀6 Plus\"},{\"brand\":\"华为\",\"model\":\"C2800\"},{\"brand\":\"华为\",\"model\":\"2601\"},{\"brand\":\"华为\",\"model\":\"G610S\"}," +
                "{\"brand\":\"华为\",\"model\":\"Ascend G302D\"},{\"brand\":\"华为\",\"model\":\"Ascend G6\"},{\"brand\":\"华为\",\"model\":\"Ascend G6\"},{\"brand\":\"华为\",\"model\":\"T8950N\"}," +
                "{\"brand\":\"华为\",\"model\":\"G610\"},{\"brand\":\"华为\",\"model\":\"C8813DQ\"},{\"brand\":\"华为\",\"model\":\"Y618\"},{\"brand\":\"华为\",\"model\":\"G630\"}," +
                "{\"brand\":\"华为\",\"model\":\"G521\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4\"}]";
        try{
            java.lang.reflect.Type type = new TypeToken<List<DeviceModelBean>>(){}.getType();
            Gson gson = new Gson();
            listDeviceModel = gson.fromJson(jsonString,type);
            Log.e("TAG",type+"");
            for(Iterator iterator = listDeviceModel.iterator();iterator.hasNext();){
                    DeviceModelBean bean = (DeviceModelBean) iterator.next();
                    Log.e(TAG,bean.getModel()+bean.getBrand());
            }
        }catch (Exception e){
            Log.e(TAG,"错误："+e.getMessage());
        }
        return listDeviceModel;
    }
    //插入数据到数据库
    public void insertModelToDb(List<DeviceModelBean> listDeviceModel){
        try{

            DbHelper dbHelper = new DbHelper(context);
            db = dbHelper.getWritableDatabase();
            //开始事务
            db.beginTransaction();
            Log.e(TAG,listDeviceModel.size()+"");
            String sql1 = "insert into device_model_info(id,model,brand) values (?,?,?)";
            for(DeviceModelBean f :listDeviceModel){
                db.execSQL(sql1,new Object[]{null,f.model,f.brand});
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //提交
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        }

    }
    //查询数据
    public Object[] queryDeviceInfo(int ids){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        int id  = 0;
        String model = "";
        String brand = "";
        SQLiteDatabase db  = dbHelper.getWritableDatabase();
        //获取游标
        Cursor cursor = db.query(true,"device_model_info",null,"id="+ids,null,null,null,null,null);
        if(cursor.moveToNext()){
            //获取列信息
             id = cursor.getInt(0);
            //获取手机型号
             model = cursor.getString(1);
            //获取手机品牌
             brand = cursor.getString(2);
        }
        return new Object[]{id,model,brand};
    }
}
