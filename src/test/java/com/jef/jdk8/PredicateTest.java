package com.jef.jdk8;

import com.jef.constant.BasicList;
import com.jef.entity.OrderInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 判断真假
 * @author Jef
 * @date 2021/2/20
 */
public class PredicateTest {
    @Test
    public void testTest() {
        Predicate<String> predicate = (s) -> s.length() > 0;
        Assert.assertTrue(predicate.test("foo"));
        Assert.assertFalse(predicate.negate().test("foo"));
        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();

        List<OrderInfo> orderInfoList = BasicList.getOrderInfoList();
        System.out.println("beforeSize=" + orderInfoList.size());
        List<OrderInfo> orderInfos = filterOrderInfoList(orderInfoList, (t) -> t.getNum() > 3);
        System.out.println("afterSize=" + orderInfos.size());
    }

    /**
     * @param orderInfoList      订单列表
     * @param orderInfoPredicate 需要符合哪些条件
     * @return 符合条件的订单列表
     */
    private List<OrderInfo> filterOrderInfoList(List<OrderInfo> orderInfoList, Predicate<OrderInfo> orderInfoPredicate) {
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfoPredicate.test(orderInfo)) {
                orderInfos.add(orderInfo);
            }
        }
        return orderInfos;
    }
}
