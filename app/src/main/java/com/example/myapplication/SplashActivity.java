package com.example.myapplication;

import android.content.Intent;

import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.utils.SpUtils;
import com.example.myapplication.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

/*
 * Package    :com.example.myapplication
 * ClassName  :SplashActivity
 * Description:music
 * Data       :2020/5/26 14:28
 */
public class SplashActivity extends BaseActivity {

  @Override
  int getLayoutID() {
    return R.layout.splash_activity;
  }

  @Override
  void initView() {
    SqlUtils utils = new SqlUtils(this);
    if (SpUtils.getBoolean(StringUtils.IsFirst, true)) {
      utils.add("Cloud9.mp3", 1, "GEMINI - Cloud 9");
      utils.add("花降.mp3", 1, "n-buna - 花降らし オルゴールver");
      utils.add("神山純一.mp3", 1, "神山純一 - 水の妖精");
      utils.add("水月陵.mp3", 1, "水月陵 - 鳥の詩 ~");
      utils.add("M01.mp3", 1, "梶浦由記 - M01");
      SpUtils.getBoolean(StringUtils.IsFirst, false);
    }

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
      }
    }, 2000);
  }
}
