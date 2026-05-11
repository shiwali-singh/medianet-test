package com.medianet.pages;

import com.medianet.utils.AppLocators;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Page Object for the Notes list (home) screen.
 *
 * Layout: fragment_notes_list.xml
 * ─────────────────────────────────────────
 * ┌─────────────────────────────────────┐
 * │  Notes          ⋮ (overflow menu)  │
 * │  ───────────────────────────────── │
 * │  [Note Type]  [Note Name]          │  ← noteTypeTextView, noteNameTextView
 * │  [Preview text]                    │  ← noteContentPreviewTextView
 * │  [Date]                            │  ← noteDateTextView
 * │  ···                               │
 * │                            [+ FAB] │  ← addNoteFab
 * └─────────────────────────────────────┘
 *
 * Tapping the FAB opens a standard AlertDialog:
 *   Title : "Create New Note"
 *   Items : "New Text Note"  |  "New Todo List"
 *
 * Delete: swipe a note card left-to-right (swipe-to-delete RecyclerView pattern).
 */
public class HomePage extends BasePage {

    @AndroidFindBy(id = AppLocators.HOME_FAB)
    private WebElement fab;

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    @Step("Tap FAB and choose 'New Text Note'")
    public void clickAddNote() {
        waitForClickable(fab).click();
        // The AlertDialog appears — tap the first list item by text
        By textOption = By.xpath(
            "//*[@text='" + AppLocators.DIALOG_OPT_TEXT + "']");
        waitForClickable(textOption).click();
    }

    @Step("Tap FAB and choose 'New Todo List'")
    public void clickAddTodo() {
        waitForClickable(fab).click();
        By todoOption = By.xpath(
            "//*[@text='" + AppLocators.DIALOG_OPT_TODO + "']");
        waitForClickable(todoOption).click();
    }

    @Step("Tap note with title: '{title}'")
    public void selectNote(String title) {
        // Match on the noteNameTextView that has the exact title text
        By locator = By.xpath(
            "//*[@resource-id='" + AppLocators.HOME_NOTE_NAME + "' and @text='" + title + "']");
        waitForClickable(locator).click();
    }

    /**
     * Deletes a note card by swiping it.
     * The RecyclerView uses a swipe-to-delete ItemTouchHelper.
     */
    @Step("Delete note '{title}' by swiping")
    public void deleteNoteBySwipe(String title) {
        By locator = By.xpath(
            "//*[@resource-id='" + AppLocators.HOME_NOTE_NAME + "' and @text='" + title + "']");
        WebElement card = waitForVisible(locator);

        int screenWidth = driver.manage().window().getSize().getWidth();
        int y      = card.getLocation().getY() + card.getSize().getHeight() / 2;
        int startX = (int) (screenWidth * 0.85);
        int endX   = (int) (screenWidth * 0.05);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, y));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(new org.openqa.selenium.interactions.Pause(finger, Duration.ofMillis(150)));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), endX, y));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipe));
    }

    // ── Assertions ───────────────────────────────────────────────────────────

    @Step("Check if note with title '{title}' is visible in the list")
    public boolean isNoteDisplayed(String title) {
        if (title == null || title.isEmpty()) return false;
        By locator = By.xpath(
            "//*[@resource-id='" + AppLocators.HOME_NOTE_NAME + "' and @text='" + title + "']");
        try {
            // Use a 5-second wait — long enough for post-navigation list render,
            // short enough that negative assertions don't drag on for 15 seconds.
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get total note card count")
    public int getNoteCount() {
        By locator = By.id(AppLocators.HOME_NOTE_NAME);
        // Brief wait for list to render after navigation
        try {
            wait.until(d -> !d.findElements(locator).isEmpty());
        } catch (Exception ignored) {
            // Home list may be genuinely empty
        }
        return findAll(locator).size();
    }

    @Step("Check if note list is empty (no cards shown)")
    public boolean isListEmpty() {
        return findAll(By.id(AppLocators.HOME_NOTE_NAME)).isEmpty();
    }
}
