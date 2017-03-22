package com.leven.booguubalancescale.bluetooth.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.adapter.LeDeviceListAdapter;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;

import org.apache.commons.lang3.StringUtils;

import me.yokeyword.fragmentation.SupportFragment;
import pl.droidsonroids.gif.GifImageView;


public class BluetoothFragment extends SupportFragment implements View.OnClickListener {
    private static final String TAG = "BluetoothFragment";
    public static final String DEVICE_ADDRESS = "DeviceAddress";
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private static final int MSG_DISCOVERY=0x1;
    private static final int MSG_FINISHED=0x2;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private BluetoothFragment.OnBluetoothFragmentInteractionListener blueInteractionListener;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    //View
    private ListView lv_devices;
    private ImageButton btnBleSearch;
    private GifImageView gifImageView;

    // Hander
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DISCOVERY: // Notify change
                    mLeDeviceListAdapter.notifyDataSetChanged();
                    break;
                case MSG_FINISHED:
                    stopSearch();
                    break;
            }
        }
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    BluetoothFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mHandler.sendEmptyMessage(1);
                        }
                    });
                }
            };

    public BluetoothFragment() {
        // Required empty public constructor
    }

    public static BluetoothFragment newInstance() {
        return new BluetoothFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ble_home, container, false);
        gifImageView = (GifImageView) rootView.findViewById(R.id.giv_ble_gif);
        btnBleSearch = (ImageButton) rootView.findViewById(R.id.btn_ble_search);
        mLeDeviceListAdapter = new LeDeviceListAdapter(getActivity());
        lv_devices = (ListView) rootView.findViewById(R.id.lv_devices);
        lv_devices.setAdapter(mLeDeviceListAdapter);
        btnBleSearch.setOnClickListener(this);
        gifImageView.setOnClickListener(this);
        return rootView;
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ble_search:
                startSearch();
                break;
            case R.id.giv_ble_gif:
                stopSearch();
                break;
        }
    }

    /**
     * 开始搜索
     */
    private void startSearch() {
        btnBleSearch.setVisibility(View.INVISIBLE);
        gifImageView.setVisibility(View.VISIBLE);
        scanLeDevice(true);

    }

    /**
     * 取消搜索
     */
    private void cancelSearch(){
        //  scanLeDevice(false);
    }

    /**
     * 结束搜索
     */
    private void stopSearch() {
        btnBleSearch.setVisibility(View.VISIBLE);
        gifImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BluetoothFragment.OnBluetoothFragmentInteractionListener) {
            blueInteractionListener = (BluetoothFragment.OnBluetoothFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        blueInteractionListener = null;
        this.getContext().unregisterReceiver(mGattUpdateReceiver);
    }

    private void setResult(String address) {
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ADDRESS, address);
        setFragmentResult(RESULT_OK, bundle);
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mHandler.sendEmptyMessage(MSG_FINISHED);
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mLeDeviceListAdapter.clear();
            mHandler.sendEmptyMessage(MSG_DISCOVERY);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {  //successful
                Log.e(TAG, "In what we need");
                Toast.makeText(BluetoothFragment.this.getActivity(), "连接成功", Toast.LENGTH_SHORT).show();
                pop();
            } else if (LeDeviceListAdapter.ACTION_CHOOSE_DEVICE_ADDRESS.equals(action)) {
                String deviceAddress = intent.getStringExtra(LeDeviceListAdapter.CHOOSE_DEVICE_ADDRESS);
                if (StringUtils.isNotBlank(deviceAddress)) {
                    Log.i(TAG, "choose device address" + deviceAddress);
                    connectDevice(deviceAddress);
                } else {
                    Toast.makeText(getActivity(), "蓝牙连接失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }else if (BluetoothLeService.ACTION_GATT_CLOSE.equals(action)){
                Toast.makeText(getActivity(), "设备已关闭，请重新搜索", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {                        //Register received event
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(LeDeviceListAdapter.ACTION_CHOOSE_DEVICE_ADDRESS);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CLOSE);
        return intentFilter;
    }

    /**
     * 连接设备，保存地址
     *
     * @param deviceAddress mac地址
     */
    private void connectDevice(String deviceAddress) {
        setResult(deviceAddress);
        blueInteractionListener.connectDevice(deviceAddress);
    }


    public interface OnBluetoothFragmentInteractionListener {

        /**
         * 连接设备
         *
         * @param address mac 地址
         * @return true 连接成功
         */
        public boolean connectDevice(String address);


    }

}
