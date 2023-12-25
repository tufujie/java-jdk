package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;

/**
 * @author tufujie
 * @date 2023/12/25
 */
// 尾节点，直接抛出常，因为到了尾节点说明当前 code 没有处理
public class TailHandler extends CustomerTypeHandler {

    @Override
    public double doHandler(String customerType, Customer customer) {
        throw new RuntimeException("当前 customerType[" + customerType + "] 没有具体的 Handler 处理");
    }
}