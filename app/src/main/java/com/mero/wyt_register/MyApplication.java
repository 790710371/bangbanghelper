package com.mero.wyt_register;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chenlei on 2016/10/11.
 */

public class MyApplication extends Application {
    public static MyApplication instance;
    XC_LoadPackage.LoadPackageParam lpparam;

    public XC_LoadPackage.LoadPackageParam getLpparam() {
        return lpparam;
    }

    public void setLpparam(XC_LoadPackage.LoadPackageParam lpparam) {
        this.lpparam = lpparam;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static MyApplication getMyApplication(){
        return instance;
    }
}
