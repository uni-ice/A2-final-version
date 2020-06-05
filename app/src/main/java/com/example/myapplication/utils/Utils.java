package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.myapplication.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Package    :com.example.myapplication.utils
 * ClassName  :Utils
 * Description:tool class
 * Data       :2020/3/24 13:42
 */
public class Utils {
  /**
   * drawable return to bitmap
   * @param drawable
   *   drawable object
   * @return bitmap object
   */
  public static Bitmap drawable2Bitmap(Drawable drawable) {
    // get drawable width and height
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();

    // get drawable color format
    Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
      : Bitmap.Config.RGB_565;
    // create bitmap
    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
    // create bitmap canvas
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    // put drawable into canvas
    drawable.draw(canvas);
    return bitmap;
  }

  /**
   * get context
   * @param context
   * @return DsisplayMetrics
   */
  private static DisplayMetrics getDisplayMetrics(Context context) {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    display.getMetrics(displayMetrics);
    return displayMetrics;
  }

  /**
   * get window width
   * @param context
   * @return WindowWidth
   */
  public static int getWindowWidth(Context context) {
    DisplayMetrics displayMetrics = getDisplayMetrics(context);
    if (displayMetrics != null)
      return displayMetrics.widthPixels;
    else
      return 0;
  }


  private static Toast toast = null;

  public static Toast getToast() {
    return toast;
  }

  public static void showToast(String msg) {
    if (toast == null)
      toast = Toast.makeText(MyApplication.mAppContext, msg, Toast.LENGTH_SHORT);
    else
      toast.setText(msg);
    toast.show();
  }


  /**
   * format time
   * @param time
   *   time
   * @return time after formatted
   */
  public static String FormatTime(long time) {
    SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    //        format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    return format.format(new Date(time));
  }

  public static String getString(int id) {
    return MyApplication.mAppContext.getString(id);
  }

}
