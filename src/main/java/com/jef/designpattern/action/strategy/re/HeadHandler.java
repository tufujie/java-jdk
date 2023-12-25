package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;
import org.apache.commons.lang.StringUtils;

/**
 * @author tufujie
 * @date 2023/12/25
 */
// 首节点，判断actionCode 是否为空
public class HeadHandler extends CustomerTypeHandler {
    @Override
    public double doHandler(String customerType, Customer customer) {
        if (StringUtils.isBlank(customerType)) {
            throw new RuntimeException("customerType 不能为空");
        }
        return successor.doHandler(customerType, customer);
    }
}

