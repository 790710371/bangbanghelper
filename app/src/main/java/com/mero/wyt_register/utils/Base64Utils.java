package com.mero.wyt_register.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by chenlei on 2016/11/20.
 */

public class Base64Utils {
    public static String bitmaptoString(Bitmap bitmap) {
        byte[] bytes;
        ByteArrayOutputStream    bm = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bm);
        bytes = bm.toByteArray();
         String s = Base64.encodeToString(bytes, Base64.DEFAULT);
        return s;
    }
    //Base64编码bitmap转化为字符串对象
//    public static String bitmaptoString(Bitmap bitmap, String picType) {
//        String s = "";
//        ByteArrayOutputStream bm = null;
//        byte[] bytes;
//        switch (picType) {
//            case "png":
//                bm = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bm);
//                bytes = bm.toByteArray();
//                s = Base64.encodeToString(bytes, Base64.DEFAULT);
//                return s;
//            case "jpeg":
//                bm = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bm);
//                bytes = bm.toByteArray();
//                s = Base64.encodeToString(bytes, Base64.DEFAULT);
//                return s;
//            case "jpg":
//                bm = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bm);
//                bytes = bm.toByteArray();
//                s = Base64.encodeToString(bytes, Base64.DEFAULT);
//                return s;
//        }
//        return s;
//    }
}

