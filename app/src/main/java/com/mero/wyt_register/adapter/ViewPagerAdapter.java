package com.mero.wyt_register.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 *@项目名称: 简易通注册助手
 *@文件名称: ViewPagerAdapter.java
 *@Date: 2016-7-15
 *@Copyright: 2016 Technology Mero Inc. All rights reserved.
 *注意：由Mero开发，禁止外泄以及使用本程序于其他的商业目的 。
 */
public class ViewPagerAdapter extends PagerAdapter {

	private List<View> views;
	@SuppressWarnings("unused")
	private Context context;
	
	public ViewPagerAdapter(){
		
	}
	public ViewPagerAdapter(Context context){
		this.context = context;
	}
	public ViewPagerAdapter(Context context,List<View> views){
		this.context = context;
		this.views = views;
	}
	/*
	 * 
	 * 返回页卡数
	 * 
	 * */
	@Override
	public int getCount() {
		return views.size();
	}
	/*
	 * 
	 *实例化当前页卡 
	 * 
	 * */
	@Override
	public Object instantiateItem(android.view.ViewGroup container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
		
	};
	/*
	 * 销毁页卡
	 * 
	 * */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
}
