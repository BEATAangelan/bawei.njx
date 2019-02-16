package com.example.dell.myapplication.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class Mapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
