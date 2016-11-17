package com.mero.wyt_register.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mero.wyt_register.BaseActivity;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.R;
import com.mero.wyt_register.net.RegisterService;
import com.mero.wyt_register.utils.AppUtils;
import com.mero.wyt_register.widget.RoundButton;

import java.io.File;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by chenlei on 2016/11/17.
 */

public class RegisterAty extends BaseActivity implements View.OnClickListener {
    private ImageView img_upload;
    private EditText edt_account = null;
    private EditText edt_pwd = null;
    private RoundButton btn_register = null;
    private static final int RESULT_OPEN_IMAGE = 1;
    private Bitmap bitmap1;
    private String picturePath;//头像路径
    private ProgressDialog pd;//进度条
    private static final int DISMISS = 1000;//进度条消失
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_upload_img:
                //上传头像
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_OPEN_IMAGE);
                break;
            case R.id.btn_to_register:
                pd = ProgressDialog.show(this,"温馨提示","正在注册...",false,true);
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
                /*
                * 上传头像
                * */
                File pictrueFile = new File(picturePath);
                final  ProgressDialog pd = ProgressDialog.show(this,"温馨提示","正在上传",false,true);
                new RegisterService(Config.URL, pictrueFile, new RegisterService.ISuccessCallback() {
                    @Override
                    public void onSuccess(String response, int id) {
                                    pd.setMessage("上传头像成功");
                                    handler.sendEmptyMessageDelayed(DISMISS,1000);
                                }

                }, new RegisterService.IFailCallback() {
                    @Override
                    public void onFail(String failMsg) {
                        pd.setMessage("上传头像失败");
                        handler.sendEmptyMessageDelayed(DISMISS,1000);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OPEN_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            //裁剪为圆形头像
            bitmap1 = AppUtils.toRoundBitmap(bitmap);
            img_upload.setImageBitmap(bitmap1);//设置到图片
        }
    }
}
