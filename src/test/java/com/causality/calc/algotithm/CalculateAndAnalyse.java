package com.causality.calc.algotithm;

import com.alibaba.fastjson.JSONObject;
import com.causality.calc.entity.GrowthLimit;
import com.causality.calc.entity.Neglect;
import com.causality.calc.entity.Underinvestment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CalculateAndAnalyse {

    @Test
    void test(){
        String arrStr =  "0,1,0;1,0,1;1,0,0";
        int[][] inputArray = CalculateCircleCount.initPublicParam(arrStr);
        ArrayList<ArrayList<Integer>> circle = CalculateCircleCount.getCircle();
        // 反馈回路 极性
        ArrayList<Integer> circleValueList = CalculateCircleCount.caculateCircleValue(inputArray, circle);
        System.out.println("反馈环数值：" + JSONObject.toJSONString(circleValueList));
        // 成长上限基模
        ArrayList<GrowthLimit> growthLimitAnalyseList = AnalyseGrowthLimit.analyse(inputArray, circleValueList, circle);
        System.out.println("成长上限基模：" + JSONObject.toJSONString(growthLimitAnalyseList));
        // 舍本逐末基模
        List<Neglect> neglectAnalyse = AnalyseNeglect.analyse(inputArray, circleValueList, circle);
        System.out.println("舍本逐末基模：" + JSONObject.toJSONString(neglectAnalyse));
        // 成长与投资不足基模
        List<Underinvestment> underinvestmentsAnalyse = AnalyseUnderinvestment.analyse(inputArray, circleValueList, circle);
        System.out.println("投资与成长不足基模：" + JSONObject.toJSONString(underinvestmentsAnalyse));
    }
}
