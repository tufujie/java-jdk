package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;
import com.jef.designpattern.action.strategy.CustomerTypeEnum;
import com.jef.designpattern.action.strategy.OldCustomerStratery;

/**
 * @author tufujie
 * @date 2023/12/25
 */
public class CustomerTypeHandler3 extends CustomerTypeHandler {
    @Override
    public double doHandler(String customerType, Customer customer) {
        if (CustomerTypeEnum.OLD.getCustomerType().equals(customerType)) {
            return new OldCustomerStratery().calcPrice(customer);
        } else {
            // 传递到下一个节点
            return successor.doHandler(customerType, customer);
        }
    }
}