package com.medianet.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static AppiumDriver driver;

    public static AppiumDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }

    private static void setupDriver() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554"); // or your device name
        options.setPlatformName("Android");
        options.setPlatformVersion("11.0"); // adjust as needed
        options.setApp("path/to/your/app.apk"); // replace with actual APK path
        options.setAppPackage("com.example.notesapp"); // replace with actual package
        options.setAppActivity("com.example.notesapp.MainActivity"); // replace with actual activity
        options.setNoReset(false);
        options.setFullReset(false);

        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}