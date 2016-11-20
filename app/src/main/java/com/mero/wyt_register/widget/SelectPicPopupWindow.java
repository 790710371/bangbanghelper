package com.mero.wyt_register.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.PopupWindow;

import com.mero.wyt_register.R;

/**
 * Created by chenlei on 2016/11/19.
 */

public class SelectPicPopupWindow extends PopupWindow {
    private Button btn_camera;
    private Button btn_photo_lib;
    private Button btn_cancle;
    private View view;
    public SelectPicPopupWindow(Activity context, View.OnClickListener onClickListener){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.select_pic_popup_window,null);
        btn_camera = (Button) view.findViewById(R.id.btn_select_camera);//拍照按钮
        btn_photo_lib = (Button) view.findViewById(R.id.btn_select_pic_photo_lib);//从图库选择按钮
        btn_cancle = (Button) view.findViewById(R.id.btn_select_pic_cancle);//取消按钮
        btn_camera.setOnClickListener(onClickListener);
        btn_photo_lib.setOnClickListener(onClickListener);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(android.R.style.Animation_Dialog);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.select_pic_popup).getTop();
                int y = (int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
