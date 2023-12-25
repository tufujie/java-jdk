package com.jef.designpattern.action.strategy.re;

import com.jef.designpattern.action.strategy.Customer;

/**
 * @author tufujie
 * @date 2023/12/25
 */
public class CustomerTypeServiceFactory {

    private CustomerTypeHandler headHandler;

    private CustomerTypeServiceFactory() {
        headHandler = new HeadHandler();

        CustomerTypeHandler handler1 = new CustomerTypeHandler1();
        CustomerTypeHandler handler2 = new CustomerTypeHandler2();
        CustomerTypeHandler handler3 = new CustomerTypeHandler3();

        TailHandler tailHandler = new TailHandler();

        headHandler.setSuccessor(handler1.setSuccessor(handler2.setSuccessor(handler3.setSuccessor(tailHandler))));
    }

    private static class SingletonHolder {
        private static CustomerTypeServiceFactory instance = new CustomerTypeServiceFactory();
    }

    public static CustomerTypeServiceFactory getInstance() {
        return SingletonHolder.instance;
    }


    public double handlePrice(String customerType, Customer customer) {
        return headHandler.doHandler(customerType, customer);
    }
}