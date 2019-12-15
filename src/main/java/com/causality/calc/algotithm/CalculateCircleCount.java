package com.causality.calc.algotithm;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class CalculateCircleCount {
    private static int[][] array;
    private static ArrayList<ArrayList<Integer>> circle;
//
//    public static void main(String[] args) {
//        String arrStr =  "0,1,0;1,0,1;1,0,0";
//        initPublicParam(arrStr);
//        ArrayList<ArrayList<Integer>> circle = getCircle();
//        System.out.println(JSONObject.toJSONString(circle));
//    }

    public static int[][] initPublicParam(String arrStr){
        String[] rowValues = arrStr.split(";");
        int[][] inputArray = new int[rowValues.length][];
        for(int rowIndex = 0; rowIndex < rowValues.length; rowIndex++){
            String[] colValues = rowValues[rowIndex].split(",");
            int[] singleRowArray = new int[colValues.length];
            for (int index = 0; index < colValues.length; index++){
                singleRowArray[index] = Integer.parseInt(colValues[index]);
            }
            inputArray[rowIndex] = singleRowArray;
        }
        array = inputArray;
        circle = new ArrayList<>();
        return array;
    }

    public static ArrayList<ArrayList<Integer>> getCircle(){
        for (int rowNum=0; rowNum<array.length; rowNum++){
            for (int colNum = rowNum; colNum< array[rowNum].length; colNum++){
                if (array[rowNum][colNum] != 0){
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(rowNum);
                    list.add(colNum);
                    findNext(rowNum, colNum, list);
                }
            }
        }
        System.out.println(JSONObject.toJSONString(circle));
        return circle;
    }

    private static void findNext(int start, int current, ArrayList<Integer> list){
        // 选取关系矩阵的某一行，行号等于已建立反馈链的最后节点脚标
        // 遍历此行所有列，但是为了防止反馈链重复，从起点脚标开始遍历
        for (int colNum = start; colNum < array[current].length; colNum++) {
            if (array[current][colNum] != 0) {
                // 复制已有不完整反馈链，在此基础上增加新找到的节点
                ArrayList<Integer> newList = new ArrayList<>(list);
                newList.add(colNum);
                if (colNum != start) { // 找到的下一节点非起始节点
                    // 若当前不完整反馈链中已有新找到的节点，
                    // 且新节点不是反馈链的第一个节点，
                    // 则跳过此节点防止反馈连部分成环死循环
                    if (list.contains(colNum)) {
                        continue;
                    }
                    findNext(start, colNum, newList);
                } else { // 找到的下一节点是起始节点，则形成反馈链
                    circle.add(newList);
                }
            }
        }
    }

    public static ArrayList<Integer> caculateCircleValue(int[][] inputArray, ArrayList<ArrayList<Integer>> circle) {
        ArrayList<Integer> circleValueList = new ArrayList<>();
        for (ArrayList<Integer> list : circle){
            int circleValue = 1;
            for (int index = 1; index < list.size(); index++){
                circleValue*= inputArray[list.get(index - 1)][list.get(index)];
            }
            circleValueList.add(circleValue);
        }
        return circleValueList;
    }
}
