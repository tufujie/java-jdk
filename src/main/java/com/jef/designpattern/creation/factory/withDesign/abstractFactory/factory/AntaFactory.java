package com.jef.designpattern.creation.factory.withDesign.abstractFactory.factory;

import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.AntaBasketball;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.AntaFootball;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.Basketball;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.Football;

/**
 * @author Jef
 * @date 2023/10/28
 */
public class AntaFactory implements AbstractFactory {

    @Override
    public Basketball createBasketball() {
        return new AntaBasketball();
    }

    @Override
    public Football createFootball() {
        return new AntaFootball();
    }
}