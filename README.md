## Java library to determine client device type.

When you use this library, it gives you a Device object.

Device objects has some methods which is available to determine information about client.
+ default boolean isNormal(); //True if this device is not a mobile or tablet device.
+ default boolean isMobile(); //True if this device is a mobile device such as an Apple iPhone or an Nexus One Android.
+ default boolean isTablet(); //True if this device is a tablet device such as an Apple iPad or a Motorola Xoom.
+ default DevicePlatform getDevicePlatform(); //Return resolved DevicePlatform

Device Platforms enum values,
+ IOS
+ ANDROID
+ UNKNOWN

Device Types enum values,
+ NORMAL
+ MOBILE
+ TABLET

## How to use

The `DeviceResolver` class should be used as a singleton in your application.

```java
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        DeviceResolver deviceResolver = DeviceResolver.getInstance();
        Device device = deviceResolver.resolveDevice(httpServletRequest);
        boolean isMobile = device.isMobile();
```