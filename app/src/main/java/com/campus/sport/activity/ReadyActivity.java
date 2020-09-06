package com.campus.sport.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campus.sport.AppClient;
import com.campus.sport.R;

/**
 * Created by Administrator on 2019/5/24 0024.
 */

public class ReadyActivity extends AppCompatActivity {

    private TextView mLc;
    private TextView mSj;
    private TextView mCs;
    private Button mOk;
    private AppClient mApp;
    private RelativeLayout mLyout1;
    private LinearLayout mLyout2;
    private ImageView mImg;
    private AnimationDrawable drawable;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            startActivity(new Intent(ReadyActivity.this,StartRunActivity.class));
            finish();
            return false;
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_layout);
        mApp= (AppClient) getApplication();
        initView();
        initData();
        initLister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(ReadyActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION
        ,Manifest.permission.ACCESS_WIFI_STATE
        ,Manifest.permission.CHANGE_WIFI_STATE
        },123);
    }

    private void initLister() {
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLyout1.setVisibility(View.GONE);
                mLyout2.setVisibility(View.VISIBLE);
                drawable.start();
                handler.sendEmptyMessageDelayed(0x001,3000);
            }
        });
    }

    private void initData() {
        mLc.setText(mApp.getLc());
        mSj.setText(mApp.getSj());
        mCs.setText(mApp.getCs());
    }

    private void initView() {
        mLc=findViewById(R.id.ready_zlc);
        mSj=findViewById(R.id.ready_sj);
        mCs=findViewById(R.id.ready_cs);
        mOk=findViewById(R.id.ready_start);
        mLyout1=findViewById(R.id.ready_layout1);
        mLyout2=findViewById(R.id.ready_layout2);
        mImg=findViewById(R.id.ready_img);
        drawable= (AnimationDrawable) mImg.getDrawable();
    }
}
