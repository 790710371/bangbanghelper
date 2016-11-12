package com.mero.wyt_register.widget;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewHelper;

import static android.support.v7.widget.AppCompatDrawableManager.get;

/**
 * Created by chenlei on 2016/11/5.
 */

public class SlidingScaleView extends ScrollView{
    private static final String TAG = "SlidingScaleView";

    private OverScroller scroller;//滚动对象
    private ViewParent viewParent;//ViewParent接口对象
    private int mScreenWidth;//屏幕宽度
    private int mScreenHeight;//屏幕高度

    private int mHeight;//总高度

    private FrameLayout layout;//最外层布局
    private ViewGroup mHidden;//隐藏部分
    private ViewGroup mTop;//顶部部分
    private ViewGroup mMiddle;//中间部分

    private float down_x;//按下时的x坐标
    private float down_y;//按下时的Y坐标
    private float scale;//进度
    private float offestAbsY;
    private long currentTime ;//当前时间
    private int timeSize;//时间差
    private float mProgress;//进度

    private VelocityTracker velocity ; //速度跟踪器
    private float velocityUp;//上滑时候的最大速度
    private float velocityDown;//下滑时候的最大速度

    private boolean isHidden = true;//头部是否隐藏，初始化是隐藏的
    private boolean isFilingEnd = false;//是否滑动完毕
    private boolean isNeedInterrupt = false;//是否需要中断

    private boolean isTranslate = false;//是否平移

