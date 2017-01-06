package com.example.administrator.breezeemediacontroller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.breezeemediacontroller.mediacontroller.BreezeeVideoManager;
import com.example.administrator.breezeemediacontroller.mediacontroller.listener.ViewListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.video.SampleVideo;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ViewListener {
    private SampleVideo sampleVideo;
    private View toptileView;
    //普通
    private String sourceNormal = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    //清晰
    private String sourceQuality = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
    //CCTV1综合
    private String sourceM3u8 = "http://58.135.196.138:8090/live/db3bd108e3364bf3888ccaf8377af077/index.m3u8";
    //cctv5体育
    private String source2M3u8 = "http://58.135.196.138:8090/live/6b9e3889ec6e2ab1a8c7bd0845e5368a/index.m3u8";
    //剪辑
    private String soucceMovie = "http://baobab.wdjcdn.com/14564977406580.mp4";
    private Map<String, String> testMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明并沉浸(虚拟键透明不顶布局)
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏透明(布局向上顶替)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//虚拟键透明(布局向下顶替)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        setContentView(R.layout.activity_main);
        sampleVideo = (SampleVideo) findViewById(R.id.mSampleVideo);
        toptileView = findViewById(R.id.toptileView);
        //设置假状态栏高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toptileView.getLayoutParams();
            layoutParams.height = getStatusBarHeight();
            toptileView.setLayoutParams(layoutParams);
        }
        toptileView.setBackgroundColor(this.getResources().getColor(android.R.color.holo_red_dark));
        toptileView.requestLayout();
        testMap = new HashMap<>();
        //1.activity 2.除播放器以外View处理的回调 3.是否加载默认View
        sampleVideo.initView(this, this, true);
        //1.播放地址 2.播放源信息头 3.是否循环播放 4.播放速度
        sampleVideo.setVideo(soucceMovie, testMap, false, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sampleVideo.onPause();
        sampleVideo.doPauseView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BreezeeVideoManager.instance().releaseMediaPlayer();
    }


    /**
     * 初始化,额外页面添加或修改
     */
    @Override
    public void initOhterView() {

    }

    /**
     * 额外横屏处理------
     */
    @Override
    public void doLandView() {

    }

    /**
     * 额外竖屏处理
     */
    @Override
    public void doPortView() {

    }

    @Override
    public void playOhterVideo() {
        //1.播放地址 2.播放源信息头 3.是否循环播放 4.播放速度
        sampleVideo.setVideo(soucceMovie, testMap, true, 1);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (sampleVideo.SCREEN_STATE == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                sampleVideo.onResolve(this, false);
                sampleVideo.doPortView();
                return false;
            } else {
                sampleVideo.onPause();
                sampleVideo.doPauseView();
                this.finish();
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * 获取状态栏高
    * */
    public  int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
