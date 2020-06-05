package com.example.myapplication.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Package    : com.example.baseutils
 * Date       : 2020/5/19 9:11
 */
public class ActivityUtils {
    private static ActivityUtils   mBaseUtils;
    public static  Stack<Activity> mActivityList = new Stack<>();


    public static ActivityUtils getInstance() {
        if (null == mBaseUtils) {
            synchronized (ActivityUtils.class) {
                if (null == mBaseUtils) {
                    mBaseUtils = new ActivityUtils();
                }
            }
        }
        return mBaseUtils;
    }


    public void addActivity(Activity pActivity) {
        mActivityList.add(pActivity);
    }

    public void removeActivity(Activity pActivity) {
        if (pActivity != null) {
            mActivityList.remove(pActivity);
            pActivity.finish();
            pActivity = null;
        }
    }

    //get current Activity
    public static Activity getCurrentActivity() {
        if (mActivityList.size() == 0)
            return null;
        else
            return mActivityList.lastElement();
    }

    //get Act array size
    public int getActivityListSize() {
        if (mActivityList != null)
            return mActivityList.size();
        else
            return 0;
    }

    /**
     * finish Act
     * @param className
     *         class name
     */
    public void UseNamefinishAct(String className) {
        Activity activity = null;
        for (Activity _ActivityRef : mActivityList) {
            try {
                if (_ActivityRef.getClass().getName().equals(className)) {
                    activity = _ActivityRef;
                    break;
                }
            } catch (NullPointerException ignored) {
            }
        }
        if (activity != null) {
            mActivityList.remove(activity);
            if (!activity.isDestroyed())
                activity.finish();
        }
    }

    /**
     * get Act
     * @param className
     *         class name
     */
    public static Activity UseNameGetAct(String className) {
        Activity activityWeakReference = null;
        for (Activity _ActivityRef : mActivityList) {
            try {
                if (_ActivityRef.getClass().getName().equals(className)) {
                    activityWeakReference = _ActivityRef;
                    break;
                }
            } catch (NullPointerException ignored) {
            }
        }
        assert activityWeakReference != null;
        return activityWeakReference;
    }

}
