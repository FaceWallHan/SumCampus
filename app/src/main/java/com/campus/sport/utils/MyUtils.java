package com.campus.sport.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.campus.sport.AppClient;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/23.
 */

public class MyUtils {
    private static List<Activity> list=new ArrayList<>();
    public static void addActivity(Activity activity){
        list.add(activity);
    }
    public static void clearActivity(){
        for (int i = 0; i < list.size(); i++) {
            Activity activity=list.get(i);
            if (activity.isFinishing()){
                activity.finish();
            }
        }
    }
    public static String getLocalIpAddress(Context context){
        String IP="";
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();////已连接wifi信息
            int ipAddress = wifiInfo.getIpAddress();
            IP = intToIp(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IP;
    }
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }
    public static Bitmap getImageAddress(Context context){
        Bitmap bitmap = null;
        Uri uri=null;
        String imgPath= (String) AppClient.getParam(DataKeys.imagePath,"");//相册路径
        String imgUri=(String) AppClient.getParam(DataKeys.imageUrl,""); //拍照路径
        if (!imgPath.equals("")){
            bitmap= BitmapFactory.decodeFile(imgPath);
        }
        if (!imgUri.equals("")){
            uri= Uri.parse(imgUri);
            try {
                bitmap= BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    public static boolean compIpAddress(String text){
        boolean is=false;
        String[] ip=text.split("\\.");
        if (ip.length==4){
            if (ip[0].equals("0")||Integer.parseInt(ip[0])<=0||Integer.parseInt(ip[0])>255){
                is=false;
            }else {
                for (int i = 1; i < ip.length; i++) {
                    if (Integer.parseInt(ip[i])<=0||Integer.parseInt(ip[i])>255){
                        is=false;
                        break;
                    }else {
                        is=true;
                    }
                }
            }
        }else {
            is=false;
        }
        return is;
    }
}
