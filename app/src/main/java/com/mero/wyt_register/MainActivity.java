package com.mero.wyt_register;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mero.wyt_register.activity.DeviceInfoAty;
import com.mero.wyt_register.activity.SettingAty;
import com.mero.wyt_register.widget.MixTextImage;

public class MainActivity extends Activity implements OnClickListener {

	private MixTextImage mixTextImage = null;
	private ImageView menuImage;
	private PopupWindow window;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//沉浸式状态栏
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_main);
		initView();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private void initView() {
		mixTextImage = (MixTextImage) findViewById(R.id.mix_01);
		menuImage = (ImageView) findViewById(R.id.menu_icon);
		mixTextImage.setOnClickListener(this);
		menuImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_icon:
//			Toast.makeText(this, "正在点击menu菜单", Toast.LENGTH_SHORT).show();
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
				break;
			case R.id.tx_item_main_item4:
				Toast.makeText(this,"正在点击作者按钮",Toast.LENGTH_SHORT).show();
				break;

		}
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("Main Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}
}
