package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mero.wyt_register.R;
import com.mero.wyt_register.widget.CustomTitleBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessorInfoAty extends Activity {
    public static final String TAG = "ProcessorInfoAty";
    private CustomTitleBar titleBar;
    private TextView tx_show_cpu_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.processor_info_layout);
        initView();
        try {
            readCpuInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initView() {
        Log.e(TAG,"正在执行ProcessInfoAty");
        tx_show_cpu_info = (TextView) findViewById(R.id.tx_show_cpu_info);
        titleBar = (CustomTitleBar) findViewById(R.id.processor_info_title_bar);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProcessorInfoAty.this,DeviceInfoAty.class));
                finish();
            }
        });
        
    }
    private void readCpuInfo() throws IOException {
        new GetProcessorInfo(this, new GetProcessorInfo.SuccessCallback() {
            @Override
            public void onSuccess() {
                Log.e("TAG","写入成功");
                //读取
                String toshow = readSdFile();
                tx_show_cpu_info.setText(toshow);
            }
        }, new GetProcessorInfo.FailCallback() {
            @Override
            public void onFail() {
                Log.e("TAG","写入失败");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tx_show_cpu_info.setText(readSdFile());
    }
    String  readSdFile(){
        String toShow = null;
        Log.e("ProcessorOnfoAty","正在读取cpu信息");
        File file = new File(Environment.getExternalStorageDirectory().toString()+"/zy/cpuinfo.txt");
        InputStream in =null;
        try {
            in =new FileInputStream(file);
            StringBuilder sb =new StringBuilder("");
          BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in));
            String s = "";
            while((s = bufferReader.readLine())!=null){
                sb.append(s).append("\n");
            }
           toShow = sb.toString();
            Log.e("cpuinfo信息打印",toShow);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return toShow;
    }
}
