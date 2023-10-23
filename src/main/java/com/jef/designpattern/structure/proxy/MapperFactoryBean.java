package com.jef.designpattern.structure.proxy;

import com.jef.constant.BasicConstant;
import com.jef.util.PrintUtil;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理类定义
 *
 * @author Jef
 * @date 2021/12/11
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {
    private Class<T> mapperInterface;

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {
            ProxySelect select = method.getAnnotation(ProxySelect.class);
            PrintUtil.printf("SQL： %s", select.value().replace("#{uId}", args[0].toString()));
            return args[0] + "," + BasicConstant.JEF_LIFE_MEAN;
        };
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperInterface}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}