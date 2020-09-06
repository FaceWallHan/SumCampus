package com.campus.sport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.campus.sport.R;
import com.campus.sport.adapter.ViewPagerAdapter;
import com.campus.sport.fragment.Fragment_Guide_First;
import com.campus.sport.fragment.Fragment_Guide_Fourth;
import com.campus.sport.fragment.Fragment_Guide_Second;
import com.campus.sport.fragment.Fragment_Guide_Third;
import com.campus.sport.utils.ScalePageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/19.
 */

public class GuideActivity extends AppCompatActivity {
    private ViewPager pager;
    private LinearLayout photo;
    private List<Fragment>list;
    private Fragment_Guide_Fourth fourth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        inView();
        addData();
        showPoint();
    }
    private void showPoint(){
        photo.removeAllViews();
        for (int i = 0; i <list.size(); i++) {
            ImageView view= new ImageView(this);
            if (0==i){
                view.setBackgroundResource(R.drawable.ic_pager_black);
            }else {
                view.setBackgroundResource(R.drawable.ic_pager_white);
            }
            view.setLayoutParams(new LinearLayout.LayoutParams(20,20, Gravity.CENTER));
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(new ViewGroup.LayoutParams(20,20));
            params.rightMargin=10;
            params.leftMargin=10;
            photo.addView(view,params);
        }
    }
    private void select(int position){
        for (int i = 0; i < photo.getChildCount(); i++) {
            ImageView view= (ImageView) photo.getChildAt(i);
            if (position==i){
                view.setBackgroundResource(R.drawable.ic_pager_black);
            }else {
                view.setBackgroundResource(R.drawable.ic_pager_white);
            }
        }
    }
    private void addData(){
        list.add(new Fragment_Guide_First());
        list.add(new Fragment_Guide_Second());
        list.add(new Fragment_Guide_Third());
        list.add(fourth);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),list));
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(list.size());
        pager.setPageTransformer(true,new ScalePageTransformer());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void inView(){
        pager=findViewById(R.id.pager);
        photo=findViewById(R.id.photo);
        list=new ArrayList<>();
        fourth=new Fragment_Guide_Fourth();
        fourth.setChangeActivity(new Fragment_Guide_Fourth.FragmentChangeActivity() {
            @Override
            public void change() {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}
