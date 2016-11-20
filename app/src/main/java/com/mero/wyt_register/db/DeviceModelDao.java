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
        String jsonString = "[{\"brand\":\"华为\",\"model\":\"c8818\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Y635\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Y635-CL00\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"P8Lite\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀X2\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀Hol-T00\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀3X畅玩版\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀6\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀4C\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀X3升级版\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C8816\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C8816D\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Mate7\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4C\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀7\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4C\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀7\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀4A\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"P8\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C2900\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Y320\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C8815\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Mate\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Y600\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀6Plus\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C8817L\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G5000\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C8817E\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"P8Lite\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"AscendP8\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4X\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G629\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G620\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀X2\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀3C\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀6Plus\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C2800\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"2601\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G610S\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"AscendG302D\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"AscendG6\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"AscendG6\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"T8950N\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G610\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"C8813DQ\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"Y618\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G630\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"G521\",\"manufacturer\":\"HUAWEI\"},{\"brand\":\"华为\",\"model\":\"荣耀畅玩4\",\"manufacturer\":\"HUAWEI\"}]";
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
            String sql1 = "insert into device_model_info(id,model,brand,manufacturer) values (?,?,?,?)";
            for(DeviceModelBean f :listDeviceModel){
                db.execSQL(sql1,new Object[]{null,f.model,f.brand,f.manufacturer});
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //提交
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        }}


    //查询数据
    public Object[] queryDeviceInfo(int ids){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        int id  = 0;
        String model = "";
        String brand = "";
        String manufacturer = "";
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
            //获取制造商
            manufacturer = cursor.getString(3);
        }
        return new Object[]{id,model,brand,manufacturer};
    }
}
