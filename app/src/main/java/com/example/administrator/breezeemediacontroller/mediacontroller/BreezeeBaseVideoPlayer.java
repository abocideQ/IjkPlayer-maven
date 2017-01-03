package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.administrator.breezeemediacontroller.mediacontroller.listener.PlayerListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Breezee on 2017/1/2.
 */

public abstract class BreezeeBaseVideoPlayer extends FrameLayout implements PlayerListener, TextureView.SurfaceTextureListener {

    private String TAG="BreezeeBaseVideoPlayer";
    private BreezeeTextureView textureView;
    private Surface surface;

    public BreezeeBaseVideoPlayer(Context context) {super(context);}

    public BreezeeBaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BreezeeBaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}

    /*
    * 1.设置播放源
    * */
    protected void setResource(String url,Map<String,String> map,boolean isLoop,float speed) {
        BreezeeVideoManager.instance().prepare(url, map, isLoop, speed);
    }

    /*
     * 2.添加TextureView
     **/
    protected void addTextureView() {
        textureView = null;
        textureView = new BreezeeTextureView(getContext());
        textureView.setSurfaceTextureListener(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(textureView);
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

    }

    @Override
    public void onAutoCompletion() {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {

    }

    @Override
    public void onInfo(int what, int extra) {

    }

    @Override
    public void onVideoSizeChanged() {

    }

    @Override
    public void onBackFullscreen() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoResume() {

    }
}
