package com.jef.designpattern.action.strategy;

/**
 * 注册处理器
 *
 * @author tufujie
 * @date 2024/1/11
 */

import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 处理器注册中心
 */
public enum HandlerRegister {

    INSTANCE;

    private static ConcurrentHashMap<Class<? extends AbstractHandlerRequest>, Set<Handler>>
            HANDLER_CENTER = new ConcurrentHashMap();

    private Lock lock = new ReentrantLock();

    HandlerRegister() {
    }

    /**
     * 持有特定请求的实现类
     *
     * @param type
     * @return
     */
    public Set<Handler> getHandlers(Class<? extends AbstractHandlerRequest> request) {
        return HANDLER_CENTER.get(request);
    }

    /**
     * 注册处理器.
     *
     * @param handler 处理器
     * @param request 处理请求类型
     */
    public void register(Handler handler, Class<? extends AbstractHandlerRequest> request) {
        assert handler != null;
        Set<Handler> handlers = HANDLER_CENTER.get(request);
        if (lock.tryLock()) {
            try {
                if (CollectionUtils.isEmpty(handlers)) {
                    handlers = new HashSet<>();
                    HANDLER_CENTER.put(request, handlers);
                }
                handlers.add(handler);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 注册处理器.
     *
     * @param handler  处理器
     * @param requests 处理请求类型集合
     */
    public void register(Handler handler, List<Class<? extends AbstractHandlerRequest>> requests) {
        assert CollectionUtils.isNotEmpty(requests);
        for (Class<? extends AbstractHandlerRequest> request : requests) {
            register(handler, request);
        }
    }

}