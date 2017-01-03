package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */

public abstract class BreezeeVideoPlayer extends BreezeeBaseVideoPlayer{

    public BreezeeVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}

    public BreezeeVideoPlayer(Context context, AttributeSet attrs) {super(context, attrs);}

    public BreezeeVideoPlayer(Context context) {super(context);}

    public void setVideo(String url, Map<String,String> map, boolean isLoop, float speed){
        setResource(url,map,isLoop,speed);
        addTextureView();
    }

}
