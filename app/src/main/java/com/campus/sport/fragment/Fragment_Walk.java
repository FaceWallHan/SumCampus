package com.campus.sport.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.activity.ReadyActivity;
import com.campus.sport.activity.StepHistoryActivity;
import com.campus.sport.activity.StepPlanActivity;
import com.campus.sport.dialog.CheckDialog;
import com.campus.sport.service.StepService;
import com.campus.sport.utils.DataKeys;
import com.campus.sport.utils.step.StepArcView;
import com.campus.sport.utils.step.UpdateUiCallBack;

import java.text.DecimalFormat;


/**
 * 记步主页
 */
public class Fragment_Walk extends Fragment implements View.OnClickListener {
    private static final String TAG ="Fragment_Walk" ;
    private View view;
    private TextView history;//查看历史步数
    private StepArcView step_arc;
    private TextView plan;//设置锻炼计划
    private TextView jogging;//run
    private TextView distance;//行走距离
    private TextView calories;//消耗卡路里
    private TextView schedule;//任务进度

    private void assignViews() {
        history=view.findViewById(R.id.history);
        step_arc=view.findViewById(R.id.step_arc);
        plan=view.findViewById(R.id.plan);
        distance=view.findViewById(R.id.distance);
        calories=view.findViewById(R.id.calories);
        schedule=view.findViewById(R.id.schedule);
        jogging=view.findViewById(R.id.jogging);
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planWalk_QTY = (String) AppClient.getParam(DataKeys.planWalk, "7000");
                step_arc.setCurrentCount(Integer.parseInt(planWalk_QTY),(int)AppClient.getParam(DataKeys.nowStep,0)+2000);
                Toast.makeText(getContext(), "教师认定学生表现成功", Toast.LENGTH_SHORT).show();
            }
        });
        calories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckDialog dialog=new CheckDialog();
                dialog.show(getChildFragmentManager(),"dialog");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null){
            view=inflater.inflate(R.layout.fragment_walk_layout,container,false);
        }
        ViewGroup parent= (ViewGroup) view.getParent();
        if (parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignViews();
        initData();
        addListener();
    }

    private void addListener() {
        plan.setOnClickListener(this);
        history.setOnClickListener(this);
        jogging.setOnClickListener(this);
    }

    private void initData() {
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) AppClient.getParam(DataKeys.planWalk, "7000");
        //设置当前步数为0
        step_arc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        setupService();
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(getContext(), StepService.class);
        isBind = getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getContext().startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) AppClient.getParam(DataKeys.planWalk, "7000");
            step_arc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());
            AppClient.setParam(DataKeys.nowStep,stepService.getStepCount());
            change(stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) AppClient.getParam(DataKeys.planWalk, "7000");
                    step_arc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                    change(stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 随着步数的改变计算数据
     * @param stepCount
     */
    public void change(int stepCount) {
        String planWalk_QTY = (String) AppClient.getParam(DataKeys.planWalk, "7000");
        int  aims=Integer.valueOf(planWalk_QTY);
        int percentage= stepCount*100/aims;
        if (percentage>=100){
            percentage=100;
        }else if (percentage==0){
            percentage=0;
        }else if (percentage<1){
            percentage=1;
        }
         double extent=1.8* 0.415*stepCount/1000;
        distance.setText(toTwoDecimalPlaces(extent)+"km");
        calories.setText(toTwoDecimalPlaces(60*extent*0.8214)+"K");
        schedule.setText(percentage+"%");
        Log.d("updateUi: ",stepCount+"步");
    }

    private String toTwoDecimalPlaces(double i) {
        //只取小数点后三位
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plan:
                startActivityForResult(new Intent(getContext(), StepPlanActivity.class),1);
                break;
            case R.id.history:
                startActivity(new Intent(getContext(), StepHistoryActivity.class));
                break;
            case R.id.jogging:
                   startActivity(new Intent(getContext(), ReadyActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            getContext().unbindService(conn);
        }
    }

}
