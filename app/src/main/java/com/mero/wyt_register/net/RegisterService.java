package com.mero.wyt_register.net;

import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.HttpMethod;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;

import okhttp3.Call;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by chenlei on 2016/11/17.
 */

public class RegisterService {
    /*
    * 上传文件
    * */
    public RegisterService(String url, File file, final ISuccessCallback successCallback, final IFailCallback failCallback){
        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                            if(null!=failCallback){
                                Log.e(TAG,"图片上传失败");
                                failCallback.onFail(e.getMessage());
                            }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject  = new JSONObject(response);
                            int status = jsonObject.getInt(Config.KEY_STATUS);
                            if(status==1){
                                Log.e(TAG,"图片上传成功");
                                if(null!=successCallback){
                                    successCallback.onSuccess(response,id);
                                }
                            }else {
                                if(null!=failCallback){
                                    failCallback.onFail(response);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    //上传数据
    public RegisterService(String url,String wyt_accout,String wyt_pwd){

    }
    public interface ISuccessCallback{
        void onSuccess(String response,int id);
    };
    public interface IFailCallback{
        void onFail(String failMsg);
    }
}
