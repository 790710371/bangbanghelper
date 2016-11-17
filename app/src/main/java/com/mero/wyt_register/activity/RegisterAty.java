package com.mero.wyt_register.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.mero.wyt_register.BaseActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.utils.AppUtils;

/**
 * Created by chenlei on 2016/11/17.
 */

public class RegisterAty extends BaseActivity implements View.OnClickListener {
    private ImageView img_upload;
    private static final int RESULT_OPEN_IMAGE = 1;

    @Override
    public void initView() {
        img_upload = (ImageView) findViewById(R.id.img_upload_img);
        img_upload.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

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
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            //裁剪为圆形头像
            Bitmap bitmap1 = AppUtils.toRoundBitmap(bitmap);
            img_upload.setImageBitmap(bitmap1);//设置到图片
        }
    }
}
