package com.jef.abstractAndInterface;

/**
 * @author tufujie
 * @date 2023/12/18
 */
public class Square implements CalInterface {

    @Override
    public float getArea(float r) {
        float area = r * r;
        System.out.println("边长为" + r + "，正方形面积=" + area);
        return area;
    }

    @Override
    public float getCircumference(float r) {
        return 4 * r;
    }
}