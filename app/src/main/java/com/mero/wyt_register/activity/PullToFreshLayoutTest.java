package com.mero.wyt_register.activity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mero.wyt_register.Base.BaseActivity;
import com.mero.wyt_register.R;
import com.mero.wyt_register.widget.PullToFreshLayout;

/**
 * Created by chenlei on 2016/11/17.
 */

public class PullToFreshLayoutTest extends BaseActivity {
    private ListView lv;
    @Override
    public void initView() {
        final PullToFreshLayout pullToFreshLayout = (PullToFreshLayout) findViewById(R.id.refreshable_view);
        lv = (ListView) findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[]{"A","B","C","D","E","F","G"});
        lv.setAdapter(adapter);
        pullToFreshLayout.setOnRefreshListener(new PullToFreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pullToFreshLayout.finishRefreshing();
            }
        },0);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.helper;
    }

    @Override
    public int getDialogIcon() {
        return 0;
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }
}
