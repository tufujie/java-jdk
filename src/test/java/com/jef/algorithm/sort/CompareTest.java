package com.jef.algorithm.sort;

import com.jef.constant.BasicConstant;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Jef
 * @date 2023/10/27
 */
public class CompareTest {

    @Test
    void testSort() {
        List<Integer> list = new ArrayList<>(Arrays.asList(BasicConstant.INTEGER_ARRAY));
        Collections.sort(list, ArraySortUtil.getDownSortComparator());
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    void testSortV2() {
        List<CustomOrderProductCompare> list = new ArrayList<>();
        list.add(new CustomOrderProductCompare(1, "test"));
        list.add(new CustomOrderProductCompare(3, "test"));
        list.add(new CustomOrderProductCompare(2, "test"));
        list.add(new CustomOrderProductCompare(2, "aest"));
        Collections.sort(list);
        for (CustomOrderProductCompare temp : list) {
            System.out.println("num=" + temp.getNum() + "&productName=" + temp.getProductName());
        }
    }

}