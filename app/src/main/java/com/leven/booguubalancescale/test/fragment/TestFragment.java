package com.leven.booguubalancescale.test.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.mikephil.charting.BuildConfig;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leven.booguubalancescale.R;
import com.leven.booguubalancescale.bluetooth.service.BluetoothLeService;
import com.leven.booguubalancescale.common.StringConverterUtil;
import com.leven.booguubalancescale.home.fragment.HomeFragment;
import com.leven.booguubalancescale.test.pojo.DataEntity;
import com.leven.booguubalancescale.test.pojo.PieDataList;
import com.leven.booguubalancescale.test.pojo.PointEntity;
import com.leven.booguubalancescale.test.pojo.ResultEntity;
import com.leven.booguubalancescale.test.view.BallView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;


public class TestFragment extends SupportFragment implements View.OnClickListener {
    private static final String TAG = "TrainFragment";
    private static final int TAG_VALUE_LENGTH = 22;
    public static final int COUNT_DOWN_SECOND = 10;


    private ArrayList<String> dataList;
    public int CENTER_OFFSET = 450;
    private int xOffset = 0;
    private int yOffset = 0;
    private int xPoint;
    private int yPoint;
    private float xg;
    private float yg;
    private OnTrainFragmentInteractionListener mListener;
    private boolean isConnected = true;
    private ResultEntity resultData;
    private boolean isCurrentFragment;
    private float sliceSpace = 1f;
    private boolean canGoBack = true;
    private StringBuilder tempData;//保存坐标点
    private ImageButton btnBackHome;
    private ImageButton btnCalibrate;
    private ImageButton btnSeeBall;
    private BootstrapButton btnStart;
    private PieChart pieChart2;//最外层
    private PieChart pieChart1;//中间层
    private PieChart pieChart0;//最里层
    private BallView ballView;

