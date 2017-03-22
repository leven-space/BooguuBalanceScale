package com.leven.booguubalancescale.train.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.train.view.BallView;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;


public class TrainResultFragment extends SupportFragment {

    private BallView ballView;

    public TrainResultFragment() {
        // Required empty public constructor
    }

    public static TrainResultFragment newInstance() {
        TrainResultFragment fragment = new TrainResultFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_train_result, container, false);
        ballView = (BallView) rootView.findViewById(R.id.ballView_train_result);
        ballView.drawPath(0, 0);
        ballView.drawPath(100, 100);
        return rootView;
    }


    private void goHome(){
        popTo(HomeFragment.class,false);
    }


}
