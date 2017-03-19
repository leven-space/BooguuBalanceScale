package com.leven.booguubalancescale.train.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leven.booguubalancescale.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainResultFragment extends SupportFragment {


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_train_result, container, false);
    }

}
