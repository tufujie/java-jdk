package com.jef.designpattern.creation.factory.withDesign.abstractFactory.product;

/**
 * @author Jef
 * @date 2023/10/28
 */
public class AntaFootball implements Football {

    @Override
    public void pirntFootballMessage() {
        System.out.println("安踏足球");
    }
}