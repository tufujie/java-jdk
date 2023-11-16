package com.jef.algorithm.sort;

import com.alibaba.fastjson.JSONObject;
import com.jef.constant.BasicConstant;
import com.jef.constant.BasicList;
import com.jef.entity.OrderInfo;
import com.jef.util.ListSortUtil;
import com.jef.util.PrintUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 数字排序
 *
 * @author Jef
 * @date 2021/4/13
 */
public class ListSortTest {

    @Test
    public void testAddAndSort() {
        List<Integer> listOne = new ArrayList<>();
        listOne.add(2);
        listOne.add(4);
        List<Integer> listTwo = new ArrayList<>();
        listTwo.add(1);
        listTwo.add(3);
        List<Integer> resultList = ListSortUtil.addAndSort(listOne, listTwo);
        System.out.println(resultList);
        int[] resultListV2 = ListSortUtil.addAndSortV2(listOne, listTwo);
        System.out.println(resultListV2);
        Set<Integer> resultSetV3 = ListSortUtil.addAndSortV3(listOne, listTwo);
        System.out.println(resultSetV3);
    }

    @Test
    void testSort() {
        List<Integer> list = new ArrayList<>(Arrays.asList(BasicConstant.INTEGER_ARRAY));
        Collections.sort(list, ArraySortUtil.getDownSortComparator());
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    void testComparable() {
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

    @Test
    void testComparator() {
        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();
        System.out.println("未排序前");
        System.out.println(orderInfoList);
        PrintUtil.printSplitLine();
        Collections.sort(orderInfoList, new Comparator<OrderInfo>() {
            @Override
            public int compare(OrderInfo o1, OrderInfo o2) {
                // 正序
                if (o1.getId() > o2.getId()) {
                    return 1;
                } else if (o1.getId() < o2.getId()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        System.out.println("排序后");
        System.out.println(orderInfoList);
    }

    @Test
    void testListSortUtil() {
        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();
        System.out.println("未排序前");
        System.out.println(orderInfoList);
        new ListSortUtil().sort(orderInfoList, "id", null);
        System.out.println("根据id排序后");
        System.out.println(orderInfoList);
        orderInfoList = BasicList.getOrderInfoList();
        new ListSortUtil().sort(orderInfoList, "createOrderTime", null);
        System.out.println("根据createOrderTime排序后");
        System.out.println(orderInfoList);
    }


}