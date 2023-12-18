package com.jef.abstractAndInterface;

import com.jef.util.BusinessUtil;

/**
 * 这个类中有多个类，注意他们之间的关系
 * 这个程序测试抽象方法和抽象类
 * 抽象类之间的集成和非抽象类继承抽象类，实现抽象方法
 */
public class AbstractTest {
    public static void main(String[] args) {
        Shepherd shepherd = new Shepherd();
        shepherd.eat();
        shepherd.run();
        shepherd.doSomething(4);

        Chihuahua chihuahua = new Chihuahua();
        chihuahua.eat();
        chihuahua.run();
        chihuahua.doSomething(4);
    }
}
//抽象类Animal
abstract class Animal {
    public abstract void eat();

    public abstract CalInterface getInstance();

    public void doSomething(int r) {
        BusinessUtil.doSomeThing();
        System.out.println(getInstance().getArea(r));
    }
}
//抽象类Dog，继承自Animal，有抽象方法run，继承了父类的抽象方法eat
abstract class Dog extends Animal {
    public abstract void run();
    @Override
    public void eat() {

    }
}
//Chihuahua继承了抽象类Dog，实现了Dog的方法run和其继承的抽象类的eat方法
class Chihuahua extends Dog {
    @Override
    public void run() {
        System.out.println("吉娃娃活泼的很，向前跑");
    }
    @Override
    public void eat(){
        System.out.println("吉娃娃吃的好少");
    }

    @Override
    public CalInterface getInstance() {
        return new Cire();
    }
}
//Shepherd继承了Dog，实现了Dog的方法run和其继承的抽象类的eat方法
class Shepherd extends Dog {
    @Override
    public void run() {
        System.out.println("牧羊犬跑得好快去接飞盘");
    }
    @Override
    public void eat(){
        System.out.println("牧羊犬齿东西狼吞虎咽的");
    }

    @Override
    public CalInterface getInstance() {
        return new Square();
    }
}