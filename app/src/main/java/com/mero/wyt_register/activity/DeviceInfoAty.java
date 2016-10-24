package com.mero.wyt_register.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.mero.wyt_register.utils.MapUtils;
import com.mero.wyt_register.utils.NetUtils;
import com.mero.wyt_register.utils.RegexUtils;
import com.mero.wyt_register.widget.CustomTitleBar;

import java.io.IOException;
import java.security.Provider;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.targetSdkVersion;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class DeviceInfoAty extends Activity implements View.OnClickListener {
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
    private static EditText edt_show_location;//地理位置
    private Button btn_save, btn_get_random;//随机生成
    private static LocationManager lm = null;
    private static String providerLocation = null;
    private LinearLayout layout_cpu;
    private String s;//定位字符串

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
                if (progress == 0) {
                    c.setProgress(50);
                } else if (progress == 100) {
                    c.setProgress(0);
                } else if (progress == 50) {
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
                if (hasFocus == false) {
                    //检查是否为三大运营商
                    String c = edt_yunyingshang.getText().toString();
                    if (!c.contains("中国")) {
                        Toast.makeText(DeviceInfoAty.this, "请输入合法运营商", Toast.LENGTH_SHORT).show();
                        edt_yunyingshang.setText("中国移动");
                    }
                }
            }
        });
        edt_ip_address = (EditText) findViewById(R.id.edt_ip_address);
        edt_ip_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    //进行正则匹配
                    boolean flag = RegexUtils.checkIpAddress(edt_ip_address.getText().toString());
                    if (flag == false) {
                        //不是IP地址,提示IP地址不对
                        Toast.makeText(DeviceInfoAty.this, "ip格式不符合", Toast.LENGTH_SHORT).show();
                        edt_ip_address.setText("127.0.0.1");
                    }
                }
            }
        });
        edt_device_type = (EditText) findViewById(R.id.edt_device_type);
        edt_device_type.setText(Build.MODEL);
        edt_show_location = (EditText) findViewById(R.id.edt_location);
        Log.e("TAG","正在尝试获取地理位置");
        getLocation(this);
    }

    //得到地理位置方法
    private synchronized void getLocation(Context context){
        Log.e("TAG","正在执行获取getLocation方法");
        //声明AMapLocationClient类对象
        AMapLocationClient mLocationClient = null;
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //设置为高精度
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置默认返回地址
        locationClientOption.setNeedAddress(true);
        //设置是否只定位一次
        locationClientOption.setOnceLocation(true);
        if(locationClientOption.isOnceLocation()){
            locationClientOption.setOnceLocationLatest(true);
        }
        //设置是否强制刷新WiFi
        locationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationClientOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationClientOption.setInterval(2000);
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //为定位进行设置
        mLocationClient.setLocationOption(locationClientOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();

    }
    AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    s =   MapUtils.getLocationStr(amapLocation);
                    edt_show_location.setText(s);
                }
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        edt_sim_xulie_num.setText(DeviceUtils.getSimNumber(this));//设置手机卡序列号
        edt_IMSI.setText(DeviceUtils.getIMSI(this));//设置imsi
        edt_IMEI.setText(DeviceUtils.getIMEI(this));//设置IMEI
        edt_phone_country.setText(DeviceUtils.getCountryZipCode(this));//设置国家
        edt_yunyingshang.setText(DeviceUtils.getProviderInfo(this));//设置运营商
        edt_ip_address.setText(NetUtils.getPhoneIp());//设置IP地址
        edt_device_type.setText(Build.MANUFACTURER + Build.MODEL);//手机类型
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_info_progress_btn:
//                DeviceUtils.getIMEI(DeviceInfoAty.this);
                break;
            case R.id.device_info_cpu_item:
                startActivity(new Intent(DeviceInfoAty.this, ProcessorInfoAty.class));
                finish();
                break;
            case R.id.btn_device_info_save:
                Toast.makeText(DeviceInfoAty.this, "正在保存", Toast.LENGTH_SHORT).show();
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.ID, Context.MODE_WORLD_READABLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("simSerialNumber", edt_sim_xulie_num.getText().toString());
                    editor.putString("imei", edt_IMEI.getText().toString());
                    editor.putString("imsi", edt_IMSI.getText().toString());
                    editor.putString("phoneCountry", edt_phone_country.getText().toString());
                    editor.apply();
                    //关闭App并且重启
                    Log.e("DeviceInfoAty", Process.myPid() + "");
                    sendBroadcast(new Intent("restart.app"));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Process.killProcess(Process.myPid());
                        }
                    }, 3000);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceInfoAty.this, "写入失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
        }
    }



    //创建位置监听器
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
