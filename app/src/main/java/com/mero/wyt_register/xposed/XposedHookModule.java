package com.mero.wyt_register.xposed;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.MyApplication;
import com.mero.wyt_register.utils.AppUtils;
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
import static android.view.View.inflate;

/**
 * Created by chenlei on 2016/10/15.
 */

public class XposedHookModule implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        MyApplication.getMyApplication().setLpparam(lpparam);
        final Class<?> cl = XposedHelpers.findClass("android.telephony.TelephonyManager",lpparam.classLoader);
        XSharedPreferences xpre = new XSharedPreferences("com.mero.wyt_register",Config.ID);
        hookMethod(cl,"getSimSerialNumber",xpre.getString("simSerialNumber",null));
    }

    private void hookMethod(Class<?> clz,String methodName,final String result) {
        XposedHelpers.findAndHookMethod(clz,methodName,new Object[]{
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        param.setResult(result);
                    }
                }


        });
    }
}
