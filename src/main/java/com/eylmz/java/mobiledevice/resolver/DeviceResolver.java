package com.eylmz.java.mobiledevice.resolver;

import com.eylmz.java.mobiledevice.device.Device;
import com.eylmz.java.mobiledevice.model.DevicePlatform;
import com.eylmz.java.mobiledevice.model.DeviceType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class DeviceResolver implements IDeviceResolver {

    private static DeviceResolver single_instance = null;

    private final List<String> mobileUserAgentPrefixes = new ArrayList<String>();

    private final List<String> mobileUserAgentKeywords = new ArrayList<String>();

    private final List<String> tabletUserAgentKeywords = new ArrayList<String>();

    private final List<String> normalUserAgentKeywords = new ArrayList<String>();

    private DeviceResolver() {
        init();
    }

    private DeviceResolver(List<String> normalUserAgentKeywords) {
        init();
        this.normalUserAgentKeywords.addAll(normalUserAgentKeywords);
    }

    public static DeviceResolver getInstance()
    {
        if (single_instance == null)
            single_instance = new DeviceResolver();

        return single_instance;
    }

    public Device resolveDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        // UserAgent keyword detection of Normal devices
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            for (String keyword : normalUserAgentKeywords) {
                if (userAgent.contains(keyword)) {
                    return resolveFallback(request);
                }
            }
        }
        // UserAgent keyword detection of Tablet devices
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // Android special case
            if (userAgent.contains("android") && !userAgent.contains("mobile")) {
                return resolveWithPlatform(DeviceType.TABLET, DevicePlatform.ANDROID);
            }
            // Apple special case
            if (userAgent.contains("ipad")) {
                return resolveWithPlatform(DeviceType.TABLET, DevicePlatform.IOS);
            }
            // Kindle Fire special case
            if (userAgent.contains("silk") && !userAgent.contains("mobile")) {
                return resolveWithPlatform(DeviceType.TABLET, DevicePlatform.UNKNOWN);
            }
            for (String keyword : tabletUserAgentKeywords) {
                if (userAgent.contains(keyword)) {
                    return resolveWithPlatform(DeviceType.TABLET, DevicePlatform.UNKNOWN);
                }
            }
        }
        // UAProf detection
        if (request.getHeader("x-wap-profile") != null || request.getHeader("Profile") != null) {
            if (userAgent != null) {
                // Android special case
                if (userAgent.contains("android")) {
                    return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.ANDROID);
                }
                // Apple special case
                if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
                    return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.IOS);
                }
            }
            return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
        }
        // User-Agent prefix detection
        if (userAgent != null && userAgent.length() >= 4) {
            String prefix = userAgent.substring(0, 4).toLowerCase();
            if (mobileUserAgentPrefixes.contains(prefix)) {
                return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
            }
        }
        // Accept-header based detection
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("wap")) {
            return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
        }
        // UserAgent keyword detection for Mobile devices
        if (userAgent != null) {
            // Android special case
            if (userAgent.contains("android")) {
                return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.ANDROID);
            }
            // Apple special case
            if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
                return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.IOS);
            }
            for (String keyword : mobileUserAgentKeywords) {
                if (userAgent.contains(keyword)) {
                    return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
                }
            }
        }
        // OperaMini special case
        @SuppressWarnings("rawtypes")
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = (String) headers.nextElement();
            if (header.contains("OperaMini")) {
                /*return LiteDevice.MOBILE_INSTANCE;*/
                return resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
            }
        }
        return resolveFallback(request);
    }

    // subclassing hooks

    /**
     * Wrapper method for allow subclassing platform based resolution
     */
    protected Device resolveWithPlatform(DeviceType deviceType, DevicePlatform devicePlatform) {
        return Device.from(deviceType, devicePlatform);
    }

    /**
     * List of user agent prefixes that identify mobile devices. Used primarily to match
     * by operator or handset manufacturer.
     */
    protected List<String> getMobileUserAgentPrefixes() {
        return mobileUserAgentPrefixes;
    }

    /**
     * List of user agent keywords that identify mobile devices. Used primarily to match
     * by mobile platform or operating system.
     */
    protected List<String> getMobileUserAgentKeywords() {
        return mobileUserAgentKeywords;
    }

    /**
     * List of user agent keywords that identify tablet devices. Used primarily to match
     * by tablet platform or operating system.
     */
    protected List<String> getTabletUserAgentKeywords() {
        return tabletUserAgentKeywords;
    }

    /**
     * List of user agent keywords that identify normal devices. Any items in this list
     * take precedence over the mobile and tablet user agent keywords, effectively
     * overriding those.
     */
    protected List<String> getNormalUserAgentKeywords() {
        return normalUserAgentKeywords;
    }

    /**
     * Initialize this device resolver implementation. Registers the known set of device
     * signature strings. Subclasses may override to register additional strings.
     */
    protected void init() {
        getMobileUserAgentPrefixes().addAll(
                Arrays.asList(KNOWN_MOBILE_USER_AGENT_PREFIXES));
        getMobileUserAgentKeywords().addAll(
                Arrays.asList(KNOWN_MOBILE_USER_AGENT_KEYWORDS));
        getTabletUserAgentKeywords().addAll(
                Arrays.asList(KNOWN_TABLET_USER_AGENT_KEYWORDS));
    }

    /**
     * Fallback called if no mobile device is matched by this resolver. The default
     * implementation of this method returns a "normal" {@link Device} that is neither
     * mobile or a tablet. Subclasses may override to try additional mobile or tablet
     * device matching before falling back to a "normal" device.
     */
    protected Device resolveFallback(HttpServletRequest request) {
        return Device.NORMAL_INSTANCE;
    }

    // internal helpers

    private static final String[] KNOWN_MOBILE_USER_AGENT_PREFIXES = new String[] {
            "w3c ", "w3c-", "acs-", "alav", "alca", "amoi", "avan", "benq", "bird",
            "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric",
            "hipt", "htc_", "inno", "ipaq", "ipod", "jigs", "kddi", "keji", "leno",
            "lg-c", "lg-d", "lg-g", "lge-", "lg/u", "maui", "maxo", "midp", "mits",
            "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki", "palm",
            "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams",
            "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
            "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh",
            "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp",
            "wapr", "webc", "winw", "winw", "xda ", "xda-" };

    private static final String[] KNOWN_MOBILE_USER_AGENT_KEYWORDS = new String[] {
            "blackberry", "webos", "ipod", "lge vx", "midp", "maemo", "mmp", "mobile",
            "netfront", "hiptop", "nintendo DS", "novarra", "openweb", "opera mobi",
            "opera mini", "palm", "psp", "phone", "smartphone", "symbian", "up.browser",
            "up.link", "wap", "windows ce" };

    private static final String[] KNOWN_TABLET_USER_AGENT_KEYWORDS = new String[] {
            "ipad", "playbook", "hp-tablet", "kindle" };

}