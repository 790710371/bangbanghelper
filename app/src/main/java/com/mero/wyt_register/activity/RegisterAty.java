package com.mero.wyt_register.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mero.wyt_register.BaseActivity;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.R;
import com.mero.wyt_register.net.RegisterService;
import com.mero.wyt_register.utils.AppUtils;
import com.mero.wyt_register.utils.Base64Utils;
import com.mero.wyt_register.utils.SDCardUtils;
import com.mero.wyt_register.widget.RoundButton;
import com.mero.wyt_register.widget.SelectPicPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by chenlei on 2016/11/17.
 */

public class RegisterAty extends BaseActivity implements View.OnClickListener {
    private ImageView img_upload;
    private EditText edt_account = null;
    private EditText edt_pwd = null;
    private RoundButton btn_register = null;
    private static final int RESULT_OPEN_IMAGE = 1;
    private Bitmap bitmap ;//存放裁剪后的头像
    private String fileName;//头像名称
    private String picturePath;//头像路径
    private ProgressDialog pd;//进度条
    private static final int DISMISS = 1000;//进度条消失
    private SelectPicPopupWindow selectPicPopupWindow;
    @Override
    public void initView() {
        img_upload = (ImageView) findViewById(R.id.img_upload_img);
        edt_account = (EditText) findViewById(R.id.edt_register_account);
        edt_pwd = (EditText) findViewById(R.id.edt_register_pwd);
        img_upload.setOnClickListener(this);
        btn_register = (RoundButton) findViewById(R.id.btn_to_register);
        btn_register.setOnClickListener(this);
    }
    @Override
    public void initData() {

    }
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DISMISS){
                pd.dismiss();
            }
        }
    };
    @Override
    public int getLayoutResourceId() {
        return R.layout.wyt_register;
    }

    @Override
    public int getDialogIcon() {
        return 0;
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    private static final int CAMERA_REQUEST_CODE = 1;//拍照返回码
    private static final int GALLERY_REQUEST_CODE = 2;//相册返回码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是拍照返回
        if(requestCode==CAMERA_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null) {
            Uri uri = data.getData();
            if (uri != null) {
                Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    picturePath = cursor.getString(cursor.getColumnIndex("_data"));
                    fileName = getBitmapName(picturePath);
                    bitmap = AppUtils.toRoundBitmap(BitmapFactory.decodeFile(picturePath));
                    //进行裁剪
                    img_upload.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(this,"保存照片失败",Toast.LENGTH_SHORT).show();
                return;
            }
        }


        //如果是相册返回
        if(requestCode==GALLERY_REQUEST_CODE&&resultCode==RESULT_OK&&null!=data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            fileName = getBitmapName(picturePath);
            cursor.close();
            //裁剪为圆形头像
            if(SDCardUtils.isSDCardEnable()){
                bitmap = AppUtils.toRoundBitmap(BitmapFactory.decodeFile(picturePath));
                img_upload.setImageBitmap(bitmap);//设置到图片
            }else {
                return;
            }
        }else {
            return;
        }
    }
    //获取图片的名称
    public String getBitmapName(String picPath){
        String bitmapName="";
        String[]  s = picPath.split("/");
        bitmapName = s[s.length-1];
        return bitmapName;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_upload_img:
                //判断是否从相册或者调用相机实现
                selectPicPopupWindow = new SelectPicPopupWindow(RegisterAty.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPicPopupWindow.dismiss();
                        switch (v.getId()){
                            case R.id.btn_select_camera:
                                //从相机拍照
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(i,1);
                                break;
                            case R.id.btn_select_pic_photo_lib:
                                //从图库选择照片
                                Intent ii = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(ii, RESULT_OPEN_IMAGE);
                                break;
                        }
                    }
                });
                selectPicPopupWindow.showAtLocation(RegisterAty.this.findViewById(R.id.register), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                break;
            case R.id.btn_to_register:
                //点击的是注册按钮
                final String wyt_account = edt_account.getText().toString();//获取账号
                final String wyt_pwd = edt_pwd.getText().toString();//获取密码
                if(TextUtils.isEmpty(wyt_account)){
                    Toast.makeText(RegisterAty.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(wyt_pwd)){
                    Toast.makeText(RegisterAty.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(wyt_account.length()>20){
                    Toast.makeText(RegisterAty.this,"您输入的账号过长",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(wyt_pwd.length()>20){
                    Toast.makeText(RegisterAty.this,"您输入的密码过长",Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = ProgressDialog.show(this,"温馨提示","正在注册...",false,true);
                if(null!=bitmap){
                    //截取图片后缀

                    String base64img = Base64Utils.bitmaptoString(bitmap);
                    //进行用户注册
                    new RegisterService(Config.URL, Config.KEY_REGISTER, wyt_account, wyt_pwd, base64img, new RegisterService.ISuccessCallback() {
                        @Override
                        public void onSuccess(String response, int id) {
                            pd.setMessage("注册成功");
                            handler.sendEmptyMessageDelayed(DISMISS,1000);
                        }
                    }, new RegisterService.IFailCallback() {
                        @Override
                        public void onFail(String failMsg) {
                            pd.setMessage("注册失败"+failMsg);
                            handler.sendEmptyMessageDelayed(DISMISS,1000);
                        }
                    });
                }

                break;
            default:
                break;
        }
    }
    private String getPicType(String picName){
        String[] s = picName.split(".");
        //数组长度
        return s[s.length-1];
    }

}
