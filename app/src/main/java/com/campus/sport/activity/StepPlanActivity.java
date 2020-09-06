package com.campus.sport.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.utils.DataKeys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2019/5/25.
 */

public class StepPlanActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_left;
    private EditText tv_step_number;
    private Switch cb_remind;
    private TextView tv_remind_time;
    private Button btn_save;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_plan_layout);
        assignViews();
        initData();
        addListener();
    }
    private void assignViews() {
        iv_left = findViewById(R.id.iv_left);
        tv_step_number = findViewById(R.id.tv_step_number);
        cb_remind = findViewById(R.id.cb_remind);
        tv_remind_time = findViewById(R.id.tv_remind_time);
        btn_save = findViewById(R.id.btn_save);
    }
    public void initData() {//获取锻炼计划
        String planWalk_QTY = (String) AppClient.getParam(DataKeys.planWalk, "7000");
        String achieveTime = (String) AppClient.getParam(DataKeys.achieveTime, "20:00");
        if (!planWalk_QTY.isEmpty()) {
            if ("0".equals(planWalk_QTY)) {
                tv_step_number.setText("7000");
            } else {
                tv_step_number.setText(planWalk_QTY);
            }
        }
        cb_remind.setChecked((boolean)AppClient.getParam(DataKeys.remind, true));
        if (!achieveTime.isEmpty()) {
            tv_remind_time.setText(achieveTime);
        }
    }
    public void addListener() {
        iv_left.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_remind_time.setOnClickListener(this);
    }
    private void save() {
        String walk_qty = tv_step_number.getText().toString().trim();
        String achieveTime = tv_remind_time.getText().toString().trim();
        if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
            AppClient.setParam(DataKeys.planWalk, "7000");
        } else {
            AppClient.setParam(DataKeys.planWalk, walk_qty);
        }
        AppClient.setParam(DataKeys.remind, cb_remind.isChecked());
        if (achieveTime.isEmpty()) {
            AppClient.setParam(DataKeys.achieveTime, "21:00");
        } else {
            AppClient.setParam(DataKeys.achieveTime, achieveTime);
        }
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void showTimeDialog() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        //获取当前的时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final DateFormat df = new SimpleDateFormat("HH:mm",Locale.CHINESE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String remainTime = calendar.get(Calendar.HOUR_OF_DAY) +
                        ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remainTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (null != date) {
                    calendar.setTime(date);
                }
                tv_remind_time.setText(df.format(date));
            }
        }, hour, minute, true).show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.tv_remind_time:
                showTimeDialog();
                break;
        }
    }
}
