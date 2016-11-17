package com.mero.wyt_register.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.HttpMethod;
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
    public  LoginService(Context context,String url,String token){
        post()
                .url(url)
                .addParams(Config.KEY_TOKEN,token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }
    //使用账号密码
    public LoginService(final Context context, String url, String account, String pwd){
        final ProgressDialog pd = ProgressDialog.show(context,"温馨提示","登录中...",false,true);
        OkHttpUtils
                .post()
                .url(url)
                .addParams(Config.KEY_ACCOUNT,account)
                .addParams(Config.KEY_PWD,pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pd.setMessage("登录失败，网络故障");
                        pd.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt(Config.KEY_STATUS);
                            if (status == 1){
                               pd.setMessage("登录成功");
                                pd.dismiss();
                            }else {
                                pd.setMessage("登录失败");
                                pd.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG,response);
                    }
                });
    }
}
