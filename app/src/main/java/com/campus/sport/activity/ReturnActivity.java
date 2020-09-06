package com.campus.sport.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.android.volley.VolleyError;
import com.campus.sport.R;
import com.campus.sport.net.VolleyLo;
import com.campus.sport.net.VolleyTo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019/3/30.
 */

public class ReturnActivity extends AppCompatActivity {
    private EditText user,email,re_tel;
    private Button submit;
    private ImageView back;
    private VolleyTo volleyTo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.return_layout);
        inView();
        setListener();
    }
    private void showDialog(String name){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setPositiveButton("确定",null);
        builder.setTitle("");
        builder.setMessage(name);
        builder.setCancelable(false);
        builder.show();
    }
    private void setVolleyTo(String userName,String email,String rel){
        volleyTo=new VolleyTo();
        volleyTo.setUrl("").setJsonObject("",userName).setJsonObject("",rel).setJsonObject("",email).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("RESULT").equals("S")){
                        showDialog("找回成功！\n原密码为："+jsonObject.getString(""));
                    }else {
                        showDialog("找回失败，请检查输入是否正确");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog("找回失败，请检查输入是否正确");
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showDialog("找回失败，请检查输入是否正确");
            }
        }).start();
    }
    private void setListener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u=user.getText().toString().trim();
                String em=email.getText().toString().trim();
                String rel=re_tel.getText().toString().trim();
                if (u.equals("")){
                    showDialog("用户名不能为空！！！");
                    return;
                }
                if (em.equals("")){
                    showDialog("邮箱不能为空！！！");
                    return;
                }
                if (!Pattern.compile("(.*[A-Za-z].*){4,}").matcher(u).matches()){
                    showDialog("用户名（不少于4位字母）");
                    return;
                }
                if (!Pattern.compile("(.*[0-9].*){4,}").matcher(em).matches()){
                    showDialog("邮箱（不少于6位数字）");
                    return;
                }
                if (!Pattern.compile("^1[0-9]{10}$").matcher(rel).matches()){
                    showDialog("手机号第一位是1且为11位！");
                    return;
                }
                setVolleyTo(u,em,rel);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReturnActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void inView(){
        back=findViewById(R.id.back);
        user=findViewById(R.id.user);
        email=findViewById(R.id.email);
        submit=findViewById(R.id.submit);
        re_tel=findViewById(R.id.re_tel);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReturnActivity.this,LoginActivity.class));
        finish();
    }
}
