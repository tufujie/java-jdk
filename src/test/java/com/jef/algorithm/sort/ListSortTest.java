package com.jef.algorithm.sort;

import com.alibaba.fastjson.JSONObject;
import com.jef.constant.BasicConstant;
import com.jef.constant.BasicList;
import com.jef.entity.OrderInfo;
import com.jef.util.ListSortUtil;
import com.jef.util.PrintUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

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
    void testListSort() {
        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();
        System.out.println("未排序前");
        System.out.println(orderInfoList);
        PrintUtil.printSplitLine();
        // 多重排序，先按照数量，然后按照折扣方式，递减
        orderInfoList.sort((o1, o2) -> o1.getNum() > o2.getNum() ? -1 : o1.getNum() < o2.getNum() ? 1 :
                Integer.compare(o2.getDiscountType(), o1.getDiscountType()));
        System.out.println("排序后");
        System.out.println(orderInfoList);

        orderInfoList = BasicList.getOrderInfoList();
        System.out.println("未排序前2");
        System.out.println(orderInfoList);
        PrintUtil.printSplitLine();
        // 多重排序，这种写法比较清晰
        // 先按照数量递减
        // 然后按照折扣方式递减
        orderInfoList.sort((o1, o2) -> {
            if (o1.getNum() != o2.getNum()) {
                return o2.getNum().compareTo(o1.getNum());
            }
            return Integer.compare(o2.getDiscountType(), o1.getDiscountType());
        });
        System.out.println("排序后2");
        System.out.println(orderInfoList);
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
        /*Collections.sort(orderInfoList, new Comparator<OrderInfo>() {
            @Override
            public int compare(OrderInfo o1, OrderInfo o2) {
                // 正序
                if (o1.getNum() > o2.getNum()) {
                    return 1;
                } else if (o1.getNum() < o2.getNum()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });*/
        Collections.sort(orderInfoList, (o1, o2) -> o1.getNum() > o2.getNum() ? 1 : o1.getNum() < o2.getNum() ? -1 : 0);
        System.out.println("排序后");
        System.out.println(orderInfoList);
    }

    @Test
    void testListSortUtil() {
        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();
        System.out.println("未排序前");
        System.out.println(orderInfoList);
        new ListSortUtil().sort(orderInfoList, "num", null);
        System.out.println("根据id排序后");
        System.out.println(orderInfoList);
        orderInfoList = BasicList.getOrderInfoList();
        new ListSortUtil().sort(orderInfoList, "createOrderTime", null);
        System.out.println("根据createOrderTime排序后");
        System.out.println(orderInfoList);
    }

    @Test
    void testListSortUtil2() {
        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();

        System.out.println("未排序前");
        System.out.println(orderInfoList);
        // 字符串时间排序
//        Collections.sort(orderInfoList, (o1, o2) -> o2.getExtraOrderId().compareTo(o1.getExtraOrderId()));
        Collections.sort(orderInfoList, Comparator.comparing(OrderInfo::getPayTime));
        System.out.println("排序后");
        System.out.println(orderInfoList);
    }

    @Test
    void testSortByComparator() {
        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();
        System.out.println("未排序前");
        System.out.println(orderInfoList);
        orderInfoList.sort(Comparator.comparing(OrderInfo::getNum, Comparator.reverseOrder()));
        System.out.println("排序后");
        System.out.println(orderInfoList);
    }


}