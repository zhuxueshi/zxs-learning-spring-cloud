package org.jeecg.modules.springaop;

import org.springframework.stereotype.Component;

@Component
public class CarImpl implements Car {
    @Override
    public void buyCar(String carName) {
        System.out.println("我买了一辆" + carName);
    }
}
