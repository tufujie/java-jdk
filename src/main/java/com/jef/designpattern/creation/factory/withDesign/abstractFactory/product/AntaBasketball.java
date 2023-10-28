package com.jef.designpattern.creation.factory.withDesign.abstractFactory.product;

/**
 * @author Jef
 * @date 2023/10/28
 */
public class AntaBasketball implements Basketball {

    @Override
    public void pirntBasketballMessage() {
        System.out.println("安踏篮球");
    }
}