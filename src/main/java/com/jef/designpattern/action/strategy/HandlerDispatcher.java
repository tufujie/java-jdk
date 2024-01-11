package com.jef.designpattern.action.strategy;

import org.apache.commons.collections.CollectionUtils;

import java.text.MessageFormat;
import java.util.Set;

/**
 * @author tufujie
 * @date 2024/1/11
 */
public class HandlerDispatcher {

    /**
     * 分发处理请求.
     * 适用于不同的模块有相同的业务需要，例如：同步、更新
     *
     * @param request 处理请求
     * @param <T>     请求数据类型
     * @return 处理结果
     */
    public <T> T dispatch(AbstractHandlerRequest request) throws Exception {
        Set<Handler> handlers = HandlerRegister.INSTANCE.getHandlers(request.getClass());
        if (CollectionUtils.isNotEmpty(handlers)) {
            for (Handler handler : handlers) {
                // 适配客户处理器
                if (adaptCustomerHandler(request, handler)) {

                    // 第1种请求
                    if (request instanceof OneRequest) {
                        return (T) ((ICustomerStrategy) handler)
                                .calcPriceV2((OneRequest) request);
                    }

                    // 第2种请求
                    if (request instanceof TwoRequest) {
                        return (T) ((ICustomerStrategy) handler)
                                .calcPriceV3((TwoRequest) request);
                    }

                }

            }
        }

        String msg = MessageFormat.format("不支持的活动类型：{0}, 请求 {1} 处理.",
                request.currentCustomerType(), request.getClass().getSimpleName());
        throw new Exception(msg);
    }

    private boolean adaptCustomerHandler(AbstractHandlerRequest request, Handler handler) {
        return handler instanceof ICustomerStrategy && ((ICustomerStrategy) handler).getCustomerType().equals(request.currentCustomerType());
    }

}