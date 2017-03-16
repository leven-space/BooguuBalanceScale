package com.leven.booguubalancescale;

import android.os.Bundle;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    private static String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    private void bindViews() {

    }



}
