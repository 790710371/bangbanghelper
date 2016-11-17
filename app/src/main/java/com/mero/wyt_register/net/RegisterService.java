package com.mero.wyt_register.net;

import com.mero.wyt_register.HttpMethod;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.reflect.Method;

import okhttp3.Call;

/**
 * Created by chenlei on 2016/11/17.
 */

public class RegisterService {
    public RegisterService(String url,File file){
        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
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
}
