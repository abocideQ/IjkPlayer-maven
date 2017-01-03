package com.example.administrator.breezeemediacontroller.mediacontroller.video;

import android.content.Context;
import android.util.AttributeSet;

import com.example.administrator.breezeemediacontroller.mediacontroller.BreezeeVideoPlayer;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.AddViewListener;

/**
 * Created by Administrator on 2017/1/3.
 */

public class SampleVideo extends BreezeeVideoPlayer implements AddViewListener {

    public SampleVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,this,true);
    }

    public SampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,this,true);
    }

    public SampleVideo(Context context) {
        super(context);
        initView(context,this,true);
    }

    /*
    * 布局的添加或修改
    * */
    @Override
    public void initOhterView() {

    }
}
