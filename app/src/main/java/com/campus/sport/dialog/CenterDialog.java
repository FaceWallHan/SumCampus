package com.campus.sport.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.campus.sport.R;


/**
 * 自定义dialog
 * Created by hp on 2019/5/23.
 */

public class CenterDialog extends Dialog implements View.OnClickListener {
    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id

    public CenterDialog(Context context, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置为居中
        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        setContentView(layoutResID);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失
        //遍历控件id,添加点击事件
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }
    }

    //点击按钮的监听事件
    @Override
    public void onClick(View view) {
        dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnCenterItemClick(this, view);
    }

    //定义一个接口，让外部类实现
    private OnCenterItemClickListener listener;
    public interface OnCenterItemClickListener {
        void OnCenterItemClick(CenterDialog dialog, View view);
    }
    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }
}
