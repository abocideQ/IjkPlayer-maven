package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.administrator.breezeemediacontroller.mediacontroller.listener.MediaListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.PlayerListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.model.BreezeeModel;

import java.util.HashMap;
import java.util.Map;


/**
 * 关联播放器、播放器自身逻辑控制
 * Created by Breezee on 2017/1/2.
 */

public abstract class BreezeeBaseVideoPlayer extends FrameLayout implements PlayerListener, TextureView.SurfaceTextureListener {

    private String TAG = "BreezeeBaseVideoPlayer";
    private BreezeeTextureView textureView;
    private Surface surface;
    private MediaListener mediaListener;

    public static int screenType = 1; //当前屏幕状态，默认1为竖屏

    public static final int CURRENT_STATE_NORMAL = 0; //正常
    public static final int CURRENT_STATE_PREPAREING = 1; //准备中
    public static final int CURRENT_STATE_PLAYING = 2; //播放中
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3; //开始缓冲
    public static final int CURRENT_STATE_PAUSE = 5; //暂停
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6; //自动播放结束
    public static final int CURRENT_STATE_ERROR = 7; //错误状态
    public static int MEDIA_STATE;

    public BreezeeBaseVideoPlayer(Context context) {
        super(context);
    }

    public BreezeeBaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BreezeeBaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    * 1.设置播放源
    * */
    protected void setResource(String url, Map<String, String> map, boolean isLoop, float speed) {
        BreezeeVideoManager.instance().prepare(url, map, isLoop, speed);
    }

    /*
     * 2.添加TextureView
     **/
    protected void addTextureView(MediaListener mediaListener) {
        this.mediaListener = mediaListener;
        textureView = null;
        textureView = new BreezeeTextureView(getContext());
        textureView.setSurfaceTextureListener(this);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        textureView.setLayoutParams(layoutParams);
        addView(textureView);
    }


    public TextureView getTexture(){
        return textureView;
    }
    /*
    * 回调获取Surface
    * */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = new Surface(surface);
        BreezeeVideoManager.instance().setDisplay(this.surface);
        BreezeeVideoManager.instance().setListener(this);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        BreezeeVideoManager.instance().setDisplay(null);
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /*
    * PlayerListener
    * */
    @Override
    public void onPrepared() {
        MEDIA_STATE = CURRENT_STATE_PLAYING;
        mediaListener.setSeekBarMax((int) BreezeeVideoManager.instance().getMediaPlayer().getDuration());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (BreezeeVideoManager.instance().getMediaPlayer().isPlaying()) {
                        mediaListener.updateSeekBar();
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onAutoCompletion() {
        Log.e("PlayerListener", "-onAutoCompletion");
        MEDIA_STATE=CURRENT_STATE_AUTO_COMPLETE;
        BreezeeVideoManager.instance().getMediaPlayer().release();
        mediaListener.resetView();
    }

    @Override
    public void onCompletion() {
        Log.e("PlayerListener", "-onCompletion");

    }

    @Override
    public void onBufferingUpdate(int percent) {
        Log.e("PlayerListener", "-onBufferingUpdate");
    }

    @Override
    public void onSeekComplete() {
        Log.e("PlayerListener", "-onSeekComplete");
    }

    @Override
    public void onError(int what, int extra) {
        Log.e("PlayerListener", "-onError" + what + "----extra=" + extra);
    }

    @Override
    public void onInfo(int what, int extra) {
        Log.e("PlayerListener", "-onInfo" + what + "---extra=" + extra);
    }

    @Override
    public void onVideoSizeChanged() {
        Log.e("PlayerListener", "-onVideoSizeChanged");
        int mVideoWidth = BreezeeVideoManager.instance().getCurrentVideoWidth();
        int mVideoHeight = BreezeeVideoManager.instance().getCurrentVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            textureView.requestLayout();
        }
    }

    @Override
    public void onBackFullscreen() {
        Log.e("PlayerListener", "-onBackFullscreen");
    }

    @Override
    public void onVideoPause() {
        onPause();
    }

    @Override
    public void onVideoResume() {
        onResume();
    }

    /*
    * 播放位置
    * */
    private Long currentPosition;

    /*
    * 暂停
    * */
    public void onPause() {
        if (BreezeeVideoManager.instance().getMediaPlayer() != null && BreezeeVideoManager.instance().getMediaPlayer().isPlaying()) {
            currentPosition = BreezeeVideoManager.instance().getMediaPlayer().getCurrentPosition();
            BreezeeVideoManager.instance().getMediaPlayer().pause();
            MEDIA_STATE = CURRENT_STATE_PAUSE;
        }
    }

    /*
    * 恢复播放
    * */
    public void onResume() {
        if (MEDIA_STATE == CURRENT_STATE_PAUSE) {
            if (currentPosition > 0 && BreezeeVideoManager.instance().getMediaPlayer() != null) {
                MEDIA_STATE = CURRENT_STATE_PLAYING;
//                BreezeeVideoManager.instance().getMediaPlayer().seekTo(currentPosition);
                BreezeeVideoManager.instance().getMediaPlayer().start();
            }
        }
    }

    /*
    * 横竖屏处理
    * */
    public void onResolve(Activity activity, boolean showLand) {
        if (showLand) {
            screenType = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            screenType = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
