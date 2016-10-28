package com.mero.wyt_register.xposed;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.MyApplication;
import com.mero.wyt_register.utils.AppUtils;
import com.mero.wyt_register.utils.DeviceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.R.attr.handle;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Environment.getExternalStorageDirectory;
import static android.view.View.X;
import static android.view.View.inflate;
import static de.robv.android.xposed.XposedHelpers.getParameterTypes;
import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by chenlei on 2016/10/15.
 */

public class XposedHookModule implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            if(!lpparam.packageName.equals("com.mero.wyt_register")){
                return;
            }
            XSharedPreferences xpre = new XSharedPreferences("com.mero.wyt_register",Config.ID);
            final Class<?> thiz = XposedHelpers.findClass("com.mero.wyt_register.MainActivity",lpparam.classLoader);
            final Class<?> cl = XposedHelpers.findClass("android.telephony.TelephonyManager",lpparam.classLoader);
            final Class<?> cz = XposedHelpers.findClass("android.location.Location",lpparam.classLoader);
            final Class<?> cc = XposedHelpers.findClass("android.net.wifi.WifiInfo",lpparam.classLoader);
            final Class<?> build = XposedHelpers.findClass("android.os.Build",lpparam.classLoader);
            try{
                hookMethod(thiz,"getResult","已安装");
                hookMethod(cl,"getSimSerialNumber",xpre.getString("simSerialNumber",null));//修改sim序列号
                hookMethod(cl,"getDeviceId",xpre.getString("imei",null));//修改设备IMEI
                hookMethod(cl,"getSubscriberId",xpre.getString("imsi",null));//修改IMSI
                hookMethod(cl,"getSimCountryIso",xpre.getString("phoneCountry",null));//设置国家
                hookMethod(build,"getString",null);//设置手机型号
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                hookMethod(cc,"getMacAddress",xpre.getString("macWifi","00:00:00:00:00:00"));
                hookMethod(cc,"getSSID",xpre.getString("ssid","当前无WiFi名称"));
                hookMethod(cc,"getBSSID",xpre.getString("bssid","00:00:00:00:00:00"));
            }catch (Exception e){
                e.printStackTrace();
            }

//            try {
//                hookMethod(cz,"getLatitude",xpre.getString("locationLa",0.00000+""));//设置经度
//                hookMethod(cz,"getLongLatitude",xpre.getString("locationLong",0.000000+""));//设置纬度
//            }catch (Exception e){
//            e.printStackTrace(;
//            }

    }
    boolean isModel= false;//查找到参数为修改手机型号
    boolean isbrand = false;//查找到参数为手机品牌
    private void hookMethod(final Class<?> clz, String methodName,final String result) {
        XposedHelpers.findAndHookMethod(clz,methodName,new Object[]{
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //如果等于ro.product.model，说明需要修改的是型号
                            if(((String)param.thisObject).equals("ro.product.model")){
                                isModel = true;
                                return;
                            }
                        //如果等于ro.product.brand，说明需要修改的是品牌
                        if(((String)param.thisObject).equals("ro.product.brand")){
                                isbrand = true;
                                return;
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if(isModel==true){
                            param.setResult(result);
                            return;
                        }
                        if(isbrand==true){
                            param.setResult(result);
                            return;
                        }
                        if(null!=result){
                            param.setResult(result);
                            return;
                        }
                    }
                }
        });

    }


}
