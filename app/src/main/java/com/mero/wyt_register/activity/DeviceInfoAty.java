package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.DeviceUtils;
import com.mero.wyt_register.utils.MapUtils;
import com.mero.wyt_register.utils.NetUtils;
import com.mero.wyt_register.utils.RegexUtils;
import com.mero.wyt_register.widget.CustomTitleBar;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static android.R.id.edit;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class DeviceInfoAty extends Activity implements View.OnClickListener {
    private static final String TAG = "DeviceInfoAty";
    private CustomTitleBar customTitleBar;
    private Button randomButton;
    private EditText edt_random_resolution;//分辨率
    private EditText edt_sim_xulie_num;//序列号
    private EditText edt_IMEI;//IMEI
    private EditText edt_IMSI;//IMSI
    private EditText edt_ip_address;//IP地址
    private EditText edt_phone_country;//手机卡国家
    private EditText edt_yunyingshang;//运营商
    private EditText edt_device_type;//手机型号
//    private EditText edt_location_la;//经度
//    private EditText edt_location_long;//纬度
    private EditText edt_mac;//手机mac地址
    private EditText edt_ssid;//手机WiFi名称，即ssid
    private EditText edt_bssid;//手机的路由Mac
    private static EditText edt_show_location;//地理位置
    private Button btn_save;//随机生成
    private static LocationManager lm = null;
    private static String providerLocation = null;
    private String s;//定位字符串

    private String fenbianlv;//分辨率
    private String android_id ;//IMEI
    private String xuliehao;//序列号
    private String imsi;//imsi
    private String country;//国家
    private String yunyingshang;//运营商
    private String IP;//ip
    private String xinghao;//手机型号
    private String location_la;//经度
    private String location_long;//纬度
    private String mac;//mac地址
    private String ssid;//WiFi名称
    private String bssid;//路由Mac地址


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
        randomButton = (Button) findViewById(R.id.device_info_random_btn);
        randomButton.setOnClickListener(this);
        //初始化手机数据
        btn_save = (Button) findViewById(R.id.btn_device_info_save);
        btn_save.setOnClickListener(this);
        edt_random_resolution = (EditText) findViewById(R.id.edt_resolution);
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
//        edt_location_la = (EditText) findViewById(R.id.edt_location_la);
//        edt_location_long = (EditText) findViewById(R.id.edt_location_long);
        edt_show_location = (EditText) findViewById(R.id.edt_location);
        Log.e("TAG","正在尝试获取地理位置");
        getLocation(this);
        edt_mac = (EditText) findViewById(R.id.edt_mac);
        edt_ssid = (EditText) findViewById(R.id.edt_ssid);
        edt_bssid = (EditText) findViewById(R.id.edt_bssid);

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
        edt_mac.setText(NetUtils.getMacAddress(this));//设置mac地址
        edt_ssid.setText(NetUtils.getSSID(this));//设置ssid
        edt_bssid.setText(NetUtils.getBssid(this));//设置bssid
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //正在点击随机生成按钮
            case R.id.device_info_random_btn:
                //开始生成随机数据
                //随机分辨率
                fenbianlv = DeviceInfoGetRandom.getRandomFenbianlvData();
                //随机android_id
                android_id = DeviceInfoGetRandom.getIMEI();
                //随机序列号
                xuliehao = DeviceInfoGetRandom.getRandomSimNumber();
                //随机IMSI
                imsi = DeviceInfoGetRandom.getIMSI();
                //随机手机型号

                //随机手机品牌

//                //随机经度
//                location_la = DeviceInfoGetRandom.getLaLocation();
//                //随机纬度
//                location_long = DeviceInfoGetRandom.getLongLocation();
                //随机Mac
                mac = DeviceInfoGetRandom.getMacAddrWithFormat(":");
                //随机称号
                ssid = DeviceInfoGetRandom.genRandomSsid(10);
                //路由器mac地址
                bssid = DeviceInfoGetRandom.getMacAddrWithFormat(":");
                //设置到输入框上去
                edt_random_resolution.setText(fenbianlv);
                edt_IMEI.setText(android_id);
                edt_sim_xulie_num.setText(xuliehao);
                edt_IMSI.setText(imsi);
//                edt_location_la.setText(location_la);
//                edt_location_long.setText(location_long);
                edt_mac.setText(mac);
                edt_ssid.setText(ssid);
                edt_bssid.setText(bssid);
                Log.e("TAG","fenbianlv:"+fenbianlv+"\t"+"android_id:"+android_id+"\t"+"xuliehao:"+xuliehao+"\t"+"imsi:"+imsi+"\t"+"mac:"+mac
                +"\t"+"ssid:"+ssid+"\t"+"location_la:"+location_la+"\t"+"location_long:"+location_long+"\t"+"mac:"+mac+"\t"+"ssid:"+ssid+"\t"+"bssid"+bssid);

                break;
            case R.id.btn_device_info_save:
                Toast.makeText(DeviceInfoAty.this, "正在保存", Toast.LENGTH_SHORT).show();
                //修改分辨率
                try {
                    RootTools.sendShell("wm size "+fenbianlv,0);
                    try {
                        SharedPreferences sharedPreferences = getSharedPreferences(Config.ID, Context.MODE_WORLD_READABLE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("simSerialNumber", edt_sim_xulie_num.getText().toString());
                        editor.putString("imei", edt_IMEI.getText().toString());
                        editor.putString("imsi", edt_IMSI.getText().toString());
                        //国家默认不注入
                        editor.putString("phoneCountry", edt_phone_country.getText().toString());
                        //注入经度
                        editor.putString("locationLa",location_la);
                        //注入纬度
                        editor.putString("locationLong",location_long);
                        //注入WiFiMac
                        editor.putString("macWifi",mac);
                        //注入ssid
                        editor.putString("ssid",ssid);
                        //注入bssid
                        editor.putString("bssid",bssid);
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RootToolsException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
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
