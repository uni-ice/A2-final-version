package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.myapplication.sql.MusicBean;
import com.example.myapplication.sql.MusicUtils;

/**
 * loop to get all music on the phone
 */
public class GetMusic {
    private static SharedPreferences sp;

    public static void Query(Context mContext) {
        MusicUtils sql = new MusicUtils(mContext);
        sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        Cursor c = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (c != null) {

            MusicBean model;

            while (c.moveToNext()) {

                model = new MusicBean();
                String music_title = c.getString(c
                        .getColumnIndex(MediaStore.Audio.Media.TITLE));
                String path = c.getString(c
                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                int time = c.getInt(c
                        .getColumnIndex(MediaStore.Audio.Media.DURATION));
                Log.e("tag", "music on the phone：" + music_title + "---" + path);
                //delete music less than 60s
                if (time < 60000) {
                    continue;
                }
                model.setName(music_title);
                model.setPath(path);
                sql.add(model);
            }
        }
    }
}
