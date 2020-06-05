package com.example.myapplication;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.utils.LocalUtils;
import com.example.myapplication.utils.SpUtils;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.Utils;

/*
 * Package    :com.example.myapplication
 * ClassName  :SettingActivity
 * Description:setting page
 * Data       :2020/3/25 11:51
 */
public class SettingActivity extends BaseActivity {
  private TextView tv_main, tv_def, tv_music;
  private ImageView img;
  private Spinner   spinner_l, spinner_m, spinner_v;
  private String[] off_on   = new String[]{Utils.getString(R.string.on), Utils.getString(R.string.off)};
  private String[] language = new String[]{"English", "中文"};

  @Override
  int getLayoutID() {
    return R.layout.setting_activity;
  }

  @Override
  void initView() {
    img = findViewById(R.id.Setting_Img);
    tv_main = findViewById(R.id.Setting_goMain);
    tv_def = findViewById(R.id.Setting_Default);
    tv_music = findViewById(R.id.Setting_Music);
    spinner_l = findViewById(R.id.Setting_l);
    spinner_m = findViewById(R.id.Setting_m);
    spinner_v = findViewById(R.id.Setting_v);

    spinner_m.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, off_on));
    spinner_v.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, off_on));
    spinner_l.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, language));
    updateState();

    spinner_l.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (language[position].equals("中文")) {
          SpUtils.putInt(StringUtils.TAG_LANGUAGE, 1);
        } else {
          SpUtils.putInt(StringUtils.TAG_LANGUAGE, 2);
        }
        LocalUtils.setLocal(mActivity);
        MyApplication.setApplicationLanguage(mActivity);
        MainActivity.reStart(mActivity);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    spinner_v.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (off_on[position].equals(Utils.getString(R.string.off))) {
          SpUtils.putBoolean(StringUtils.Vibrate, false);
        } else {
          SpUtils.putBoolean(StringUtils.Vibrate, true);
        }
        updateState();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    spinner_m.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (off_on[position].equals(Utils.getString(R.string.off))) {
          SpUtils.putBoolean(StringUtils.Sound, false);
        } else {
          SpUtils.putBoolean(StringUtils.Sound, true);
        }
        updateState();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    tv_main.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    tv_def.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SpUtils.putBoolean(StringUtils.Sound, true);
        SpUtils.putBoolean(StringUtils.Vibrate, true);
          SpUtils.putInt(StringUtils.TAG_LANGUAGE, 2);
        LocalUtils.setLocal(mActivity);
        MyApplication.setApplicationLanguage(mActivity);
        MainActivity.reStart(mActivity);
      }
    });

    tv_music.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(SettingActivity.this, MusicActivity.class));
      }
    });

    img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void updateState() {
    boolean sound = SpUtils.getBoolean(StringUtils.Sound, true);
    boolean vibrate = SpUtils.getBoolean(StringUtils.Vibrate, true);
    int language = SpUtils.getInt(StringUtils.TAG_LANGUAGE, 2);

    spinner_l.setSelection(language == 2 ? 0 : 1, true);
    spinner_v.setSelection(vibrate ? 0 : 1, true);
    spinner_m.setSelection(sound ? 0 : 1, true);

  }
}
