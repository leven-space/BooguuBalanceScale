package com.leven.booguubalancescale.train.fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.train.view.BallView;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;


public class TrainResultFragment extends SupportFragment implements View.OnClickListener {

    private BallView ballView;
    private PieChart pieInstabilityChart;
    private PieChart pieDirectionChart;
    private PieChart pieFallingChart;
    private ImageButton btnBackHome;

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
        bindView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {
        ballView = (BallView) rootView.findViewById(R.id.ballView_train_result);
        ballView.bringToFront();
        btnBackHome = (ImageButton) rootView.findViewById(R.id.btn_train_result_back_home);
        btnBackHome.setOnClickListener(this);
        pieInstabilityChart = (PieChart) rootView.findViewById(R.id.pie_train_result_instability);
        pieDirectionChart = (PieChart) rootView.findViewById(R.id.pie_train_result_direction);
        pieFallingChart= (PieChart) rootView.findViewById(R.id.pie_train_result_falling);
        setData(pieInstabilityChart);
        setData(pieDirectionChart);
        setData(pieFallingChart);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_train_result_back_home:
                goHome();
                break;
        }
    }

    private void setData(PieChart pieChart) {
        int count = 2;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(45, i));
            xVals.add(i + "");
        }
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);
        // dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("45bce3"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(xVals, dataSet);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true); //绘制中心空白
        pieChart.setHoleRadius(70f);//中心大小
        pieChart.setHoleColor(Color.WHITE);//中心空白颜色
        pieChart.setDrawSliceText(true); //显示中心文字
        pieChart.setRotationEnabled(false);//是否旋转
        pieChart.setTouchEnabled(false);//是否可以点击
        pieChart.getLegend().setEnabled(false);
        pieChart.setDescription("");
        pieChart.setNoDataText("");
        pieChart.highlightValues(null);
        pieChart.setMaxAngle(180f); // HALF CHART
        pieChart.setRotationAngle(180f);
        pieChart.invalidate();

    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }




    private void goHome() {
        popTo(HomeFragment.class, false);
    }


}
