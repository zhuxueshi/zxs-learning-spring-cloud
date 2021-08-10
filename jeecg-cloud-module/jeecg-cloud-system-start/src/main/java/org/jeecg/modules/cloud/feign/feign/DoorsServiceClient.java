package org.jeecg.modules.cloud.feign.feign;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.entity.BasDevice;
import org.jeecg.modules.cloud.feign.feign.fallback.JeecgTestClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 常规feign接口定义
 */
@FeignClient(value = "ls-cloud-doors", fallbackFactory = JeecgTestClientFallback.class)
@Component
public interface DoorsServiceClient {

    @GetMapping(value = "/dev/list")
    List<JSONObject> getDeviceList();
}
