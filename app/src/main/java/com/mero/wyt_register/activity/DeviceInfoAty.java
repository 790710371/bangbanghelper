package com.mero.wyt_register.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.DeviceUtils;
import com.mero.wyt_register.widget.CustomTitleBar;

import java.util.logging.Logger;

import static android.content.ContentValues.TAG;
import static com.mero.wyt_register.R.id.edt_IMEI;
import static com.mero.wyt_register.R.id.edt_phone_country;
import static com.mero.wyt_register.R.id.edt_sim_xulie_num;

public class DeviceInfoAty extends Activity implements View.OnClickListener{
    private static final String TAG = "DeviceInfoAty";
    private CustomTitleBar customTitleBar;
    private EditText edt_sim_xulie_num;//序列号
    private EditText edt_IMEI;//IMEI
    private EditText edt_IMSI;//IMSI
    private EditText edt_phone;//手机号码
    private EditText edt_phone_xulie_num;//手机卡序列号
    private EditText edt_phone_country;//手机卡国家
    private EditText edt_yunyingshang_code;//运营商代码
    private EditText edt_yunyingshang;//运营商
    private Button btn_save,btn_get_random;
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
    }
    private String simNum;//Sim卡序列号
    private String IMEI;//IMEI
    private String IMSI;//IMSI
    private String phoneNum;//手机号码
    private String countryCode;//国家代码
    private String providerCode;//运营商代码
    private String providerName;//运营商名字
    private void initView() {
        customTitleBar = (CustomTitleBar) findViewById(R.id.title_bar_device_info);
        customTitleBar.onClick(new CustomTitleBar.TitleBarImageListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(DeviceInfoAty.this, MainActivity.class));
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
        edt_yunyingshang = (EditText) findViewById(R.id.edt_yunyingshang);
        edt_yunyingshang_code = (EditText) findViewById(R.id.edt_yuunyingshang_code);
        simNum = DeviceUtils.getSimNumber(this);//获取手机卡序列号
        IMEI = DeviceUtils.getIMEI(this);//获取IMEI
        IMSI = DeviceUtils.getIMSI(this);//获取IMSI
        phoneNum = DeviceUtils.getPhoneNum(this);//获取手机号
        countryCode = DeviceUtils.getCountryZipCode(this);//获取国家代码
        providerCode = DeviceUtils.getSubScriberId(this);//获取运营商代码
        providerName = DeviceUtils.getProviderInfo(this);
        edt_sim_xulie_num.setText(simNum);//设置手机卡序列号
        edt_IMEI.setText(IMEI);//设置手机IMEI
        edt_IMSI.setText(IMSI);//设置手机IMSI
        edt_phone.setText(phoneNum);//设置手机号
        edt_phone_country.setText(countryCode);//设置国家代码
        edt_yunyingshang_code.setText(providerCode);//设置运营商代码
        edt_yunyingshang.setText(providerName);//设置运营商


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_device_info_get_random:
                Toast.makeText(DeviceInfoAty.this,DeviceUtils.getIMEI(this),Toast.LENGTH_SHORT).show();
                break;
            case  R.id.btn_device_info_save:
                Toast.makeText(DeviceInfoAty.this,"正在保存",Toast.LENGTH_SHORT).show();
                try {
                    SharedPreferences sharedPreferences =getSharedPreferences(Config.ID,Context.MODE_WORLD_READABLE);
                    SharedPreferences.Editor editor =  sharedPreferences.edit();
                    editor.putString("imei","dsaaaaaaaaaaaaaaaaa");
                    editor.apply();
                    String s = DeviceUtils.getIMEI(this);
                    Toast.makeText(DeviceInfoAty.this,s,Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(DeviceInfoAty.this,"写入失败",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
