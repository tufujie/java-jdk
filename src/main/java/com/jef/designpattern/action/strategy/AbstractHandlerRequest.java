package com.jef.designpattern.action.strategy;

/**
 * @author tufujie
 * @date 2024/1/11
 */

/**
 * 抽象处理器请求（作为对外服务的契约，对不同类型的活动分开处理）
 */
public interface AbstractHandlerRequest {

    /**
     * 当前客户类型
     */
    String currentCustomerType();

}