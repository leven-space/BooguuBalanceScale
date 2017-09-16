package com.leven.booguubalancescale.test.util;

import android.util.Log;

import com.leven.booguubalancescale.test.pojo.DataEntity;
import com.leven.booguubalancescale.test.pojo.ResultEntity;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;

/**
 * 计算相关业务数据
 * Created by thinkpad on 2017/3/26.
 */

public class CalculationUtil {
    private static final String TAG = "CalculationUtil";

    /**
     * [(xi-xi-1)^2+(yi-yi-1)^2+(zi-zi-1)^2]/ T*10^4
     * <p>
     * T=5
     */
    public static int instability(ResultEntity resultEntity) {
        ArrayList<DataEntity> dataEntities = resultEntity.getDataEntities();
        double value = 0;
        int realNum;
        for (int i = 0, len = dataEntities.size(); i < len; i++) {
            if (i == 0) {
                continue;
            }
            DataEntity current = dataEntities.get(i);
            DataEntity last = dataEntities.get(i - 1);
            value += Math.sqrt(Math.pow((current.getXg() - last.getXg()), 2) + Math.pow((current.getYg() - last.getYg()), 2));
        }
        Log.i(TAG, "instability: " + value);
        if (value < 4.5) {
            realNum = 0;
        } else if (4.5 <= value && value <= 24.5) {
            realNum = Double.valueOf(Math.ceil((value - 4.5) * 5)).intValue();
        } else {
            realNum = 100;
        }

        Log.i(TAG, "instability: " + realNum);
        return realNum;

    }


    public static double direction(ResultEntity resultEntity) {
        ArrayList<DataEntity> dataEntities = resultEntity.getDataEntities();
        float xg = resultEntity.getxPoint();
        float yg = resultEntity.getyPoint();

        double sumX1 = 0;
        double sumY1 = 0;
        int len1 = 0;

        double sumX2 = 0;
        double sumY2 = 0;
        int len2 = 0;

        double sumX3 = 0;
        double sumY3 = 0;
        int len3 = 0;

        double sumX4 = 0;
        double sumY4 = 0;
        int len4 = 0;

        for (int i = 0, len = dataEntities.size(); i < len; i++) {
            DataEntity point = dataEntities.get(i);
            double x = point.getXg();
            double y = point.getYg();
            if (x > xg && y > yg) {
                sumX1 += x;
                sumY1 += y;
                len1++;
            } else if (x < xg && y > yg) {
                sumX2 += x;
                sumY2 += y;
                len2++;
            } else if (x < xg && y < yg) {
                sumX3 += x;
                sumY3 += y;
                len3++;
            } else if (x > xg && y < yg) {
                sumX4 += x;
                sumY4 += y;
                len4++;
            }
        }
        sumX1 = average(sumX1, len1);
        sumY1 = average(sumY1, len1);

        sumX2 = average(sumX2, len2);
        sumY2 = average(sumY2, len2);

        sumX3 = average(sumX3, len3);
        sumY3 = average(sumY3, len3);

        sumX4 = average(sumX4, len4);
        sumY4 = average(sumY4, len4);

        Log.d(TAG, "direction: " + sumX1 + ": " + sumY1);
        Log.d(TAG, "direction: " + sumX2 + ": " + sumY2);
        Log.d(TAG, "direction: " + sumX3 + ": " + sumY3);
        Log.d(TAG, "direction: " + sumX4 + ": " + sumY4);

        double value1 = Math.sqrt((Math.pow(sumX1, 2) + Math.pow(sumY1, 2)));
        double value2 = Math.sqrt((Math.pow(sumX2, 2) + Math.pow(sumY2, 2)));
        double value3 = Math.sqrt((Math.pow(sumX3, 2) + Math.pow(sumY3, 2)));
        double value4 = Math.sqrt((Math.pow(sumX4, 2) + Math.pow(sumY4, 2)));


        double sum = value1 + value2 + value3 + value4;
        value1 = value1 / sum;
        value2 = value2 / sum;
        value3 = value3 / sum;
        value4 = value4 / sum;


        double averageNum = average(sum, 4);
        double tempNum = Math.pow(value1 - averageNum, 2) + Math.pow(value2 - averageNum, 2) + Math.pow(value3 - averageNum, 2) + Math.pow(value4 - averageNum, 2);
        tempNum = Math.sqrt(tempNum / 4) * 20;

        return tempNum;

    }


    public static double falling(double instabilityValue, double directionValue) {

        double temp = Math.sqrt(instabilityValue) * directionValue;
        if (temp > 10) {
            temp = temp - 10;
        }
        return temp;

    }

    private static double average(double sum, int len) {
        if (len == 0) {
            return 0;
        } else {
            return sum / len;
        }
    }

    public static double[] fourierLowPassFilter(double[] data, double lowPass, double frequency) {
        //data: input data, must be spaced equally in time.
        //lowPass: The cutoff frequency at which
        //frequency: The frequency of the input data.

        //The apache Fft (Fast Fourier Transform) accepts arrays that are powers of 2.
        int minPowerOf2 = 1;
        while (minPowerOf2 < data.length)
            minPowerOf2 = 2 * minPowerOf2;

        //pad with zeros
        double[] padded = new double[minPowerOf2];
        for (int i = 0; i < data.length; i++)
            padded[i] = data[i];



        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fourierTransform = transformer.transform(padded, TransformType.FORWARD);

        //build the frequency domain array
        double[] frequencyDomain = new double[fourierTransform.length];
        for (int i = 0; i < frequencyDomain.length; i++)
            frequencyDomain[i] = frequency * i / (double) fourierTransform.length;

        //build the classifier array, 2s are kept and 0s do not pass the filter
        double[] keepPoints = new double[frequencyDomain.length];
        keepPoints[0] = 1;
        for (int i = 1; i < frequencyDomain.length; i++) {
            if (frequencyDomain[i] < lowPass)
                keepPoints[i] = 2;
            else
                keepPoints[i] = 0;
        }

        //filter the fft
        for (int i = 0; i < fourierTransform.length; i++)
            fourierTransform[i] = fourierTransform[i].multiply((double) keepPoints[i]);

        //invert back to time domain
        Complex[] reverseFourier = transformer.transform(fourierTransform, TransformType.INVERSE);

        //get the real part of the reverse
        double[] result = new double[data.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = reverseFourier[i].getReal();
        }

        return result;
    }


}
