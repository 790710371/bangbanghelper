package com.mero.wyt_register.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mero.wyt_register.R;

/**
 * Created by chenlei on 2016/11/13.
 */

public class MenuFragment extends Fragment{
    public static final String TAG  = "MenuFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu,container,false);
        ImageView imageView01 = (ImageView) view.findViewById(R.id.img_memu_item_01);
        return view;
    }
}
