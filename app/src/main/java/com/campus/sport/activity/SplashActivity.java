package com.campus.sport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.utils.DataKeys;

/**
 * Created by Administrator on 2019/5/22.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        if ((boolean)AppClient.getParam(DataKeys.guide,false)){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this,GuideActivity.class));
            AppClient.setParam(DataKeys.guide,true);
        }
        finish();
    }
}
