package com.mero.wyt_register.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.MyApplication;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.AppUtils;
import com.mero.wyt_register.utils.DeviceUtils;
import com.mero.wyt_register.utils.NetUtils;
import com.mero.wyt_register.utils.RegexUtils;
import com.mero.wyt_register.widget.CustomTitleBar;

import java.security.Provider;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public  class DeviceInfoAty extends Activity implements View.OnClickListener {
    private static final String TAG = "DeviceInfoAty";
    private CustomTitleBar customTitleBar;
    private CircularProgressButton circularProgressButton;
    private EditText edt_sim_xulie_num;//序列号
    private EditText edt_IMEI;//IMEI
    private EditText edt_IMSI;//IMSI
    private EditText edt_ip_address;//IP地址
    private EditText edt_phone_country;//手机卡国家
    private EditText edt_yunyingshang;//运营商
    private EditText edt_device_type;//手机型号
    private static  EditText edt_show_location;//地理位置
    private Button btn_save, btn_get_random;//随机生成
    private static LocationManager lm =null;
    private static String providerLocation =null;
    private LinearLayout layout_cpu;
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
        circularProgressButton = (CircularProgressButton) findViewById(R.id.device_info_progress_btn);
        circularProgressButton.setText("随机生成");
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircularProgressButton c = (CircularProgressButton) v;
                c.setIndeterminateProgressMode(true);
                int progress = c.getProgress();
                if(progress==0){
                    c.setProgress(50);
                }else if(progress==100){
                    c.setProgress(0);
                }else if(progress==50){
                    c.setProgress(100);
                }
            }
        });
        layout_cpu = (LinearLayout) findViewById(R.id.device_info_cpu_item);
        layout_cpu.setOnClickListener(this);

        //初始化手机数据
        btn_save = (Button) findViewById(R.id.btn_device_info_save);
        btn_save.setOnClickListener(this);
        edt_IMEI = (EditText) findViewById(R.id.edt_IMEI);
        edt_IMSI = (EditText) findViewById(R.id.edt_IMSI);
        edt_phone_country = (EditText) findViewById(R.id.edt_phone_country);
        edt_sim_xulie_num = (EditText) findViewById(R.id.edt_sim_xulie_num);
        edt_yunyingshang = (EditText) findViewById(R.id.edt_yunyingshang);
        edt_yunyingshang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    //检查是否为三大运营商
                    String  c = edt_yunyingshang.getText().toString();
                    if(!c.contains("中国")){
                     Toast.makeText(DeviceInfoAty.this,"请输入合法运营商",Toast.LENGTH_SHORT).show();
                        edt_yunyingshang.setText("中国移动");
                    }
                }
            }
        });
        edt_ip_address = (EditText) findViewById(R.id.edt_ip_address);
        edt_ip_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    //进行正则匹配
                    boolean flag = RegexUtils.checkIpAddress(edt_ip_address.getText().toString());
                    if(flag==false){
                        //不是IP地址,提示IP地址不对
                        Toast.makeText(DeviceInfoAty.this,"ip格式不符合",Toast.LENGTH_SHORT).show();
                        edt_ip_address.setText("127.0.0.1");
                    }
                }
            }
        });
        edt_device_type = (EditText) findViewById(R.id.edt_device_type);
        edt_device_type.setText(Build.MODEL);
        edt_show_location = (EditText) findViewById(R.id.edt_location);
        StringBuilder sb =new StringBuilder("");
        Location location = getLocation(this);
        Log.e("TAG","location"+location);
        if(location!=null){
            String locationPoint = sb.append("(").append(location.getLatitude()).append(",").append(location.getLongitude()).append(")").toString();
            edt_show_location.setText(locationPoint);
        }else{
            Log.e("TAG","location为空");
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        edt_sim_xulie_num.setText(DeviceUtils.getSimNumber(this));//设置手机卡序列号
        edt_IMSI.setText(DeviceUtils.getIMSI(this));//设置imsi
        edt_IMEI.setText(DeviceUtils.getIMEI(this));//设置IMEI
        edt_phone_country.setText(DeviceUtils.getCountryZipCode(this));//设置国家
        edt_yunyingshang .setText(DeviceUtils.getProviderInfo(this));//设置运营商
        edt_ip_address.setText(NetUtils.getPhoneIp());//设置IP地址
        edt_device_type.setText(Build.MANUFACTURER+Build.MODEL);//手机类型
        StringBuilder sb =new StringBuilder("");
        Location location = getLocation(MyApplication.getMyApplication());
        Log.e("TAG","location"+location);
        if(location!=null){
            String locationPoint = sb.append("(").append(location.getLatitude()).append(",").append(location.getLongitude()).append(")").toString();
            edt_show_location.setText(locationPoint);
        }else{
            Log.e("TAG","location为空");
            return;
        }

    }
    //地理位置监听器
    final static LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            StringBuilder sb =new StringBuilder("");
            location = getLocation(MyApplication.getMyApplication());
            Log.e("TAG","location"+location);
            if(location!=null){
                String locationPoint = sb.append("(").append(location.getLatitude()).append(",").append(location.getLongitude()).append(")").toString();
                edt_show_location.setText(locationPoint);
            }else{
                Log.e("TAG","location为空");
                return;
            }
        }
    };
    //获取地理位置
    public static Location getLocation(Context context) {
        Location location = null;
        //获取位置管理服务
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //判断网络是否连接
        if(NetUtils.isNetworkConnected(context)){
            //网络可用采用网络定位
            providerLocation = LocationManager.NETWORK_PROVIDER;
        }else if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS可用，采用GPS定位
            providerLocation = LocationManager.GPS_PROVIDER;
        }else{
            //都不可用
          Log.e("TAG","providerLocation"+providerLocation);
        }
        Log.e("TAG",NetUtils.isNetworkConnected(context)+""+lm.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if(Build.VERSION.SDK_INT<23){
            Log.e("TAG","SDK版本小于23");
            Log.e("TAG","LocationManager:"+lm);
            Log.e("TAG","providerLocation:"+providerLocation);
            if (lm != null) {
                //模糊定位权限或者精确定位权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //获取Location
                        location = lm.getLastKnownLocation(providerLocation);
                    }
                }else{
                //权限不够，即便开启GPS或者网络也无法定位
                location = null;
                Log.e("TAG","权限不够，即便开启GPS或者网络也无法定位");
            }
            return location;
            }else if(Build.VERSION.SDK_INT>=23){
            Log.e("TAG","SDK版本大于等于23");
            if (lm != null) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //获取Location
                        location = lm.getLastKnownLocation(providerLocation);
                }else{
                    //权限不够，即便开启GPS或者网络也无法定位
                    location = null;
                    Log.e("TAG","权限不够，即便开启GPS或者网络也无法定位");
                }
                return  location;
            }
        }
        return location;
    }
    //权限检查并移除监听器
    public static void checkSelfPermissionAndRemoveListener(Context context){
        if(Build.VERSION.SDK_INT<23){
            Log.e("TAG","SDK版本小于23");
            if (lm != null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lm.removeUpdates(locationListener);
                }
            }
        }else if(Build.VERSION.SDK_INT>=23){
            Log.e("TAG","SDK版本大于等于23");
            if (lm != null) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lm.removeUpdates(locationListener);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkSelfPermissionAndRemoveListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_info_progress_btn:
//                DeviceUtils.getIMEI(DeviceInfoAty.this);
                break;
            case R.id.device_info_cpu_item:
                startActivity(new Intent(DeviceInfoAty.this,ProcessorInfoAty.class));
                finish();
                break;
            case R.id.btn_device_info_save:
                Toast.makeText(DeviceInfoAty.this, "正在保存", Toast.LENGTH_SHORT).show();
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.ID, Context.MODE_WORLD_READABLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("simSerialNumber", edt_sim_xulie_num.getText().toString());
                    editor.putString("imei",edt_IMEI.getText().toString());
                    editor.putString("imsi",edt_IMSI.getText().toString());
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
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
