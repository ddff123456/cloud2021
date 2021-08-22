package com.ye.springcloud.hander;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ye.springcloud.entities.CommonResult;

/**
 * 客户自定义限流处理逻辑类，为了解耦
 */

public class CustomerBlockHandler {
    public static CommonResult handlerException(BlockException exception) {
        return new CommonResult(4444,"按客戶自定义,global handlerException----1");
    }

    public static CommonResult handlerException2(BlockException exception) {
        return new CommonResult(4444,"按客戶自定义,global handlerException----2");
    }
}