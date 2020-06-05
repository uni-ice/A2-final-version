package com.example.myapplication.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/*
 * Package    :com.example.myapplication.utils
 * ClassName  :LocalUtils
 * Description:
 * Data       :2020/6/2 13:58
 */
public class LocalUtils {
  /**
   * get language set
   * @param context
   * @return
   */
  public static Locale getSetLanguageLocale(Context context) {
    switch (SpUtils.getInt(StringUtils.TAG_LANGUAGE, 2)) {
      case 0:
        return getSystemLocale(context);
      case 1:
        return Locale.CHINA;
      case 2:
      default:
        return Locale.ENGLISH;
    }
  }

  /**
   * get system locale
   * @return Locale对象
   */
  public static Locale getSystemLocale(Context context) {
    Locale locale;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      locale = LocaleList.getDefault().get(0);
    } else {
      locale = Locale.getDefault();
    }
    return locale;
  }


  public static Context setLocal(Context context) {
    return updateResources(context, getSetLanguageLocale(context));
  }

  private static Context updateResources(Context context, Locale locale) {
    Locale.setDefault(locale);
    Resources res = context.getResources();
    Configuration config = new Configuration(res.getConfiguration());
    if (Build.VERSION.SDK_INT >= 17) {
      config.setLocale(locale);
      context = context.createConfigurationContext(config);
    } else {
      config.locale = locale;
      res.updateConfiguration(config, res.getDisplayMetrics());
    }
    return context;
  }
}
