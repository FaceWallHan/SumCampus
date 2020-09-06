package com.campus.sport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.fragment.Fragment_Exchange;
import com.campus.sport.fragment.Fragment_Ranking;
import com.campus.sport.fragment.Fragment_Self;
import com.campus.sport.fragment.Fragment_Walk;
import com.campus.sport.utils.DataKeys;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private BottomNavigationBar bottom_bar;
    private List<Fragment>list;
    private TextView title;
    private String[] titleMain=new String[]{"运动","排名","积分商城","我的"};
    private Fragment currentFragment=new Fragment();
    private int currentIndex = 0;
    private FragmentManager fragmentManager;
    private Fragment_Self self;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inView();
        setListener();
        superviseFragment(savedInstanceState);
    }
    private void  superviseFragment(Bundle savedInstanceState){
        fragmentManager=getSupportFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT,0);
            list.clear();
            list.add(fragmentManager.findFragmentByTag(0+""));
            list.add(fragmentManager.findFragmentByTag(1+""));
            list.add(fragmentManager.findFragmentByTag(2+""));
            //恢复fragment页面
            restoreFragment();
        }else{      //正常启动时调用
            list.add(new Fragment_Walk());
            list.add(new Fragment_Ranking());
            list.add(new Fragment_Exchange());
            list.add(self);
            showFragment();
        }
    }
    private void inView(){
        bottom_bar=findViewById(R.id.bottom_bar);
        list=new ArrayList<>();
        title=findViewById(R.id.title);
        title.setText(titleMain[0]);
        self=new Fragment_Self();
        self.setCloseActivity(new Fragment_Self.closeActivity() {
            @Override
            public void change() {
                AppClient.setParam(DataKeys.reUserName,"");
                AppClient.setParam(DataKeys.reUserName,"");
                AppClient.setParam(DataKeys.userName,"");
                AppClient.setParam(DataKeys.login,false);
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void setListener(){
        bottom_bar.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottom_bar.addItem(new BottomNavigationItem(R.drawable.lv01,titleMain[0]).setActiveColor(Color.RED))
                  .addItem(new BottomNavigationItem(R.drawable.lv03,titleMain[1]).setActiveColor(Color.BLUE))
                  .addItem(new BottomNavigationItem(R.drawable.lv04,titleMain[2]).setActiveColor(Color.parseColor("#F200FF")))
                  .addItem(new BottomNavigationItem(R.drawable.lv05,titleMain[3]).setActiveColor(Color.parseColor("#00FFFB")))
                  .setFirstSelectedPosition(0)
                  .initialise();
        bottom_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                title.setText(titleMain[position]);
                currentIndex=position;
                showFragment();
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }
    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if(!list.get(currentIndex).isAdded()){
            transaction
                    .hide(currentFragment)
                    .add(R.id.fragment,list.get(currentIndex),""+currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

        }else{
            transaction
                    .hide(currentFragment)
                    .show(list.get(currentIndex));
        }
        currentFragment = list.get(currentIndex);
        transaction.commit();

    }
    /**
     * 恢复fragment
     */
    private void restoreFragment(){
        FragmentTransaction mBeginTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            if(i == currentIndex){
                mBeginTransaction.show(list.get(i));
            }else{
                mBeginTransaction.hide(list.get(i));
            }
        }
        mBeginTransaction.commit();
        //把当前显示的fragment记录下来
        currentFragment = list.get(currentIndex);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT,currentIndex);
        super.onSaveInstanceState(outState);
    }
}
