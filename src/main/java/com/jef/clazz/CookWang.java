package com.jef.clazz;

/**
 * @author tufujie
 * @date 2024/1/24
 */

/**
 * 王厨师
 */
public class CookWang implements Cook {

    @Override
    public void cooking() {
        // 急忙补上肉
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("这是一道回锅肉材料有：").append("葱、").append("辣椒、").append("醋、").append("锅、").append("肉");
        System.out.println(stringBuffer.toString());
    }
}