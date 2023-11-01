package com.jef.designpattern.structure.flyweight;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Redis工具类
 * 处理库存
 *
 * @author Jef
 * @date 2021/12/8
 */
public class StockUtils {

    private AtomicInteger stock = new AtomicInteger(0);

    /**
     * 库存扣减
     *
     * @return int
     * @author Jef
     * @date 2021/12/8
     */
    public int getStockUsed() {
        return stock.addAndGet(1);
    }


}