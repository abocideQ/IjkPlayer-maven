package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.administrator.breezeemediacontroller.R;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.MediaListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.ViewListener;

import java.util.Map;

/**
 * 添加布局、布局对播放器的控制
 * Created by Administrator on 2017/1/3.
 */

public abstract class BreezeeVideoPlayer extends BreezeeBaseVideoPlayer implements MediaListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    //parent
    private int parentHeigh = 240;
    private int parentWith;
    //顶部布局
    private ViewGroup topViewGoup;
    public final static
    @android.support.annotation.IdRes
    int topViewGoupId = 0x950813;
    //底部底部
    private ViewGroup bottomViewGoup;
    public final static
    @android.support.annotation.IdRes
    int bottomViewGoupId = 0x941212;
    //顶部、底部布局高度
    private int viewGoupHeigh = 100;
    //播放按钮
    private ImageView ig_play;
    public final static
    @android.support.annotation.IdRes
    int ig_playId = 0x951007;
    private int ig_playWeith = 70;
    private int ig_playHeigh = 70;
    //进度条
    private SeekBar seekBar;
    public final static
    @android.support.annotation.IdRes
    int seekBarId = 0x951014;
    private int playingProgress;
    private boolean isTouching = false;
    //横屏按钮
    private ImageView ig_toOrientation;
    public final static
    @android.support.annotation.IdRes
    int ig_toOrientationId = 0x951013;
    private int ig_toOrientationWeigh = 50;
    private int ig_toOrientationHeigh;

    private Activity activity;
    private ViewListener viewListener;
    //BreezeeViews
    private boolean ifInitBreezeeViews;

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
    public void initView(Activity activity, Context context, ViewListener listener, boolean ifInitBreezeeViews) {
        this.ifInitBreezeeViews = ifInitBreezeeViews;
        this.activity = activity;
        this.viewListener = listener;
        this.setBackgroundResource(android.R.color.black);
        //顶部ViewGoup
        topViewGoup = new RelativeLayout(context);
        bottomViewGoup = new RelativeLayout(context);
        topViewGoup.setId(topViewGoupId);
        bottomViewGoup.setId(bottomViewGoupId);
        FrameLayout.LayoutParams paramsTop = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGoupHeigh);
        paramsTop.gravity = Gravity.TOP;
        topViewGoup.setBackgroundColor(getContext().getResources().getColor(R.color.VideoBlack));
        topViewGoup.setLayoutParams(paramsTop);
        topViewGoup.setAlpha((float) 0.5);
        //底部ViewGoup
        FrameLayout.LayoutParams paramsBottom = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGoupHeigh);
        paramsBottom.gravity = Gravity.BOTTOM;
        bottomViewGoup.setBackgroundColor(getContext().getResources().getColor(R.color.VideoBlack));
        bottomViewGoup.setLayoutParams(paramsBottom);
        bottomViewGoup.setAlpha((float) 0.5);

        if (ifInitBreezeeViews) {
            //播放按钮
            ig_play = new ImageView(context);
            ig_play.setId(ig_playId);
            ig_play.setOnClickListener(this);
            RelativeLayout.LayoutParams ig_playParams = new RelativeLayout.LayoutParams(ig_playWeith, ig_playWeith);
            ig_playParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ig_playParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ig_playParams.leftMargin = 15;
            ig_play.setLayoutParams(ig_playParams);
            ig_play.setImageResource(R.drawable.video_pause_normal);
            ig_play.setAlpha((float) 1.0);
            bottomViewGoup.addView(ig_play);

            //横屏按钮
            ig_toOrientation = new ImageView(context);
            ig_toOrientation.setId(ig_toOrientationId);
            ig_toOrientation.setOnClickListener(this);
            RelativeLayout.LayoutParams ig_toOrientationParams = new RelativeLayout.LayoutParams(ig_toOrientationWeigh, ig_toOrientationWeigh);
            ig_toOrientationParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ig_toOrientationParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ig_toOrientationParams.rightMargin = 15;
            ig_toOrientation.setLayoutParams(ig_toOrientationParams);
            ig_toOrientation.setImageResource(R.drawable.video_enlarge);
            ig_toOrientation.setAlpha((float) 1.0);
            bottomViewGoup.addView(ig_toOrientation);
            //进度条
            seekBar = new SeekBar(context);
            seekBar.setId(seekBarId);
            RelativeLayout.LayoutParams seekBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            seekBarParams.addRule(RelativeLayout.CENTER_VERTICAL);
            seekBarParams.addRule(RelativeLayout.LEFT_OF, ig_toOrientation.getId());
            seekBarParams.addRule(RelativeLayout.RIGHT_OF, ig_play.getId());
            seekBar.setLayoutParams(seekBarParams);
//            seekBar.setProgressDrawable(getContext().getResources().getDrawable(R.color.white));
//            seekBar.setScrollBarStyle(android.R.style.Widget_DeviceDefault_Light_SeekBar);
            seekBar.setAlpha((float) 1.0);
            seekBar.setOnSeekBarChangeListener(this);
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
        addTextureView(this);
        topViewGoup.bringToFront();
        bottomViewGoup.bringToFront();
        requestLayout();
    }

    /*
    * 设置顶部、底部布局高度
    * */
    public void setViewGoupHeigh(int heigh) {
        viewGoupHeigh = heigh;
    }

    /*
    * OnSeekBarChangeListener
    * */
    public Handler seekHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (ifInitBreezeeViews)
                seekBar.setProgress((int) BreezeeVideoManager.instance().getMediaPlayer().getCurrentPosition());
        }
    };

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouching = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (ifInitBreezeeViews) {
            isTouching = false;
            BreezeeVideoManager.instance().getMediaPlayer().seekTo(seekBar.getProgress());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void updateSeekBar() {
        if (!isTouching)
            seekHandler.sendEmptyMessage(0);
    }

    @Override
    public void setSeekBarMax(int max) {
        if (ifInitBreezeeViews)
            seekBar.setMax(max);
    }

    @Override
    public void resetView() {
        if (ifInitBreezeeViews) {
            seekBar.setProgress(0);
            ig_play.setImageResource(R.drawable.video_play_pressed);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ig_playId:
                if (MEDIA_STATE == CURRENT_STATE_PLAYING) {
                    onPause();
                    doPauseView();
                } else if (MEDIA_STATE == CURRENT_STATE_PAUSE) {
                    onResume();
                    doResumeView();
                } else if (MEDIA_STATE == CURRENT_STATE_AUTO_COMPLETE) {
                    if (BreezeeVideoManager.instance().getMediaPlayer() != null)
                        BreezeeVideoManager.instance().getMediaPlayer().release();
                    if (getTexture() != null)
                        removeView(getTexture());
                    doResumeView();
                    if (viewListener != null)
                        viewListener.playOhterVideo();
                } else {
                    if (BreezeeVideoManager.instance().getMediaPlayer() != null)
                        BreezeeVideoManager.instance().getMediaPlayer().release();
                    if (getTexture() != null)
                        removeView(getTexture());
                    doResumeView();
                    if (viewListener != null)
                        viewListener.playOhterVideo();
                }
                break;
            case ig_toOrientationId:
                if (screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    onResolve(activity, false);
                    doPortView();
                } else if (screenType == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    onResolve(activity, true);
                    if (viewListener != null)
                        doLandView(viewListener);
                }
                break;
        }
    }

    /*
    * 暂停View变化
    * */
    public void doPauseView() {
        if (ifInitBreezeeViews) {
            ig_play.setImageResource(R.drawable.video_play_pressed);
        }
    }

    /*
    * 恢复View变化
    * */
    public void doResumeView() {
        if (ifInitBreezeeViews) {
            ig_play.setImageResource(R.drawable.video_pause_normal);
        }
    }

    /*
    * 横屏View变化
    * */
    public void doLandView(ViewListener viewListener) {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
            requestLayout();
        } else if (getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
            requestLayout();
        }
        if (viewListener != null)
            viewListener.doLandView();
    }

    /*
    * 竖屏View变化
    * */
    public void doPortView() {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
            requestLayout();
        } else if (getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
            requestLayout();
        }
        if (viewListener != null)
            viewListener.doPortView();
    }

    /*
    * 进度条拖动完成后View变化
    * */
    public void doChangedTheSeekBar() {
        if (ifInitBreezeeViews) {

        }
    }

    /*
    * 进度条拖动中View变化
    * */
    public void doChangingSeekBar() {
        if (ifInitBreezeeViews) {

        }
    }

}
