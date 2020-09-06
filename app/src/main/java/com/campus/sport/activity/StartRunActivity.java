package com.campus.sport.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.sql.MySQLites;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2019/5/27.
 */

public class StartRunActivity extends AppCompatActivity  implements LocationSource, AMapLocationListener {
    private AMap aMap;
    private TextureMapView mapView;
    private UiSettings uiSettings;
    //以前的定位点
    private LatLng oldLatLng;
    //是否是第一次定位
    private boolean isFirstLatLng;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    //经纬度
    private double latitude = 0;
    private double longitude = 0;

    //显示数值
    private TextView lichen_tv;
    private Uri imageUri;
    private TextView time_tv;
    private Button pause_tv;

    private boolean isExercise = false;//是否在运动

    private List<String> Paths;

    private AppClient mApp;
    private long time = 0;
    private double mileage = 0;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time += 1000;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));//格林威治标准时间
            String shi = simpleDateFormat.format(time);
            Examine(shi);
            time_tv.setText(shi);
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_run);
        inView(savedInstanceState);
    }
    private void inView(Bundle savedInstanceState){
        //保持屏幕常亮
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mApp= (AppClient) getApplication();
        Paths=mApp.getPaths();
        Paths.clear();
        mapView = findViewById(R.id.map);
        lichen_tv = findViewById(R.id.lichengtv);
        time_tv = findViewById(R.id.shijiantv);
        pause_tv = findViewById(R.id.zantingtv);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        isFirstLatLng = true;
        boolean isOpenGps = initGPS();
        if (isOpenGps) {
            init();
        }
        Start();
        pause_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("qqqqqqqqqqqqqqq","ck");
                isExercise = false;
                handler.removeCallbacks(runnable);
                startActivity(new Intent(StartRunActivity.this,DisplayImageActivity.class));
                finish();
            }
        });
    }
    private boolean initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则跳转， 至设置开启界面，设置完毕后返回到首页
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("提示");
            dialog.setMessage("为了更好的为您服务，请您打开GPS!");
            dialog.setCancelable(false);
            //界面上左边按钮，及其监听
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 1); // 设置完成后返回到原来的界面

                        }
                    });
            //界面上右边按钮，及其监听
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(StartRunActivity.this, "您没有授权GPS", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
            return false;
        } else {
            return true;
        }
    }

    private void Start(){
        isExercise = true;
        handler.postDelayed(runnable, 1000);//启动计时线程
    }
    //打开GPS后定位
    private void Examine(String sj){
        switch (sj){
            case "00:00:50":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            case "00:00:30":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            case "00:00:10":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            case "00:13:00":
                showAlertDialog("提示", "两分钟后将进行防作弊拍照",null);
                break;
            case "00:14:00":
                showAlertDialog("提示", "一分钟后将进行防作弊拍照", null);
                break;
            case "00:15:00":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            case "00:28:00":
                showAlertDialog("提示", "两分钟后将进行防作弊拍照",null);
                break;
            case "00:29:00":
                showAlertDialog("提示", "一分钟后将进行防作弊拍照", null);
                break;
            case "00:30:00":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            case "00:43:00":
                showAlertDialog("提示", "两分钟后将进行防作弊拍照",null);
                break;
            case "00:44:00":
                showAlertDialog("提示", "一分钟后将进行防作弊拍照", null);
                break;
            case "00:45:00":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            case "00:58:00":
                showAlertDialog("提示", "两分钟后将进行防作弊拍照",null);
                break;
            case "00:59:00":
                showAlertDialog("提示", "一分钟后将进行防作弊拍照", null);
                break;
            case "01:00:00":
                showAlertDialog("提示", "请在两分钟内进行防作弊拍照，否则视为作弊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickCamera();
                    }
                });
                break;
            default:
                break;
        }
    }
    private void showAlertDialog(String title,String message,DialogInterface.OnClickListener ck){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", ck);
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void init() {
        Log.i("qqqqqqqqqqqqqqq","init");
        if (aMap == null) {
            aMap = mapView.getMap();
        } else {
            aMap.clear();
            aMap = mapView.getMap();
        }
        //定位
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setScaleControlsEnabled(true);//显示刻度尺,比例
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位 LOCATION_TYPE_LOCATE、跟随 LOCATION_TYPE_MAP_FOLLOW 或地图根据面向方向旋转 LOCATION_TYPE_MAP_ROTATE
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        aMap.setMapType(1);//设置地图的样式

        //画线
        // 缩放级别（zoom）：地图缩放级别范围为【4-20级】，值越大地图越详细
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置圆形(范围)边距的颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        //设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);
        uiSettings=aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
    }

    /**
     * 绘制两个坐标点之间的线段,从以前位置到现在位置
     */
    private void setUpMap(LatLng oldData, LatLng newData) {
        /**
         * 1、折线(Polyline)：折线的关键类为 Polyline，在地图上定义了一组相连
         的线段。 Polyline 对象由一组经纬度坐标组成，并以有序序列形式建立一系列
         的线段，通过aMap.addPolyline(PolylineOptions options)来添加到地图上，
         PolylineOptions的属性有：
         • add(LatLng.. point) 添加折线的顶点，可以同时添加多个。
         • width 设置线的宽度。
         • color 设置线的颜色。
         • visible 设置“false ” ，线不可见。
         • setDottedLine(true) 设置线为虚线。
         • setCustomTexture() 可以给线段添加自定义纹理。
         • geodesic(true)设置线段是否为大地曲线线段。
         */
        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData).width(10)//轨迹宽度
                .geodesic(true)
                .color(Color.argb(100, 0, 255, 0)));//轨迹颜色
    }
    public void clickCamera(){
        //创建File对象，用于存储拍照后的图片
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        Date date=new Date(System.currentTimeMillis());
        File outputImage=new File(getExternalCacheDir(),format.format(date)+".jpg");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(this,"com.campus.sport.fileProvider",
                    outputImage);
        }else {
            imageUri= Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,3);
    }
    private String toTwoDecimalPlaces(double i) {
        //只取小数点后三位
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format(i);
    }
    private void addMarker(double la, double lo, String title, String snippet, boolean drag, int icon) {
        aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 1)//图标摆放在地图上的基准点。默认情况下，锚点是从图片下沿的中间处。
                .position(new LatLng(la, lo))//标记的坐标 位置
                .title(title)//点击坐标后的title
                .snippet(snippet)//点击坐标后的摘要
                .draggable(drag)//不可拖动
                //.perspective(true)//标记有近大远小效果
                .icon(BitmapDescriptorFactory.fromResource(icon))//自定义标记
        );
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
        handler.removeCallbacks(runnable);
        deactivate();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //定位成功
                latitude = amapLocation.getLatitude();//获取纬度
                longitude = amapLocation.getLongitude();//获取经度
                LatLng newLatLng = new LatLng(latitude, longitude);//实例化坐标
                if (isFirstLatLng) {
                    //记录第一次的定位信息
                    oldLatLng = newLatLng;
                    isFirstLatLng = false;
                    //添加标记
                    addMarker(37.46054, 116.3573, "1", "操场南", false, R.drawable.biao45);
                    addMarker(37.461352, 116.3578, "2", "操场东", false, R.drawable.biao45);
                    addMarker(37.46198, 116.3573, "3", "操场北", false, R.drawable.biao45);
                    addMarker(37.461233, 116.356915, "4", "操场西", false, R.drawable.biao45);
                }
                //位置有变化
                if (oldLatLng != newLatLng) {
                    Log.e("位置变化Amap", amapLocation.getLatitude() + "," + amapLocation.getLongitude());
                    //计算运动的距离
                    float dis = AMapUtils.calculateLineDistance(oldLatLng, newLatLng);
                    if (dis >= 20) {
                        //运动速度过快,给出提示
                        Log.d("onLocationChan  ged: ", "GPS信号较弱请在空旷处运动");
                    } else {
                        if (dis != 0) {
                            if (isExercise) {//判断是否开始跑步
                                setUpMap(oldLatLng, newLatLng);
                                mileage += dis;
                                String mileageStr = toTwoDecimalPlaces(mileage);
                                Log.d("onLocationChanged: ", mileageStr + "这是里程");
                                if (mileageStr.length()>=6) {
                                    lichen_tv.setText(toTwoDecimalPlaces(mileage/1000)+"km");
                                }else {
                                    lichen_tv.setText(mileageStr + "m");
                                }
                            }
                        }
                    }
                    oldLatLng = newLatLng;
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("arrText:", errText);
//                Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
                if (isFirstLatLng) {
                    Toast.makeText(this, "定位失败:请检查GPS及网络", Toast.LENGTH_LONG).show();
                    isFirstLatLng = false;
                }
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(
                    AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(false);
            /**
             * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
             * 注意：只有在高精度模式下的单次定位有效，其他方式无效
             */
            mLocationOption.setGpsFirst(true);
            // 设置发送定位请求的时间间隔,最小值为1000ms,1秒更新一次定位信息
            mLocationOption.setInterval(1000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            init();
        }
        if (requestCode==3){
            if (resultCode==RESULT_OK){
                Paths.add(imageUri+"");
                Toast.makeText(StartRunActivity.this,"防作弊检测成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showAlertDialog("提示", "是否停止此次运动？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isExercise = false;
                handler.removeCallbacks(runnable);
                startActivity(new Intent(StartRunActivity.this,DisplayImageActivity.class));
                finish();
            }
        });
    }
}
