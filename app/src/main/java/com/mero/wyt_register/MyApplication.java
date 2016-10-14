package com.mero.wyt_register;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenlei on 2016/10/11.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void  setDefaultPreferencesParams(){
        SharedPreferences sharedPreferences =getSharedPreferences(Config.ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*
        * 在这里配置初始配置
        * */
        editor.putBoolean("isAutoChanged",false);
        editor.putBoolean("isOpenNotify",false);
        editor.commit();
    }
}
