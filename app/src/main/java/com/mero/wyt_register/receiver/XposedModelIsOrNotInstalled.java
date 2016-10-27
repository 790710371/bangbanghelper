package com.mero.wyt_register.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.MyApplication;

/**
 * Created by chenlei on 2016/10/26.
 */

public class XposedModelIsOrNotInstalled extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("xposed.isInstalledOrNot")){
            Log.e("TAG","入口函数广播已接收到");
            SharedPreferences sharedPreferences = MyApplication.getMyApplication().getSharedPreferences(Config.ID,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isModelInstalledOrNot","已安装");
            editor.commit();
        }
    }
}
