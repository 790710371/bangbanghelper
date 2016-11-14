package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
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
import com.mero.wyt_register.MyApplication;
import com.mero.wyt_register.R;
import com.mero.wyt_register.db.DeviceModelDao;
import com.mero.wyt_register.utils.DeviceUtils;
import com.mero.wyt_register.utils.MapUtils;
import com.mero.wyt_register.utils.NetUtils;
import com.mero.wyt_register.utils.RegexUtils;
import com.mero.wyt_register.widget.CustomTitleBar;
import com.mero.wyt_register.widget.RoundButton;

import java.util.Random;

public class DeviceInfoAty extends Activity implements View.OnClickListener {
    private static final String TAG = "DeviceInfoAty";
    private CustomTitleBar customTitleBar;
    private RoundButton randomButton;
    private EditText edt_sim_xulie_num;//序列号
    private EditText edt_IMEI;//IMEI
    private EditText edt_IMSI;//IMSI
    private EditText edt_sdk_int;//SDK
    private EditText edt_release;//安卓系统版本
    private EditText edt_manufacturer;//制造商
    private EditText edt_hardware;//硬件
    private EditText edt_ip_address;//IP地址
    private EditText edt_phone_country;//手机卡国家
    private EditText edt_phone_num;//手机号
    private EditText edt_yunyingshang;//provider
    private EditText edt_device_type;//手机型号
    private EditText edt_mac;//手机mac地址
    private EditText edt_ssid;//手机WiFi名称，即ssid
    private EditText edt_bssid;//手机的路由Mac
    private static EditText edt_show_location;//地理位置
    private RoundButton btn_save;//随机生成
    private static LocationManager lm = null;
    private static String providerLocation = null;
    private String s;//定位字符串

