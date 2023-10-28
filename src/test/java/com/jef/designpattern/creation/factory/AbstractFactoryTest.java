package com.jef.designpattern.creation.factory;

import com.jef.designpattern.creation.factory.withDesign.abstractFactory.factory.AbstractFactory;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.factory.AntaFactory;
import com.jef.designpattern.creation.factory.withDesign.abstractFactory.factory.LiningFactory;

import org.junit.jupiter.api.Test;

/**
 * @author Jef
 * @date 2023/10/28
 */
public class AbstractFactoryTest {

    @Test
    void testAbstractFactory() {
        AbstractFactory liningFactory = new LiningFactory();
        liningFactory.createBasketball().pirntBasketballMessage();
        liningFactory.createFootball().pirntFootballMessage();

        AbstractFactory antaFactory = new AntaFactory();
        antaFactory.createBasketball().pirntBasketballMessage();
        antaFactory.createFootball().pirntFootballMessage();
    }

}