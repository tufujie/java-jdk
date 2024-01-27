package com.jef.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tufujie
 * @date 2024/1/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitPointVo implements Serializable {

    private static final long serialVersionUID = 3488358560990212807L;
    private String one;

    private String two;

    private String three;

}