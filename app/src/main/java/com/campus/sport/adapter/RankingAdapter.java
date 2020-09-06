package com.campus.sport.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.campus.sport.R;
import com.campus.sport.bean.DataRanking;
import com.campus.sport.utils.ShapeImageView;

import java.util.List;

/**
 * Created by Administrator on 2019/5/23.
 */

public class RankingAdapter extends ArrayAdapter<DataRanking> {
    private List<DataRanking>list;
    private LayoutInflater inflater;

    public RankingAdapter(@NonNull Context context,  List<DataRanking> list) {
        super(context, 0);
        this.list = list;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public DataRanking getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        TextView item_index;
        ShapeImageView item_head;
        TextView item_step_count;
        TextView item_name;
        TextView item_integral;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= inflater.inflate(R.layout.ranking_item_layout,parent,false);
            holder=new ViewHolder();
            holder.item_head=convertView.findViewById(R.id.item_head);
            holder.item_step_count=convertView.findViewById(R.id.item_step_count);
            holder.item_index=convertView.findViewById(R.id.item_index);
            holder.item_integral=convertView.findViewById(R.id.item_integral);
            holder.item_name=convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        DataRanking ranking=list.get(position);
        switch (position){
            case 0:
                holder.item_index.setBackgroundResource(R.drawable.guanjun);
                break;
            case 1:
                holder.item_index.setBackgroundResource(R.drawable.yajun);
                break;
            case 2:
                holder.item_index.setBackgroundResource(R.drawable.jijun);
                break;
            default:
                holder.item_index.setText(ranking.getIndex()+"");
                break;
        }
        holder.item_head.setBackgroundResource(R.drawable.sex);
        //holder.item_index.setText(ranking.getIndex()+"");
        holder.item_integral.setText(ranking.getIntegral());
        holder.item_step_count.setText(ranking.getStepCount()+"");
        holder.item_name.setText(ranking.getName());
        return convertView;
    }
}
