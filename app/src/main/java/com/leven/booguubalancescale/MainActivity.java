package com.leven.booguubalancescale;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.leven.booguubalancescale.bluetooth.fragment.BluetoothFragment;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;
import com.leven.booguubalancescale.common.CmdUtil;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.test.fragment.TestFragment;
import com.pixplicity.easyprefs.library.Prefs;

import org.apache.commons.lang3.StringUtils;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity implements HomeFragment.OnHomeFragmentInteractionListener,
        BluetoothFragment.OnBluetoothFragmentInteractionListener, TestFragment.OnTrainFragmentInteractionListener {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String DEFAULT_DEVICE = "00:0E:16:00:05:67";
    private static final int REQUEST_ENABLE_BT = 0x2;
    private static final String TAG = "MainActivity";
    public final static String ACTION_AUTO_CONNECT_FAILE =
            "com.example.bluetooth.le.ACTION_AUTO_CONNECT_FAIL";
    public final static String ACTION_AUTO_CONNECT_SUCCESS =
            "com.example.bluetooth.le.ACTION_AUTO_CONNECT_SUCCESS";
    private BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isSupportDevice = true;
    private boolean isOpenBluetooth = true;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            boolean flag = autoConnectDevice();
            if (false) {
                broadcastUpdate(ACTION_AUTO_CONNECT_SUCCESS);
            } else {
                broadcastUpdate(ACTION_AUTO_CONNECT_FAILE);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private boolean autoConnectDevice() {
        String address = Prefs.getString("mac", null);
        if (StringUtils.isEmpty(address)) {
            return false;
        }
        try {
            mBluetoothLeService.connect(address);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            isSupportDevice = false;
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
        }

        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //绑定蓝牙服务
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        boolean bindService = this.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "Try to bindService=" + bindService);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_CANCELED == resultCode && requestCode == REQUEST_ENABLE_BT) {
            isOpenBluetooth = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void sendAmountCmd() {
        mBluetoothLeService.writeValue(CmdUtil.collectedAmount);

    }

    @Override
    public void sendBeginCollectDataCmd() {
        mBluetoothLeService.writeValue(CmdUtil.collectedData);
    }

    @Override
    public void sendStopCollectDataCmd() {
        if (BuildConfig.DEBUG) Log.d(TAG, "停止采样");
        mBluetoothLeService.writeValue(CmdUtil.stopCollectedData);
        // mBluetoothLeService.close();
        String address = Prefs.getString("mac", DEFAULT_DEVICE);
        //mBluetoothLeService.connect(address);
    }


    @Override
    public boolean isOpenBluetooth() {
        return isOpenBluetooth;
    }

    @Override
    public boolean isSupportDevice() {
        return isSupportDevice;
    }


}
