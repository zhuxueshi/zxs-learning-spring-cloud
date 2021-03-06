package org.jeecg.modules.cloud.feign.feign;

import org.jeecg.common.api.vo.Result;
import org.jeecg.entity.BasDevice;
import org.jeecg.modules.cloud.constant.CloudConstant;
import org.jeecg.modules.cloud.feign.feign.fallback.JeecgTestClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 常规feign接口定义
 */
@FeignClient(value = "zxs-cloud-school", fallbackFactory = JeecgTestClientFallback.class)
@Component
public interface ZxsCloudSchoolClient {

    @GetMapping(value = "/dev/list")
    List<BasDevice> getDeviceList();
}
