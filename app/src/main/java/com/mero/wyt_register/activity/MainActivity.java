package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.R;
import com.mero.wyt_register.widget.MixTextImage;
import com.mero.wyt_register.widget.RoundButton;
import com.stericson.RootTools.RootTools;

import static android.R.attr.start;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private MixTextImage mixTextImage = null;
	private ImageView menuImage;
	private RoundButton btn_install_xposed;
	private PopupWindow window;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		initView();
		initData();
	}


	public void initView() {
		//得到配置信息
		btn_install_xposed = (RoundButton) findViewById(R.id.btn_xposed_install);
		mixTextImage = (MixTextImage) findViewById(R.id.mix_01);
		menuImage = (ImageView) findViewById(R.id.menu_icon);
		mixTextImage.setOnClickListener(this);
		menuImage.setOnClickListener(this);
		btn_install_xposed.setOnClickListener(this);
	}

	public void initData() {
		sharedPreferences = getSharedPreferences(Config.ID,MODE_PRIVATE);
	}


	//用于判断模块是否安装
	public static  String getResult(){
		return "未安装";
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG,"正在执行onResum");

		boolean isRoot = RootTools.isRootAvailable();
		String isXposedInstalled = sharedPreferences.getString(Config.KEY_IS_INSTALL_XPOSED,Config.VALUE_NOT_INSTALLED);
		String isModuleInstalled =getResult();
		Log.e(TAG,"正在进行判断:"+"isXposedInstalled:"+isXposedInstalled+"\t+isModuleInstalled:"+isModuleInstalled);
		if(isRoot==true&&isXposedInstalled.equals("已安装")&&isModuleInstalled.equals("已安装")){
			btn_install_xposed.setText("已安装");
			btn_install_xposed.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_icon:
				//此处弹出popouWindow对话框
				View windowView = LayoutInflater.from(this).inflate(R.layout.main_menu_item,null);
				window = new PopupWindow(this);
				window.setContentView(windowView);
				int[] loation = new int[2];
				menuImage.getLocationInWindow(loation);
				//设置为可触摸焦点
				window.setFocusable(true);
				window.setWidth(200);
				window.setHeight(400);
				window.showAtLocation(menuImage, Gravity.TOP|Gravity.LEFT,loation[0],loation[1]+menuImage.getHeight());

				//设置Pop欧Window内的功能点击事件
				TextView tx_setting = (TextView)windowView. findViewById(R.id.tx_item_main_item1);
				TextView tx_feedback = (TextView) windowView.findViewById(R.id.tx_item_main_item2);
				TextView tx_help= (TextView) windowView.findViewById(R.id.tx_item_main_item3);
				TextView tx_author = (TextView)windowView.findViewById(R.id.tx_item_main_item4);
				tx_setting.setOnClickListener(this);
				tx_feedback.setOnClickListener(this);
				tx_help.setOnClickListener(this);
				tx_author.setOnClickListener(this);

				break;
			case R.id.btn_xposed_install:
				String s = btn_install_xposed.getText().toString();
				Log.e(TAG,"点击按钮的文字是："+s);
				if((!TextUtils.isEmpty(s))&&s.equals("点击安装")){
					startActivity(new Intent(MainActivity.this,InstallXposedAty.class));
				}
				break;
			case R.id.mix_01:
				Toast.makeText(this, "正在点击淘宝注册", Toast.LENGTH_SHORT).show();
				break;
			case R.id.tx_item_main_item1:
				startActivity(new Intent(this, SettingAty.class));
				if(null!=window){
					window.dismiss();
				}
				break;
			case R.id.tx_item_main_item2:
				startActivity(new Intent(this, DeviceInfoAty.class));
				if(null!=window){
					window.dismiss();
				}
				break;
			case R.id.tx_item_main_item3:
				Toast.makeText(this,"正在点击帮助按钮",Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this, CleanDetailAty.class));
				break;
			case R.id.tx_item_main_item4:
				startActivity(new Intent(MainActivity.this,PullToFreshLayoutTest.class));
				break;

		}
	}



	@Override
	public void onStart() {
		super.onStart();


	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
