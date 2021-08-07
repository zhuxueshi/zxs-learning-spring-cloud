package org.jeecg.modules.designpattern.proxy.staticproxy;

/**
 * 找车代理对象类
 */
public class CarProxy implements ICarDao {
    private ICarDao icarDao = new CarDaoImpl();
    @Override
    public void findCar() {
        icarDao.findCar();
    }
}
