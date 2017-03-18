package com.leven.booguubalancescale;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.leven.booguubalancescale.bluetooth.fragment.BluetoothFragment;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;
import com.leven.booguubalancescale.common.CmdUtil;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.setting.fragment.SettingFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SupportActivity implements HomeFragment.OnHomeFragmentInteractionListener,BluetoothFragment.OnBluetoothFragmentInteractionListener {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public final static String ACTION_AUTO_CONNECT_FAILE =
            "com.example.bluetooth.le.ACTION_AUTO_CONNECT_FAIL";
    public final static String ACTION_AUTO_CONNECT_SUCCESS =
            "com.example.bluetooth.le.ACTION_AUTO_CONNECT_SUCCESS";
    private static String TAG = "MainActivity";
    private BluetoothLeService mBluetoothLeService;
    private boolean isSupportDevice = true;

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
            boolean flag=autoConnectDevice();
            if(flag){
                broadcastUpdate(ACTION_AUTO_CONNECT_SUCCESS);
            }else {
                broadcastUpdate(ACTION_AUTO_CONNECT_FAILE);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private boolean autoConnectDevice() {
        return false;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            isSupportDevice = false;
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            isSupportDevice = false;
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
        }

        //绑定蓝牙服务
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        boolean bindService = this.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "Try to bindService=" + bindService);

        //跳转主页
        if (savedInstanceState == null) {
            setDefaultFragmentBackground(R.color.main_background_color);

            loadRootFragment(R.id.app_main_container, HomeFragment.newInstance());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public boolean connectDevice(String address) {
        return mBluetoothLeService.connect(address);
    }

    @Override
    public void getAmount() {
        mBluetoothLeService.writeValue(CmdUtil.collectedAmount);
    }

    @Override
    public void getData() {
        mBluetoothLeService.writeValue(CmdUtil.collectedData);

    }

    @Override
    public boolean isSupportDevice() {
        return isSupportDevice;
    }
}
