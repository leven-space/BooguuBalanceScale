package com.leven.booguubalancescale.train.fragment;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.train.view.BallView;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;


public class TrainFragment extends SupportFragment implements View.OnClickListener {

    private OnTrainFragmentInteractionListener mListener;
    private float sliceSpace = 1f;
    private ImageButton btnBackHome;
    private ImageButton btnCalibrate;
    private ImageButton btnSeeBall;
    private BootstrapButton btnStart;
    private PieChart pieChart2;//最外层
    private PieChart pieChart1;//中间层
    private PieChart pieChart0;//最里层
    private BallView ballView;

    public TrainFragment() {
        // Required empty public constructor
    }


    public static TrainFragment newInstance() {
        TrainFragment fragment = new TrainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_train, container, false);
        bindView(rootView);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrainFragmentInteractionListener) {
            mListener = (OnTrainFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_train_back_home:
                break;
            case R.id.btn_train_calibrate:
                break;
            case R.id.btn_train_start:
                start(TrainResultFragment.newInstance());
                break;
            case R.id.btn_train_see_ball:
                break;
        }
    }

    public interface OnTrainFragmentInteractionListener {

    }

    /**
     * 初始化视图组件
     */
    private void bindView(View rootView) {
        //按钮
        btnBackHome = (ImageButton) rootView.findViewById(R.id.btn_train_back_home);
        btnCalibrate = (ImageButton) rootView.findViewById(R.id.btn_train_calibrate);
        btnSeeBall = (ImageButton) rootView.findViewById(R.id.btn_train_see_ball);
        btnStart = (BootstrapButton) rootView.findViewById(R.id.btn_train_start);
        btnStart.setTextColor(ColorTemplate.rgb("17ABDC"));
        btnBackHome.setOnClickListener(this);
        btnCalibrate.setOnClickListener(this);
        btnSeeBall.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        //初始化小球位置
        ballView = (BallView) rootView.findViewById(R.id.ballView);
        ballView.bringToFront();
        //初始化圆环位置
        pieChart2 = (PieChart) rootView.findViewById(R.id.pieChart2);
        pieChart1 = (PieChart) rootView.findViewById(R.id.pieChart1);
        pieChart0 = (PieChart) rootView.findViewById(R.id.pieChart0);
        setData2();
        setData1();
        setData0();
    }

    private void setData2() {
        int count = 3;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(45, i));
            xVals.add(i + "");
        }
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(sliceSpace);
        // dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("45bce3"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(xVals, dataSet);
        pieChart2.setData(data);
        pieChart2.setDrawHoleEnabled(true); //绘制中心空白
        pieChart2.setHoleRadius(70f);//中心大小
        pieChart2.setHoleColor(ColorTemplate.rgb("ffffff"));//中心空白颜色
        pieChart2.setDrawSliceText(false); //显示中心文字
        pieChart2.setRotationEnabled(false);//是否旋转
        pieChart2.setTouchEnabled(false);//是否可以点击
        pieChart2.getLegend().setEnabled(false);
        pieChart2.setDescription("");
        pieChart2.setNoDataText("");
        pieChart2.highlightValues(null);
        pieChart2.invalidate();
    }


    private void setData1() {
        int count = 3;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(45, i));
            xVals.add(i + "");
        }
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(sliceSpace);
        //dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("6ccae9"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(xVals, dataSet);
        pieChart1.setData(data);
        pieChart1.setHoleColor(ColorTemplate.rgb("ffffff"));
        pieChart1.setDrawHoleEnabled(true);
        pieChart1.setDrawSliceText(false);
        pieChart1.setRotationEnabled(false);
        pieChart1.setTouchEnabled(false);
        pieChart1.setHoleRadius(55f);
        pieChart1.getLegend().setEnabled(false);
        pieChart1.setDescription("");
        pieChart1.setNoDataText("");
        pieChart1.highlightValues(null);
        pieChart1.invalidate();
    }

    private void setData0() {
        int count = 3;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(45, i));
            xVals.add(i + "");
        }
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(sliceSpace);
        // dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("94d8ef"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(xVals, dataSet);
        pieChart0.setData(data);
        pieChart0.setDrawHoleEnabled(true);
        pieChart0.setDrawSliceText(false);
        pieChart0.setRotationEnabled(false);
        pieChart0.setTouchEnabled(false);
        pieChart0.setHoleRadius(5f);
        pieChart0.getLegend().setEnabled(false);
        pieChart0.setDescription("");
        pieChart0.setNoDataText("");
        pieChart0.highlightValues(null);
        pieChart0.invalidate();
    }


}
