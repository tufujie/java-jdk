package com.jef.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author tufujie
 * @date 2023/12/7
 */
@Data
public class BusinessRequestV2Vo {

    @NotEmpty(message = "列表信息不能为空")
    private List<BusinessRequestVo> businessRequestList;

    /**
     * 店铺id
     */
    @NotNull(message = "店铺id不能为空")
    private Long shopId;

}