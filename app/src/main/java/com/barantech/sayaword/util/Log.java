package com.barantech.sayaword.util;

/**
 * Created by mary on 11/30/15.
 */
public class Log {
    private final static String  TAG = "SayAWordLog";
    private static boolean isActive = true;

    public static void e(String error){
        e(TAG,error);
    }
    public static void e(String tag, String error){
        if(isActive){
            android.util.Log.e(tag, error);
        }
    }
    public static void w(String error){
        w(TAG,error);
    }
    public static void w(String tag, String error){
        if(isActive){
            android.util.Log.w(tag, error);
        }
    }
    public static void i(String error){
        i(TAG,error);
    }
    public static void i(String tag, String error){
        if(isActive){
            android.util.Log.i(tag, error);
        }
    }
    public static void d(String error){
        d(TAG,error);
    }
    public static void d(String tag, String error){
        if(isActive){
            android.util.Log.d(tag, error);
        }
    }
    public static void v(String error){
        v(TAG,error);
    }
    public static void v(String tag, String error){
        if(isActive){
            android.util.Log.v(tag, error);
        }
    }
}
