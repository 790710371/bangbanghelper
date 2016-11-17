package com.mero.wyt_register.activity;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mero.wyt_register.BaseActivity;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.R;
import com.mero.wyt_register.net.LoginService;
import com.mero.wyt_register.widget.RoundButton;

/**
 * Created by chenlei on 2016/11/16.
 */

public class LoginAty extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LoginAty";
    private EditText edt_account = null;
    private EditText edt_pwd = null;
    private RoundButton btn_login_click_to_login;
    private TextView tx_register;
    @Override
    public void initView() {
        edt_account = (EditText) findViewById(R.id.edt_login_account);
        edt_pwd = (EditText) findViewById(R.id.edt_login_pwd);
        tx_register = (TextView) findViewById(R.id.tx_login_click_to_register);
        btn_login_click_to_login = (RoundButton) findViewById(R.id.btn_login_click_to_login);
        btn_login_click_to_login.setOnClickListener(this);
        tx_register.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.wyt_login;
    }

    @Override
    public int getDialogIcon() {
        return 0;
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_click_to_login:
                //点击的是登录按钮
                final String wyt_account = edt_account.getText().toString();//获取账号
                final String wyt_pwd = edt_pwd.getText().toString();//获取密码
                if(TextUtils.isEmpty(wyt_account)){
                    Toast.makeText(LoginAty.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(wyt_pwd)){
                    Toast.makeText(LoginAty.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(wyt_account.length()>20){
                    Toast.makeText(LoginAty.this,"您输入的账号过长",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(wyt_pwd.length()>20){
                    Toast.makeText(LoginAty.this,"您输入的密码过长",Toast.LENGTH_SHORT).show();
                    return;
                }

                String token =  Config.getTokenFromPreferences(this);
                //如果token不为空，就使用token
                if(!TextUtils.isEmpty(token)){
                    new LoginService(LoginAty.this,Config.URL,token);
                }
                //采用账号密码登录
                if(TextUtils.isEmpty(token)) {
                    new LoginService(LoginAty.this,Config.URL,wyt_account,wyt_pwd);
                }
                break;
            case R.id.tx_login_click_to_register:
                showActivity(this,RegisterAty.class);
                break;
        }

    }
}
