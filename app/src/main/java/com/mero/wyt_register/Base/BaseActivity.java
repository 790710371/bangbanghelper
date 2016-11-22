package com.mero.wyt_register.Base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by chenlei on 2016/11/13.
 */

public abstract class BaseActivity extends Activity {
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(setStatusBarColor());
        setContentView(getLayoutResourceId());//设置布局
        initView();//初始化布局控件
        initData();//初始化数据
        builder = new AlertDialog.Builder(this);
    }
    //初始化布局控件
    public abstract void initView();
    //初始化数据
    public abstract  void initData();
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    //获取布局的id
    public abstract int getLayoutResourceId();
    //获取对话框的icon的id
    public abstract  int getDialogIcon();
    //设置Dialog对话框
    public void showMessageDialog(String msg, int iconId, DialogInterface.OnClickListener onClickListener){
        builder.setIcon(getDialogIcon());
        builder.setMessage(msg);
        builder.setPositiveButton("确定",onClickListener);
        builder.setNegativeButton("取消",onClickListener);
        builder.create().show();
    }
    //跳转到指定的activity
    public  void showActivity(Context context,Class<? extends Context> contextClass){
        Intent intent  = new Intent(context,contextClass);
        startActivity(intent);
    }
    //沉浸式标题栏颜色
    public abstract int setStatusBarColor();

}
