package com.mero.wyt_register.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.AppUtils;
import com.mero.wyt_register.widget.CustomTitleBar;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class InstallXposedAty extends Activity implements View.OnClickListener {
    private static final String TAG = "InstallXposedAty";
    private CustomTitleBar title_bar;
    private Button btn_isRoot;
    private Button btn_install_xposed;
    private Button btn_install_module;
    private Button btn_fresh;
    private SharedPreferences sharedPreferences;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        sharedPreferences = getSharedPreferences(Config.ID, Context.MODE_PRIVATE);
        setContentView(R.layout.xposed_install);
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String s = bundle.getString("isInstalled");
            btn_install_xposed.setText(s);
        }
    };

    private void initView() {
        title_bar= (CustomTitleBar) findViewById(R.id.title_bar_xposed_install);
        title_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InstallXposedAty.this, MainActivity.class));
                finish();
            }
        });
        btn_isRoot = (Button) findViewById(R.id.btn_root);
        btn_install_module = (Button) findViewById(R.id.btn_xposed_install_module);
        btn_install_xposed = (Button) findViewById(R.id.btn_xposed_click_install);
        btn_fresh = (Button) findViewById(R.id.btn_fresh);
        btn_isRoot.setOnClickListener(this);
        btn_install_module.setOnClickListener(this);
        btn_install_xposed.setOnClickListener(this);
        btn_fresh.setOnClickListener(this);

//        checkPackageInstalled(InstallXposedAty.this);
        boolean isInstalled = sharedPreferences.getBoolean(Config.IS_INSTALL_XPOSED, false);
        String tx = btn_install_xposed.getText().toString();
    }

    private void checkPackageInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> listInfo = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : listInfo) {
            Log.e(TAG, packageInfo.packageName);
            if (packageInfo.packageName.equals(getResources().getString(R.string.xposed_package_name))) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.IS_INSTALL_XPOSED, true);
                editor.commit();
                //更新ui，将按钮马上安装变成已安装
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("isInstalled", "已安装");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        Log.e(TAG,"正在提交UI更新信息");
                    }
                }.start();
                return;
            }
        }
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("isInstalled", "安装框架");
                msg.setData(bundle);
                handler.sendMessage(msg);
                Log.e(TAG,"正在提交UI更新信息");
            }
        }.start();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Config.IS_INSTALL_XPOSED, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isRoot =RootTools.isRootAvailable();
        if (isRoot == true) {
            btn_isRoot.setText("已root");
            btn_isRoot.setEnabled(false);
        } else {
            btn_isRoot.setText("未root");
        }
        checkPackageInstalled(this);
    }

    private static final String path = Environment.getExternalStorageDirectory().toString();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_root:
                if (btn_isRoot.getText().toString().equals("未root")) {
                    Toast.makeText(InstallXposedAty.this, "请root", Toast.LENGTH_SHORT).show();
                } else {
                    btn_isRoot.setEnabled(false);
                }
                break;
            case R.id.btn_xposed_click_install:
                String s = btn_install_xposed.getText().toString();
                if ((!TextUtils.isEmpty(s)) && s.equals("安装框架")) {
                    //------------------------------------------
                    // created by chenlei 2016/10/17/ 9:47
                    //------------------------------------------
                        copyInstallPackageToSd(getApplicationContext(), new SuccessCallback() {
                            @Override
                            public void success() {
                                AppUtils.installApk(InstallXposedAty.this,new File(path+"/zy/XposedInstaller.apk"));
                            }
                        }, new FailedCallback() {
                            @Override
                            public void fail() {
                            }
                        });


            }else if((!TextUtils.isEmpty(s)) && s.equals("已安装")){
                    btn_install_xposed.setEnabled(false);
                }
                break;
            case R.id.btn_xposed_install_module:
                break;
            case R.id.btn_fresh:
                Toast.makeText(InstallXposedAty.this,"正在点击刷新",Toast.LENGTH_SHORT).show();
                checkPackageInstalled(getApplicationContext());
                Toast.makeText(InstallXposedAty.this,sharedPreferences.getBoolean(Config.IS_INSTALL_XPOSED,false)+"",Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void copyInstallPackageToSd(Context context, final SuccessCallback successCallback, final FailedCallback failedCallback){
        //检测SD卡是否插入
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    OutputStream out = null;
                    InputStream in = null;
                    File file = new File(path+"/zy");
                    File myFile = new File(file,"XposedInstaller.apk");
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    if(!myFile.exists()){
                        try {
                            myFile.createNewFile();
                            out = new FileOutputStream(myFile);
                            in = getApplicationContext().getAssets().open("XposedInstaller.apk");
                            byte[] b =new byte[1024];
                            int len = 0;
                            while((len =in.read(b))!=-1){
                                out.write(b,0,len);
                            }
                            if(null!=successCallback){
                                successCallback.success();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if(null!=in){
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if(null!=failedCallback){
                                        failedCallback.fail();
                                    }
                                }
                            }
                            if(null!=out){
                                try {
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if(null!=failedCallback){
                                        failedCallback.fail();
                                    }
                                }
                            }

                        }
                    }else{
                        if(null!=successCallback){
                            successCallback.success();
                        }
                    }
                }
            }.start();



        } else{
            // We can only read the media
            final AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("温馨提示").setMessage("sd卡不可读写").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();;
        }
    }

    public interface SuccessCallback{
        void success();
    }
    public interface FailedCallback{
        void fail();
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("InstallXposedAty Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(InstallXposedAty.this,MainActivity.class));
            finish();
        }
        return false;
    }
}
