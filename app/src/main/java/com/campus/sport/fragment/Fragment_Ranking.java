package com.campus.sport.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.campus.sport.adapter.RankingAdapter;
import com.campus.sport.bean.DataRanking;
import com.campus.sport.net.VolleyLo;
import com.campus.sport.net.VolleyTo;
import com.campus.sport.utils.DataKeys;
import com.campus.sport.utils.MyUtils;
import com.campus.sport.utils.ShapeImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2019/5/21.
 */

public class Fragment_Ranking extends Fragment {
    private ListView ranking;
    private ShapeImageView ranking_head;
    private TextView ranking_name,step_count,integral;
    private View view;
    private VolleyTo volleyTo;
    private RankingAdapter adapter;
    private List<DataRanking>list;
    private LinearLayout main,ranking_layout;
    private Button login;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null){
            view=inflater.inflate(R.layout.fragment_ranking_layout,container,false);
        }
        ViewGroup parent= (ViewGroup) view.getParent();
        if (parent!=null){
            parent.removeView(view);
        }
        Log.i("Fragment_Ranking", "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Fragment_Ranking", "onActivityCreated: ");
        inView();
//        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
//        builder.setTitle("提示");
//        builder.setMessage("恭喜获得冠军(额外1000积分奖励)");
//        builder.setPositiveButton("确定",null);
//        builder.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }
    private void setVolleyTo(){
        volleyTo=new VolleyTo();
        volleyTo.setUrl("get_step_count").setmDialog(getContext()).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    list.clear();
                    JSONArray array=new JSONArray(jsonObject.getString("ROWS_DETAIL"));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object=array.getJSONObject(i);
                        int integral= (int) (object.getInt("Stepcount")*0.2);
                        list.add(new DataRanking(i+1,object.getString("UserName"),object.getInt("Stepcount"),integral+""));
                    }
                    Collections.sort(list,new DataRanking.stepCountAsc());
                    for (int i = 0; i < list.size(); i++) {
                        DataRanking ranking=list.get(i);
                        ranking.setIndex(i+1);
                        list.set(i,ranking);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }).start();
    }
    private void getMuchResource(){
        ranking_name.setText(AppClient.getParam(DataKeys.userName,"未确认生命体").toString());
        ranking_head.setImageBitmap(MyUtils.getImageAddress(getContext()));
    }
    private void addData(){
        Random random=new Random();
        for (int i = 0; i <10; i++) {
            int step=random.nextInt(10000);
            int integral= (int) (step*0.2);
            list.add(new DataRanking(i+1,random.nextInt(10)+"名字",step,integral+""));
        }
        Collections.sort(list,new DataRanking.stepCountAsc());
        for (int i = 0; i < list.size(); i++) {
            DataRanking ranking=list.get(i);
            ranking.setIndex(i+1);
            list.set(i,ranking);
        }
        adapter.notifyDataSetChanged();
    }
    private void inView(){
        main=view.findViewById(R.id.main);
        ranking_layout=view.findViewById(R.id.ranking_layout);
        login=view.findViewById(R.id.login);
        ranking=view.findViewById(R.id.ranking);
        ranking_head=view.findViewById(R.id.ranking_head);
        ranking_name=view.findViewById(R.id.ranking_name);
        step_count=view.findViewById(R.id.step_count);
        integral=view.findViewById(R.id.integral);
        list=new ArrayList<>();
        adapter=new RankingAdapter(getContext(),list);
        ranking.setAdapter(adapter);
        step_count.setText(AppClient.getParam(DataKeys.nowStep,0)+"");
        String step=(int)AppClient.getParam(DataKeys.nowStep,0)*0.2+"";
        integral.setText(step.substring(0,step.indexOf(".")));
        if (AppClient.getParam(DataKeys.userName,"").equals("")){
            ranking_layout.setVisibility(View.GONE);
        }else {
            main.setVisibility(View.GONE);
            getMuchResource();
            addData();
            setVolleyTo();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            Log.i("Fragment_Ranking", "onHiddenChanged: ");
            step_count.setText(AppClient.getParam(DataKeys.nowStep,0)+"");
            String step=(int)AppClient.getParam(DataKeys.nowStep,0)*0.2+"";
            integral.setText(step.substring(0,step.indexOf(".")));
        }else {
            step_count.setText(AppClient.getParam(DataKeys.nowStep,0)+"");
            String step=(int)AppClient.getParam(DataKeys.nowStep,0)*0.2+"";
            integral.setText(step.substring(0,step.indexOf(".")));
        }

    }
}
