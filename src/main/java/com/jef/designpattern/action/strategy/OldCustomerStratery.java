package com.jef.designpattern.action.strategy;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;

/**
 * 具体算法实现，为老客户计算应报的价格
 * @author Jef
 * @date 2019/2/13
 */
public class OldCustomerStratery implements ICustomerStrategy {
    static {
        beanMap.put("old", new OldCustomerStratery());
    }

    @Override
    public double calcPrice(Customer customer) {
        System.out.println("对于老客户，统一折扣5%");
        return customer.getGoodsPrice() * (1 - 0.05);
//        return goodsPrice * 0.95;
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
        return "old";
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