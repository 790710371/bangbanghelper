package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.DeviceUtils;
import com.mero.wyt_register.utils.NetUtils;
import com.mero.wyt_register.widget.CustomTitleBar;

import org.w3c.dom.Text;

import static android.R.id.edit;


public class DeviceInfoAty extends Activity implements View.OnClickListener {
    private static final String TAG = "DeviceInfoAty";
    private CustomTitleBar customTitleBar;
    private EditText edt_sim_xulie_num;//序列号
    private EditText edt_IMEI;//IMEI
    private EditText edt_IMSI;//IMSI
    private EditText edt_phone;//手机号码
    private EditText edt_ip_address;//IP地址
    private EditText edt_phone_country;//手机卡国家
    private Button btn_save, btn_get_random;//随机生成
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
        setContentView(R.layout.main_device_info);
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        customTitleBar = (CustomTitleBar) findViewById(R.id.title_bar_device_info);
        customTitleBar.onClick(new CustomTitleBar.TitleBarImageListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(DeviceInfoAty.this, MainActivity.class));
                finish();
            }
        });

        //初始化手机数据
        btn_save = (Button) findViewById(R.id.btn_device_info_save);
        btn_get_random = (Button) findViewById(R.id.btn_device_info_get_random);
        btn_save.setOnClickListener(this);
        btn_get_random.setOnClickListener(this);
        edt_IMEI = (EditText) findViewById(R.id.edt_IMEI);
        edt_IMSI = (EditText) findViewById(R.id.edt_IMSI);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_phone_country = (EditText) findViewById(R.id.edt_phone_country);
        edt_sim_xulie_num = (EditText) findViewById(R.id.edt_sim_xulie_num);
        edt_ip_address = (EditText) findViewById(R.id.edt_ip_address);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edt_sim_xulie_num.setText(DeviceUtils.getSimNumber(this));//设置手机卡序列号
        edt_IMSI.setText(DeviceUtils.getIMSI(this));//设置imsi
        edt_IMEI.setText(DeviceUtils.getIMEI(this));//设置IMEI
        edt_phone.setText(DeviceUtils.getPhoneNum(this));//设置手机号
        edt_phone_country.setText(DeviceUtils.getCountryZipCode(this));//设置国家
        edt_ip_address.setText(NetUtils.getLocalIpAddress());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_device_info_get_random:
//                DeviceUtils.getIMEI(DeviceInfoAty.this);
                break;
            case R.id.btn_device_info_save:
                Toast.makeText(DeviceInfoAty.this, "正在保存", Toast.LENGTH_SHORT).show();
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.ID, Context.MODE_WORLD_READABLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("simSerialNumber", edt_sim_xulie_num.getText().toString());
                    editor.putString("imei",edt_IMEI.getText().toString());
                    editor.putString("imsi",edt_IMSI.getText().toString());
                    editor.putString("phoneNumber",edt_phone.getText().toString());
                    editor.putString("phoneCountry",edt_phone_country.getText().toString());
                    editor.apply();
                    //关闭App并且重启
                    Log.e("DeviceInfoAty",Process.myPid()+"");
                    sendBroadcast(new Intent("restart.app"));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Process.killProcess(Process.myPid());
                        }
                    },3000);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceInfoAty.this, "写入失败"+e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DeviceInfoAty Page") // TODO: Define a title for the content shown.
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
        super
        .onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
