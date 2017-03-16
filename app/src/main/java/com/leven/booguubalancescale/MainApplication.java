package com.leven.booguubalancescale;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * BooguuBalanceScale
 *
 * @author leven.chen
 * @email chenlong_cl@foxmail.com
 * 2017/3/16.
 */

public class MainApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
