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
import com.campus.sport.bean.DataHistory;

import java.util.List;

/**
 * Created by Administrator on 2019/5/25.
 */

public class HistoryStepAdapter extends ArrayAdapter<DataHistory> {
    private List<DataHistory>list;
    private LayoutInflater inflater;

    public HistoryStepAdapter(@NonNull Context context,  List<DataHistory> list) {
        super(context, 0);
        this.list = list;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.history_item_layout,parent,false);
            holder=new ViewHolder();
            holder.item_distance=convertView.findViewById(R.id.item_distance);
            holder.item_step_count=convertView.findViewById(R.id.item_step_count);
            holder.item_time=convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        DataHistory dataHistory=list.get(position);
        holder.item_step_count.setText(dataHistory.getStepCount());
        holder.item_distance.setText(dataHistory.getDistance());
        holder.item_time.setText(dataHistory.getTime());
        return convertView;
    }
    private class ViewHolder{
        TextView item_time,item_step_count,item_distance;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public DataHistory getItem(int position) {
        return list.get(position);
    }
}
