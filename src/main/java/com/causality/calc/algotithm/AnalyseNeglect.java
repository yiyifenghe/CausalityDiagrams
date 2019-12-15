package com.causality.calc.algotithm;

import com.causality.calc.entity.Neglect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnalyseNeglect {
    public static List<Neglect> analyse(int[][] inputArray, ArrayList<Integer> circleValueList, ArrayList<ArrayList<Integer>> circle) {
        List<Neglect> neglectList = new ArrayList<>();
        for (int indexL1 = 0; indexL1 < circle.size(); indexL1++){
            if (circleValueList.get(indexL1) <= 0){
                continue;
            }
            // L1 > 0 到达下面逻辑
            for (int indexL2 = 0; indexL2 < circle.size(); indexL2++){
                if (circleValueList.get(indexL2) >= 0){
                    continue;
                }
                // L2 < 0 到达下面逻辑
                // L3 从 indexL2开始遍历，避免L2 L3顺序颠倒产生两组基模
                for (int indexL3 = indexL2; indexL3 < circle.size(); indexL3++){
                    if (circleValueList.get(indexL3) >= 0){
                        continue;
                    }
                    // L1 L2由于反馈链极性限制决定不可能是同一条反馈链，不需要额外判断
                    // 但是L2 L3 破坏了这个限制，需要判断是否是同一条反馈链
                    if (indexL2 == indexL3){
                        continue;
                    }
                    // L1L2L3至少存在一条反馈回路|Y|>=2
                    if (Math.abs(circleValueList.get(indexL1)) < 2 &&
                    Math.abs(circleValueList.get(indexL2)) < 2 &&
                    Math.abs(circleValueList.get(indexL3)) < 2){
                        continue;
                    }
                    // 到达“舍本逐末基模”节点包含判断逻辑
                    HashSet<Integer> hashSetL1 = new HashSet<>(circle.get(indexL1));
                    HashSet<Integer> hashSetL2 = new HashSet<>(circle.get(indexL2));
                    HashSet<Integer> hashSetL3 = new HashSet<>(circle.get(indexL3));
                    int sizeL2 = hashSetL2.size();
                    int sizeL3 = hashSetL3.size();
                    hashSetL2.removeAll(hashSetL1);
                    hashSetL3.removeAll(hashSetL1);
                    if (sizeL2 == hashSetL2.size()){
                        continue;
                    }
                    if (sizeL3 == hashSetL3.size()){
                        continue;
                    }
                    hashSetL2 = new HashSet<>(circle.get(indexL2));
                    hashSetL3 = new HashSet<>(circle.get(indexL3));
                    sizeL2 = hashSetL2.size();
                    hashSetL2.removeAll(hashSetL3);
                    if (sizeL2 == hashSetL2.size()){
                        continue;
                    }
                    // 到达舍本逐末基模逻辑
                    // 基于上述验证，L2\L3包含L1的节点，且L2包含L3的节点
                    // 且L1 > 0, L2、L3 < 0
                    Neglect neglect = new Neglect();
                    neglect.setL1(circle.get(indexL1));
                    neglect.setL2(circle.get(indexL2));
                    neglect.setL3(circle.get(indexL3));
                    neglectList.add(neglect);
                }
            }
        }
        return neglectList;
    }
}
