package com.jef.genericity;

import com.jef.extendtest.Apple;
import com.jef.extendtest.Fruit;
import com.jef.extendtest.RedApple;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jef
 * @date 2018/11/30 16:16
 */
public class GenericityTest {

    @Test
    public void testE() {
        List<? extends Fruit> fruits = new ArrayList<Apple>();
//        fruits.add(new Fruit());

        List<? super Apple> apples = new ArrayList<Fruit>();
        apples.add(new Apple());
        apples.add(new RedApple());
        
    }

}
