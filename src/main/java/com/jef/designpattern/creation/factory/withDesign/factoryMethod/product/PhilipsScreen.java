package com.jef.designpattern.creation.factory.withDesign.factoryMethod.product;

/**
 * 具体产品-飞利浦显示器
 *
 * @author Jef
 * @date 2023/6/27
 */
public class PhilipsScreen implements Screen {

    public PhilipsScreen() {
        System.out.println("制造飞利浦显示器");
    }

    @Override
    public void watch() {
        System.out.println("观看飞利浦显示器");
    }
}