package com.mero.wyt_register.xposed;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.mero.wyt_register.Config;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chenlei on 2016/10/15.
 */

public class XposedHookModule implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            if(!lpparam.packageName.equals("com.mero.wyt_register")){
                return;
            }
            XSharedPreferences xpre = new XSharedPreferences("com.mero.wyt_register",Config.ID);
            final Class<?> thiz = XposedHelpers.findClass("com.mero.wyt_register.MainActivity",lpparam.classLoader);
            final Class<?> cl = XposedHelpers.findClass("android.telephony.TelephonyManager",lpparam.classLoader);
            final Class<?> cz = XposedHelpers.findClass("android.location.LocationManager",lpparam.classLoader);
            final Class<?> cc = XposedHelpers.findClass("android.net.wifi.WifiInfo",lpparam.classLoader);
            final Class<?> build = XposedHelpers.findClass("android.os.Build",lpparam.classLoader);
            final Class<?> wi = XposedHelpers.findClass("android.net.wifi.WifiManager",lpparam.classLoader);
            try{
                hookMethod(thiz,"getResult","已安装");
                hookMethod(cl,"getSimSerialNumber",xpre.getString("simSerialNumber",null));//修改sim序列号
                hookMethod(cl,"getDeviceId",xpre.getString("imei",null));//修改设备IMEI
                hookMethod(cl,"getSubscriberId",xpre.getString("imsi",null));//修改IMSI
                hookMethod(cl,"getSimCountryIso",xpre.getString("phoneCountry",null));//设置国家
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                hookMethod(cc,"getMacAddress",xpre.getString("macWifi","00:00:00:00:00:00"));
                hookMethod(cc,"getSSID",xpre.getString("ssid","当前无WiFi名称"));
                hookMethod(cc,"getBSSID",xpre.getString("bssid","00:00:00:00:00:00"));
            }catch (Exception e){
                e.printStackTrace();
            }
        try{
            /**
             * hook地理位置，排除除了GPS定位的其他定位，基站和WiFi定位方式
             * 有参数的为GPS定位
             */
            hookMethod(cl,"getCellLocation",null);
            hookMethod(cl,"getNeighboringCellInfo",null);
            hookMethod(wi,"getScanResults",null);
            hookMethod(cz,"requestLocationUpdates",null);
        }catch (Exception e){
            e.printStackTrace();
        }
            try{
                /**
                 * hook 手机型号
                 * */
                hookMethod(build,"getString",xpre.getString("model",null));//设置手机型号
                hookMethod(build,"getString",xpre.getString("brand",null));//修改手机品牌
            }catch (Exception e){
                e.printStackTrace();
            }



    }
    //需要修改手机型号
    boolean model = false;
    //需要修改的手机品牌
    boolean brand = false;
    private void hookMethod(final Class<?> clz, final String methodName, final String result) {
        XposedHelpers.findAndHookMethod(clz,methodName,new Object[]{
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if(methodName.equals("requestLocationUpdates)")){
                            if (param.args.length == 4 && (param.args[0] instanceof String)) {
                                //位置监听器,当位置改变时会触发onLocationChanged方法
                                LocationListener ll = (LocationListener)param.args[3];

                                Class<?> clazz = LocationListener.class;
                                Method m = null;
                                for (Method method : clazz.getDeclaredMethods()) {
                                    if (method.getName().equals("onLocationChanged")) {
                                        m = method;
                                        break;
                                    }
                                }

                                try {
                                    if (m != null) {
                                        Object[] args = new Object[1];
                                        Location l = new Location(LocationManager.GPS_PROVIDER);
                                        double la=getLaLocation();
                                        double lo=getLongLocation();
                                        l.setLatitude(la);
                                        l.setLongitude(lo);
                                        args[0] = l;
                                        m.invoke(ll, args);
                                        XposedBridge.log("fake location: " + la + ", " + lo);
                                    }
                                } catch (Exception e) {
                                    XposedBridge.log(e);
                                }
                            }
                            return;
                        }
                        //修改手机型号和品牌
                        if(methodName.equals("getString")){
                            if(((String)param.args[0]).equals("ro.product.model")){
                                model = true;
                                return;
                            }
                            if(((String)param.args[0]).equals("ro.product.brand")){
                                brand = true;
                                return;
                            }
                        }

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if(model==true){
                            param.setResult(result);
                            return;
                        }
                        if(brand==true){
                            param.setResult(result);
                            return;
                        }
                        if(result==null){
                            return;
                        }
                        param.setResult(result);
                    }
                }
        });

    }

    //得到经度
    public static double getLaLocation(){
        //73~135
        Random random =new Random();
        double b =(random.nextDouble()+1)*70;//生成70到140范围内
        if(b>70&&b<80){
            b+=20;//偏移进到90到100范围内
        }
        if(b>135&&b<140){
            b-=20;//同样偏移进去
        }
        DecimalFormat decimal = new DecimalFormat("###.######");
        return  Double.parseDouble(decimal.format(b));
    }
    //得到纬度
    public static double getLongLocation(){
        //3~53
        Random random =new Random();
        double b =(random.nextDouble()+1)*30;//生成70到140范围内
        if(b>53&&b<60){
            b-=10;
        }
        DecimalFormat decimal = new DecimalFormat("###.######");
        return  Double.parseDouble(decimal.format(b));
    }


}
