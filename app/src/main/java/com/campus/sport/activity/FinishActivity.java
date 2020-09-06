package com.campus.sport.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.sport.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2019/5/25 0025.
 */

public class FinishActivity extends AppCompatActivity {

    private TextView mLc;
    private TextView mSj;
    private GridView mGridView;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_layout);
        final ImageView imageView=findViewById(R.id.finsh_img);
        TextView sj=findViewById(R.id.finsh_sj);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_camera();
            }
        });
        sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap=getImage(path);
                imageView.setImageBitmap(bitmap);
            }
        });

    }

    private Bitmap getImage(String path){
        File file = new File(path);
        if(file.exists()){
            Bitmap bm = BitmapFactory.decodeFile(path);
            return bm;
        }
        return null;
    }

    public void click_camera(){
        Log.i("bbbbbb","ccccccc");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        Date date=new Date(System.currentTimeMillis());
        Intent intent=new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        path="/sdcard/"+simpleDateFormat.format(date)+".jpg";
        File file=new File(path);
        Uri uri=Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivity(intent);
    }
}
