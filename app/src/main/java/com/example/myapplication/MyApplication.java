package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.example.myapplication.utils.LocalUtils;

import java.util.Locale;

/**
 * application
 */
public class MyApplication extends Application {
  public static Context mAppContext;

  @Override
  public void onCreate() {
    super.onCreate();
    setApplicationLanguage(mAppContext);
  }

  /**
   * ser language
   */
  public static void setApplicationLanguage(Context context) {
    Resources resources = context.getApplicationContext().getResources();
    DisplayMetrics dm = resources.getDisplayMetrics();
    Configuration config = resources.getConfiguration();
    Locale locale = LocalUtils.getSetLanguageLocale(context);
    config.locale = locale;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      LocaleList localeList = new LocaleList(locale);
      LocaleList.setDefault(localeList);
      config.setLocales(localeList);
      context.getApplicationContext().createConfigurationContext(config);
      Locale.setDefault(locale);
    }
    resources.updateConfiguration(config, dm);
    mAppContext = context.getApplicationContext();
  }


  @Override
  protected void attachBaseContext(Context base) {
    mAppContext = base;
    super.attachBaseContext(LocalUtils.setLocal(base));
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    LocalUtils.setLocal(mAppContext);
  }


}
