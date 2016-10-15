package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.widget.CustomTitleBar;
import com.mero.wyt_register.widget.SwitchButton;

public class SettingAty extends Activity{
    private CustomTitleBar customTitleBar;
    private SwitchButton auto_change;
    private SwitchButton open_notify;
    private SharedPreferences sharedPreferences;
    boolean isAutoChanged;
    boolean isOpenNotify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        sharedPreferences = this.getSharedPreferences(Config.ID, Context.MODE_PRIVATE);
        isAutoChanged = sharedPreferences.getBoolean("isAutoChanged",false);
        isOpenNotify =sharedPreferences.getBoolean("isOpenNotify",false);
        setContentView(R.layout.main_setting);
        initView();
        onEvent();
    }

    private void initView() {
        customTitleBar = (CustomTitleBar) findViewById(R.id.title_bar_setting);
        auto_change = (SwitchButton) findViewById(R.id.switch_btn_auto_change);
        open_notify = (SwitchButton) findViewById(R.id.switch_btn_open_notify);
        auto_change.setCheckedImmediately(isAutoChanged);
        open_notify.setCheckedImmediately(isOpenNotify);
    }

    private void onEvent() {
        customTitleBar.onClick(new CustomTitleBar.TitleBarImageListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(SettingAty.this,MainActivity.class));
            }
        });
        auto_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor .putBoolean("isAutoChanged",true);
                    editor.commit();

                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor .putBoolean("isAutoChanged",false);
                    editor.commit();
                }
            }
        });
        open_notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor .putBoolean("isOpenNotify",true);
                    editor.commit();
                    /*开启通知栏*/
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor .putBoolean("isOpenNotify",false);
                    editor.commit();
                }
            }
        });
    }


}
