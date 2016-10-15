package com.mero.wyt_register.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by chenlei on 2016/10/15.
 */

public class DeviceUtils {
    private DeviceUtils(){
        throw new ExceptionInInitializerError("不可实例化");
    }
    public static String getDeviceId(Context context){
        String deviceId = "";
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        try{
           deviceId = tm.getDeviceId();
        }catch (Exception e){
          deviceId = "获取deviceId失败";
        }finally {
            return deviceId;
        }
    }
}
