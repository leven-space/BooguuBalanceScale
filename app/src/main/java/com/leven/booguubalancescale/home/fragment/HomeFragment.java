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
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.leven.booguubalancescale.BuildConfig;
import com.leven.booguubalancescale.MainActivity;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.fragment.BluetoothFragment;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;
import com.leven.booguubalancescale.common.StringConverterUtil;
import com.leven.booguubalancescale.test.fragment.TestFragment;
import com.pixplicity.easyprefs.library.Prefs;

import org.apache.commons.lang3.StringUtils;

import me.yokeyword.fragmentation.SupportFragment;


public class HomeFragment extends SupportFragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private static final String STSTUS_CONNECTED = "Connected";
    private static final String STSTUS_DISCONNECTED = "Disconnected";
    private static final int REQUEST_CHOOSE_BLE = 0x2;
    private HomeFragment.OnHomeFragmentInteractionListener homeInteractionListener;
    private BootstrapThumbnail bthumbHomeRounded;
    private ImageButton btnHomeTrain;
    private ImageButton btnHomeTest;
    private TextView tvBattery;
    private TextView tvStatus;
    private boolean isConnected;//是否连接
    private boolean isCurrentFragment;

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
        tvBattery = (TextView) rootView.findViewById(R.id.tv_home_battery_number);
        tvStatus = (TextView) rootView.findViewById(R.id.tv_home_connect_status);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home_train:
                homeInteractionListener.sendAmountCmd();
                break;
            case R.id.btn_home_test:
                start(TestFragment.newInstance());
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
            Log.i(TAG, "蓝牙地址: " + deviceAddress);
            if (StringUtils.isNotBlank(deviceAddress)) {
                Prefs.putString("mac", deviceAddress);
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
    public void onSupportVisible() {
        super.onSupportVisible();
        // todo,当该Fragment对用户可见时
        isCurrentFragment = true;
        if (isConnected) {
            homeInteractionListener.sendAmountCmd();
        }

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        isCurrentFragment = false;
        // todo,当该Fragment对用户不可见时

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
                isConnected = false;
                tvStatus.setText(HomeFragment.STSTUS_DISCONNECTED);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //do work
            {
                Log.e(TAG, "In what we need");
                //连接成功，并获取电量
                tvStatus.setText(HomeFragment.STSTUS_CONNECTED);
                isConnected = true;
                homeInteractionListener.sendAmountCmd();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //Receive Date
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.e(TAG, "RECV DATA:" + data);
                //设置电压值
                if (isCurrentFragment) {
                    setAmountData(data);
                }
            } else if (MainActivity.ACTION_AUTO_CONNECT_FAILE.equals(action)) {
                if (BuildConfig.DEBUG) Log.d(TAG, "start device search");
                startForResult(BluetoothFragment.newInstance(), REQUEST_CHOOSE_BLE);

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

        /**
         * 发送获取电量
         */
        public void sendAmountCmd();

        /**
         * 是否支持ble蓝牙设备
         *
         * @return true 支持 false 不支持
         */
        public boolean isSupportDevice();

        /**
         * 是否打开蓝牙
         *
         * @return true
         */
        public boolean isOpenBluetooth();

    }


    private void setAmountData(String data) {
        String full = "100%";
        String low = "5%";
        Float aFloat = null;
        try {
            aFloat = StringConverterUtil.hexToFloat(data);
        } catch (NumberFormatException e) {
            return;
        }

        if ("05".equals(data)) {
            return;
        }
        float amount = aFloat * 2;
        if (BuildConfig.DEBUG) Log.d(TAG, "amount:" + amount);
        if (amount > 4.2) {
            tvBattery.setText(full);
        } else if (amount < 3.0) {
            tvBattery.setText(low);
        } else {

            Double value = amount / 4.2 * 100;
            tvBattery.setText(value.intValue() + "%");
        }

    }


}
