package com.jef.designpattern.creation.factory;

import com.jef.designpattern.creation.factory.withDesign.factoryMethod.factory.PhilipsScreenFactory;
import com.jef.designpattern.creation.factory.withDesign.factoryMethod.factory.SamsungScreenFactory;
import com.jef.designpattern.creation.factory.withDesign.factoryMethod.factory.ScreenFactory;
import com.jef.designpattern.creation.factory.withDesign.factoryMethod.product.Screen;

import org.junit.jupiter.api.Test;

/**
 * 工厂方法客户端
 *
 * @author Jef
 * @date 2023/6/27
 */
public class FactoryMethodTest {

    @Test
    void testFactoryMethod() {
        // 飞利浦显示器
        ScreenFactory philipsScreenFactory = new PhilipsScreenFactory();
        Screen philipsScreen = philipsScreenFactory.create();
        philipsScreen.watch();

        // 三星显示器
        ScreenFactory samsungScreenFactory = new SamsungScreenFactory();
        Screen samsungScreen = samsungScreenFactory.create();
        samsungScreen.watch();
    }
}