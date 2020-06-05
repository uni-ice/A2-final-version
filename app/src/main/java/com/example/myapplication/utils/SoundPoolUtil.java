package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.SoundPool;

import com.example.myapplication.R;

//play click sound effects tool
public class SoundPoolUtil {
    private static SoundPoolUtil soundPoolUtil;
    private        SoundPool     soundPool;
    public         int           click, remove;

    //single column mode
    public static SoundPoolUtil getInstance(Context context) {
        if (soundPoolUtil == null)
            soundPoolUtil = new SoundPoolUtil(context);
        return soundPoolUtil;
    }

    @SuppressLint("NewApi")
    private SoundPoolUtil(Context context) {
        soundPool = new SoundPool.Builder().build();
        //load sound file
        click = soundPool.load(context, R.raw.click, 1);
        remove = soundPool.load(context, R.raw.remove, 1);
    }

    public void play(int number) {
        /**
         * play sound
         * params explanation：
         * //left ear canal volume [0~1]
         * //right ear canal volume[0~1]
         * //Play priority [0 means the lowest priority]
         * //cycle mode [0 means cycle once, -1 means keep looping, other means number +1 means the number of cycles corresponding to the current number]
         * //play speed [1 is normal, the range is from 0 to 2]
         */
        soundPool.play(number, 1, 1, 0, 0, 1);
    }
}
