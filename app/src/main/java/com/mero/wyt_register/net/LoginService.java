package com.mero.wyt_register.net;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creaed by chenlei on 2016/11/15.
 */

public class LoginService {
    //使用token登录
    public LoginService(final String url,String token,final ISuccessCallback successCallback,final IFailedCallback failedCallback){
      new BaseHttpConnection(url, HttpMethod.POST, new BaseHttpConnection.ISuccessCallback() {
          @Override
          public void success(String result) {
              try {
                  JSONObject jsonObject= new JSONObject(result);
                  int status = jsonObject.getInt(Config.KEY_STATUS);
                  if(status==0){
                      if(null!=failedCallback){
                          failedCallback.fail();
                      }
                  }else if(status==1){
                      if(null!=successCallback){
                          //把返回的token保存起来
                          String token = jsonObject.getString(Config.KEY_TOKEN);
                          successCallback.success(result);
                      }
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
      }, new BaseHttpConnection.IFailedCallback() {
          @Override
          public void fail() {
                //登录失败
              if(null!=failedCallback){
                  failedCallback.fail();
              }
          }
      },token);
    }
    //使用账号密码登录
    public LoginService(final String url,String account,String pwd,final ISuccessCallback successCallback,final IFailedCallback failedCallback){
        new BaseHttpConnection(url, HttpMethod.POST, new BaseHttpConnection.ISuccessCallback() {
            @Override
            public void success(String result) {
                try {
                    JSONObject jsonObject= new JSONObject(result);
                    int status = jsonObject.getInt(Config.KEY_STATUS);
                    if(status==0){
                        if(null!=failedCallback){
                            failedCallback.fail();
                        }
                    }else if(status==1){
                        if(null!=successCallback){
                            successCallback.success(result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new BaseHttpConnection.IFailedCallback() {
            @Override
            public void fail() {
                //登录失败
                if(null!=failedCallback){
                    failedCallback.fail();
                }
            }
        },account,pwd);
    }
    //成功回调接口
    public interface ISuccessCallback{
        void success(String result);
    }
    //失败回调接口
    public interface IFailedCallback{
        void fail();
    }
}
