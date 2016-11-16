package com.mero.wyt_register.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mero.wyt_register.Config;
import com.mero.wyt_register.R;

/**
 * Created by chenlei on 2016/11/13.
 */

public class MenuFragment extends Fragment implements View.OnClickListener{
    public static final String TAG  = "MenuFragment";
    private View view;
    private ImageView img_menu_login;//登录
    private TextView tx_user_name;//用户名
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu,container,false);
        initView();
        return view;
    }

    private void initView() {
        img_menu_login = (ImageView) view.findViewById(R.id.img_menu_icon);
        tx_user_name = (TextView) view.findViewById(R.id.tx_menu_user_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_menu_icon:
                //点击头像登录
            
                break;
        }
    }
}
