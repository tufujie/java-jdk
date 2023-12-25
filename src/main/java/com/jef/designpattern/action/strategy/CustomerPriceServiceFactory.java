package com.jef.designpattern.action.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tufujie
 * @date 2023/12/25
 */
public class CustomerPriceServiceFactory {
    private CustomerPriceServiceFactory() {

    }

    private static class SingletonHolder {
        private static CustomerPriceServiceFactory instance = new CustomerPriceServiceFactory();
    }

    public static CustomerPriceServiceFactory getInstance() {
        return SingletonHolder.instance;
    }

    private static final Map<String, ICustomerStrategy> CUSTOMER_TYPE_SERVICE_MAP = new HashMap<String, ICustomerStrategy>();

    static {
        CUSTOMER_TYPE_SERVICE_MAP.put(new LargeCustomerStratery().getCustomerType(), new LargeCustomerStratery());
        CUSTOMER_TYPE_SERVICE_MAP.put(new OldCustomerStratery().getCustomerType(), new OldCustomerStratery());
        CUSTOMER_TYPE_SERVICE_MAP.put(new NormalCustomerStratery().getCustomerType(), new NormalCustomerStratery());
    }

    private static ICustomerStrategy getServiceByCustomerType(String customerType) {
        ICustomerStrategy customerStrategy = CUSTOMER_TYPE_SERVICE_MAP.get(customerType);
        if (customerStrategy == null) {
            throw new RuntimeException("非法 customerType");
        }
        return customerStrategy;
    }

    public double handlePrice(String customerType, Customer customer) {
        return getServiceByCustomerType(customerType).calcPrice(customer);
    }
}