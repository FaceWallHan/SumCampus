package com.campus.sport.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.activity.LoginActivity;
import com.campus.sport.activity.MainActivity;
import com.campus.sport.adapter.ShopAdapter;
import com.campus.sport.bean.Shopping;
import com.campus.sport.net.VolleyLo;
import com.campus.sport.net.VolleyTo;
import com.campus.sport.utils.DataKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/21.
 */

public class Fragment_Exchange extends Fragment {

    private ListView mListView;
    private ShopAdapter mAdapter;
    private List<Shopping> mList;
    private VolleyTo volleyTo;
    private List<Integer> Imgages;
    private int mJf=1000;
    private VolleyTo volleyTo2;
    private LinearLayout exchange_layout,main;
    private Button login;
    private TextView other,name_tv;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inView();
        compView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange_layout,container,false);
    }
    private void addData(){
        Imgages.add(R.drawable.shouhuan1);
        Imgages.add(R.drawable.tiaosheng);
        Imgages.add(R.drawable.yujiadian);
        Imgages.add(R.drawable.dianzicheng);
        Imgages.add(R.drawable.biliqi);
        Imgages.add(R.drawable.lali);
        Imgages.add(R.drawable.shuibei);
        mAdapter=new ShopAdapter(getContext(),R.layout.shop_item,mList);
        mAdapter.setData(new ShopAdapter.SetData() {
            @Override
            public void setdata(int index) {
                Log.i("eeeeeeeeeeee","qqqq---"+index);
                final Shopping shopping=mList.get(index);
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("购买提示");
                if (Integer.parseInt(shopping.getPrice())>mJf){
                    builder.setMessage("您的积分不够兑换此物品，请多运动赚取积分");
                    builder.show();
                    return;
                }
                builder.setMessage("确定购买"+shopping.getTitle()+"，这将花费您"+shopping.getPrice()+"积分");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setVolleyTo2(shopping.getTitle());
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        mListView.setAdapter(mAdapter);
        setVolleyTo();
    }
    private void compView(){
        if (AppClient.getParam(DataKeys.userName,"").equals("")){
            exchange_layout.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        }else {
            main.setVisibility(View.GONE);
            addData();
        }
    }
    private void inView(){
        mListView=getView().findViewById(R.id.list);
        mList=new ArrayList<>();
        Imgages=new ArrayList<>();
        exchange_layout=getView().findViewById(R.id.exchange_layout);
        main=getView().findViewById(R.id.main);
        login=getView().findViewById(R.id.login);
        name_tv=getView().findViewById(R.id.name_tv);
        other=getView().findViewById(R.id.other);
        name_tv.setText(AppClient.getParam(DataKeys.userName,"")+"");
        String step=(int)AppClient.getParam(DataKeys.nowStep,0)*0.2+"";
        other.setText(step.substring(0,step.indexOf(".")));
    }
    private void setVolleyTo2(String name){
        volleyTo2=new VolleyTo();
        volleyTo2.setUrl("update_reward").setJsonObject("reward",name).setmDialog(getContext()).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject.has("RESULT")){
                    try {
                        String result=jsonObject.getString("RESULT");
                        if (result.equals("S")){
                            showAlertDialog("提示","购买成功");
                            setVolleyTo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }).start();
    }


    private void showAlertDialog(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定",null);
        builder.show();
    }

    private void setVolleyTo(){
        volleyTo=new VolleyTo();
        volleyTo.setUrl("shop").setmDialog(getContext()).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject.has("RESULT")){
                    try {
                        String result=jsonObject.getString("RESULT");
                        if (result.equals("S")){
                            mList.clear();
                            String body=jsonObject.getString("ROWS_DETAIL");
                            JSONArray jsonArray=new JSONArray(body);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String title=jsonObject1.getString("Reward");
                                String price=jsonObject1.getString("Price");
                                String numbar=jsonObject1.getString("Numbar");
                                String labe=jsonObject1.getString("Introduction");
                                String hint=jsonObject1.getString("Label");
                                mList.add(new Shopping(i+1,Imgages.get(i),title,labe,hint,"包邮",price,"剩余库存"+numbar));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("hhhhhhhhhhhhhh","eeeeeee");
            }
        }).start();
    }
}
