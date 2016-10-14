package com.mero.wyt_register.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mero.wyt_register.R;

import static com.mero.wyt_register.R.id.img_left;


public class CustomTitleBar extends RelativeLayout {
    private RelativeLayout layout;
    private TextView tx_middle = null;//中间title文字
    public CustomTitleBar(Context context) {
        super(context);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_title,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.Title_bar);//得到属性集对象
        initTyped(typedArray);
    }

    private void initTyped(TypedArray typedArray) {
        layout = (RelativeLayout) findViewById(R.id.relative_title_bar_left);
        tx_middle = (TextView) findViewById(R.id.tx_middle_title);
        String text = typedArray.getString(R.styleable.Title_bar_text);//文字内容
        int textSize = (int) typedArray.getDimension(R.styleable.Mix_text_size,20);//文字大小
        int textColor = typedArray.getInt(R.styleable.Title_bar_textColor,0xFFFFFFFF);//文字颜色
        tx_middle.setText(text);
        tx_middle.setTextSize(textSize);
        tx_middle.setTextColor(textColor);
        typedArray.recycle();;
    }

    /*设置文字内容*/
    private void setTextContent(String text){
        tx_middle.setText(text);
    }

    //设置文字的颜色
    private void setTextColor(int color){
        tx_middle.setTextColor(color);
    }
    //设置文字的大小
    private void setTextSize(int size){
        tx_middle.setTextSize(size);
    }

    //设置左侧的图片监听
    public void onClick(final TitleBarImageListener  titleBarImageListener){
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=titleBarImageListener){
                    titleBarImageListener.onClick();
                }
            }
        });
    }
    //设置左侧图片的点击回调接口
    public interface  TitleBarImageListener{
        void onClick();
    }
}
