package com.campus.sport.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.campus.sport.R;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Administrator on 2019/5/28.
 */

public class DiaplayAdapter extends ArrayAdapter<Bitmap> {

    private int mLayout;

    public DiaplayAdapter(@NonNull Context context, int resource, @NonNull List<Bitmap> objects) {
        super(context, resource, objects);
        mLayout=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Bitmap path=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(mLayout,parent,false);
        ImageView imageView=view.findViewById(R.id.display_img);
        imageView.setImageBitmap(path);
        return view;
    }
}
