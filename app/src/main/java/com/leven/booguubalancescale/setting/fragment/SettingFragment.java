package com.leven.booguubalancescale.setting.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.leven.booguubalancescale.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 *
 */
public class SettingFragment extends SupportFragment implements View.OnClickListener {

    private BootstrapButton btnQuit;
    private BootstrapButton btnSetting;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_setting, container, false);
        BootstrapButton btnSetting = (BootstrapButton) rootView.findViewById(R.id.btn_setting);
        BootstrapButton btnQuit = (BootstrapButton) rootView.findViewById(R.id.btn_quit);
        btnSetting.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_setting:
              //  loadRootFragment();
                break;
            case R.id.btn_quit:
                break;
        }
    }
}
