package com.medianet.utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that captures a screenshot on every test failure and attaches
 * it to the Allure report so failures are immediately visual.
 *
 * Registered via @Listeners on BaseTest — no testng.xml wiring needed.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String label = "Failure — " + result.getName();
        ScreenshotUtil.attachToAllure(label);
        System.out.printf("[FAILED]  %s%n", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.printf("[PASSED]  %s%n", result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.printf("[SKIPPED] %s%n", result.getName());
    }
}
