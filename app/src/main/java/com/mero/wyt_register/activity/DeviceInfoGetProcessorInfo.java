package com.mero.wyt_register.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by chenlei on 2016/10/22.
 */

public class DeviceInfoGetProcessorInfo {
   public static String path  =Environment.getExternalStorageDirectory().toString();
    DeviceInfoGetProcessorInfo(final Context context, final SuccessCallback successCallback, final FailCallback failCallback) throws IOException {
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
                    File myFile = new File(file,"cpuinfo.txt");
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    if(!myFile.exists()){
                        try {
                            myFile.createNewFile();
                            out = new FileOutputStream(myFile);
                            java.lang.Process process =Runtime.getRuntime().exec("cat /proc/cpuinfo");
                            in = process.getInputStream();
                            byte[] b =new byte[1024];
                            int len = 0;
                            while((len =in.read(b))!=-1){
                                out.write(b,0,len);
                            }
                            if(null!=successCallback){
                                successCallback.onSuccess();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if(null!=in){
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if(null!=failCallback){
                                        failCallback.onFail();
                                    }
                                }
                            }
                            if(null!=out){
                                try {
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if(null!=failCallback){
                                        failCallback.onFail();
                                    }
                                }
                            }

                        }
                    }else{
                        if(null!=successCallback){
                            successCallback.onSuccess();
                        }
                    }
                }
            }.start();



        } else{
            // We can only read the media
            final AlertDialog.Builder builder =new AlertDialog.Builder(context);
            builder.setTitle("温馨提示").setMessage("sd卡不可读写").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();;
        }

    }
    public static interface SuccessCallback{
        void onSuccess();
    }
    public static interface FailCallback{
        void onFail();
    }
}
