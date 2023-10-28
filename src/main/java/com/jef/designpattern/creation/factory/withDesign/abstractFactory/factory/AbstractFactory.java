package com.jef.designpattern.creation.factory.withDesign.abstractFactory.factory;

import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.Basketball;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.Football;

/**
 * @author Jef
 * @date 2023/10/28
 */
public interface AbstractFactory {

    Basketball createBasketball();

    Football createFootball();
}