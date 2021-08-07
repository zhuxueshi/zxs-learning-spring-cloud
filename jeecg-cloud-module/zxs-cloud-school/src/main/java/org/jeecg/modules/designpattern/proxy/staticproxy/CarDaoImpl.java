package org.jeecg.modules.designpattern.proxy.staticproxy;

/**
 * 找车实现类
 */
public class CarDaoImpl implements ICarDao {
    @Override
    public void findCar() {
        System.out.println("我要找车.....");
    }
}
