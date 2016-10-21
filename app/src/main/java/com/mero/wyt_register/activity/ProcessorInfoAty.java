package com.mero.wyt_register.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mero.wyt_register.R;
import com.mero.wyt_register.widget.CustomTitleBar;

public class ProcessorInfoAty extends Activity {
    private CustomTitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processor_info_layout);
        initView();
    }

    private void initView() {
        titleBar = (CustomTitleBar) findViewById(R.id.processor_info_title_bar);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProcessorInfoAty.this,DeviceInfoAty.class));
                finish();
            }
        });
    }
}
