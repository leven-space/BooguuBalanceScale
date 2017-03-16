package com.leven.booguubalancescale;

import android.os.Bundle;
import android.view.View;

import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.setting.fragment.SettingFragment;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    private static String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.app_main_container, SettingFragment.newInstance());  // 加载根Fragment
        }
    }



}
