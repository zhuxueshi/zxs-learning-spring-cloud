package org.jeecg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ZxsCloudSchoolApplication {

    public static void main(String[] args) {
//        String filename= RandomStringUtils.randomAlphanumeric(8);
//        System.out.println(filename);
        SpringApplication.run(ZxsCloudSchoolApplication.class, args);
    }
}
