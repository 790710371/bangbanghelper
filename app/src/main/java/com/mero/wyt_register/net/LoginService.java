package com.mero.wyt_register.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.HttpMethod;
import com.mero.wyt_register.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

import static android.app.ProgressDialog.show;
import static com.google.android.gms.internal.zzs.TAG;
import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * Creaed by chenlei on 2016/11/15.
 */

public class LoginService {
    private static  final  String TAG = "loginService";
    private static final int DISMISS_TIME = 2000;
    //使用token登录
    public  LoginService(final Context context, String url, String token, final ISuccessCallback successCallback, final IFailCallback failCallback){
        post()
                .url(url)
                .addParams(Config.KEY_TOKEN,token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(null!=failCallback){
                            failCallback.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            int status = object.getInt(Config.KEY_STATUS);
                            if(status==1){
                                //登录成功
                                Log.e(TAG,context.getString(R.string.login_success));
                                if(null!=successCallback){
                                   successCallback.onSuccess(response,id);
                                }
                            }else {
                                //登录失败
                                Log.e(TAG,context.getString(R.string.login_fail));
                                if(null!=failCallback){
                                    failCallback.onFail(response);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG,response);
                    }
                });
    }
    //使用账号密码
    public LoginService(final Context context, String url, String account, String pwd, final ISuccessCallback successCallback, final IFailCallback failCallback){
        OkHttpUtils
                .post()
                .url(url)
                .addParams(Config.KEY_ACCOUNT,account)
                .addParams(Config.KEY_PWD,pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                       if(null!=failCallback){
                           failCallback.onFail(e.getMessage());
                       }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt(Config.KEY_STATUS);
                            if (status == 1){
                                if(null!=successCallback){
                                    Log.e(TAG,"登录成功");
                                    successCallback.onSuccess(response,id);
                                }
                            }else {
                                if(null!=failCallback){
                                    Log.e(TAG,"登录失败");
                                    failCallback.onFail(response);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG,response);
                    }
                });
    }
    public interface ISuccessCallback{
        void onSuccess(String response, int id);
    }
    public interface IFailCallback{
        void onFail(String s);
    }
}
