package com.mero.wyt_register.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.DeviceUtils;

/**
 *@项目名称: 简易通注册助手
 *@文件名称: WelcomeAty.java
 *@Date: 2016-7-15
 *@Copyright: 2016 Technology Mero Inc. All rights reserved.
 *注意：由Mero开发，禁止外泄以及使用本程序于其他的商业目的 。
 */
@SuppressLint("HandlerLeak") public class WelcomeAty extends Activity{
	private static final String TAG="WelcomActivity";
	private boolean isFirstIn = true;//判断是否是首次
	private static final int TIME=2000;
	private static final int GO_HOME=1000;
	private static final int GO_GUIDE=1001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom_activity);
		init();
	}
	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences(Config.ID, MODE_PRIVATE);
		isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
		Log.e(TAG, "正在判断是否为首次进入软件...");
		Log.e(TAG,isFirstIn+"" );
		if(!isFirstIn){
			handler.sendEmptyMessageDelayed(GO_HOME,TIME);
			Log.e(TAG,isFirstIn+"第一次判断" );
			Log.e(TAG, "非首次进入");
		}else{
			Log.e(TAG, "首次进入");
			handler.sendEmptyMessageDelayed(GO_GUIDE,TIME);
			SharedPreferences.Editor editor1 = sharedPreferences.edit();
			Log.e(TAG,isFirstIn+"第二次判断" );
			editor1.putBoolean("isFirstIn", false);
			Log.e(TAG,isFirstIn+"第三次判断" );
			editor1.commit();
		}
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
		}
		
		private void goGuide() {
			Intent intent = new Intent(WelcomeAty.this,GuideAty.class);
			startActivity(intent);
			finish();
		}

		private void goHome() {
			Intent intent = new Intent(WelcomeAty.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
}
