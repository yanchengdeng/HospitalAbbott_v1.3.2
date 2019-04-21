package com.comvee.hospitalabbott.tool.ble;

import com.vise.baseble.common.BleExceptionCode;
import com.vise.baseble.exception.BleException;

public class DeviceFoundException extends BleException {
    public DeviceFoundException() {
        super(BleExceptionCode.OTHER_ERR, "未找到指定设备! ");
    }
}