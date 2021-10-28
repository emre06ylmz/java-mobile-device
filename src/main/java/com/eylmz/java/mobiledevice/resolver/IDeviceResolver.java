package com.eylmz.java.mobiledevice.resolver;

import com.eylmz.java.mobiledevice.device.Device;

import javax.servlet.http.HttpServletRequest;

public interface IDeviceResolver {

    /**
     * Resolve the device that originated the web request.
     */
    default Device resolveDevice(HttpServletRequest request) {
        return null;
    }
}
