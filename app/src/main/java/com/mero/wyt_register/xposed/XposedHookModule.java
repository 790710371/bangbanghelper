package com.mero.wyt_register.xposed;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.MyApplication;
import com.mero.wyt_register.utils.AppUtils;
import com.mero.wyt_register.utils.DeviceUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.View.X;
import static android.view.View.inflate;
import static de.robv.android.xposed.XposedHelpers.getParameterTypes;

/**
 * Created by chenlei on 2016/10/15.
 */

public class XposedHookModule implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            XSharedPreferences xpre = new XSharedPreferences("com.mero.wyt_register",Config.ID);
            final Class<?> cl = XposedHelpers.findClass("android.telephony.TelephonyManager",lpparam.classLoader);
                hookMethod(cl,"getSimSerialNumber",xpre.getString("simSerialNumber",null));//修改sim序列号
                hookMethod(cl,"getDeviceId",xpre.getString("imei",null));//修改设备IMEI
                hookMethod(cl,"getSubscriberId",xpre.getString("imsi",null));//修改IMSI
                hookMethod(cl,"getSimCountryIso",xpre.getString("phoneCountry",null));//设置国家
    }

    private void hookMethod(final Class<?> clz, String methodName,final String result) {
        XposedHelpers.findAndHookMethod(clz,methodName,new Object[]{
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        XposedBridge.log("正在测试beforeHookedMethod");
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
