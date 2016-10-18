package com.mero.wyt_register.xposed;

import android.telephony.TelephonyManager;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.utils.DeviceUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.view.View.X;

/**
 * Created by chenlei on 2016/10/15.
 */

public class XposedHookModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //过滤包
//        if(!lpparam.equals("android.telephony")){
//            return;
//        }
        XSharedPreferences xSharedPreferences = new XSharedPreferences(this.getClass().getPackage().getName(), Config.ID);
        String result = xSharedPreferences.getString("imei",null);
        //获取IMEI
        hookMethod("android.telephony.TelephonyManager","getDeviceId",lpparam,result);



    }

    public void hookMethod(String className,String methodHook,XC_LoadPackage.LoadPackageParam lpparam,final String result){
        final Class<?> myClass = XposedHelpers.findClass(className,lpparam.classLoader);
        XposedBridge.hookAllMethods(myClass, methodHook, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log("更新前");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(result);
            }
        });
    }

}
