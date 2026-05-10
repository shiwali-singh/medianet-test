package com.medianet.pages;

import com.medianet.utils.AppLocators;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

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
     * Deletes a note by swiping its card from right to left.
     * The RecyclerView uses a swipe-to-delete ItemTouchHelper.
     */
    @Step("Delete note '{title}' by swiping left")
    public void deleteNoteBySwipe(String title) {
        By locator = By.xpath(
            "//*[@resource-id='" + AppLocators.HOME_NOTE_NAME + "' and @text='" + title + "']");
        WebElement card = waitForVisible(locator);

        int startX = card.getLocation().getX() + card.getSize().getWidth() - 10;
        int startY = card.getLocation().getY() + card.getSize().getHeight() / 2;
        int endX   = card.getLocation().getX() + 10;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), endX, startY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipe));
    }

    // ── Assertions ───────────────────────────────────────────────────────────

    @Step("Check if note with title '{title}' is visible in the list")
    public boolean isNoteDisplayed(String title) {
        By locator = By.xpath(
            "//*[@resource-id='" + AppLocators.HOME_NOTE_NAME + "' and @text='" + title + "']");
        return isPresent(locator);
    }

    @Step("Get total note card count")
    public int getNoteCount() {
        return findAll(By.id(AppLocators.HOME_NOTE_NAME)).size();
    }

    @Step("Check if note list is empty (no cards shown)")
    public boolean isListEmpty() {
        return findAll(By.id(AppLocators.HOME_NOTE_NAME)).isEmpty();
    }
}
