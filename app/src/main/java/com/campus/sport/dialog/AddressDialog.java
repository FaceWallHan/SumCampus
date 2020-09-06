package com.campus.sport.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.utils.DataKeys;
import com.campus.sport.utils.MyUtils;

/**
 * Created by Administrator on 2019/5/23.
 */

@SuppressLint("ValidFragment")
public class AddressDialog extends DialogFragment {
    private EditText mIp1ET;
    private EditText mIp2ET;
    private EditText mIp3ET;
    private EditText mIp4ET;
    private Button mCloseBtn;
    private Button mOkBtn;
    private Activity mActivity;
    public AddressDialog(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setValue();
        setListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        params.windowAnimations = R.style.bottomSheet_animation;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.set_ip_dialog, container, false);
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mIp1ET = (EditText) getView().findViewById(R.id.ip1_edit_text);
        mIp2ET = (EditText) getView().findViewById(R.id.ip2_edit_text);
        mIp3ET = (EditText) getView().findViewById(R.id.ip3_edit_text);
        mIp4ET = (EditText) getView().findViewById(R.id.ip4_edit_text);
        mCloseBtn = (Button) getView().findViewById(R.id.close);
        mOkBtn = (Button) getView().findViewById(R.id.ok_button);
    }
    /**
     * 显示Ip地址
     */
    private void setValue() {
        String ipStr[] = AppClient.getParam(DataKeys.ipAddress,"").toString().split("\\.");
        if (ipStr.length == 4) {
            mIp1ET.setText(ipStr[0]);
            mIp2ET.setText(ipStr[1]);
            mIp3ET.setText(ipStr[2]);
            mIp4ET.setText(ipStr[3]);
        }else if (ipStr.length==0&&!MyUtils.getLocalIpAddress(mActivity).equals("")){
            ipStr = MyUtils.getLocalIpAddress(mActivity).split("\\.");
            mIp1ET.setText(ipStr[0]);
            mIp2ET.setText(ipStr[1]);
            mIp3ET.setText(ipStr[2]);
            mIp4ET.setText(ipStr[3]);
        }
    }
    /**
     * 设置监听器
     *
     */
    private void setListener() {
        mCloseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddressDialog.this.dismiss();
            }
        });
        mOkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setIP();
            }
        });
    }
    /**
     * 对用户输入的Ip进行转化、校验和保存
     *
     */
    private void setIP() {
        try {
            // 获取Ip地址
            int ip1, ip2, ip3, ip4;
            ip1 = Integer.parseInt(mIp1ET.getText().toString().trim());
            ip2 = Integer.parseInt(mIp2ET.getText().toString().trim());
            ip3 = Integer.parseInt(mIp3ET.getText().toString().trim());
            ip4 = Integer.parseInt(mIp4ET.getText().toString().trim());

            // 校验Ip地址
            String ipStr = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
            if ( "".equals(ipStr)) {
                Toast.makeText(mActivity,"请输入IP地址",Toast.LENGTH_SHORT).show();
            } else {
                if (!MyUtils.compIpAddress(ipStr)) {
                    Toast.makeText(mActivity,"无效的IP地址",Toast.LENGTH_SHORT).show();
                }else {
                    AppClient.setParam(DataKeys.ipAddress,ipStr);
                    Toast.makeText(mActivity,"IP地址设置成功",Toast.LENGTH_SHORT).show();
                    AddressDialog.this.dismiss();
                }
            }
        } catch (Exception e) {
            Toast.makeText(mActivity,"无效的IP地址",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(700, 500);
    }
}
