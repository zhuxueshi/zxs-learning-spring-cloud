package org.jeecg.modules.designpattern.proxy.dynamicproxy;

public class DynamicProyxTest {
    public static void main(String[] args) {
        //目标对象
        IGirlDao target = new GirlfriendDaoImpl();
        //代理对象
        IGirlDao iGirlDao = (IGirlDao) new GirlProxy(target).getProxyInstance();
        iGirlDao.findGirlfriend();
    }
}
