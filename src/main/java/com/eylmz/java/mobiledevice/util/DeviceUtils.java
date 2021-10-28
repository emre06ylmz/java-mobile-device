package com.eylmz.java.mobiledevice.util;

import javax.servlet.http.HttpServletRequest;

import com.eylmz.java.mobiledevice.device.Device;
import org.springframework.web.context.request.RequestAttributes;

public class DeviceUtils {

    /**
     * The name of the request attribute the current Device is indexed by.
     * The attribute name is 'currentDevice'.
     */
    public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";

    /**
     * Static utility method that extracts the current device from the web request.
     * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
     * @param request the servlet request
     * @return the current device, or null if no device has been resolved for the request
     */
    public static Device getCurrentDevice(HttpServletRequest request) {
        return (Device) request.getAttribute(CURRENT_DEVICE_ATTRIBUTE);
    }

    /**
     * Static utility method that extracts the current device from the web request.
     * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
     * Throws a runtime exception if the current device has not been resolved.
     * @param request the servlet request
     * @return the current device
     */
    public static Device getRequiredCurrentDevice(HttpServletRequest request) {
        Device device = getCurrentDevice(request);
        if (device == null) {
            throw new IllegalStateException("No current device is set in this request and one is required - have you configured a DeviceResolverHandlerInterceptor?");
        }
        return device;
    }

    /**
     * Static utility method that extracts the current device from the request attributes map.
     * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
     * @param attributes the request attributes
     * @return the current device, or null if no device has been resolved for the request
     */
    public static Device getCurrentDevice(RequestAttributes attributes) {
        return (Device) attributes.getAttribute(CURRENT_DEVICE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    }


}