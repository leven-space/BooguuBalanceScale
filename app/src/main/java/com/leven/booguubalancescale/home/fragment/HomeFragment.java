package com.leven.booguubalancescale.home.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.leven.booguubalancescale.BuildConfig;
import com.leven.booguubalancescale.MainActivity;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.fragment.BluetoothFragment;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;
import com.leven.booguubalancescale.train.fragment.TrainFragment;

import org.apache.commons.lang3.StringUtils;

import me.yokeyword.fragmentation.SupportFragment;


public class HomeFragment extends SupportFragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private static final int REQUEST_CHOOSE_BLE = 0x2;
    private HomeFragment.OnHomeFragmentInteractionListener homeInteractionListener;
    private BootstrapThumbnail bthumbHomeRounded;
    private ImageButton btnHomeTrain;
    private ImageButton btnHomeTest;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        bthumbHomeRounded = (BootstrapThumbnail) rootView.findViewById(R.id.bthumb_home_rounded);
        bthumbHomeRounded.setRounded(true);
        bthumbHomeRounded.setBootstrapSize(DefaultBootstrapSize.SM);
        bthumbHomeRounded.setBorderDisplayed(false);
        btnHomeTest = (ImageButton) rootView.findViewById(R.id.btn_home_test);
        btnHomeTrain = (ImageButton) rootView.findViewById(R.id.btn_home_train);
        btnHomeTrain.setOnClickListener(this);
        btnHomeTest.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home_train:
                break;
            case R.id.btn_home_test:
                start(TrainFragment.newInstance());
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnHomeFragmentInteractionListener) {
            homeInteractionListener = (HomeFragment.OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        boolean isSupportDevice = homeInteractionListener.isSupportDevice();
        if (isSupportDevice) {
            context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // 目标Fragment调用setFragmentResult()后，在其出栈时，会回调该方法
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_BLE && resultCode == RESULT_OK) {
            // 在此通过Bundle data 获取返回的数据
            String deviceAddress = data.getString(BluetoothFragment.DEVICE_ADDRESS);
            if (StringUtils.isNotBlank(deviceAddress)) {
                //save address
            } else {
                Log.w(TAG, "onFragmentResult: 获取蓝牙地址失败");
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.homeInteractionListener = null;
        this.getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public boolean onBackPressedSupport() {
        // 默认flase，继续向上传递
        return super.onBackPressedSupport();
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //successful
                Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //Disconnect
                Log.i(TAG, "onReceive: disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //do work
            {
                Log.e(TAG, "In what we need");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //Receive Date
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.e(TAG, "RECV DATA:" + data);
            } else if (MainActivity.ACTION_AUTO_CONNECT_FAILE.equals(action)) {
                if (BuildConfig.DEBUG) Log.d(TAG, "start device search");
                start(BluetoothFragment.newInstance());
            } else if (MainActivity.ACTION_AUTO_CONNECT_SUCCESS.equals(action)) {

            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {                        //Register received event
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        intentFilter.addAction(MainActivity.ACTION_AUTO_CONNECT_FAILE);
        intentFilter.addAction(MainActivity.ACTION_AUTO_CONNECT_SUCCESS);
        return intentFilter;
    }


    public interface OnHomeFragmentInteractionListener {

        public void getAmount();

        public void getData();

        /**
         * 是否支持ble蓝牙设备
         *
         * @return true 支持 false 不支持
         */
        public boolean isSupportDevice();

        /**
         * 是否打开蓝牙
         * @return true
         */
        public boolean isOpenBluetooth();

    }


}
