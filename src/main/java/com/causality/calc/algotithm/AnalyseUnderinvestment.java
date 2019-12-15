package com.causality.calc.algotithm;

import com.causality.calc.entity.Neglect;
import com.causality.calc.entity.Underinvestment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyseUnderinvestment {

    private static final Logger log = LoggerFactory.getLogger(AnalyseUnderinvestment.class);


    public static List<Underinvestment> analyse(int[][] inputArray, ArrayList<Integer> circleValueList, ArrayList<ArrayList<Integer>> circle) {
        List<Underinvestment> underinvestmentList = new ArrayList<>();
        List<HashSet<Integer>> listSet = new ArrayList<>();
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
                // （！！！此行说明错误！！！） 为了避免L2 L3交换位置形成两组基模，L3遍历从indexL2开始
                // L3遍历需要从0开始，避免L3在反馈环list中处于L2之前，导致被遗漏，
                // 需要额外判断，避免L2 L3交换产生两组“成长与投资不足基模”
                for (int indexL3 = 0; indexL3 < circle.size(); indexL3++){
                    // |Y3| > 2，若不满足此条件跳过
                    if (Math.abs(circleValueList.get(indexL3)) < 2){
                        continue;
                    }
                    // L1 L2由于反馈链极性限制决定不可能是同一条反馈链，不需要额外判断
                    // 但是L2 L3 破坏了这个限制，需要判断是否是同一条反馈链
                    if (indexL2 == indexL3) {
                        continue;
                    }
                    // 额外判断，L1+L2+L3与L1+L3+L2应认定为同一组基模
                    // 若检出重复，忽略当前基模
                    if (checkDuplicate(listSet, indexL1, indexL2, indexL3)){
                        log.info("成长与投资不足基模发现反馈环顺序交换重复，跳过");
                        continue;
                    }
                    // 到达“成长于投资不足基模”节点包含判断逻辑
                    // L1 L3包含L2
                    HashSet<Integer> hashSetL1 = new HashSet<>(circle.get(indexL1));
                    HashSet<Integer> hashSetL2 = new HashSet<>(circle.get(indexL2));
                    HashSet<Integer> hashSetL3 = new HashSet<>(circle.get(indexL3));
                    int sizeL1 = hashSetL1.size();
                    int sizeL3 = hashSetL3.size();
                    hashSetL1.removeAll(hashSetL2);
                    hashSetL3.removeAll(hashSetL2);
                    if (sizeL1 == hashSetL1.size()){
                        continue;
                    }
                    if (sizeL3 == hashSetL3.size()){
                        continue;
                    }
                    // L1 与 L3相互不包含
                    hashSetL1 = new HashSet<>(circle.get(indexL1));
                    hashSetL3 = new HashSet<>(circle.get(indexL3));
                    sizeL1 = hashSetL1.size();
                    hashSetL1.removeAll(hashSetL3);
                    if (sizeL1 != hashSetL1.size()){
                        continue;
                    }
                    // 到达成长与投资不足基模逻辑
                    // 基于上述验证，L2\L3包含L1的节点，且L2包含L3的节点
                    // 且L1 > 0, L2、L3 < 0
                    Underinvestment underinvestment = new Underinvestment();
                    underinvestment.setL1(circle.get(indexL1));
                    underinvestment.setL2(circle.get(indexL2));
                    underinvestment.setL3(circle.get(indexL3));
                    underinvestmentList.add(underinvestment);
                    HashSet<Integer> set = new HashSet<>();
                    set.add(indexL1);
                    set.add(indexL2);
                    set.add(indexL3);
                    listSet.add(set);
                }
            }
        }
        return underinvestmentList;
    }

    private static boolean checkDuplicate(List<HashSet<Integer>> listSet, int indexL1, int indexL2, int indexL3) {
        for(HashSet<Integer> set : listSet){
            set.remove(indexL1);
            set.remove(indexL2);
            set.remove(indexL3);
            if (set.size() == 0){
                return true;
            }
        }
        return false;
    }
}
