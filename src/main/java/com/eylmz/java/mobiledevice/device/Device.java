package com.eylmz.java.mobiledevice.device;

import com.eylmz.java.mobiledevice.model.DevicePlatform;
import com.eylmz.java.mobiledevice.model.DeviceType;

public class Device implements IDevice {

    public static final Device NORMAL_INSTANCE = new Device(DeviceType.NORMAL);

    public static final Device MOBILE_INSTANCE = new Device(DeviceType.MOBILE);

    public static final Device TABLET_INSTANCE = new Device(DeviceType.TABLET);

    private final DeviceType deviceType;

    private final DevicePlatform devicePlatform;

    public DevicePlatform getDevicePlatform() {
        return this.devicePlatform;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    /**
     * Creates a LiteDevice with DeviceType of NORMAL and DevicePlatform UNKNOWN
     */
    public Device() {
        this(DeviceType.NORMAL, DevicePlatform.UNKNOWN);
    }

    /**
     * Creates a LiteDevice with DevicePlatform UNKNOWN
     * @param deviceType the type of device i.e. NORMAL, MOBILE, TABLET
     */
    public Device(DeviceType deviceType) {
        this(deviceType, DevicePlatform.UNKNOWN);
    }

    /**
     * Creates a LiteDevice
     * @param deviceType the type of device i.e. NORMAL, MOBILE, TABLET
     * @param devicePlatform the platform of device, i.e. IOS or ANDROID
     */
    public Device(DeviceType deviceType, DevicePlatform devicePlatform) {
        this.deviceType = deviceType;
        this.devicePlatform = devicePlatform;
    }

    public boolean isNormal() {
        return this.deviceType == DeviceType.NORMAL;
    }

    public boolean isMobile() {
        return this.deviceType == DeviceType.MOBILE;
    }

    public boolean isTablet() {
        return this.deviceType == DeviceType.TABLET;
    }

    public static Device from(DeviceType deviceType, DevicePlatform devicePlatform) {
        return new Device(deviceType, devicePlatform);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Device ");
        builder.append("type").append("=").append(this.deviceType);
        builder.append("]");
        return builder.toString();
    }

}
