package com.example.administrator.breezeemediacontroller.mediacontroller.listener;

/**
 * Created by Administrator on 2017/1/3.
 */

public interface ViewListener {
    void initOhterView();//添加，修改默认View以外
    void doLandView();//横屏操作
    void doPortView();//竖屏操作
    void playOhterVideo();//播放另外一条视频
}
