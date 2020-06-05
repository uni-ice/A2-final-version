package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.utils.ActivityUtils;
import com.example.myapplication.utils.LocalUtils;

/*
 * Package    :com.example.myapplication
 * ClassName  :BaseActivity
 * Description:Act based class
 * Data       :2020/6/1 16:55
 */
public abstract class BaseActivity extends AppCompatActivity {
  protected ActivityUtils utils;
  protected   Activity      mActivity;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutID());
    mActivity = this;
    utils = ActivityUtils.getInstance();
    utils.addActivity(this);
    initView();
  }

  abstract int getLayoutID();

  abstract void initView();

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(LocalUtils.setLocal(newBase));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    utils.removeActivity(this);
  }
}
