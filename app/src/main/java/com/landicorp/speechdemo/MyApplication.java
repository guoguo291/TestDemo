package com.landicorp.speechdemo;

import android.app.Application;

import com.landicorp.speechdemo.utils.SpeechUtils;

/**
 * Created by guoj on 2019/2/12.
 */

public class MyApplication extends Application {
    private SpeechUtils speechUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        speechUtils=SpeechUtils.getInstance(this);
    }
}
