package org.jeecg.modules.designpattern.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class GirlProxy {
    // 接收一个目标对象
    private Object target;

    public GirlProxy(Object target) {
        this.target = target;
    }
    // 返回对目标对象(target)代理后的对象(proxy)
    public Object getProxyInstance() {
        Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 获取当前执行的方法的方法名
                String methodName = method.getName();
                System.out.println("代理执行方法:" + methodName + ";开启事务...");
                // 执行目标对象方法
                Object result = method.invoke(target, args);
                System.out.println("代理执行方法:" + methodName + ";提交事务...");
                return result;
            }
        });
        return proxy;
    }
}
