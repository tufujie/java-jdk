package com.jef.designpattern.action.strategy;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 策略接口实现类，也就是具体的处理者
 * 具体算法实现，为大客户计算应报的价格
 * @author Jef
 * @date 2019/2/13
 */
public class LargeCustomerStratery implements ICustomerStrategy {

    static {
        beanMap.put("large", new LargeCustomerStratery());
    }

    @Override
    public double calcPrice(Customer customer) {
        System.out.println("对于大客户，统一折扣10%");
        return customer.getGoodsPrice() * (1 - 0.1);
//        return goodsPrice * 0.9;
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
        return "large";
    }

    public static void main(String[] args) {
       /* new LargeCustomerStratery();
        new NormalCustomerStratery();
        new OldCustomerStratery();*/
        Map<String, ICustomerStrategy> map = ICustomerStrategy.beanMap;
        System.out.println(map.size());
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