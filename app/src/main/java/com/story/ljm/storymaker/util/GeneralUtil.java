package com.story.ljm.storymaker.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ljm on 2017-05-15.
 */

public class GeneralUtil {
    public static boolean IS_DEBUG_MODE = true;

    public static void Log(String log){
        Log.d("StoryMaker", log);
    }

    public static int makeRandomInteger(){
        return (int) (Math.random()*(Integer.MAX_VALUE-1));
    }
    public static String getDateTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss");
        return format.format(date);
    }
}
