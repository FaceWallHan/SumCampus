package com.campus.sport.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.net.VolleyLo;
import com.campus.sport.net.VolleyTo;
import com.campus.sport.utils.DataKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019/3/30.
 */

public class JoinActivity extends AppCompatActivity {
    private EditText user,word,reWord,email,tel;
    private Button submit;
    private ImageView back;
    private VolleyTo volleyTo;
    private Spinner sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);
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
    private void setVolleyTo(final String userName, String email, String rel, final String passWord){
        volleyTo=new VolleyTo();
        volleyTo.setUrl("").setJsonObject("",userName).setJsonObject("",email).setJsonObject("",rel).setJsonObject("",passWord).setJsonObject("",sex.getSelectedItem().toString()).setmDialog(this).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("RESULT").equals("S")){
                        showDialog(userName,passWord);
                    }else {
                        showDialog("注册失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog("注册失败");
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showDialog("注册失败");
            }
        }).start();
    }
    private void showDialog(final String name, final String password){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setPositiveButton("确定",null);
        builder.setTitle("提示");
        builder.setMessage("注册成功！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppClient.setParam(DataKeys.reUserName,name);
                AppClient.setParam(DataKeys.rePassWord,password);
                startActivity(new Intent(JoinActivity.this,LoginActivity.class));
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    private void setListener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u=user.getText().toString().trim();
                String w=word.getText().toString().trim();
                String rw=reWord.getText().toString().trim();
                String em=email.getText().toString().trim();
                String te=tel.getText().toString().trim();
                if (u.equals("")){
                    showDialog("用户名不能为空！！！");
                    return;
                }
                if (w.equals("")){
                    showDialog("密码不能为空！！！");
                    return;
                }
                if (rw.equals("")){
                    showDialog("确认密码不能为空！！！");
                    return;
                }
                if (em.equals("")){
                    showDialog("邮箱不能为空！！！");
                    return;
                }
                if (te.equals("")){
                    showDialog("手机号不能为空！！！");
                    return;
                }
                if (!Pattern.compile("(.*[A-Za-z].*){4,}").matcher(u).matches()){
                    showDialog("用户名（不少于4位字母）");
                    return;
                }
                if (!Pattern.compile("(.*[0-9].*){4,}").matcher(w).matches()){
                    showDialog("用户密码（不少于6位数字）");
                    return;
                }
                if (!Pattern.compile("(.*[0-9].*){4,}").matcher(rw).matches()){
                    showDialog("确认密码密码（不少于6位数字）");
                    return;
                }
                if (!Pattern.compile("(.*[0-9].*){4,}").matcher(em).matches()){
                    showDialog("邮箱（不少于6位数字）");
                    return;
                }
                if (!Pattern.compile("^1[0-9]{10}$").matcher(te).matches()){
                    showDialog("手机号第一位是1且为11位！");
                    return;
                }
                if (!rw.equals(w)){
                    showDialog("两次密码不一致");
                    return;
                }
                setVolleyTo(u,em,te,w);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JoinActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void inView(){
        back=findViewById(R.id.back);
        user=findViewById(R.id.user);
        word=findViewById(R.id.word);
        reWord=findViewById(R.id.reWord);
        email=findViewById(R.id.email);
        submit=findViewById(R.id.submit);
        tel=findViewById(R.id.tel);
        sex=findViewById(R.id.sex);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JoinActivity.this,LoginActivity.class));
        finish();
    }
}
