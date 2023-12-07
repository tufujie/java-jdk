package com.jef.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(value = 0, message = "最小为0")
    @Max(value = 99999, message = "最大为99999")
    @NotNull(message = "商品数量不能为空")
    private Integer num;

    @Dict(type = "activityType")
    private String type;

}