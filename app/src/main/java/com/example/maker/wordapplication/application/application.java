package com.example.maker.wordapplication.application;

import android.app.Activity;
import android.app.Application;

import butterknife.ButterKnife;

public class application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       // ButterKnife.bind((Activity) getApplicationContext());
    }
}
