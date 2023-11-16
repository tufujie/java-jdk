package com.jef.algorithm.sort;

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
    public int compareTo(Object o) {
        if (o instanceof CustomOrderProductCompare) {
            CustomOrderProductCompare orderProduct = (CustomOrderProductCompare) o;
            if (this.num > orderProduct.num) {
                // >对应1则表示正序（从小到大），-1则表示逆序（从大到小）
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