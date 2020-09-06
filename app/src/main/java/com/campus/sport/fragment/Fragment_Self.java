package com.campus.sport.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campus.sport.AppClient;
import com.campus.sport.R;
import com.campus.sport.activity.LoginActivity;
import com.campus.sport.dialog.AddressDialog;
import com.campus.sport.dialog.CenterDialog;
import com.campus.sport.utils.DataKeys;
import com.campus.sport.utils.MyUtils;
import com.campus.sport.utils.ShapeImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2019/5/20.
 */

public class Fragment_Self extends Fragment {
    private View view;
    private static final int CHOOSE_PHOTO=2;//打开相册返回标记
    private ShapeImageView head;
    private CenterDialog dialog;
    private Uri imageUri=null;//保存头像的地址
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private LinearLayout address;
    private TextView exit;
    private TextView name;
    private closeActivity closeActivity;
    private LinearLayout self_layout,main;
    private Button login;

    public void setCloseActivity(Fragment_Self.closeActivity closeActivity) {
        this.closeActivity = closeActivity;
    }

    public interface closeActivity{
        void change();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null){
            view=inflater.inflate(R.layout.fragment_self_layout,container,false);
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
        inView();
        compView();
    }
    private void compView(){
        if (AppClient.getParam(DataKeys.userName,"").equals("")){
            self_layout.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        }else {
            main.setVisibility(View.GONE);
            setListener();
        }
    }
    private void inView(){
        head=view.findViewById(R.id.head);
        name=view.findViewById(R.id.name);
        address=view.findViewById(R.id.address);
        exit=view.findViewById(R.id.exit);
        dialog=new CenterDialog(getContext(), R.layout.dialog_layout, new int[]{R.id.photo_choose, R.id.photograph});
        name.setText((String)AppClient.getParam(DataKeys.userName,"用户名"));
        if (MyUtils.getImageAddress(getContext())!=null){
            head.setImageBitmap(MyUtils.getImageAddress(getContext()));
        }
        self_layout=view.findViewById(R.id.self_layout);
        main=view.findViewById(R.id.main);
        login=view.findViewById(R.id.login);
    }

    private void setListener(){
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressDialog dialog=new AddressDialog(getActivity());
                dialog.show(getChildFragmentManager(),"");
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("确认退出当前此账户吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeActivity.change();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.setCancelable(false);
                builder.show();
            }
        });
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        dialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.photo_choose:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            // 检查该权限是否已经获取
                            int i = ContextCompat.checkSelfPermission(getContext(), permissions[0]);
                            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                            if (i != PackageManager.PERMISSION_GRANTED) {
                                // 如果没有授予该权限，就去提示用户请求
                                showDialogTipUserRequestPermission();
                            }else {
                                openAlbum();
                            }
                        }

                        break;
                    case R.id.photograph:
                        setCamera();
                        break;
                }
            }
        });
    }
    //打开相册
    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 3://相机
                if (resultCode==RESULT_OK){
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        head.setImageBitmap(bitmap);
                        AppClient.setParam(DataKeys.imageUrl, String.valueOf(imageUri));
                        AppClient.setParam(DataKeys.imagePath,"");
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 检查该权限是否已经获取
                        int i = ContextCompat.checkSelfPermission(getContext(), permissions[0]);
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            // 如果没有授予该权限，就去提示用户请求
                            showDialogTipUserRequestPermission();
                        }
                    }
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT>=19){
                        //4.4系统及以上使用这个处理图片
                        handleImageOnKitkat(data);
                    }else{
                        //4.4系统及以下使用这个方法处理图片
                        handleImageBeforeKitkat(data);
                    }
                }
                break;
            case 123:
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(getContext(), permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 提示用户应该去应用设置界面手动开启权限
                        //showDialogTipUserGoToAppSetting();
                        ActivityCompat.requestPermissions(getActivity(), permissions, 321);
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(getContext(), "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        String name=getString(R.string.app_name);
        new AlertDialog.Builder(getContext())
                .setTitle("存储权限不可用")
                .setMessage("由于"+name+"需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用"+name)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 开始提交请求权限
                        ActivityCompat.requestPermissions(getActivity(), permissions, 321);
                    }
                })
                .setNegativeButton("取消", null).setCancelable(false).show();
    }

        //兼容老版本图片路径
    private void handleImageBeforeKitkat(Intent data) {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    //对于Android6.0以上寻找图片真实路径
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        Log.i("handleImageOnKitkat", "输出路径——————————————："+uri);
        if (DocumentsContract.isDocumentUri(getContext(),uri)){
            //如果是document类型的Uri,则通过document id 处理
            String docId=DocumentsContract.getDocumentId(uri);

            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }

        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式
            imagePath=getImagePath(uri,null);

        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }
    //获取图片路径
    private String getImagePath(Uri uri, String selection) {
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContext().getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //显示图片
    private void displayImage(String imagePath) {
        if (imagePath!=null){
            //调用SharedPreferences中editor的编辑器进行编辑存储
            AppClient.setParam(DataKeys.imagePath, imagePath);
            AppClient.setParam(DataKeys.imageUrl,"");
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            head.setImageBitmap(bitmap);
            bitmap.recycle();
        }else {
            Toast.makeText(getContext(), "没有找到图片路径", Toast.LENGTH_SHORT).show();
        }
    }
    private void setCamera() {
        //创建File对象，用于存储拍照后的图片
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        Date date=new Date(System.currentTimeMillis());
        File outputImage=new File(getActivity().getExternalCacheDir(),format.format(date)+".jpg");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(getContext(),"com.campus.sport.fileProvider",
                    outputImage);
        }else {
            imageUri= Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,3);
    }
}
