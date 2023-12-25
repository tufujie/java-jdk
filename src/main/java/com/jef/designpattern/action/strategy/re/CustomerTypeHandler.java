package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;

/**
 * @author tufujie
 * @date 2023/12/25
 */
public abstract class CustomerTypeHandler {

    //后继节点
    protected CustomerTypeHandler successor;

    /**
     * @param customerType
     */
    public void handler(String customerType, Customer customer) {
        doHandler(customerType, customer);
    }

    //设置后继节点
    protected CustomerTypeHandler setSuccessor(CustomerTypeHandler successor) {
        this.successor = successor;
        return this;
    }

    //处理请求
    public abstract double doHandler(String customerType, Customer customer);

}