    private String fenbianlv;//分辨率
    private String android_id ;//IMEI
    private String xuliehao;//序列号
    private String phoneNum;//手机号
    private String imsi;//imsi
    private String model;//随机型号
    private String brand;//随机品牌
    private String manufacturer;//制造商
    private int sdk ; //sdk版本
    private String release;//系统版本
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
        randomButton = (RoundButton) findViewById(R.id.device_info_random_btn);
        randomButton.setOnClickListener(this);
        //初始化手机数据
        btn_save = (RoundButton) findViewById(R.id.btn_device_info_save);
        btn_save.setOnClickListener(this);
        edt_IMEI = (EditText) findViewById(R.id.edt_IMEI);
        edt_IMSI = (EditText) findViewById(R.id.edt_IMSI);
        edt_sdk_int = (EditText) findViewById(R.id.edt_SDK_INT);
        edt_release = (EditText) findViewById(R.id.edt_release);
        edt_hardware = (EditText) findViewById(R.id.edt_hardware);
        edt_phone_country = (EditText) findViewById(R.id.edt_phone_country);
        edt_phone_num = (EditText) findViewById(R.id.edt_phone_number);
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
        edt_manufacturer = (EditText) findViewById(R.id.edt_manufacturer);
        edt_device_type = (EditText) findViewById(R.id.edt_device_type);
        edt_device_type.setText(Build.MODEL);
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
        edt_release.setText(Build.VERSION.RELEASE);//安卓系统版本
        edt_hardware.setText(Build.HARDWARE);//硬件
        edt_manufacturer.setText(Build.MANUFACTURER);
        edt_phone_num.setText(DeviceUtils.getPhoneNum(this));//设置手机号
        edt_phone_country.setText(DeviceUtils.getCountryZipCode(this));//设置国家
        Log.e(TAG,DeviceUtils.getProviderInfo(this));
        edt_yunyingshang.setText(DeviceUtils.getProviderInfo(this));//设置运营商
        edt_ip_address.setText(NetUtils.getPhoneIp());//设置IP地址
        edt_device_type.setText(Build.BRAND +"\t"+ Build.MODEL);//手机类型
        Log.e("TAG",Build.BRAND+Build.MODEL);
        edt_mac.setText(NetUtils.getMacAddress(this));//设置mac地址
        edt_ssid.setText(NetUtils.getSSID(this));//设置ssid
        edt_bssid.setText(NetUtils.getBssid(this));//设置bssid
        edt_sdk_int.setText(String.valueOf(Build.VERSION.SDK_INT));//设置SDK
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //随机生成按钮
            case R.id.device_info_random_btn:
                android_id = DeviceInfoGetRandom.getIMEI();//得到imei
                xuliehao = DeviceInfoGetRandom.getRandomSimNumber();//得到sim卡序列号
                imsi = DeviceInfoGetRandom.getIMSI();//得到IMSI
                sdk = DeviceInfoGetRandom.getSDK_INT_version();//得到SDK版本号
                Log.e("TAG","原来的SDK"+Build.VERSION.SDK_INT);
                release = DeviceInfoGetRandom.getRandomRelease();//随机系统版本
                phoneNum = DeviceInfoGetRandom.getPhoneNum();//随机手机号
                Object[] obj  = getQueryDeviceParam(49);//查询数据库得到随机品牌
                model =(String)obj[1];//得到手机型号
                brand = (String) obj[2];//得到手机品牌
                manufacturer = (String) obj[3];//得到制造商
                mac = DeviceInfoGetRandom.getMacAddrWithFormat(":");//得到mac
                ssid = DeviceInfoGetRandom.genRandomSsid(10);//得到WiFi名称
                bssid = DeviceInfoGetRandom.getMacAddrWithFormat(":");//路由器mac地址
                //设置到输入框上去
                edt_IMEI.setText(android_id);
                edt_sim_xulie_num.setText(xuliehao);
                edt_IMSI.setText(imsi);
                edt_sdk_int.setText(String.valueOf(sdk));
                edt_release.setText(release);
                edt_phone_num.setText(phoneNum);
                edt_mac.setText(mac);
                edt_ssid.setText(ssid);
                edt_bssid.setText(bssid);
                edt_device_type.setText(brand+"\t"+model);
                edt_manufacturer.setText(manufacturer);
                Log.e("TAG","fenbianlv:"+fenbianlv+"\t"+"android_id:"+android_id+"\t"+"xuliehao:"+xuliehao+"\t"+"imsi:"+imsi+"\t"+"mac:"+mac
                +"\n"+"ssid:"+ssid+"\t"+"location_la:"+"\t"+"\t"+"mac:"+mac+"\t"+"ssid:"+ssid+"\t"+"bssid"+bssid+
                        "brand:"+brand+"model:"+model+"\n"+"manufacturer:"+manufacturer+"sdk:"+sdk+"provider"+DeviceUtils.getProviderInfo(MyApplication.getMyApplication()));
                break;
            case R.id.btn_device_info_save:
                Toast.makeText(DeviceInfoAty.this, "正在保存", Toast.LENGTH_SHORT).show();
                //修改分辨率
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.ID, Context.MODE_WORLD_READABLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("simSerialNumber", edt_sim_xulie_num.getText().toString());//sim序列号
                    editor.putString("imei", edt_IMEI.getText().toString());//IMEI
                    editor.putString("imsi", edt_IMSI.getText().toString());//IMSI
                    editor.putInt("sdk_int",Integer.parseInt(edt_sdk_int.getText().toString()));//sdk版本
                    editor.putString("release",release);//系统版本
                    editor.putString("phoneNum",edt_phone_num.getText().toString());//手机号
                    editor.putString("phoneCountry", edt_phone_country.getText().toString());//国家编号
                    editor.putString("macWifi",edt_mac.getText().toString());//手机的mac地址
                    editor.putString("ssid",edt_ssid.getText().toString());//WiFi名称
                    editor.putString("bssid",edt_bssid.getText().toString());//路由mac地址
                    editor.putString("model",model);//手机型号
                    editor.putString("brand",brand);//手机品牌
                    editor.putString("manufacturer",edt_manufacturer.getText().toString());//手机制造商
                    Log.e("TAG","model:"+model+"brand:"+brand+"manufacturer"+manufacturer);
                    editor.apply();
                    //关闭App并且重启
                    Log.e("DeviceInfoAty", Process.myPid() + "");
                    sendBroadcast(new Intent("restart.app"));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Process.killProcess(Process.myPid());
                        }
                    },0);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceInfoAty.this, "写入失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                break;
        }
    }

    //得到随机手机参数
    public Object[] getQueryDeviceParam(int i){
        int id = new Random().nextInt(i)+1;
        DeviceModelDao dao = new DeviceModelDao(this);
        Object[] obj = dao.queryDeviceInfo(id);
        return obj;
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
