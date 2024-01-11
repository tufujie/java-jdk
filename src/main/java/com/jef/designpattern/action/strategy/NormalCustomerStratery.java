package com.jef.designpattern.action.strategy;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;

/**
 * 具体算法实现，为新客户或者是普通客户计算应报的价格
 * @author Jef
 * @date 2019/2/13
 */
public class NormalCustomerStratery implements ICustomerStrategy {

    static {
        beanMap.put("normal", new NormalCustomerStratery());
    }

    @Override
    public double calcPrice(Customer customer) {
        System.out.println("对于新客户或者是普通客户，没有折扣");
        return customer.getGoodsPrice();
    }

    @Override
    public BigDecimal calcPriceV2(OneRequest abstractOneRequest) {
        System.out.println(this.getCustomerType() + " calcPriceV2");
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcPriceV3(TwoRequest request) {
        System.out.println(this.getCustomerType() + " calcPriceV3");
        return BigDecimal.ZERO;
    }

    @Override
    public String getCustomerType() {
        return "normal";
    }

    @Override
    public void register() {
        // 向注册中心，注册该处理期可以处理的 请求类型集合
        HandlerRegister.INSTANCE.register(this,
                ImmutableList.<Class<? extends AbstractHandlerRequest>>builder()
                        .add(OneRequest.class)
                        .add(TwoRequest.class)
                        .build());
    }
}