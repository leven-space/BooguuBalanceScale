package com.leven.booguubalancescale.bluetooth.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.fragment.BleHomeFragment;

import org.apache.commons.lang3.StringUtils;

import me.yokeyword.fragmentation.SupportActivity;

public class BleActivity extends SupportActivity  implements BleHomeFragment.OnFragmentInteractionListener{

    private static final String TAG="BleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        if (savedInstanceState == null) {
            boolean blank = StringUtils.isNoneBlank("aa");
            Log.i(TAG, "onCreate: "+blank);
            loadRootFragment(R.id.fl_container, BleHomeFragment.newInstance("aa","bb"));
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
