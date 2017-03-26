package com.leven.booguubalancescale.test.util;

import com.leven.booguubalancescale.test.fragment.TestFragment;
import com.leven.booguubalancescale.test.pojo.DataEntity;
import com.leven.booguubalancescale.test.pojo.ResultEntity;

import java.util.ArrayList;

/**
 * 计算相关业务数据
 * Created by thinkpad on 2017/3/26.
 */

public class CalculationUtil {


    /**
     * [(xi-xi-1)^2+(yi-yi-1)^2+(zi-zi-1)^2]/ T*10^4
     * <p>
     * T=5
     */
    public static double instability(ResultEntity resultEntity) {
        ArrayList<DataEntity> dataEntities = resultEntity.getDataEntities();
        double value = 0;
        for (int i = 0, len = dataEntities.size(); i < len; i++) {
            if (i == 0) {
                continue;
            }
            DataEntity current = dataEntities.get(i);
            DataEntity last = dataEntities.get(i - 1);
            value += Math.pow((current.getXg() - last.getXg()), 2) + Math.pow((current.getYg() - last.getYg()), 2) + Math.pow((current.getZg() - last.getZg()), 2);
        }

        return value / TestFragment.COUNT_DOWN_SECOND;

    }
}
