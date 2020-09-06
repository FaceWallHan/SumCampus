package com.campus.sport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.dialog.AddressDialog;
import com.campus.sport.net.VolleyLo;
import com.campus.sport.net.VolleyTo;
import com.campus.sport.utils.DataKeys;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText user,word;
    private Button login;
    private TextView join,re;
    private VolleyTo volleyTo;
    private LinearLayout ip_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
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
    private void setVolleyTo( String name, String pass){
        volleyTo=new VolleyTo();
        volleyTo.setUrl("login").setJsonObject("username",name).setJsonObject("password",pass).setmDialog(this).setVolleyLo(new VolleyLo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("RESULT").equals("S")){
                        AppClient.setParam(DataKeys.login,true);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else {
                        showDialog("登录失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog("登录失败");
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showDialog("登录失败");
            }
        }).start();
    }
    private void setListener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=user.getText().toString().trim();
                String w=word.getText().toString().trim();
                if (n.equals("")||w.equals("")){
                    showDialog("用户名或密码不能为空！！！");
                }else {
                    AppClient.setParam(DataKeys.userName,n);
                    setVolleyTo(n,w);
                }
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,JoinActivity.class));
                finish();
            }
        });
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ReturnActivity.class));
                finish();
            }
        });
        ip_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressDialog dialog=new AddressDialog(LoginActivity.this);
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });
    }
    private void inView(){
        user=findViewById(R.id.user);
        word=findViewById(R.id.word);
        login=findViewById(R.id.login);
        join=findViewById(R.id.join);
        re=findViewById(R.id.return_word);
        ip_address=findViewById(R.id.ip_address);
        user.setText(AppClient.getParam(DataKeys.userName,"")+"");
        if ((boolean)AppClient.getParam(DataKeys.login,false)&&!AppClient.getParam(DataKeys.userName,"").equals("")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        String userName= (String) AppClient.getParam(DataKeys.reUserName,"");
        String passWord= (String) AppClient.getParam(DataKeys.rePassWord,"");
        if (!userName.equals("")&&!passWord.equals("")){
            user.setText(userName);
            word.setText(passWord);
        }
    }
}
