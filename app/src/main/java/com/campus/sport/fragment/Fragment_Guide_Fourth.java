package com.campus.sport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campus.sport.R;

/**
 * Created by Administrator on 2019/5/19.
 */

public class Fragment_Guide_Fourth extends Fragment {
    private TextView go;
    private FragmentChangeActivity changeActivity;

    public void setChangeActivity(FragmentChangeActivity changeActivity) {
        this.changeActivity = changeActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_fourth_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        go=getView().findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity.change();
            }
        });
    }
    public interface FragmentChangeActivity{
        void change();
    }
}
