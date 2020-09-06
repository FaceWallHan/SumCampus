package com.campus.sport.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.GridView;


import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.adapter.DiaplayAdapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2019/5/28.
 */

public class DisplayImageActivity extends AppCompatActivity {

    private GridView gridView;
    private List<String> mList;
    private List<Bitmap> bitmaps;
    private DiaplayAdapter mAdapter;
    private AppClient mApp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_layout);
        mApp= (AppClient) getApplication();
        initView();
        initData();
    }

    private void initData() {
        mList=mApp.getPaths();
        bitmaps=new ArrayList<>();
        for (int i=0;i<mList.size();i++){
            bitmaps.add(getPathImage(mList.get(i)));
        }
        Log.i("eeeeeeeeee","aaa---"+mList.size());
        mAdapter=new DiaplayAdapter(this,R.layout.display_item,bitmaps);
        gridView.setAdapter(mAdapter);
    }

    private void initView() {
        gridView=findViewById(R.id.display_grid);
    }

    private Bitmap getPathImage(String path){
        Bitmap bitmap=null;
        Uri uri=null;
        if (!path.equals("")){
            uri= Uri.parse(path);
            try {
                bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
