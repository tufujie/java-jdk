package com.jef.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品实体类
 *
 * @author tufujie
 * @date 2023/12/7
 */
@Data
public class Product implements Serializable {

    /**
     * 商品id
     */
    private Long id;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 更新时间
     */
    private Date gmtModified;

    public Product(Long id, Integer count) {
        this.id = id;
        this.count = count;
    }

}