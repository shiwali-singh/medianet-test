package com.medianet.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Thread-local AndroidDriver factory.
 *
 * Each test thread gets its own driver instance, which enables safe parallel
 * execution if TestNG is configured to run tests concurrently.
 * Call {@link #createDriver()} in @BeforeMethod and {@link #quitDriver()} in
 * @AfterMethod so every test starts with a clean app state.
 */
public final class DriverManager {

    private static final ThreadLocal<AndroidDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {}

    /** Returns the driver for the current thread. Never null after createDriver(). */
    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialised. Call createDriver() first.");
        }
        return driver;
    }

    /** Creates a fresh AndroidDriver and stores it on the current thread. */
    public static void createDriver() {
        UiAutomator2Options options = buildOptions();
        try {
            URL appiumUrl = new URL(AppConfig.getAppiumUrl());
            AndroidDriver driver = new AndroidDriver(appiumUrl, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(AppConfig.getImplicitWait()));
            DRIVER_THREAD_LOCAL.set(driver);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium URL: " + AppConfig.getAppiumUrl(), e);
        }
    }

    /** Quits the driver for the current thread and removes it from thread-local storage. */
    public static void quitDriver() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }

    private static UiAutomator2Options buildOptions() {
        String appPath = resolveAppPath(AppConfig.getAppPath());

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(AppConfig.getDeviceName());
        options.setPlatformName("Android");
        options.setPlatformVersion(AppConfig.getPlatformVersion());
        options.setApp(appPath);
        options.setAppPackage(AppConfig.getAppPackage());
        options.setAppActivity(AppConfig.getAppActivity());
        options.setNoReset(AppConfig.isNoReset());
        options.setFullReset(AppConfig.isFullReset());
        // Ensure the app is in a fresh state at the start of each test
        options.setAutoGrantPermissions(true);
        options.setNewCommandTimeout(Duration.ofSeconds(300));
        return options;
    }

    /**
     * Resolves a potentially relative APK path against the project working directory
     * so the property can be given as a simple relative path in CI/CD environments.
     */
    private static String resolveAppPath(String raw) {
        java.io.File f = new java.io.File(raw);
        if (f.isAbsolute()) return raw;
        return Paths.get(System.getProperty("user.dir"), raw).toAbsolutePath().toString();
    }
}
