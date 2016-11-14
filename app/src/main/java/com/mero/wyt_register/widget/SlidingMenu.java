package com.mero.wyt_register.widget; /**
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 *@项目名称: SlidingMenu
 *@文件名称: SlidingMenu.java
 *@Date: 2016-9-23
 *@Copyright: 2016 Technology Mero Inc. All rights reserved.
 *注意：由Mero开发，禁止外泄以及使用本程序于其他的商业目的 。
 */
public class SlidingMenu extends HorizontalScrollView{
	public static final String TAG = "SlidingMenu";
	private ViewGroup mMenuLayout;
	private ViewGroup mMainLayout;
	private int mScreenWidth;
	private int mPaddingRight = 100;
	private float downX;
	private float upX;
	private int mMenuWidth;
	private LinearLayout layout;
	private float d;
	private float inLeftOrRight;
	private int count = 0;
	private float velocityMax =0.0f;
	private boolean isNeedScrollByMaxVelocity = false;
	/**
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		mScreenWidth = displayMetrics.widthPixels;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.HorizontalScrollView#onMeasure(int, int)
	 */

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		layout = (LinearLayout)getChildAt(0);
		//菜单布局
		mMenuLayout = (ViewGroup) layout.getChildAt(0);
		//主界面布局
		mMainLayout = (ViewGroup) layout.getChildAt(1);
		mMenuWidth = mScreenWidth - mPaddingRight;//菜单宽度
		Log.e("TAG",	"mMenuWidth:"+mMenuWidth+"");
		//设置菜单宽度
		mMenuLayout.getLayoutParams().width = mMenuWidth;
		//主布局宽度
		mMainLayout.getLayoutParams().width = mScreenWidth;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	boolean flag  =false;//默认菜单menu隐藏
	/*
	 * (non-Javadoc)
	 * @see android.widget.HorizontalScrollView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		VelocityTracker velocityTracker = VelocityTracker.obtain();//获取速度跟踪器
		velocityTracker.addMovement(ev);
		velocityTracker.computeCurrentVelocity(1,8.0f);
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = ev.getX();//按下的横坐标
				break;

			case MotionEvent.ACTION_UP:
				upX = ev.getX();//松开的横坐标
				d = upX - downX;//计算绝对值判断是否滑动
				float ds = Math.abs(d);
				if(flag==false){
					//左移
					if(d<0){
						this.smoothScrollTo(mMenuWidth, 0);
						flag = false;
					}
					//右移
					if(d>0){
						if(ds>mScreenWidth/5||isNeedScrollByMaxVelocity==true){
							this.smoothScrollTo(0, 0);
							flag = true;//显示
						}else{
							this.smoothScrollTo(mMenuWidth, 0);
							flag = false;//隐藏
						}
					}
				}else if(flag == true){
					//左移
					if(d<0){
						if(ds<mScreenWidth/5||isNeedScrollByMaxVelocity==true){
							this.smoothScrollTo(0, 0);
							flag = true;
						}else{
							this.smoothScrollTo(mMenuWidth, 0);
							flag = false;
						}
					}
					//右移
					if(d>0){
						this.smoothScrollTo(0, 0);
						flag = true;
						break;
					}
				}
				velocityMax = 0.0f;//恢复初始值
				isNeedScrollByMaxVelocity = false;//恢复初始状态
				return true;
				case MotionEvent.ACTION_MOVE :
					float velocityX = velocityTracker.getYVelocity();
					if(velocityX>3.0f){
						velocityMax = velocityX;
					}
					if(velocityMax>3.0f){
						isNeedScrollByMaxVelocity = true;
					}
				break;
				default:
					break;
		}
		return super.onTouchEvent(ev);
	}


	/*
	 * (non-Javadoc)
	 * @see android.widget.HorizontalScrollView#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		this.scrollTo(mMenuWidth, 0);
		flag = false;
	}
	/* (non-Javadoc)
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		count++;
		Log.e(TAG,l+"进度l"+l);
		//缩放比例
		float scale = (float)l/mMenuWidth;
		Log.e(TAG, scale+"缩放度");
		//左菜单缩放
		float scaleLeft = 1-0.3f*scale;
		ViewHelper.setScaleX(mMenuLayout, scaleLeft);
		ViewHelper.setScaleY(mMenuLayout, scaleLeft);
		//透明度缩放
		float alphaLeft = 1- scale;
		ViewHelper.setAlpha(mMenuLayout, alphaLeft);
		//主界面
		float scaleRight = 0.93f + scale * 0.07f;
		ViewHelper.setScaleX(mMainLayout, scaleRight);
		ViewHelper.setScaleY(mMainLayout, scaleRight);
		//视觉差平移
		ViewHelper.setTranslationX(mMenuLayout, mMenuWidth * scale);
	}


}
