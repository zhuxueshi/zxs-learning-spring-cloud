package org.jeecg.modules.designpattern.proxy.dynamicproxy;

public class GirlfriendDaoImpl implements IGirlDao {
    @Override
    public void findGirlfriend() {
        System.out.println("我要找女朋友.....");
    }
}
