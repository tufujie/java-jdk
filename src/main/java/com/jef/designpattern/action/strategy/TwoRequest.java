package com.jef.designpattern.action.strategy;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author tufujie
 * @date 2024/1/11
 */
public class TwoRequest implements AbstractHandlerRequest, Serializable {

    private static final long serialVersionUID = -3697593333671595095L;

    @NotNull(message = "客户类型不能为空")
    private String customerType;

    @Override
    public String currentCustomerType() {
        return getCustomerType();
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}