package com.leven.booguubalancescale.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.leven.booguubalancescale.BuildConfig;
import com.leven.booguubalancescale.R;

import java.util.ArrayList;

/**
 * List Adapter
 * Created by thinkpad on 2017/3/18.
 */

public class LeDeviceListAdapter extends BaseAdapter {
    public static final String ACTION_CHOOSE_DEVICE_ADDRESS = "om.leven.booguubalancescale.bluetooth.adapter.ACTION_CHOOSE_DEVICE_ADDRESS";
    public static final String CHOOSE_DEVICE_NAME = "CHOOSE_DEVICE_NAME";
    public static final String CHOOSE_DEVICE_ADDRESS = "CHOOSE_DEVICE_ADDRESS";
    private static final String TAG = "LeDeviceListAdapter";
    private ArrayList<BluetoothDevice> mLeDevices;
    private FragmentActivity activity;
    private LayoutInflater mInflator;


    public LeDeviceListAdapter(FragmentActivity activity) {
        super();
        this.mLeDevices = new ArrayList<>();
        this.activity = activity;
        this.mInflator = activity.getLayoutInflater();

    }

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.btnListItem = (BootstrapButton) view.findViewById(R.id.btn_list_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final BluetoothDevice device = mLeDevices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.btnListItem.setText(deviceName);
        else
            viewHolder.btnListItem.setText(R.string.unknown_device);
        viewHolder.btnListItem.setBadgeText(device.getAddress());
        viewHolder.btnListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) Log.d(TAG, "device choose" + deviceName);
                Intent intent = new Intent(ACTION_CHOOSE_DEVICE_ADDRESS);
                intent.putExtra(CHOOSE_DEVICE_NAME, device.getName());
                intent.putExtra(CHOOSE_DEVICE_ADDRESS, device.getAddress());
                activity.sendBroadcast(intent);
            }
        });
        return view;
    }

    static class ViewHolder {
        BootstrapButton btnListItem;

    }
}
