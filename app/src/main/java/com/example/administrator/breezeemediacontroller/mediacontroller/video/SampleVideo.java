package com.example.administrator.breezeemediacontroller.mediacontroller.video;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.example.administrator.breezeemediacontroller.mediacontroller.BreezeeVideoPlayer;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.ViewListener;

/**
 * Created by Administrator on 2017/1/3.
 */

public class SampleVideo extends BreezeeVideoPlayer {

    private Context context;
    public SampleVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public SampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SampleVideo(Context context) {
        super(context);
        this.context=context;
    }

    //1.activity 2.除播放器以外View的处理 3.是否加载默认View
    public void initView(Activity activity, ViewListener listener, boolean ifInitBreezeeViews){
        initView(activity,context,listener,true);
    }

}
