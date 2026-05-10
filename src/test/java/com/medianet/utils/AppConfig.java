package com.medianet.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads appium.properties from the test classpath so every capability lives
 * in one place and tests never hard-code device/app details.
 */
public final class AppConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = AppConfig.class.getClassLoader()
                .getResourceAsStream("appium.properties")) {
            if (is == null) {
                throw new IllegalStateException(
                        "appium.properties not found on classpath. " +
                        "Make sure src/test/resources/appium.properties exists.");
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load appium.properties", e);
        }
    }

    private AppConfig() {}

    public static String getAppiumUrl()       { return get("appium.url", "http://127.0.0.1:4723"); }
    public static String getDeviceName()      { return get("device.name", "emulator-5554"); }
    public static String getPlatformVersion() { return get("platform.version", "11.0"); }
    public static String getAppPath()         { return get("app.path", "apps/notes-app.apk"); }
    public static String getAppPackage()      { return get("app.package", "com.example.notesapp"); }
    public static String getAppActivity()     { return get("app.activity", "com.example.notesapp.MainActivity"); }
    public static int    getImplicitWait()    { return getInt("implicit.wait", 0); }
    public static int    getExplicitWait()    { return getInt("explicit.wait", 15); }
    public static boolean isNoReset()         { return getBool("no.reset", false); }
    public static boolean isFullReset()       { return getBool("full.reset", false); }

    private static String  get(String key, String def)  { return PROPS.getProperty(key, def); }
    private static int     getInt(String key, int def)  { return Integer.parseInt(get(key, String.valueOf(def))); }
    private static boolean getBool(String key, boolean def) { return Boolean.parseBoolean(get(key, String.valueOf(def))); }
}
