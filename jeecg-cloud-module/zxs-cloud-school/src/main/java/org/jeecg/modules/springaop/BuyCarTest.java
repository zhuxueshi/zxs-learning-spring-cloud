package org.jeecg.modules.springaop;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BuyCarTest {
    ApplicationContext appCtx = new ClassPathXmlApplicationContext("application-car.xml",BuyCarTest.class);
    public static void main(String[] args) {
        ApplicationContext appCtx = new ClassPathXmlApplicationContext("application-car.xml",BuyCarTest.class);
//        ApplicationContext appCtx = new FileSystemXmlApplicationContext("E:\\zxs-leraning-project\\learning-projects\\jeecg-boot\\jeecg-boot\\jeecg-cloud-module\\zxs-cloud-school\\src\\main\\java\\org\\jeecg\\modules\\springaop\\application-car.xml");
        Car car = (Car)appCtx.getBean("carImpl");
        car.buyCar("宝马");
        car.buyCar("特斯拉");
    }


    @Test
    public void testCar() throws Exception {
        //从Spring的IOC容器中获取对象,使用接口接收！
        Car car = (Car)appCtx.getBean("carImpl");
        System.out.println(car.getClass());
        car.buyCar("宝马");
        car.buyCar("特斯拉");
    }
}
