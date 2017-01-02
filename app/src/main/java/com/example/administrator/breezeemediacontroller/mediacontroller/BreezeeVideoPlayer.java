package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.administrator.breezeemediacontroller.mediacontroller.listener.PlayerListener;


/**
 * Created by Breezee on 2017/1/2.
 */

public abstract class BreezeeVideoPlayer extends FrameLayout implements PlayerListener, TextureView.SurfaceTextureListener {

//    private BreezeeTextureView textureView;

//    protected ViewGroup mTextureViewContainer; //渲染控件父类


    public BreezeeVideoPlayer(Context context) {
        super(context);
    }
    public BreezeeVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BreezeeVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}

    /*
     * 添加播放的view
     **/
//    protected void addTextureView() {
//        if (mTextureViewContainer.getChildCount() > 0) {
//            mTextureViewContainer.removeAllViews();
//        }
//        textureView = null;
//        textureView = new BreezeeTextureView(getContext());
//        textureView.setSurfaceTextureListener(this);
//        textureView.setRotation(mRotate);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mTextureViewContainer.addView(textureView, layoutParams);
//    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

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
