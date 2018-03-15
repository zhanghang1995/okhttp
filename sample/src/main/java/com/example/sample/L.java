package com.example.sample;

import android.util.Log;

/**
 * 打印日志错误信息
 * Created by 74993 on 2018/3/15.
 */

public class L {
    private static final String TAG = "";
    private static boolean DEBUG = true;

    public static void e(String message){
        if(DEBUG)
            Log.e(TAG,message);
    }
}
