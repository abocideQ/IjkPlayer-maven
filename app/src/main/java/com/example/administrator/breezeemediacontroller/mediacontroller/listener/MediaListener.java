package com.example.administrator.breezeemediacontroller.mediacontroller.listener;

import android.app.Activity;

/**
 * Created by Administrator on 2017/1/4.
 */

public interface MediaListener {
    void setSeekBarMax(int max);//seekBar设置
    void updateSeekBar();//更新进度条
    void resetView();//播放完后初始化View
    void bringViewsToFront();//让TopViewGoup和BottomViewGoup到顶层
    void setTotalTime(String time);
    void setPlayTime(String time);
    void isShowProgressBar(boolean isShow);
    void checkNetWork();
    void addErroView();
}
