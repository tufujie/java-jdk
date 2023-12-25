package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;
import com.jef.designpattern.action.strategy.CustomerTypeEnum;
import com.jef.designpattern.action.strategy.LargeCustomerStratery;

/**
 * @author tufujie
 * @date 2023/12/25
 */
public class CustomerTypeHandler2 extends CustomerTypeHandler {
    @Override
    public double doHandler(String customerType, Customer customer) {
        if (CustomerTypeEnum.LARGE.getCustomerType().equals(customerType)) {
            return new LargeCustomerStratery().calcPrice(customer);
        } else {
            // 传递到下一个节点
            return successor.doHandler(customerType, customer);
        }
    }
}