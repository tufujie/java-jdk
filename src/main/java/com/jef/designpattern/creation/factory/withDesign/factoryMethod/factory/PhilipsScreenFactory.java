package com.jef.designpattern.creation.factory.withDesign.factoryMethod.factory;

import com.jef.designpattern.creation.factory.withDesign.factoryMethod.product.PhilipsScreen;
import com.jef.designpattern.creation.factory.withDesign.factoryMethod.product.Screen;

/**
 * 具体产品工厂-飞利浦显示器工厂
 *
 * @author Jef
 * @date 2023/6/27
 */
public class PhilipsScreenFactory extends ScreenFactory {

    @Override
    protected Screen createScreen() {
        return new PhilipsScreen();
    }

    @Override
    protected void testScreen(Screen screen) {
        System.out.println("测试飞利浦显示器");
    }
}