package com.jef.designpattern.action.strategy;

import java.util.HashMap;
import java.util.Map;

public enum CustomerTypeEnum {
    LARGE("large", "大客户", new LargeCustomerStratery()),
    NORMAL("normal", "普通客户", new NormalCustomerStratery()),
    OLD("old", "老客户", new OldCustomerStratery()),
    ;

    private String customerType;

    private String customerTypeDesc;

    private ICustomerStrategy customerStrategy;

    CustomerTypeEnum(String customerType, String customerTypeDesc, ICustomerStrategy customerStrategy) {
        this.customerType = customerType;
        this.customerStrategy = customerStrategy;
    }

    public String getCustomerType() {
        return customerType;
    }

    public ICustomerStrategy getCustomerStrategy() {
        return customerStrategy;
    }

    private static Map<String, ICustomerStrategy> customerStrategyMap;

    /**
     * 获取对应类型的实现类
     *
     * @param customerType 客户类型
     * @return 对应类型的实现类
     */
    public static synchronized ICustomerStrategy getByCustomerType(String customerType) {
        if (customerStrategyMap == null) {
            customerStrategyMap = new HashMap<>();
            for (CustomerTypeEnum customerTypeEnum : CustomerTypeEnum.values()) {
                customerStrategyMap.put(customerTypeEnum.getCustomerType(), customerTypeEnum.getCustomerStrategy());
            }
        }
        return customerStrategyMap.get(customerType);
    }
}
