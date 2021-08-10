package com.ls;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients(basePackages = {"org.jeecg"})
@ComponentScan({"com.ls","org.jeecg"})
@MapperScan(value = {"com.ls.modules.mapper"})
@EnableAsync
public class CloudDoorsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudDoorsApplication.class, args);
    }
}
