package com.jef.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tufujie
 * @date 2023/12/7
 */
public class BeanValidatorTest {

    @Test
    @DisplayName("测试实体填写校验")
    void testBeanValidator() {
        BusinessRequestVo businessRequestVo = new BusinessRequestVo();
        try {
            BeanValidator.validate(businessRequestVo);
        } catch (Exception e) {
            System.out.println("校验结果1：" + e.getMessage());
        }
        businessRequestVo.setGoodsId(1L);
        businessRequestVo.setNum(-1);
        try {
            BeanValidator.validate(businessRequestVo);
        } catch (Exception e) {
            System.out.println("校验结果2：" + e.getMessage());
        }
        businessRequestVo.setNum(1);
        businessRequestVo.setType(DictEnum.ONE_TYPE.getType());
        try {
            BeanValidator.validate(businessRequestVo);
        } catch (Exception e) {
            System.out.println("校验结果3：" + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试实体填写校验")
    void testBeanValidatorV2() {
        BusinessRequestV2Vo businessRequestV2Vo = new BusinessRequestV2Vo();
        List<BusinessRequestVo> businessRequestList = new ArrayList<>();
        BusinessRequestVo businessRequestVo = new BusinessRequestVo();
        businessRequestList.add(businessRequestVo);
        businessRequestV2Vo.setBusinessRequestList(businessRequestList);
        try {
            BeanValidator.validate(businessRequestV2Vo);
        } catch (Exception e) {
            System.out.println("校验结果：" + e.getMessage());
        }
        for (BusinessRequestVo temp : businessRequestV2Vo.getBusinessRequestList()) {
            try {
                BeanValidator.validate(temp);
            } catch (Exception e) {
                System.out.println("依赖对象校验结果：" + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("测试实体填写校验信息")
    void testGetBeanValidatorMessage() {
        BusinessRequestVo businessRequestVo = new BusinessRequestVo();
        String checkMessage = BeanValidator.getValidate(businessRequestVo);
        if (StringUtils.isNotEmpty(checkMessage)) {
            System.out.println("校验信息=" + checkMessage);
        }
    }

}