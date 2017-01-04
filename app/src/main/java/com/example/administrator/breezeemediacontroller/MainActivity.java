package com.example.administrator.breezeemediacontroller;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.breezeemediacontroller.mediacontroller.listener.ViewListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.video.SampleVideo;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ViewListener {
    private SampleVideo sampleVideo;
    //普通
    private String sourceNormal = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    //清晰
    private String sourceQuality = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
    //CCTV1综合
    private String sourceM3u8="http://58.135.196.138:8090/live/db3bd108e3364bf3888ccaf8377af077/index.m3u8";
    //cctv5体育
    private String source2M3u8="http://58.135.196.138:8090/live/6b9e3889ec6e2ab1a8c7bd0845e5368a/index.m3u8";
    //剪辑
    private String soucceMovie="http://baobab.wdjcdn.com/14564977406580.mp4";
    private Map<String, String> testMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sampleVideo = (SampleVideo) findViewById(R.id.mSampleVideo);
        testMap = new HashMap<>();
        //1.activity 2.除播放器以外View的处理 3.是否加载默认View
        sampleVideo.initView(this,this,true);
        //1.播放地址 2.播放源信息头 3.是否循环播放 4.播放速度
        sampleVideo.setVideo(soucceMovie, testMap, false, 1);
    }

    /*
    * 额外页面添加或修改
    * */
    @Override
    public void initOhterView() {

    }

    /*
    * 额外横屏处理-------------建议自行添加titlebar进行操作
    * */
    @Override
    public void doLandView() {
        setFullScreen(true);
        setStatusBarVisibility(true);
    }
    /*
    * 额外竖屏处理
    * */
    @Override
    public void doPortView() {
        setStatusBarVisibility(false);
        setFullScreen(false);
    }

    @Override
    public void playOhterVideo() {
        //1.播放地址 2.播放源信息头 3.是否循环播放 4.播放速度
        sampleVideo.setVideo(soucceMovie, testMap, true, 1);
    }


    /**
     * 1.设置是否全屏
     * @param enable
     */
    private void setFullScreen(boolean enable) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 2.透明状态栏，全屏不变，只在有全屏时有效
     * @param enable
     */
    private void setStatusBarVisibility(boolean enable) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取ActionBar高度
     *
     * @param activity activity
     * @return ActionBar高度
     */
    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }
}
