package com.medianet.utils;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * Parent class for every test class.
 *
 * Lifecycle
 * ---------
 * @BeforeMethod  — creates a fresh AndroidDriver (app is reinstalled unless
 *                  no.reset=true in appium.properties), making each test
 *                  fully independent.
 * @AfterMethod   — attaches a screenshot to Allure on failure, then quits the
 *                  driver so the next test starts clean.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    /** Convenience accessor for subclasses and page objects. */
    protected AndroidDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.createDriver();
        driver = DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            ScreenshotUtil.attachToAllure("Screenshot on failure");
        }
        DriverManager.quitDriver();
    }
}
