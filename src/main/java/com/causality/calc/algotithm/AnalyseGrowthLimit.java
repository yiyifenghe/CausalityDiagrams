package com.causality.calc.algotithm;

import com.causality.calc.entity.GrowthLimit;

import java.util.ArrayList;
import java.util.HashSet;

public class AnalyseGrowthLimit {

    public static ArrayList<GrowthLimit> analyse(int[][] inputArray, ArrayList<Integer> circleValueList, ArrayList<ArrayList<Integer>> circle) {
        ArrayList<GrowthLimit> growthLimitList = new ArrayList<>();
        for (int indexL1 = 0; indexL1 < circle.size(); indexL1++){
            if (circleValueList.get(indexL1) <= 0){
                continue;
            }
            // L1 > 0 时到达下面逻辑
            for (int indexL2 = 0; indexL2 < circle.size(); indexL2++){
                if (circleValueList.get(indexL2) >= 0){
                    continue;
                }
                // L2 < 0 时到达下面逻辑
                // 将L1和L2两个反馈链节点转为set
                // 从L1set中删除L2set的元素，如果L1元素数量有变化，
                // 说明L1和L2中包含相同元素
                HashSet<Integer> hashSetL1 = new HashSet<>(circle.get(indexL1));
                HashSet<Integer> hashSetL2 = new HashSet<>(circle.get(indexL2));
                int size = hashSetL1.size();
                hashSetL1.removeAll(hashSetL2);
                if (size > hashSetL1.size()){
                    // L1和L2中包含相同元素，L1与L2构成“成长上限基模”
                    GrowthLimit growthLimit = new GrowthLimit();
                    growthLimit.setL1(circle.get(indexL1));
                    growthLimit.setL2(circle.get(indexL2));
                    growthLimitList.add(growthLimit);
                }
            }
        }
        return growthLimitList;
    }
}