    private int count = 0;
    private int t;
    public SlidingScaleView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SlidingScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScrollView(context);
        Log.e(TAG,"构造:"+scroller.isFinished());
    }
    //初始化滚动事件
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initScrollView(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display =  wm.getDefaultDisplay();
        DisplayMetrics out = new DisplayMetrics();
        display.getMetrics(out);
        mScreenWidth = out.widthPixels;//得到屏幕宽度
        mScreenHeight =out.heightPixels;//得到屏幕高度    ``
        Log.e(TAG,"mScreenWidth:"+mScreenHeight+"mScreenHeight:"+mScreenHeight);

         scroller = new OverScroller(getContext());//得到
        new ImageView(context).setBackgroundResource(0);
    }
    //判断是否拖动
    public boolean isDragging(){
        return isDragging;
    }
    //判断是否存在速度跟踪器
    public void isVelocityTrackerNotExist(){
        if(null == velocity){
            velocity = VelocityTracker.obtain();
        }
    }
    //结束标志
    private void endDrag(){
        isDragging = false;
    }
    //屏幕尺寸
    private int[] getScreenPixels(Context c){
        return new int[]{mScreenWidth,mScreenHeight};
    }
    //hidden部分高度
    private  int getHiddenHeight(){
        return mHidden.getChildAt(0).getMeasuredHeight();
    }
    //top部分高度
    private int getTopHeight(){
        return mTop.getChildAt(0).getMeasuredHeight();
    }
    //middle部分高度
    private int getMiddleHeight(){
        return mMiddle.getChildAt(0).getMeasuredHeight();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到布局
        layout = (FrameLayout) getChildAt(0);//得到最外层布局
        mTop = (ViewGroup) layout.getChildAt(0);//得到top部分
        mHidden = (ViewGroup) layout.getChildAt(1);//得到隐藏部分布局
        mMiddle = (ViewGroup) layout.getChildAt(2);
        //测量hidden宽高
        mHidden.getLayoutParams().width = mScreenWidth;
        mHidden.getLayoutParams().height = mHidden.getChildAt(0).getMeasuredHeight();
        //测量top宽高
        mTop.getLayoutParams().width = mScreenWidth ;
        mTop.getLayoutParams().height = mTop.getChildAt(0).getMeasuredHeight()+mTop.getPaddingTop();
        //测量middle宽高
        mMiddle.getLayoutParams().width = mScreenWidth;
        mMiddle.getLayoutParams().height = mMiddle.getChildAt(0).getMeasuredHeight()+mMiddle.getPaddingTop();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private static int status  ;//状态标志
    private static final int STATUS_EQUIMENT = 0;//静止状态
    private static final int STATUS_UP = 1;//上滑状态中
    private static final int STATUS_DOWN = 2;//下滑状态中
    private boolean isDragging = false ;//是否拖动

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        isVelocityTrackerNotExist();
        MotionEvent event = MotionEvent.obtain(ev);
        final int actionMasked = event.getActionMasked();
        velocity.addMovement(event);
        velocity.computeCurrentVelocity(1,8f);
        switch (actionMasked){
            case MotionEvent.ACTION_DOWN:
                isFilingEnd = false;
                down_x = (int) ev.getX();//按下时候的x坐标
                down_y = (int) ev.getY();//按下时候的y坐标
                currentTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                float x_up = ev.getX();
                float y_up = ev.getY();
                //得到即时的移动y方向的位移
                float mOffestY = y_up - down_y;
                float mOffestYAbs = Math.abs(mOffestY);
                if(mOffestY<0){
                    //如果隐藏
                    if(isHidden==true){
                        //如果滑动距离或者速度达到阈值
                        if(mOffestYAbs>SlidingScaleViewConfigure.maxOffest||velocityUp>SlidingScaleViewConfigure.maxVelocity){
                            this.smoothScrollTo(0,2*getHiddenHeight());
                        }else{
                            //否则滑动距离或者速度没有达到阈值
                            this.smoothScrollTo(0, getHiddenHeight());
                        }
                    }else {
                        //如果显示
                    }

                }else {
                    //下滑

                }
                endDrag();
                return true;
            case MotionEvent.ACTION_MOVE:
                float move_x = (int) ev.getX();
                float move_y = (int) ev.getY();
                //得到即时的移动y方向的位移
                float offestY = move_y - down_y;
                float offestYAbs = Math.abs(offestY);
                //判断是否滑动
                if(offestYAbs>0){
                    isDragging = true;//拖动中
                }
                if(offestY<0){
                    status = STATUS_UP;
                    //上滑
                    float max = velocity.getYVelocity();
                    //如果速度大于3.0f
                    if(max>SlidingScaleViewConfigure.maxVelocity){
                        velocityUp = max;//获取此时的最大速度
                    }
                    //如果上滑并且顶部下滑平移距离等于200dp
                    if(status==STATUS_UP&&mHidden.getTranslationY()==(getTopHeight()-getHiddenHeight())){
                        isNeedInterrupt = true;//需要停止滑动
                    }
                    if(isNeedInterrupt){
                        isHidden = false;//顶部已经隐藏
                        return false;//中断滑动
                    }
                }else{
                    //下滑
                    float max = velocity.getYVelocity();
                    if(max<SlidingScaleViewConfigure.maxVelocity){
                        velocityDown = max;
                    }
                }

                if(velocity!=null){
                    velocity.clear();;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(0,getHiddenHeight());
        isHidden = true;
    }
    //滑动范围
    public int getComputeScrollRange(){
        return computeVerticalScrollRange();
    }

    //得到隐藏层的高度
    private int getHiddenViewHeight(){
        return mHidden.getHeight();
    }
    //得到top层的高度
    private int getTopViewHeight(){
        return mTop.getHeight();
    }
    //得到middle层的完整高度
    private int getMiddleViewHeight(){
        return mMiddle.getHeight();
    }
    //得到所有布局的高度
    private int getTotalViewHeight() {
        return getHiddenHeight()+getTopHeight()+getMiddleHeight();
    }
    private Handler handler =new Handler();
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(t==400){

        }
        Log.e(TAG,"当前进度\t:"+t);
        float scale = (float)(t-getHiddenHeight())/getHiddenHeight();
        Log.e(TAG,"进度未修正值："+scale);
        scale =scale<1.0f?scale:1.0f;//修正进度
        Log.e(TAG,scale+"");
        if(isHidden==true&&status==STATUS_UP){
            ViewHelper.setTranslationY(mHidden,(float)(getTopHeight()-getHiddenHeight())*scale);
//            ViewHelper.setTranslationY(mTop, -(float) (getHiddenHeight()*(0.7+0.3*scale)));
        }
        if(status==STATUS_UP&&t>=(getTopHeight()-getHiddenHeight())){
            //上滑完毕
            isFilingEnd = true;
        }else {
            //上滑未结束
            isFilingEnd = false;
        }
        Log.e(TAG,isHidden+"\t"+status);

    }

    @Override
    public void fling(int velocityY) {
        Log.e(TAG,"fling"+velocityY);
        super.fling(velocityY/3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    //配置类
    static class SlidingScaleViewConfigure{
        private static float maxVelocity = 3.0f;
        private static float maxOffest = 80;
    }
}
