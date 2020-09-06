package com.campus.sport.activity;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.campus.sport.R;
import com.campus.sport.fragment.Fragment_Start_Run;

/**
 * Created by Administrator on 2019/5/23 0023.
 */

public class RunActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_layout);
        Fragment fragment= new Fragment_Start_Run();
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment ff) {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.run_frag,ff);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setTitle("提示");
        builder.show();
    }
}
