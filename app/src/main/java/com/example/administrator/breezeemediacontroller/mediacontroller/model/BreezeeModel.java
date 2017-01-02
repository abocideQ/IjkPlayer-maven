package com.example.administrator.breezeemediacontroller.mediacontroller.model;

import java.util.Map;

/**
 * 视频播放信息
 * Created by Breezee on 2017/01/02.
 */

public class BreezeeModel {

    //播放地址
    String url;

    //信息头
    Map<String, String> mapHeadData;

    //速度
    float speed = 1;

    //是否循环播放
    boolean looping;

    public BreezeeModel(String url, Map<String, String> mapHeadData, boolean loop, float speed) {
        this.url = url;
        this.mapHeadData = mapHeadData;
        this.looping = loop;
        this.speed = speed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getMapHeadData() {
        return mapHeadData;
    }

    public void setMapHeadData(Map<String, String> mapHeadData) {
        this.mapHeadData = mapHeadData;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
