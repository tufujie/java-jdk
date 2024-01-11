package com.jef.algorithm.sort;

import java.util.*;

/**
 * @author tufujie
 * @date 2024/1/5
 */
public class SortByHashMap {

    public static void main(String[] args) {

        Map<String, Integer> aMap = new HashMap<String, Integer>();

        aMap.put("Five", 5);
        aMap.put("Seven", 7);
        aMap.put("Eight", 8);
        aMap.put("One", 1);
        aMap.put("Two", 2);
        aMap.put("Three", 3);

        sortMapByValuesDesc(aMap);

    }

    /**
     * 按值倒序排序
     *
     * @param aMap
     */
    private static void sortMapByValuesDesc(Map<String, Integer> aMap) {

        Set<Map.Entry<String, Integer>> mapEntries = aMap.entrySet();

        System.out.println("Values and Keys before sorting ");
        for (Map.Entry<String, Integer> entry : mapEntries) {
            System.out.println(entry.getValue() + " - " + entry.getKey());
        }

        List<Map.Entry<String, Integer>> aList = new LinkedList<>(mapEntries);

        Collections.sort(aList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> ele1, Map.Entry<String, Integer> ele2) {
                return ele2.getValue().compareTo(ele1.getValue());
            }
        });

        Map<String, Integer> aMap2 = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : aList) {
            aMap2.put(entry.getKey(), entry.getValue());
        }

        System.out.println("Values and Keys after sorting ");
        for (Map.Entry<String, Integer> entry : aMap2.entrySet()) {
            System.out.println(entry.getValue() + " - " + entry.getKey());
        }

    }

}