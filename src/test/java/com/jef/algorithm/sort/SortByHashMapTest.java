package com.jef.algorithm.sort;

import org.junit.Test;

import java.util.*;

/**
 * @author tufujie
 * @date 2024/1/5
 */
public class SortByHashMapTest {

    @Test
    public void testSortByHashMap() {

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

    @Test
    public void testAutoUnboxing() {

        Map<String, Integer> map = new HashMap<String, Integer>();

        map.put("Five", 5);
        map.put("Seven", 7);
        map.put("Eight", 8);
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        int num = 3;
        PriorityQueue<Integer> heap = new PriorityQueue<>(num);
        int i = 0;
        HashSet<String> hashSet = new HashSet<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (i++ < num) {
                heap.offer(entry.getValue());
                hashSet.add(entry.getKey());
            }
        }
        Map<Integer, String> numMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (!numMap.containsKey(entry.getValue())) {
                numMap.put(entry.getValue(), entry.getKey());
            }
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (heap.peek() != null) {
                if (heap.peek() < entry.getValue() && !heap.contains(entry.getValue())) {
                    Integer peek = heap.peek();
                    hashSet.remove(numMap.get(peek));
                    heap.poll();
                    heap.offer(entry.getValue());
                    hashSet.add(entry.getKey());
                }
            } else {
                heap.poll();
                heap.add(entry.getValue());
                hashSet.add(entry.getKey());
            }
        }
        System.out.println(hashSet);
    }

}