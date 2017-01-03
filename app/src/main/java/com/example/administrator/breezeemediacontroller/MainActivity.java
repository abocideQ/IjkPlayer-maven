package com.example.administrator.breezeemediacontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.breezeemediacontroller.mediacontroller.video.SampleVideo;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);
        sampleVideo = (SampleVideo) findViewById(R.id.mSampleVideo);
        testMap = new HashMap<>();
        sampleVideo.setVideo(soucceMovie, testMap, true, 1);
    }
}
