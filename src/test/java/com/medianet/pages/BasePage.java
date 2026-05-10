package com.medianet.pages;

import com.medianet.utils.AppConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Shared driver access and helper waits for all page objects.
 * Every page inherits from here rather than duplicating wait boilerplate.
 */
public abstract class BasePage {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(AppConfig.getExplicitWait()));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ── Waits ────────────────────────────────────────────────────────────────

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitForText(By locator, String text) {
        return wait.until(ExpectedConditions.textToBe(locator, text));
    }

    // ── Presence checks ──────────────────────────────────────────────────────

    protected boolean isPresent(By locator) {
        try {
            return !driver.findElements(locator).isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Text helpers ─────────────────────────────────────────────────────────

    /** Clear existing text then type new text. */
    protected void clearAndType(WebElement element, String text) {
        waitForVisible(element).clear();
        element.sendKeys(text);
    }

    // ── List helpers ─────────────────────────────────────────────────────────

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected WebElement findByIndex(By locator, int index) {
        List<WebElement> elements = findAll(locator);
        if (index >= elements.size()) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for element list of size " + elements.size());
        }
        return elements.get(index);
    }
}
