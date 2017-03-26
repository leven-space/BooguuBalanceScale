package com.leven.booguubalancescale.test.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
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
import com.leven.booguubalancescale.test.pojo.ResultEntity;
import com.leven.booguubalancescale.test.util.CalculationUtil;
import com.leven.booguubalancescale.test.util.MyColorTemplate;
import com.leven.booguubalancescale.test.view.PathView;

import java.text.NumberFormat;
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
    private FrameLayout flContraliner;
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
        Log.d(TAG, "bindView: 绑定视图");
        flContraliner = (FrameLayout) rootView.findViewById(R.id.frameLayout_train_result);
        //初始化轨迹图
        pathView = new PathView(this.getContext(), resultData.getPointEntities());
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

        double instabilityValue = CalculationUtil.instability(resultData);
        instabilityChart(instabilityValue);

        double directionValue = CalculationUtil.direction(resultData);
        directionChart(directionValue);

        double fallingValue = CalculationUtil.falling(instabilityValue, directionValue);
        fallingChart(fallingValue);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "instabilityValue:" + instabilityValue + "sdfs" + directionValue + "sdfs" + fallingValue);


    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        // todo,当该Fragment对用户不可见时
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "bindView: 销毁视图");
        super.onDestroyView();
        resultData = null;


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


    private void instabilityChart(double value) {
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        Double xPercent;
        if (value <= 35) {
            xPercent = Math.ceil(100 - (25 / 35) * value);

            for (int c : MyColorTemplate.RESULT_PIE_LOW) {
                colors.add(c);
            }
        } else if (value > 35 && value <= 65) {
            xPercent = Math.ceil(145 - 2 * value);
            for (int c : MyColorTemplate.RESULT_PIE_MID) {
                colors.add(c);
            }
        } else {
            xPercent = Math.ceil(15 - ((15 / 35) * (value - 65)));
            for (int c : MyColorTemplate.RESULT_PIE_HIGH) {
                colors.add(c);
            }
        }
        //赋值
        float temp = (float) value;
        yVals1.add(new Entry(temp, 0));
        xVals.add(0 + "");
        yVals1.add(new Entry((100 - temp), 0));
        xVals.add(1 + "");

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);
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
        pieInstabilityChart.setDrawSliceText(false);//隐藏x值
        pieInstabilityChart.getLegend().setEnabled(false);
        pieInstabilityChart.setDescription("");
        pieInstabilityChart.setNoDataText("");
        pieInstabilityChart.highlightValues(null);
        pieInstabilityChart.setMaxAngle(240f); // HALF CHART
        pieInstabilityChart.setRotationAngle(150f);
        pieInstabilityChart.setDrawCenterText(true);
        pieInstabilityChart.setCenterText(generateCenterSpannableText((int) value + ""));
        pieInstabilityChart.invalidate();

    }

    private void directionChart(double value) {
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        if (value <= 3.5) {
            for (int c : MyColorTemplate.RESULT_PIE_LOW) {
                colors.add(c);
            }
        } else if (value > 3.5 && value <= 6.5) {
            for (int c : MyColorTemplate.RESULT_PIE_MID) {
                colors.add(c);
            }
        } else {
            for (int c : MyColorTemplate.RESULT_PIE_HIGH) {
                colors.add(c);
            }
        }

        //赋值
        float temp = (float) value * 10;
        yVals1.add(new Entry(temp, 0));
        xVals.add(0 + "");
        yVals1.add(new Entry((100 - temp), 0));
        xVals.add(1 + "");

        NumberFormat ft = NumberFormat.getInstance();
        ft.setMaximumFractionDigits(1);
        String strValue = ft.format(value);

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
        pieDirectionChart.setDrawSliceText(false);//隐藏x值
        pieDirectionChart.setDescription("");
        pieDirectionChart.setNoDataText("");
        pieDirectionChart.highlightValues(null);
        pieDirectionChart.setMaxAngle(240f); // HALF CHART
        pieDirectionChart.setRotationAngle(150f);
        pieDirectionChart.setCenterText(generateCenterSpannableText(strValue));
        pieDirectionChart.invalidate();

    }

    private void fallingChart(double value) {
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<>();

        if (value <= 35) {
            for (int c : MyColorTemplate.RESULT_PIE_LOW) {
                colors.add(c);
            }
        } else if (value > 35 && value <= 65) {
            for (int c : MyColorTemplate.RESULT_PIE_MID) {
                colors.add(c);
            }
        } else {
            for (int c : MyColorTemplate.RESULT_PIE_HIGH) {
                colors.add(c);
            }
        }

        //赋值
        float temp = (float) value;
        yVals1.add(new Entry(temp, 0));
        xVals.add(0 + "");
        yVals1.add(new Entry((100 - temp), 0));
        xVals.add(1 + "");


        pieFallingChart.setCenterText(generateCenterSpannableText(Math.ceil(value) + "%"));

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
        pieFallingChart.setDrawSliceText(false);//隐藏x值
        pieFallingChart.getLegend().setEnabled(false);
        pieFallingChart.setDescription("");
        pieFallingChart.setNoDataText("");
        pieFallingChart.highlightValues(null);
        pieFallingChart.setMaxAngle(240f); // HALF CHART
        pieFallingChart.setRotationAngle(150f);
        pieFallingChart.invalidate();

    }


    private SpannableString generateCenterSpannableText(String value) {
        SpannableString ss = new SpannableString(value);
        ss.setSpan(new RelativeSizeSpan(2.5f), 0, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    private void goHome() {
        popTo(HomeFragment.class, false);
    }

    private void testAgain() {
        popTo(TestFragment.class, false);
    }


}
