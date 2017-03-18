package com.leven.booguubalancescale.bluetooth.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.adapter.LeDeviceListAdapter;

import me.yokeyword.fragmentation.SupportFragment;


public class BluetoothFragment extends SupportFragment {
    private static final String TAG = "BluetoothFragment";
    public static final String DEVICE_ADDRESS="DeviceAddress";
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    //View
    private ListView lv_devices;

    // Hander
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // Notify change
                    mLeDeviceListAdapter.notifyDataSetChanged();
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
        mLeDeviceListAdapter = new LeDeviceListAdapter(getActivity().getLayoutInflater());
        lv_devices = (ListView) rootView.findViewById(R.id.lv_devices);
        lv_devices.setAdapter(mLeDeviceListAdapter);
        lv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                String deviceAddress=device.getAddress();
                Log.i(TAG, "choose device: "+deviceAddress);
                setResult(deviceAddress);
                pop();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }

    @Override
    public void onStart() {
        super.onStart();
        scanLeDevice(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setResult(String address){
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ADDRESS, address);
        setFragmentResult(RESULT_OK,bundle);
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mLeDeviceListAdapter.clear();
            mHandler.sendEmptyMessage(1);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

}
