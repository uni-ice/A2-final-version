package com.example.myapplication.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * vibrate help class
 * androidManifest.xml add:
 * <uses-permission android:name="android.permission.VIBRATE" />
 */
public class VibrateTool {
    private static Vibrator vibrator;

    /**
     * simple vibration
     * @param context     get vibration context
     * @param millisecond virbration time (ms)
     */
    @SuppressWarnings("static-access")
    public static void vibrateOnce(Context context, int millisecond) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(millisecond);
    }

    /**
     * complex vibration
     * @param context get vibration context
     * @param pattern vibration state
     *
     * @param repeate
     *
     *
     */
    @SuppressWarnings("static-access")
    public static void vibrateComplicated(Context context, long[] pattern, int repeate) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeate);
    }

    /**
     * stop vibration
     */
    public static void vibrateStop() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}