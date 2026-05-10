package com.medianet.utils;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

/**
 * Captures a screenshot from the current driver and attaches it to the
 * Allure report under the given label.
 */
public final class ScreenshotUtil {

    private ScreenshotUtil() {}

    /**
     * Takes a screenshot and attaches it to the running Allure step.
     *
     * @param label the attachment label visible in the Allure HTML report
     */
    public static void attachToAllure(String label) {
        try {
            AndroidDriver driver = DriverManager.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(label, "image/png", new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            // Non-fatal: log but don't mask the real test failure
            System.err.println("[ScreenshotUtil] Could not capture screenshot: " + e.getMessage());
        }
    }
}
