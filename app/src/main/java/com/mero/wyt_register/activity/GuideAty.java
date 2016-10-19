package com.mero.wyt_register.activity;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.mero.wyt_register.MainActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.adapter.ViewPagerAdapter;

/**
 *@项目名称: 简易通注册助手
 *@文件名称: GuideCty.java
 *@Date: 2016-7-15
 *@Copyright: 2016 Technology Mero Inc. All rights reserved.
 *注意：由Mero开发，禁止外泄以及使用本程序于其他的商业目的 。
 */
public class GuideAty extends Activity implements OnPageChangeListener{
	
	private List<View> views;
	
	private int ids[] = {R.id.iv1,R.id.iv2,R.id.iv3};//3个导航点
	
	private ImageView dots[];
	private ImageButton go;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private View view1,view2,view3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_view_page);
		init();
		initDots();
		
	}
	
	private void init() {
		
		LayoutInflater inflater=LayoutInflater.from(this);
		views = new ArrayList<View>();
		view1 = inflater.inflate(R.layout.viewpager_01, null);//实例化3个view
		view2 = inflater.inflate(R.layout.viewpager_02, null);
		view3 = inflater.inflate(R.layout.viewpager_03, null);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPagerAdapter = new ViewPagerAdapter(this, views);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(viewPagerAdapter);
		go = (ImageButton) views.get(2).findViewById(R.id.go);
		go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GuideAty.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		viewPager.setOnPageChangeListener(this);
	}
	
	private void initDots() {
		dots = new ImageView[views.size()];
		for(int i=0;i<dots.length;i++){
			dots[i] = (ImageView) findViewById(ids[i]);
		}
	}



	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		for(int i = 0;i<ids.length;i++){
			if(position==i){
				dots[i].setImageResource(R.drawable.dot_selected_shape);
			}else{
				dots[i].setImageResource(R.drawable.dot_no_selected_shape);
			}
		}
	}
}
