package com.causality.calc.controller;

import com.alibaba.fastjson.JSONObject;
import com.causality.calc.algotithm.AnalyseGrowthLimit;
import com.causality.calc.algotithm.AnalyseNeglect;
import com.causality.calc.algotithm.AnalyseUnderinvestment;
import com.causality.calc.algotithm.CalculateCircleCount;
import com.causality.calc.entity.GrowthLimit;
import com.causality.calc.entity.Neglect;
import com.causality.calc.entity.Underinvestment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("calculate")
public class CalculateController {

    private static final Logger log = LoggerFactory.getLogger(CalculateController.class);

    @RequestMapping(value = "result")
    public Map<String, String> result(String arrStr) {
        log.info("计算过程开始，入参:{}", arrStr);
        Map<String, String> result = new HashMap<>();
        try {
            int[][] inputArray = CalculateCircleCount.initPublicParam(arrStr);
            ArrayList<ArrayList<Integer>> circle = CalculateCircleCount.getCircle();
            // 反馈回路 极性
            ArrayList<Integer> circleValueList = CalculateCircleCount.caculateCircleValue(inputArray, circle);
            log.info("反馈环数值：" + JSONObject.toJSONString(circleValueList));
            // 成长上限基模
            ArrayList<GrowthLimit> growthLimitAnalyseList = AnalyseGrowthLimit.analyse(inputArray, circleValueList, circle);
            log.info("成长上限基模：" + JSONObject.toJSONString(growthLimitAnalyseList));
            // 舍本逐末基模
            List<Neglect> neglectAnalyse = AnalyseNeglect.analyse(inputArray, circleValueList, circle);
            log.info("舍本逐末基模：" + JSONObject.toJSONString(neglectAnalyse));
            // 成长与投资不足基模
            List<Underinvestment> underinvestmentsAnalyse = AnalyseUnderinvestment.analyse(inputArray, circleValueList, circle);
            log.info("投资与成长不足基模：" + JSONObject.toJSONString(underinvestmentsAnalyse));
            result.put("circle", getCircleString(circleValueList, circle));
            result.put("growthLimit", getGrowthLimit(growthLimitAnalyseList));
            result.put("neglect", getNeglect(neglectAnalyse));
            result.put("underinvestment", getUnderinvestments(underinvestmentsAnalyse));
            result.put("success", "true");
        } catch (Exception e) {
            log.error("计算过程发生异常", e);
            result.put("success", "false");
        }
        log.info("计算过程完成");
        return result;
    }

    private String getUnderinvestments(List<Underinvestment> underinvestmentsAnalyse) {
        StringBuilder builder = new StringBuilder();
        for (Underinvestment underinvestment : underinvestmentsAnalyse) {
            builder.append("L1:").append(parseString(underinvestment.getL1()));
            builder.append(";");
            builder.append("L2:").append(parseString(underinvestment.getL2()));
            builder.append(";");
            builder.append("L3:").append(parseString(underinvestment.getL3()));
            builder.append("\r\n");
        }
        builder.append("共").append(underinvestmentsAnalyse.size()).append("组");
        log.info("投资与成长不足基模：{}组\r\n{}", underinvestmentsAnalyse.size(), builder.toString());
        return parseEmptyString(builder.toString());
    }

    private String getNeglect(List<Neglect> neglectAnalyse) {
        StringBuilder builder = new StringBuilder();
        for (Neglect neglect : neglectAnalyse) {
            builder.append("L1:").append(parseString(neglect.getL1()));
            builder.append(";");
            builder.append("L2:").append(parseString(neglect.getL2()));
            builder.append(";");
            builder.append("L3:").append(parseString(neglect.getL3()));
            builder.append("\r\n");
        }
        builder.append("共").append(neglectAnalyse.size()).append("组");
        log.info("舍本逐末基模：{}组\r\n{}", neglectAnalyse.size(), builder.toString());
        return parseEmptyString(builder.toString());
    }

    private String getGrowthLimit(ArrayList<GrowthLimit> growthLimitAnalyseList) {
        StringBuilder builder = new StringBuilder();
        for (GrowthLimit growthLimit : growthLimitAnalyseList) {
            builder.append("L1:").append(parseString(growthLimit.getL1()));
            builder.append(";");
            builder.append("L2:").append(parseString(growthLimit.getL2()));
            builder.append("\r\n");
        }
        builder.append("共").append(growthLimitAnalyseList.size()).append("组");
        log.info("成长上限基模：{}组\r\n{}", growthLimitAnalyseList.size(), builder.toString());
        return parseEmptyString(builder.toString());
    }

    private String parseString(ArrayList<Integer> list) {
        StringBuilder builder = new StringBuilder();
        for (int colIndex = 0; colIndex < list.size(); colIndex++) {
            builder.append(list.get(colIndex));
            if (colIndex != list.size() - 1) {
                builder.append("->");
            }
        }
        return parseEmptyString(builder.toString());
    }

    private String parseEmptyString(String string) {
        if (StringUtils.isEmpty(string)) {
            return "无数据";
        }
        return string;
    }

    private String getCircleString(ArrayList<Integer> circleValueList, ArrayList<ArrayList<Integer>> circle) {
        StringBuilder builder = new StringBuilder();
        for (int rowIndex = 0; rowIndex < circle.size(); rowIndex++) {
            builder.append(circleValueList.get(rowIndex)).append(":");
            ArrayList<Integer> rowList = circle.get(rowIndex);
            for (int colIndex = 0; colIndex < rowList.size(); colIndex++) {
                builder.append(rowList.get(colIndex));
                if (colIndex != rowList.size() - 1) {
                    builder.append("->");
                }
            }
            builder.append("\r\n");
        }
        builder.append("共").append(circle.size()).append("组");
        log.info("反馈环：{}组\r\n{}", circle.size(), builder.toString());
        return parseEmptyString(builder.toString());
    }
}

