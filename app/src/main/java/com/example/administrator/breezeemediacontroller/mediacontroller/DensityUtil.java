package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/29.
 */

public class DensityUtil {
    /*
    * dp---->px
    * */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px---->dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
