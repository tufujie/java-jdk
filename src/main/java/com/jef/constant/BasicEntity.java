package com.jef.constant;

import com.jef.entity.OrderInfo;
import com.jef.entity.User;

/**
 * @author Jef
 * @date 2019/3/14
 */
public class BasicEntity {
    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setName(BasicConstant.USER_NAME);
        user.setPhone(BasicConstant.USER_PHONE);
        user.setAge(2);
        user.setType(2);
        return user;
    }

    public static User getUserV2() {
        User user = new User();
        user.setId(2L);
        user.setName(BasicConstant.STR_ONE);
        user.setPhone(BasicConstant.USER_PHONE);
        user.setAge(2);
        user.setType(2);
        return user;
    }

    public static OrderInfo getOrderInfo() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId("test");
        orderInfo.setNum(2);
        return orderInfo;
    }
}