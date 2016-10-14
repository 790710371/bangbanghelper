package com.mero.wyt_register.widget;

import com.mero.wyt_register.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *@项目名称: 帮帮助手
 *@文件名称: MixTextImage.java
 *@Date: 2016-7-16
 *@Copyright: 2016 Technology Mero Inc. All rights reserved.
 *注意：由Mero开发，禁止外泄以及使用本程序于其他的商业目的 。
 */
public class MixTextImage extends LinearLayout{
	private ImageView imageView = null;
	private TextView tx_fun_type = null;
	public MixTextImage(Context context){
		super(context);
	}
	public MixTextImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//或LayoutInflater layoutInflater LayoutInflater.from(context);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.mix_text_image, this);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Mix);
		initTyped(typedArray);
	}
	/**
	 * @param typedArray
	 */
	private void initTyped(TypedArray typedArray) {
		
		tx_fun_type = (TextView) findViewById(R.id.tx_fun_type);
		String text = typedArray.getString(R.styleable.Mix_text);
		int textColor = typedArray.getColor(R.styleable.Mix_text_color, 0xffffffff);
		float textSize =  typedArray.getDimension(R.styleable.Mix_text_size, 20);
		tx_fun_type.setText(text);
		tx_fun_type.setTextColor(textColor);
		tx_fun_type.setTextSize(textSize);
		
		
		imageView = (ImageView) findViewById(R.id.img_icon_left);
		
		int imageSrc = typedArray.getResourceId(R.styleable.Mix_image_src, 0);//从属性集得到图片
		int imageBg = typedArray.getResourceId(R.styleable.Mix_image_bg, 0);//从属性集得到背景图
		int imageWidth = (int) typedArray.getDimension(R.styleable.Mix_image_width,30);
		int imageHeight = (int) typedArray.getDimension(R.styleable.Mix_image_height, 30);
		int imageAlpha = typedArray.getInt(R.styleable.Mix_image_alpha, 255);
		imageView.setImageResource(imageSrc);
		imageView.setBackgroundResource(imageBg);
		imageView.setAlpha(imageAlpha);
		LayoutParams layoutParams = new LayoutParams(imageWidth, imageHeight);
		imageView.setLayoutParams(layoutParams);//设置图片高度
		typedArray.recycle();
	}
	
	//设置图片资源
	@SuppressWarnings("unused")
	private void setImgResource(int resId){
		imageView.setImageResource(resId);
	}

	
	//设置控件背景图片
	@SuppressWarnings("unused")
	private void setBgImage(int resId){
		imageView.setImageResource(resId);
	}
	
	//设置图片的高度和宽度
	private void setImageSize(int width,int height){
		LayoutParams layoutParams = new LayoutParams(width,height);
		imageView.setLayoutParams(layoutParams);
	}
	
	//设置图片的不透明度
	private void setImageAlpha(int alpha){
		imageView.setAlpha(alpha);
	}
	
	//设置文字内容
	private void setText(String text){
		tx_fun_type.setText(text);
	}
	
	
	//设置文字颜色
	private void setTextColor(int colorValue){
		tx_fun_type.setTextColor(colorValue);
	}
	
	//设置文字大小
	private void setTextSize(int size){
		tx_fun_type.setTextSize(size);
	}
	//设置图片点击事件
	public void onMixTextImageClick(final MixTextImageListener mixTextImageListener){
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mixTextImageListener.mClick();
			}
		});
	}
	//回调接口
	public interface MixTextImageListener{
		public void mClick();
	}
}
