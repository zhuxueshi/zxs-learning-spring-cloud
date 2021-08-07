package org.jeecg.modules.springaop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CarAspect {

    @Before("pointcut_()")
    void beforeBuyCarDoSomething(){
        System.out.println("买车之前,我需要做一些市场调查.....");
    }
    @After("pointcut_()")
    void afterBuyCarDoSomething(){
        System.out.println("买车之后,我需要提升开车技术技能.....");
        System.out.println("===================================================");
    }
    @Around("pointcut_()")
    void arroudBuyCarDoSomething(ProceedingJoinPoint pjp){
        System.out.println("买车环绕之前....");
        try {
            pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("买车环绕之后....");
        System.out.println("===================================================");
    }

    // 定义一个切入点表达式变量  （后面使用这个切入点表达式的时候，直接引用方法名即可）
    @Pointcut("execution(* *.buyCar(..))")
    public void pointcut_(){}
}
