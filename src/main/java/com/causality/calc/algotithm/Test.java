package com.causality.calc.algotithm;

import com.alibaba.fastjson.JSONObject;
import com.causality.calc.entity.GrowthLimit;
import com.causality.calc.entity.Neglect;
import com.causality.calc.entity.Underinvestment;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.remove(3);
        set.remove(2);
        System.out.println(set.size());
    }

}
