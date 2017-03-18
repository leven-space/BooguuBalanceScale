package com.leven.booguubalancescale.home.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.fragment.BluetoothFragment;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;
import com.leven.booguubalancescale.setting.fragment.SettingFragment;

import org.apache.commons.lang3.StringUtils;

import me.yokeyword.fragmentation.SupportFragment;

import static android.content.Context.BIND_AUTO_CREATE;


public class HomeFragment extends SupportFragment {
    private static final String TAG="HomeFragment";
    private static final int REQUEST_ENABLE_BT = 0x1;
    private static final int REQUEST_CHOOSE_BLE = 0x2;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean autoConnFlag=true;
    private BluetoothLeService mBluetoothLeService;
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        return  rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        boolean isSupportDevice=true;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            isSupportDevice=false;
            Toast.makeText(this.getContext(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            isSupportDevice=false;
            Toast.makeText(context, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
        }
        if(isSupportDevice){
            Intent gattServiceIntent = new Intent(getContext(), BluetoothLeService.class);
            Log.d(TAG, "Try to bindService=" + this.getActivity().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
            context.registerReceiver(mGattUpdateReceiver,makeGattUpdateIntentFilter());
            //如果设备从未连接过蓝牙或自动连接失败，跳转到设备连接页面
            Log.d(TAG, "onAttach: 跳转蓝牙连接页面");
            autoConnFlag=false;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!autoConnFlag){
            startForResult(BluetoothFragment.newInstance(),REQUEST_CHOOSE_BLE);
        }

    }

    // 目标Fragment调用setFragmentResult()后，在其出栈时，会回调该方法
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_BLE && resultCode == RESULT_OK ) {
            // 在此通过Bundle data 获取返回的数据
            String deviceAddress=data.getString(BluetoothFragment.DEVICE_ADDRESS);
            if(StringUtils.isNotBlank(deviceAddress)){
                mBluetoothLeService.connect(deviceAddress);
            }else {
                Log.w(TAG, "onFragmentResult: 获取蓝牙地址失败" );
            }

        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.getActivity().unbindService(mServiceConnection);
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
        return intentFilter;
    }



}
