package com.jef.clazz;

/**
 * @author tufujie
 * @date 2024/1/24
 */
public class HotDeployTest {

    public static void main(String[] args) throws Exception {
        // 初始化容器
        SpringBeanFactory springBeanFactory = new SpringBeanFactory();

        while (true) {
            Thread.sleep(2000);
            Cook bean = springBeanFactory.getBean(Cook.class);
            bean.cooking();
        }

    }

}