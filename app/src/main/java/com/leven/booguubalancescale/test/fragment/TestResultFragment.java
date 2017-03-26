package com.leven.booguubalancescale.test.fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leven.booguubalancescale.BuildConfig;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.test.pojo.PointEntity;
import com.leven.booguubalancescale.test.pojo.ResultEntity;
import com.leven.booguubalancescale.test.util.CalculationUtil;
import com.leven.booguubalancescale.test.util.MyColorTemplate;
import com.leven.booguubalancescale.test.view.PathView;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;


public class TestResultFragment extends SupportFragment implements View.OnClickListener {
    private static final String TAG = "TestResultFragment";
    private static final String ARG_PARAM = "param";
    private ResultEntity resultData;
    private PathView pathView;
    private PieChart pieInstabilityChart;
    private PieChart pieDirectionChart;
    private PieChart pieFallingChart;
    private ImageButton btnBackHome;
    private Button btnTestAgain;


    public TestResultFragment() {
        // Required empty public constructor
    }

    public static TestResultFragment newInstance(ResultEntity resultData) {
        TestResultFragment fragment = new TestResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, resultData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            resultData = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_train_result, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {
        FrameLayout flContraliner = (FrameLayout) rootView.findViewById(R.id.frameLayout_train_result);
        //初始化轨迹图
        pathView = new PathView(this.getContext(), getPoint());
        int params = ViewGroup.LayoutParams.MATCH_PARENT;
        flContraliner.addView(pathView, params);
        //初始化按钮
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

        double value = CalculationUtil.instability(resultData);
        if (BuildConfig.DEBUG) Log.d(TAG, "value:" + value);
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

        for (int c : MyColorTemplate.RESULT_PIE_COLORS) {
            colors.add(c);
        }

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
        pieInstabilityChart.setCenterText(generateCenterSpannableText("21%"));
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
        pieDirectionChart.setCenterText(generateCenterSpannableText("21%"));
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
        pieFallingChart.setCenterText(generateCenterSpannableText("21%"));
        pieFallingChart.invalidate();

    }


    private SpannableString generateCenterSpannableText(String value) {
        SpannableString s = new SpannableString(value);
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


    private ArrayList<PointEntity> getPoint() {
        ArrayList<PointEntity> list = new ArrayList<>();
        list.add(new PointEntity(100, 100));
        list.add(new PointEntity(100, 200));
        list.add(new PointEntity(200, 110));
        list.add(new PointEntity(23, 100));
        list.add(new PointEntity(100, 50));
        return list;
    }


}
