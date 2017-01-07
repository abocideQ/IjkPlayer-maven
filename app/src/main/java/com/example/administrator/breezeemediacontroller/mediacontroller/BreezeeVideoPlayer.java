package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.breezeemediacontroller.R;
import com.example.administrator.breezeemediacontroller.mediacontroller.item.CommonUtil;
import com.example.administrator.breezeemediacontroller.mediacontroller.item.DensityUtil;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.MediaListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.ViewListener;

import java.util.Map;

/**
 * 添加布局、布局的控制
 * Created by Administrator on 2017/1/3.
 */

public abstract class BreezeeVideoPlayer extends BreezeeBaseVideoPlayer implements MediaListener
        , SeekBar.OnSeekBarChangeListener, View.OnClickListener, View.OnTouchListener {
    //parent
    private boolean isViewGoupVisible = true;
    private int ViewGoupHeigh = 260;
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
    private int viewGoupHeigh = 120;
    //播放按钮
    private ImageView ig_play;
    public final static
    @android.support.annotation.IdRes
    int ig_playId = 0x951007;
    private int ig_playWeith = 90;
    private int ig_playHeigh = 90;
    //回退按钮
    private ImageView ig_back;
    public final static
    @android.support.annotation.IdRes
    int ig_backId = 0x951000;
    private int ig_backWeith = 70;
    private int ig_backHeigh = 70;
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
    private int ig_toOrientationWeigh = 70;
    private int ig_toOrientationHeigh;
    //视频时间显示
    private TextView tv_playTime;
    private TextView tv_totalTime;
    public final static
    @android.support.annotation.IdRes
    int tv_playTimeId = 0x951000;
    public final static
    @android.support.annotation.IdRes
    int tv_totalTimeId = 0x951001;
    //缓冲图
    private ProgressBar progressBar;
    public final static
    @android.support.annotation.IdRes
    int progressBarId = 0x9511522;
    //播放错误
    private TextView tv_error;
    public final static
    @android.support.annotation.IdRes
    int tv_errorId = 0x9511523;
    //音量、亮度、进度
    private TextView tv_Info;
    public final static
    @android.support.annotation.IdRes
    int tv_InfoId = 0x9511523;
    private int ViewGoupWith;//屏幕当前宽度
    protected int mScreenWidth; //屏幕宽度
    protected int mScreenHeight; //屏幕高度


    private Activity activity;
    private ViewListener viewListener;//由Acticity或Fragment传入，控制View
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

    /**
     * 初始化播放器以外View
     */
    public void initView(Activity activity, Context context, ViewListener listener, boolean ifInitBreezeeViews) {
        this.ifInitBreezeeViews = ifInitBreezeeViews;
        this.activity = activity;
        this.viewListener = listener;
        this.setBackgroundResource(android.R.color.black);
        this.setOnTouchListener(this);
        //顶部ViewGoup
        topViewGoup = new RelativeLayout(context);
        bottomViewGoup = new RelativeLayout(context);
        topViewGoup.setId(topViewGoupId);
        bottomViewGoup.setId(bottomViewGoupId);
        viewGoupHeigh = DensityUtil.dip2px(context, 45);
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
            //控件图标大小初始化
            ig_playWeith = DensityUtil.dip2px(context, 30);
            ig_toOrientationWeigh = DensityUtil.dip2px(context, 30);
            ig_backWeith = DensityUtil.dip2px(context, 30);
            //播放按钮
            ig_play = new ImageView(context);
            ig_play.setId(ig_playId);
            ig_play.setOnClickListener(this);
            RelativeLayout.LayoutParams ig_playParams = new RelativeLayout.LayoutParams(ig_playWeith, ig_playWeith);
            ig_playParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ig_playParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ig_playParams.leftMargin = DensityUtil.dip2px(context, 10);
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
            ig_toOrientationParams.rightMargin = DensityUtil.dip2px(context, 10);
            ig_toOrientation.setLayoutParams(ig_toOrientationParams);
            ig_toOrientation.setPadding(DensityUtil.dip2px(context, 5)
                    , DensityUtil.dip2px(context, 5)
                    , DensityUtil.dip2px(context, 5)
                    , DensityUtil.dip2px(context, 5));
            ig_toOrientation.setImageResource(R.drawable.video_enlarge);
            ig_toOrientation.setAlpha((float) 1.0);
            bottomViewGoup.addView(ig_toOrientation);

            //回退按钮
            ig_back = new ImageView(context);
            ig_back.setId(ig_backId);
            ig_back.setOnClickListener(this);
            RelativeLayout.LayoutParams ig_backParams = new RelativeLayout.LayoutParams(ig_backWeith, ig_backWeith);
            ig_backParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ig_backParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ig_backParams.leftMargin = 25;
            ig_back.setLayoutParams(ig_backParams);
            ig_back.setPadding(DensityUtil.dip2px(context, 5)
                    , DensityUtil.dip2px(context, 5)
                    , DensityUtil.dip2px(context, 5)
                    , DensityUtil.dip2px(context, 5));
            ig_back.setImageResource(R.drawable.video_back);
            ig_back.setAlpha((float) 1.0);
            topViewGoup.addView(ig_back);

            //显示时间
            tv_playTime = new TextView(context);
            tv_totalTime = new TextView(context);
            tv_playTime.setId(tv_playTimeId);
            tv_totalTime.setId(tv_totalTimeId);
            tv_playTime.setText("00:00");
            tv_totalTime.setText("00:00");
            tv_totalTime.setTextColor(getContext().getResources().getColor(android.R.color.white));
            tv_playTime.setTextColor(getContext().getResources().getColor(android.R.color.white));
            RelativeLayout.LayoutParams tv_playTimeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv_playTimeParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tv_playTimeParams.addRule(RelativeLayout.RIGHT_OF, ig_play.getId());
            tv_playTimeParams.leftMargin = DensityUtil.dip2px(context, 7);
            tv_playTime.setLayoutParams(tv_playTimeParams);
            bottomViewGoup.addView(tv_playTime);

            RelativeLayout.LayoutParams tv_totalTimeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv_totalTimeParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tv_totalTimeParams.addRule(RelativeLayout.LEFT_OF, ig_toOrientation.getId());
            tv_totalTimeParams.rightMargin = DensityUtil.dip2px(context, 5);
            tv_totalTime.setLayoutParams(tv_totalTimeParams);
            bottomViewGoup.addView(tv_totalTime);

            //进度条
            seekBar = new SeekBar(context);
            seekBar.setId(seekBarId);
            RelativeLayout.LayoutParams seekBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            seekBarParams.addRule(RelativeLayout.CENTER_VERTICAL);
            seekBarParams.addRule(RelativeLayout.LEFT_OF, tv_totalTime.getId());
            seekBarParams.addRule(RelativeLayout.RIGHT_OF, tv_playTime.getId());
            seekBar.setLayoutParams(seekBarParams);
//            seekBar.setProgressDrawable(getContext().getResources().getDrawable(R.color.white));
//            seekBar.setScrollBarStyle(android.R.style.Widget_DeviceDefault_Light_SeekBar);
            seekBar.setAlpha((float) 1.0);
            seekBar.setOnSeekBarChangeListener(this);
            bottomViewGoup.addView(seekBar);

            progressBar = new ProgressBar(context);
            progressBar.setId(progressBarId);
            FrameLayout.LayoutParams progressBarParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            progressBarParams.gravity = Gravity.CENTER;
            progressBar.setLayoutParams(progressBarParams);
            addView(progressBar);

            //音量、进度、亮度
            tv_Info = new TextView(context);
            tv_Info.setId(tv_InfoId);
            tv_Info.setTextColor(getContext().getResources().getColor(android.R.color.white));
            FrameLayout.LayoutParams tv_InfoParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv_InfoParams.gravity = Gravity.CENTER;
            tv_Info.setLayoutParams(tv_InfoParams);
            addView(tv_Info);
            requestLayout();
            tv_Info.setVisibility(INVISIBLE);

        }

        ViewGoupWith = this.getMeasuredWidth();
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
//        try {
//            brightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
        //监听回调(加入个人布局)
        if (listener != null)
            listener.initOhterView();

        //添加布局
        addView(topViewGoup);
        addView(bottomViewGoup);
        if (tv_error != null)
            removeView(tv_error);
        requestLayout();
    }

    /**
     * 初始化播放器(开始播放)
     */
    public void setVideo(String url, Map<String, String> map, boolean isLoop, float speed) {
        if (tv_error != null)
            removeView(tv_error);
        setResource(url, map, isLoop, speed);
        addTextureView(this);
    }

    /**
     * 设置顶部、底部布局高度
     */
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
        setPlayTime(CommonUtil.stringForTime(seekBar.getProgress()));
    }

    /*
    * MediaListener
    * */
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
            MEDIA_STATE = CURRENT_STATE_AUTO_COMPLETE;
            seekBar.setProgress(0);
            ig_play.setImageResource(R.drawable.video_play_pressed);
            requestLayout();
        }
        requestLayout();
    }

    @Override
    public void bringViewsToFront() {
        topViewGoup.bringToFront();
        bottomViewGoup.bringToFront();
        requestLayout();
    }

    @Override
    public void setTotalTime(String time) {
        if (ifInitBreezeeViews)
            tv_totalTime.setText(time);
    }

    @Override
    public void setPlayTime(String time) {
        if (ifInitBreezeeViews)
            tv_playTime.setText(time);
    }

    @Override
    public void isShowProgressBar(boolean isShow) {
        if (ifInitBreezeeViews) {
            if (isShow) {
                progressBar.setVisibility(VISIBLE);
                progressBar.bringToFront();
                requestLayout();
            } else {
                progressBar.setVisibility(INVISIBLE);
                requestLayout();
            }
        }
        requestLayout();
    }

    @Override
    public void checkNetWork() {
        if (!CommonUtil.isNetworkAvailable(activity)) {
            BreezeeVideoManager.instance().releaseMediaPlayer();
            resetView();
            MEDIA_STATE = CURRENT_STATE_ERROR;
            isShowProgressBar(false);
            addErroView();
        } else {
            BreezeeVideoManager.instance().releaseMediaPlayer();
            resetView();
            MEDIA_STATE = CURRENT_STATE_ERROR;
            isShowProgressBar(false);
            addErroView();
        }
    }

    @Override
    public void addErroView() {
        if (ifInitBreezeeViews) {
            tv_error = new TextView(getContext());
            tv_error.setId(tv_errorId);
            tv_error.setText("播放出错,请尝试重新观看或检查网络是否正常!!");
            tv_error.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
            FrameLayout.LayoutParams tv_errorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv_errorParams.gravity = Gravity.CENTER;
            tv_error.setLayoutParams(tv_errorParams);
            addView(tv_error);
            requestLayout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ig_playId:
                if (MEDIA_STATE == CURRENT_STATE_PLAYING) {
                    onPause();
                    doPauseView();
                    MEDIA_STATE = CURRENT_STATE_PAUSE;
                } else if (MEDIA_STATE == CURRENT_STATE_PAUSE) {
                    onResume();
                    doResumeView();
                    MEDIA_STATE = CURRENT_STATE_PLAYING;
//                    isShowProgressBar(true);
                } else if (MEDIA_STATE == CURRENT_STATE_AUTO_COMPLETE) {
                    if (BreezeeVideoManager.instance().getMediaPlayer() != null)
                        BreezeeVideoManager.instance().getMediaPlayer().release();
                    if (getTexture() != null)
                        removeView(getTexture());
                    doResumeView();
                    if (viewListener != null)
                        viewListener.playOhterVideo();
                    MEDIA_STATE = CURRENT_STATE_PLAYING;
                    isShowProgressBar(true);
                } else {
                    if (BreezeeVideoManager.instance().getMediaPlayer() != null)
                        BreezeeVideoManager.instance().getMediaPlayer().release();
                    if (getTexture() != null)
                        removeView(getTexture());
                    doResumeView();
                    if (viewListener != null)
                        viewListener.playOhterVideo();
                    MEDIA_STATE = CURRENT_STATE_PLAYING_AGAIN;
                    if (seekBar != null)
                        seekBar.setProgress(currentPosition);
                    isShowProgressBar(true);
                }
                break;
            case ig_toOrientationId:
                if (SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    onResolve(activity, false);
                    doPortView();
                } else if (SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    onResolve(activity, true);
                    if (viewListener != null)
                        doLandView(viewListener);
                }
                break;
            case ig_backId:
                if (SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    onResolve(activity, false);
                    doPortView();
                } else {
                    onPause();
                    doPauseView();
                    activity.finish();
                }
                break;
        }
    }

    protected float mDownX;//触摸的X

    protected float mDownY; //触摸的Y

    protected boolean mChangeVolume = false;//是否改变音量

    protected boolean mChangePosition = false;//是否改变播放进度

    protected boolean mBrightness = false;//是否改变亮度

    private int pointId;

    private boolean ifTouchIt = true;//是否第一次

    private int lastVolume; //上一次调节后的音量

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v != topViewGoup && v != bottomViewGoup) {
                mDownX = x;
                mDownY = y;
                mChangeVolume = false;
                mChangePosition = false;
                mBrightness = false;
                pointId = event.getPointerId(0);
                if (isViewGoupVisible) {
                    animation(topViewGoup, viewGoupHeigh);
                    animation(bottomViewGoup, -viewGoupHeigh);
                    isViewGoupVisible = false;
                } else {
                    animation(topViewGoup, 0);
                    animation(bottomViewGoup, 0);
                    isViewGoupVisible = true;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float deltaX = x - mDownX;
            float deltaY = y - mDownY;
            float absDeltaX = Math.abs(deltaX);
            float absDeltaY = Math.abs(deltaY);
            if (pointId == event.getPointerId(0)) {
                if (SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    if (mDownX > mScreenHeight / 2) {
                        int deltaV = (int) ((volumeMax * deltaY * 3 / mScreenHeight));
                        if (ifTouchIt) {
                            changeVolume(deltaV);
                        } else {
                            changeVolume(deltaV - lastVolume);
                        }
                        setInfoView(1);
                    } else {
                        changeBrightness((int) deltaY);
                        Log.e("sd-s--d-sd", "-------------->" + brightness + "---------------" + deltaY);
                    }
                }
                pointId = event.getPointerId(0);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            ifTouchIt = false;
            lastVolume = currentVolume;
            if (ifInitBreezeeViews) {
                tv_Info.setVisibility(INVISIBLE);
            }
        }
        return true;
    }

    /**
     * 暂停View变化
     */
    public void doPauseView() {
        if (ifInitBreezeeViews) {
            ig_play.setImageResource(R.drawable.video_play_pressed);
        }
    }

    /**
     * 恢复View变化
     */
    public void doResumeView() {
        if (ifInitBreezeeViews) {
            ig_play.setImageResource(R.drawable.video_pause_normal);
        }
    }

    /**
     * 横屏View变化
     */
    public void doLandView(ViewListener viewListener) {
        if (getParent() instanceof RelativeLayout) {
            requestLayout();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
            requestLayout();
        } else if (getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(params);
            requestLayout();
        }
        if (ifInitBreezeeViews)
            ig_toOrientation.setImageResource(R.drawable.video_shrink);
        upAndDownTopView(true);//显示假状态栏
        requestLayout();
    }

    /**
     * 竖屏View变化
     */
    public void doPortView() {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = DensityUtil.dip2px(getContext(), ViewGoupHeigh);
            setLayoutParams(params);
            //显示状态栏
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            requestLayout();
        } else if (getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = DensityUtil.dip2px(getContext(), ViewGoupHeigh);
            setLayoutParams(params);
            //显示状态栏
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            requestLayout();
        }
        if (ifInitBreezeeViews)
            ig_toOrientation.setImageResource(R.drawable.video_enlarge);
        upAndDownTopView(true);//显示假状态栏
        requestLayout();
    }

    /**
     * 动画
     */
    private ObjectAnimator mAnimator;
    private int duration = 300;

    private void animation(final View view, final int y) {
        mAnimator = ObjectAnimator.ofInt(view, "scrollY", y);
        mAnimator.setDuration(duration);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (y == 0) {
                    if (view == topViewGoup)
                        topViewGoup.setVisibility(VISIBLE);
                    else if (view == bottomViewGoup)
                        bottomViewGoup.setVisibility(VISIBLE);
                    if (SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        //显示状态栏
                        if (viewListener != null)
                            viewListener.doPortView();
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        upAndDownTopView(true);//显示假状态栏
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
                if (y != 0) {
                    if (view == topViewGoup)
                        topViewGoup.setVisibility(INVISIBLE);
                    else if (view == bottomViewGoup)
                        bottomViewGoup.setVisibility(INVISIBLE);
                    if (SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        //隐藏状态栏
                        if (viewListener != null)
                            viewListener.doLandView();
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                        upAndDownTopView(false);//隐藏假状态栏
                    }
                }
                requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    /*
    * 显示/隐藏假titileBar
    * */
    public void upAndDownTopView(boolean isDown) {
        if (isDown) {
            if (getParent() instanceof RelativeLayout) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = CommonUtil.getStatusBarHeight(activity);
                setLayoutParams(params);
                requestLayout();
            } else if (getParent() instanceof LinearLayout) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = CommonUtil.getStatusBarHeight(activity);
                setLayoutParams(params);
                requestLayout();
            }
        } else {
            if (getParent() instanceof RelativeLayout) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 0;
                setLayoutParams(params);
                requestLayout();
            } else if (getParent() instanceof LinearLayout) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 0;
                setLayoutParams(params);
                requestLayout();
            }
        }
    }

    /**
     * 音量、进度、亮度View控制 1、2、3
     */
    public void setInfoView(int type) {
        if (ifInitBreezeeViews) {
            tv_Info.bringToFront();
            tv_Info.setVisibility(VISIBLE);
            if (type == 1) {
                if (currentVolume < 0)
                    tv_Info.setText("音量：" + "0");
                else if (currentVolume >= 0 && currentVolume < volumeMax && currentVolume * 10 <= 100)
                    tv_Info.setText("音量：" + currentVolume * 10);
                else
                    tv_Info.setText("音量：" + "100");
            } else if (type == 2) {
                tv_Info.setText("进度：0");
            } else {
                if ((int) (brightness * 100) > 100)
                    tv_Info.setText("亮度：100");
                else
                    tv_Info.setText("亮度：" + (int) (brightness * 100));
            }
        }
        requestLayout();
    }

    /**
     * 亮度调节
     */
    private float brightness = (float) 0.5;
    private float addNum = (float) 0.015;

    public void changeBrightness(int y) {
        if (y <= 0) {
            if (brightness + addNum <= 1.1)
                brightness += addNum;
        } else {
            if (brightness - addNum >= 0)
                brightness -= addNum;
        }
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.screenBrightness = brightness;
            window.setAttributes(lp);
            setInfoView(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
