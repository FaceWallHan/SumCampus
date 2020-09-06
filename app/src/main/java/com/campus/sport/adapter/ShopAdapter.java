package com.campus.sport.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campus.sport.R;
import com.campus.sport.bean.Shopping;

import java.util.List;

/**
 * Created by Administrator on 2019/5/21 0021.
 */

public class ShopAdapter extends ArrayAdapter<Shopping> {

    private int mLayout;

    private SetData data;

    public void setData(SetData data) {
        this.data = data;
    }

    public interface SetData{
        void setdata(int index);
    }


    public ShopAdapter(@NonNull Context context, int resource, @NonNull List<Shopping> objects) {
        super(context, resource, objects);
        mLayout=resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Shopping shopping=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(mLayout,parent,false);
        ImageView img=view.findViewById(R.id.shop_img);
        TextView title=view.findViewById(R.id.shop_title);
        TextView lable=view.findViewById(R.id.shop_lable);
        TextView hint1=view.findViewById(R.id.shop_hint1);
        TextView hint2=view.findViewById(R.id.shop_hint2);
        TextView price=view.findViewById(R.id.shop_price);
        TextView number=view.findViewById(R.id.shop_number);
        LinearLayout layout=view.findViewById(R.id.shop_layout);
        img.setImageResource(shopping.getImg());
        title.setText(shopping.getTitle());
        lable.setText(shopping.getLable());
        hint1.setText(shopping.getHint1());
        hint2.setText(shopping.getHint2());
        price.setText(shopping.getPrice());
        number.setText(shopping.getNumber());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setdata(position);
            }
        });
        return view;
    }
}
