package com.jef.algorithm;

import com.jef.util.BitUtil;
import org.junit.jupiter.api.Test;

/**
 * @author tufujie
 * @date 2024/1/18
 */
public class BitTest {

    @Test
    void testSetAndGet() {
        int a = 1;
        a = BitUtil.set(a, 1);
        System.out.println(a);
        System.out.println(a);
        System.out.println(BitUtil.read(a, 1));
    }

}