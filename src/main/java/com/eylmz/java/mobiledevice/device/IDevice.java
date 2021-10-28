package com.eylmz.java.mobiledevice.device;

import com.eylmz.java.mobiledevice.model.DevicePlatform;

public interface IDevice {

    /**
     * True if this device is not a mobile or tablet device.
     */
    default boolean isNormal() {
        return true;
    }

    /**
     * True if this device is a mobile device such as an Apple iPhone or an Nexus One Android.
     * Could be used by a pre-handle interceptor to redirect the user to a dedicated mobile web site.
     * Could be used to apply a different page layout or stylesheet when the device is a mobile device.
     */
    default boolean isMobile() {
        return false;
    }

    /**
     * True if this device is a tablet device such as an Apple iPad or a Motorola Xoom.
     * Could be used by a pre-handle interceptor to redirect the user to a dedicated tablet web site.
     * Could be used to apply a different page layout or stylesheet when the device is a tablet device.
     */
    default boolean isTablet() {
        return false;
    }

    /**
     *
     * @return resolved DevicePlatform
     */
    default DevicePlatform getDevicePlatform() {
        return DevicePlatform.UNKNOWN;
    }

}
