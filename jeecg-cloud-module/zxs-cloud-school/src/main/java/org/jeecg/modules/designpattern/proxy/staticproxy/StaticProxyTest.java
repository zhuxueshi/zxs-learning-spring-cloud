package org.jeecg.modules.designpattern.proxy.staticproxy;

public class StaticProxyTest {
    public static void main(String[] args) {
        //代理对象
        ICarDao proxy = new CarProxy();
        proxy.findCar();
    }
}
