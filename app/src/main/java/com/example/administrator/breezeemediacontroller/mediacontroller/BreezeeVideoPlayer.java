package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.administrator.breezeemediacontroller.R;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.AddViewListener;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */

public abstract class BreezeeVideoPlayer extends BreezeeBaseVideoPlayer {
    //顶部布局
    private ViewGroup topViewGoup;
    private final static @android.support.annotation.IdRes int topViewGoupId = 0x950813;
    //底部底部
    private ViewGroup bottomViewGoup;
    private final static @android.support.annotation.IdRes int bottomViewGoupId = 0x941212;
    //顶部、底部布局高度
    private int viewGoupHeigh=100;
    //播放按钮
    private ImageView ig_play;
    private final static @android.support.annotation.IdRes int ig_playId = 0x951007;
    private int ig_playWeith=70;
    private int ig_playHeigh=70;
    //进度条
    private SeekBar seekBar;
    private final static @android.support.annotation.IdRes int seekBarId = 0x951014;
    //横屏按钮
    private ImageView ig_toOrientation;
    private final static @android.support.annotation.IdRes int ig_toOrientationId = 0x951013;
    private int ig_toOrientationWeigh=50;
    private int ig_toOrientationHeigh;

    //添加View监听
    private AddViewListener listener;

    public BreezeeVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BreezeeVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BreezeeVideoPlayer(Context context) {
        super(context);
    }

    public ViewGroup getParentViewGroup() {
        return this;
    }

    /*
    * 初始化播放器以外View
    * */
    public void initView(Context context,AddViewListener listener,boolean ifInitMyselfsView) {
        //顶部ViewGoup
        topViewGoup = new RelativeLayout(context);
        bottomViewGoup = new RelativeLayout(context);
        topViewGoup.setId(topViewGoupId);
        bottomViewGoup.setId(bottomViewGoupId);
        FrameLayout.LayoutParams paramsTop=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGoupHeigh);
        paramsTop.gravity=Gravity.TOP;
        topViewGoup.setBackgroundColor(getContext().getResources().getColor(R.color.VideoBlack));
        topViewGoup.setLayoutParams(paramsTop);
        topViewGoup.setAlpha((float) 0.5);
        //底部ViewGoup
        FrameLayout.LayoutParams paramsBottom=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGoupHeigh);
        paramsBottom.gravity=Gravity.BOTTOM;
        bottomViewGoup.setBackgroundColor(getContext().getResources().getColor(R.color.VideoBlack));
        bottomViewGoup.setLayoutParams(paramsBottom);
        bottomViewGoup.setAlpha((float) 0.5);

        if (ifInitMyselfsView){
            //播放按钮
            ig_play=new ImageView(context);
            ig_play.setId(ig_playId);
            RelativeLayout.LayoutParams ig_playParams=new RelativeLayout.LayoutParams(ig_playWeith, ig_playWeith);
            ig_playParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ig_playParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ig_playParams.leftMargin=15;
            ig_play.setLayoutParams(ig_playParams);
            ig_play.setImageResource(R.drawable.video_pause_normal);
            ig_play.setAlpha((float) 1.0);
            bottomViewGoup.addView(ig_play);

            //横屏按钮
            ig_toOrientation=new ImageView(context);
            ig_toOrientation.setId(ig_toOrientationId);
            RelativeLayout.LayoutParams ig_toOrientationParams=new RelativeLayout.LayoutParams(ig_toOrientationWeigh, ig_toOrientationWeigh);
            ig_toOrientationParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ig_toOrientationParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ig_toOrientationParams.rightMargin=15;
            ig_toOrientation.setLayoutParams(ig_toOrientationParams);
            ig_toOrientation.setImageResource(R.drawable.video_enlarge);
            ig_toOrientation.setAlpha((float) 1.0);
            bottomViewGoup.addView(ig_toOrientation);
            //进度条
            seekBar=new SeekBar(context);
            seekBar.setId(seekBarId);
            RelativeLayout.LayoutParams seekBarParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            seekBarParams.addRule(RelativeLayout.CENTER_VERTICAL);
            seekBarParams.addRule(RelativeLayout.LEFT_OF,ig_toOrientation.getId());
            seekBarParams.addRule(RelativeLayout.RIGHT_OF,ig_play.getId());
            seekBar.setLayoutParams(seekBarParams);
//            seekBar.setProgressDrawable(getContext().getResources().getDrawable(R.color.white));
//            seekBar.setScrollBarStyle(android.R.style.Widget_DeviceDefault_Light_SeekBar);
            seekBar.setAlpha((float) 1.0);
            bottomViewGoup.addView(seekBar);
        }

        //监听回调
        if (listener != null)
            listener.initOhterView();

        //添加布局
        addView(topViewGoup);
        addView(bottomViewGoup);
        requestLayout();
    }

    /*
    * 初始化播放器(开始播放)
    * */
    public void setVideo(String url, Map<String, String> map, boolean isLoop, float speed) {
        setResource(url, map, isLoop, speed);
        addTextureView();
        topViewGoup.bringToFront();
        bottomViewGoup.bringToFront();
        requestLayout();
    }

    /*
    * 设置顶部、底部布局高度
    * */
    public void setViewGoupHeigh(int heigh){
        viewGoupHeigh=heigh;
    }

}