    public TestFragment() {
        // Required empty public constructor
    }


    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
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
        resultData = new ResultEntity();
        tempData = new StringBuilder();
        dataList = new ArrayList<>();
        return rootView;
    }

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
        CENTER_OFFSET = (int) ballView.getCenterXY();
        //初始化圆环位置
        pieChart2 = (PieChart) rootView.findViewById(R.id.pieChart2);
        pieChart1 = (PieChart) rootView.findViewById(R.id.pieChart1);
        pieChart0 = (PieChart) rootView.findViewById(R.id.pieChart0);
        initBackground();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrainFragmentInteractionListener) {
            mListener = (OnTrainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getContext().unregisterReceiver(mGattUpdateReceiver);
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        isCurrentFragment = true;
        // todo,当该Fragment对用户可见时
        canGoBack = true;
        btnCalibrate.setEnabled(true);
        btnStart.setEnabled(true);
        btnStart.setText(R.string.btn_start);
        if (com.leven.booguubalancescale.BuildConfig.DEBUG)
            Log.d(TAG, "isConnected:" + isConnected);

        if (isConnected) {
            mListener.sendBeginCollectDataCmd();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        isCurrentFragment = false;
        if (com.leven.booguubalancescale.BuildConfig.DEBUG)
            Log.d(TAG, "isConnected:" + isConnected);
        // todo,当该Fragment对用户不可见时
        mListener.sendStopCollectDataCmd();
    }

    @Override
    public boolean onBackPressedSupport() {
        // 默认flase，继续向上传递
        return !canGoBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_train_back_home:
                goHome();
                break;
            case R.id.btn_train_calibrate:
                calibrate();
                break;
            case R.id.btn_train_start:
                goStart();
                break;
            case R.id.btn_train_see_ball:
                if (View.VISIBLE == ballView.getVisibility()) {
                    ballView.setVisibility(View.INVISIBLE);
                    btnSeeBall.setImageResource(R.mipmap.ball_hide);
                } else {
                    ballView.setVisibility(View.VISIBLE);
                    btnSeeBall.setImageResource(R.mipmap.ball_see);
                }
                break;
        }
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //successful
                Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //Disconnect
                Log.i(TAG, "onReceive: disconnected");
                isConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //do work
            {
                Log.e(TAG, "In what we need");
                isConnected = true;
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //Receive Date
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                convertData(data);
            }
        }
    };

    /**
     * 组装蓝牙传输参数
     *
     * @param data
     * @return
     */
    private ArrayList<String> assembleData(String data) {
        ArrayList<String> values = new ArrayList<>();
        if (StringUtils.isNotEmpty(tempData)) {
            int needLength = TAG_VALUE_LENGTH - tempData.length();
            String needStr = StringUtils.substring(data, 0, needLength);
            String tag = StringUtils.substring(data, needLength, needLength + 2);
            if (!StringUtils.equalsAny(tag, "C5", "C6")) {
                Log.e(TAG, "convertData: 错误的传输数据");
            } else {
                tempData.append(needStr);
                values.add(tempData.toString());
            }
            tempData.delete(0, tempData.length());
        }

        if (StringUtils.containsAny(data, "C5", "C6")) {
            int startIndex = StringUtils.indexOfAny(data, "C5", "C6");
            int endIndex = startIndex + TAG_VALUE_LENGTH;
            if (startIndex <= 20 && data.length() >= 20) {
                String value = StringUtils.substring(data, startIndex, endIndex);
                values.add(value);
                String surplusStr = StringUtils.substring(data, endIndex);
                assembleData(surplusStr);
            } else {
                String partValue = StringUtils.substring(data, startIndex);
                tempData.append(partValue);
            }
        }
        return values;
    }

    private void convertData(String data) {
        ArrayList<String> tempDataList = assembleData(data);
        for (String t : tempDataList) {
            String xStr = StringUtils.substring(t, 10, 14);
            String yStr = StringUtils.substring(t, 14, 18);
            String zStr = StringUtils.substring(t, 18, 22);
            try {
                short x = ((short) StringConverterUtil.hexToInteger(xStr));
                short y = (short) StringConverterUtil.hexToInteger(yStr);
                short z = (short) StringConverterUtil.hexToInteger(zStr);
                float xg = Float.valueOf(x) / 16384;
                float yg = Float.valueOf(y) / 16384;
                float zg = Float.valueOf(z) / 16384;
                if (!canGoBack) {
                    resultData.add(new DataEntity(xg, yg, zg));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (dataList.size() < 20) {
            dataList.addAll(tempDataList);
        } else {
            short sumX = 0;
            short sumY = 0;
            for (String t : dataList) {
                try {
                    String xStr = StringUtils.substring(t, 10, 14);
                    String yStr = StringUtils.substring(t, 14, 18);
                    short x = ((short) StringConverterUtil.hexToInteger(xStr));
                    short y = (short) StringConverterUtil.hexToInteger(yStr);
                    xg = Float.valueOf(x) / 16384;
                    yg = Float.valueOf(y) / 16384;
                    sumX += xg * 5000;
                    sumY += yg * 5000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int len = dataList.size();
            xPoint = (sumX / len) + CENTER_OFFSET;
            yPoint = (sumY / len) + CENTER_OFFSET;
            dataList.clear();
            int tempX = xPoint - xOffset;
            int tempY = yPoint - yOffset;
            ballView.move(tempX, tempY);
            if (!canGoBack) {
                resultData.add(new PointEntity(tempX, tempY));
            }
        }

    }


    private static IntentFilter makeGattUpdateIntentFilter() {                        //Register received event
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public interface OnTrainFragmentInteractionListener {

        void sendBeginCollectDataCmd();

        void sendStopCollectDataCmd();


    }


    private void startCountDownTime(long second) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
        CountDownTimer timer = new CountDownTimer(second * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                float ss = millisUntilFinished / 1000;
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "onTick  " + millisUntilFinished / 1000);
                String text = Float.valueOf(ss).intValue() + "";
                if (com.leven.booguubalancescale.BuildConfig.DEBUG) Log.d(TAG, text);
                TestFragment.this.btnStart.setText(text);
                TestFragment.this.btnStart.setEnabled(false);
            }

            @Override
            public void onFinish() {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "onFinish -- 倒计时结束");
                this.cancel();
                btnStart.setText("0");
                TestFragment.this.start(TestResultFragment.newInstance(resultData), SupportFragment.SINGLETOP);
                resultData = new ResultEntity();
            }
        };
        timer.start();// 开始计时
    }

    private void goHome() {
        popTo(HomeFragment.class, false);
    }

    private void calibrate() {
        xOffset = xPoint - CENTER_OFFSET;
        yOffset = yPoint - CENTER_OFFSET;
        resultData.setyPoint(yg);
        resultData.setxPoint(xg);
        //ballView.calibrate();
    }

    private void goStart() {
        //开始倒计时
        btnStart.setText("5");
        startCountDownTime(COUNT_DOWN_SECOND);
        //设置返回键不可用
        canGoBack = false;
        //设置按钮无效
        btnCalibrate.setEnabled(false);
    }


    private void initBackground() {
        int count = 3;
        PieDataList entity = new PieDataList();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(45, i));
            xVals.add(i + "");
        }
        entity.setyVals1(yVals1);
        entity.setxVals(xVals);
        setData2(entity);
        setData1(entity);
        setData0(entity);

    }

    ;

    private void setData2(PieDataList entity) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        PieDataSet dataSet = new PieDataSet(entity.getyVals1(), "");
        dataSet.setSliceSpace(sliceSpace);
        // dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("45bce3"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(entity.getxVals(), dataSet);
        pieChart2.setData(data);
        pieChart2.setDrawHoleEnabled(true); //绘制中心空白
        pieChart2.setHoleRadius(70f);//中心大小
        pieChart2.setHoleColor(Color.WHITE);//中心空白颜色
        pieChart2.setDrawSliceText(false); //显示中心文字
        pieChart2.setRotationEnabled(false);//是否旋转
        pieChart2.setTouchEnabled(false);//是否可以点击
        pieChart2.getLegend().setEnabled(false);
        pieChart2.setDescription("");
        pieChart2.setNoDataText("");
        pieChart2.highlightValues(null);
        pieChart2.invalidate();
    }


    private void setData1(PieDataList entity) {
        int count = 3;
        ArrayList<Integer> colors = new ArrayList<Integer>();
        PieDataSet dataSet = new PieDataSet(entity.getyVals1(), "");
        dataSet.setSliceSpace(sliceSpace);
        //dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("6ccae9"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(entity.getxVals(), dataSet);
        pieChart1.setData(data);
        pieChart1.setHoleColor(Color.WHITE);
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

    private void setData0(PieDataList entity) {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        PieDataSet dataSet = new PieDataSet(entity.getyVals1(), "");
        dataSet.setSliceSpace(sliceSpace);
        // dataSet.setSelectionShift(1f);
        colors.add(ColorTemplate.rgb("94d8ef"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(entity.getxVals(), dataSet);
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
