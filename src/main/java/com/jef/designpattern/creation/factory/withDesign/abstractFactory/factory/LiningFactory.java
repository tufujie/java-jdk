package com.jef.designpattern.creation.factory.withDesign.abstractFactory.factory;

import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.Basketball;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.Football;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.LiningBasketball;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.product.LiningFootball;

/**
 * @author Jef
 * @date 2023/10/28
 */
public class LiningFactory implements AbstractFactory {

    @Override
    public Basketball createBasketball() {
        return new LiningBasketball();
    }

    @Override
    public Football createFootball() {
        return new LiningFootball();
    }
}