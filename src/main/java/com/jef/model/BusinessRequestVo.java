package com.jef.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tufujie
 * @date 2023/12/7
 */
@Data
public class BusinessRequestVo {

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long goodsId;
    /**
     * 数量
     */
    @NotNull(message = "商品数量不能为空")
    private Integer num;

}