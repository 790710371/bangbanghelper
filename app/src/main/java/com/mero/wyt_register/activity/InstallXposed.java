package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.AppUtils;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static com.stericson.RootTools.RootTools.isRootAvailable;

public class InstallXposed extends Activity implements View.OnClickListener{
    private Button btn_isRoot;
    private Button btn_install_xposed;
    private Button btn_install_module;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        sharedPreferences =getSharedPreferences(Config.ID,Context.MODE_PRIVATE);
        setContentView(R.layout.xposed_install);
        initView();
    }
    private final  Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           Bundle bundle = msg.getData();
            String s = bundle.getString("isInstalled");
            btn_install_xposed.setText("已安装");
        }
    };
    private void initView() {
        btn_isRoot= (Button) findViewById(R.id.btn_root);
        btn_install_module= (Button) findViewById(R.id.btn_xposed_install_module);
        btn_install_xposed= (Button) findViewById(R.id.btn_xposed_click_install);
        btn_isRoot.setOnClickListener(this);
        btn_install_module.setOnClickListener(this);
        btn_install_xposed.setOnClickListener(this);
        boolean isRoot = RootTools.isRootAvailable();
        if(isRoot==true){
            btn_isRoot.setText("已root");
        }else{
            btn_isRoot.setText("未root");
        }
        boolean isInstalled = sharedPreferences.getBoolean(Config.IS_INSTALL_XPOSED,false);
        String tx = btn_install_xposed.getText().toString();
        if(isInstalled==false){
            btn_install_xposed.setText("安装框架");
            return;
        }else if(isInstalled==true){
           btn_install_xposed.setText("已安装");
        }
    }

    private void checkPackageInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> listInfo = pm.getInstalledPackages(0);
        for(PackageInfo packageInfo:listInfo) {
            Log.e(TAG, packageInfo.packageName);
            if (packageInfo.packageName.equals(getResources().getString(R.string.xposed_package_name))) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.IS_INSTALL_XPOSED, true);
                editor.apply();
                //更新ui，将按钮马上安装变成已安装
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("isInstalled", "已安装");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }.start();
            }
     }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_root:
                if(btn_isRoot.getText().toString().equals("未root")){
                    Toast.makeText(InstallXposed.this,"请root",Toast.LENGTH_SHORT).show();
                }else {
                    btn_isRoot.setEnabled(false);
                }
                break;
            case R.id.btn_xposed_click_install:
                String s = btn_install_xposed.getText().toString();
                if((!TextUtils.isEmpty(s))&&s.equals("安装框架")){
                    //------------------------------------------
                    //安装APK失败，这地方需要修改
                    // created by chenlei 2016/10/17/ 9:47
                    //------------------------------------------
                    AppUtils.installApk(InstallXposed.this,new File("\\assets\\XposedInstaller.apk"));

                    //检测是否安装成功
                    checkPackageInstalled(this);
                }
                break;
            case R.id.btn_xposed_install_module:
                break;
        }
    }
}
