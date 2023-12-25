package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;
import com.jef.designpattern.action.strategy.CustomerTypeEnum;
import com.jef.designpattern.action.strategy.NormalCustomerStratery;

/**
 * @author tufujie
 * @date 2023/12/25
 */
public class CustomerTypeHandler1 extends CustomerTypeHandler {
    @Override
    public double doHandler(String customerType, Customer customer) {
        if (CustomerTypeEnum.NORMAL.getCustomerType().equals(customerType)) {
            return new NormalCustomerStratery().calcPrice(customer);
        } else {
            // 传递到下一个节点
            return successor.doHandler(customerType, customer);
        }
    }
}