package com.leven.booguubalancescale.bluetooth.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leven.booguubalancescale.R;

import me.yokeyword.fragmentation.SupportFragment;


public class BluetoothFragment extends SupportFragment {

    public BluetoothFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ble_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
