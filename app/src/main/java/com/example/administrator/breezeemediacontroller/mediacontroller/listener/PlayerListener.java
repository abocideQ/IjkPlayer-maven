package com.example.administrator.breezeemediacontroller.mediacontroller.listener;

public interface PlayerListener {
    void onPrepared();

    void onAutoCompletion();//非循环情况下执行

    void onCompletion();

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onError(int what, int extra);

    void onInfo(int what, int extra);

    void onVideoSizeChanged();

    void onBackFullscreen();

    void onVideoPause();

    void onVideoResume();
}
