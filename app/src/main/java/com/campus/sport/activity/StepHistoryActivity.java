package com.campus.sport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.adapter.HistoryStepAdapter;
import com.campus.sport.bean.DataHistory;
import com.campus.sport.net.VolleyLo;
import com.campus.sport.net.VolleyTo;
import com.campus.sport.utils.DataKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2019/5/25.
 */

public class StepHistoryActivity extends AppCompatActivity {
    private ImageView iv_left;
    private ListView lv;
    private VolleyTo volleyTo;
    private List<DataHistory>list;
    private HistoryStepAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_history_layout);
        assignViews();
        setVolleyTo();
    }
    private void setVolleyTo(){
        volleyTo=new VolleyTo();
        volleyTo.setUrl("").setJsonObject("", AppClient.getParam(DataKeys.userName,"")).setmDialog(this).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    list.clear();
                    JSONArray array=new JSONArray(jsonObject.getString(""));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object=array.getJSONObject(i);
                        list.add(new DataHistory(object.getString(""),object.getString(""),object.getString("")));
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
    private void assignViews() {
        Random random=new Random();
        iv_left = findViewById(R.id.iv_left);
        lv = findViewById(R.id.lv);
        list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.CHINESE);
            Date date=new Date(System.currentTimeMillis());
            int index=random.nextInt(10000);
            list.add(new DataHistory(format.format(date),index+"",index*0.2+""));
        }
        adapter=new HistoryStepAdapter(this,list);
        lv.setAdapter(adapter);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
