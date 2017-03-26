package com.leven.booguubalancescale.test.pojo;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * BooguuBalanceScale
 *封装背景需要的饼图数据
 * @author leven.chen
 * @email chenlong_cl@foxmail.com
 * 2017/3/22.
 */

public class PieDataList {

    private ArrayList<Entry> yVals1 ;
    private ArrayList<String> xVals ;

    public PieDataList() {
        this.yVals1 = new ArrayList<Entry>();
        this.xVals  = new ArrayList<String>();
    }

    public ArrayList<Entry> getyVals1() {
        return yVals1;
    }

    public void setyVals1(ArrayList<Entry> yVals1) {
        this.yVals1 = yVals1;
    }

    public ArrayList<String> getxVals() {
        return xVals;
    }

    public void setxVals(ArrayList<String> xVals) {
        this.xVals = xVals;
    }
}
