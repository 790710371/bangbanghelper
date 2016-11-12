package com.mero.wyt_register.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by chenlei on 2016/10/21.
 */

public class NetUtils {
    public static final String TAG = "NetUtils";

    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "127.0.0.1";
    }

    public static  boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //得到MAC地址
    public static String getMacAddress(Context context){
        String macAddress  = "";
        if(context !=null){
            try{
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wm.getConnectionInfo();
                macAddress =  wifiInfo.getMacAddress();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return macAddress;
    }
    //得到SSID
    public static String getSSID(Context context){
        String ssid = "";
        if(context!=null){
            try{
               WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wm.getConnectionInfo();
               ssid =  info.getSSID();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ssid;
    }
    //得到BSSID地址
    public static  String  getBssid(Context context){
        String bssid = "";
        if(context!=null){
            try{
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo =  wm.getConnectionInfo();
                bssid = wifiInfo.getBSSID();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bssid;
    }
}
