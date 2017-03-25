package com.leven.booguubalancescale.train.fragment;


import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.train.util.MyColorTemplate;
import com.leven.booguubalancescale.train.view.BallView;
import com.leven.booguubalancescale.train.view.PathView;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;


public class TrainResultFragment extends SupportFragment implements View.OnClickListener {

    private PathView pathView;
    private PieChart pieInstabilityChart;
    private PieChart pieDirectionChart;
    private PieChart pieFallingChart;
    private ImageButton btnBackHome;
    private Button btnTestAgain;

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
        pathView = (PathView) rootView.findViewById(R.id.ballView_train_result);
        pathView.bringToFront();
        btnBackHome = (ImageButton) rootView.findViewById(R.id.btn_train_result_back_home);
        btnBackHome.setOnClickListener(this);
        btnTestAgain = (Button) rootView.findViewById(R.id.btn_train_result_tast_again);
        btnTestAgain.setOnClickListener(this);
        pieInstabilityChart = (PieChart) rootView.findViewById(R.id.pie_train_result_instability);
        pieDirectionChart = (PieChart) rootView.findViewById(R.id.pie_train_result_direction);
        pieFallingChart = (PieChart) rootView.findViewById(R.id.pie_train_result_falling);
        ArrayList<Integer> colors = initPieColor();
        instabilityChart(colors);
        directionChart(colors);
        fallingChart(colors);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_train_result_back_home:
                goHome();
                break;
            case R.id.btn_train_result_tast_again:
                testAgain();
                break;
        }
    }


    private void instabilityChart(ArrayList<Integer> colors) {
        int count = 3;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(45, i));
            xVals.add(i + "");
        }
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);
        // dataSet.setSelectionShift(1f);
        for (int c : MyColorTemplate.RESULT_PIE_COLORS) {
            colors.add(c);
        }
        // colors.add(ColorTemplate.rgb("45bce3"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(xVals, dataSet);
        pieInstabilityChart.setData(data);
        pieInstabilityChart.setDrawHoleEnabled(true); //绘制中心空白
        pieInstabilityChart.setHoleRadius(90f);//中心大小
        pieInstabilityChart.setHoleColor(Color.WHITE);//中心空白颜色
        pieInstabilityChart.setDrawSliceText(true); //显示中心文字
        pieInstabilityChart.setRotationEnabled(false);//是否旋转
        pieInstabilityChart.setTouchEnabled(false);//是否可以点击
        pieInstabilityChart.getLegend().setEnabled(false);
        pieInstabilityChart.setDescription("");
        pieInstabilityChart.setNoDataText("");
        pieInstabilityChart.highlightValues(null);
        pieInstabilityChart.setMaxAngle(240f); // HALF CHART
        pieInstabilityChart.setRotationAngle(150f);
        pieInstabilityChart.invalidate();

    }

    private void directionChart(ArrayList<Integer> colors) {
        int count = 2;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
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
        pieDirectionChart.setData(data);
        pieDirectionChart.setDrawHoleEnabled(true); //绘制中心空白
        pieDirectionChart.setHoleRadius(90f);//中心大小
        pieDirectionChart.setHoleColor(Color.WHITE);//中心空白颜色
        pieDirectionChart.setDrawSliceText(true); //显示中心文字
        pieDirectionChart.setRotationEnabled(false);//是否旋转
        pieDirectionChart.setTouchEnabled(false);//是否可以点击
        pieDirectionChart.getLegend().setEnabled(false);
        pieDirectionChart.setDescription("");
        pieDirectionChart.setNoDataText("");
        pieDirectionChart.highlightValues(null);
        pieDirectionChart.setMaxAngle(240f); // HALF CHART
        pieDirectionChart.setRotationAngle(150f);
        pieDirectionChart.invalidate();


    }

    private void fallingChart(ArrayList<Integer> colors) {
        int count = 2;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
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
        pieFallingChart.setData(data);
        pieFallingChart.setDrawHoleEnabled(true); //绘制中心空白
        pieFallingChart.setHoleRadius(90f);//中心大小
        pieFallingChart.setHoleColor(Color.WHITE);//中心空白颜色
        pieFallingChart.setDrawSliceText(true); //显示中心文字
        pieFallingChart.setRotationEnabled(false);//是否旋转
        pieFallingChart.setTouchEnabled(false);//是否可以点击
        pieFallingChart.getLegend().setEnabled(false);
        pieFallingChart.setDescription("");
        pieFallingChart.setNoDataText("");
        pieFallingChart.highlightValues(null);
        pieFallingChart.setMaxAngle(240f); // HALF CHART
        pieFallingChart.setRotationAngle(150f);
        pieFallingChart.invalidate();


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

    private void testAgain() {
        pop();
    }

    /**
     * 初始化颜色值
     *
     * @return
     */
    private ArrayList<Integer> initPieColor() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : MyColorTemplate.RESULT_PIE_COLORS) {
            colors.add(c);
        }
        return colors;
    }


}
