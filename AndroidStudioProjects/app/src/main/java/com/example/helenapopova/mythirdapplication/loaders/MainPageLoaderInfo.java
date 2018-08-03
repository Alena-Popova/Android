package com.example.helenapopova.mythirdapplication.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.helenapopova.mythirdapplication.BuildConfig;

public class MainPageLoaderInfo extends AsyncTaskLoader<String> {
    private final String TAG = "LoaderInfo";
    public static final String OUT_STR = "ftdi_answer";
    public static final String TEMP = "get_temp";
    public static final String FTAGMENT_SETTINFS = "f_settings";

    private boolean settings;

    public MainPageLoaderInfo(Context context, Bundle args) {
        super(context);
        if (args != null)
            settings = args.getBoolean(FTAGMENT_SETTINFS);
    }


    @Override
    public String loadInBackground() {
        if (!settings) {
            return null;
        }
        outputLogs("loadInBackground");
        return "load";
    }

    @Override
    public void forceLoad() {
        outputLogs("forceLoad");
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        outputLogs("onStartLoading");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        outputLogs("onStopLoading");
    }

    @Override
    public void deliverResult(String data) {
        outputLogs("deliverResult");
        super.deliverResult(data);
    }


    public void outputLogs(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
}
