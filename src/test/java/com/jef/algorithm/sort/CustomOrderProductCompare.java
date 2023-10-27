package com.jef.algorithm.sort;

import org.jetbrains.annotations.NotNull;

/**
 * @author Jef
 * @date 2023/10/27
 */
public class CustomOrderProductCompare implements Comparable {
    private int num;

    private String productName;

    public String getProductName() {
        return productName;
    }

    public Integer getNum() {
        return num;
    }

    public CustomOrderProductCompare(int num, String productName) {
        this.num = num;
        this.productName = productName;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof CustomOrderProductCompare) {
            CustomOrderProductCompare orderProduct = (CustomOrderProductCompare) o;
            if (this.num > orderProduct.num) {
                return 1;
            } else if (this.num < orderProduct.num) {
                return -1;
            } else {
                return this.productName.compareTo(orderProduct.productName);
            }
        }
        throw new RuntimeException("传入的数据类型不一致");
    }
}