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
    public void  setDefaultPreferencesParams(){
        SharedPreferences sharedPreferences =getSharedPreferences(Config.ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*
        * 在这里配置初始配置
        * */
        editor.putBoolean(Config.IS_INSTALL_XPOSED,false);//是否已经安装xposed
        editor.putBoolean("isAutoChanged",false);
        editor.putBoolean("isOpenNotify",false);
        editor.commit();
    }
}
