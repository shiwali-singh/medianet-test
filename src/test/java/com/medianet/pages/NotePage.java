package com.medianet.pages;

import com.medianet.utils.AppLocators;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object for the Note editor screen.
 *
 * Layout: fragment_note.xml
 * ─────────────────────────────────────────
 * ┌────────────────────────────────────┐
 * │  ← (back)     Note                │
 * │  ──────────────────────────────── │
 * │  [noteNameEditText          ]      │  ← title (always editable)
 * │                                   │
 * │  [noteContentEditText       ]      │  ← body  (always editable, fills space)
 * │                                   │
 * │  [      Save Note          ]      │  ← saveNoteButton (full-width button)
 * └────────────────────────────────────┘
 *
 * Key design notes
 * ─────────────────
 * • There is NO separate "Edit" button — both fields are EditTexts and are always
 *   editable.  Opening a note and directly modifying the fields, then tapping Save,
 *   is the only edit flow.
 * • There is NO "Delete" button in this fragment.  Deletion is performed via a
 *   left swipe on the note card on the home screen (see HomePage.deleteNoteBySwipe).
 * • An error dialog (custom_dialog.xml, button id: dialog_button) appears when the
 *   user tries to save without a title.
 */
public class NotePage extends BasePage {

    @AndroidFindBy(id = AppLocators.NOTE_TITLE)
    private WebElement titleField;

    @AndroidFindBy(id = AppLocators.NOTE_CONTENT)
    private WebElement contentField;

    @AndroidFindBy(id = AppLocators.NOTE_SAVE)
    private WebElement saveButton;

    public NotePage(AndroidDriver driver) {
        super(driver);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    @Step("Type note title: '{title}'")
    public void enterTitle(String title) {
        waitForVisible(titleField).sendKeys(title);
    }

    @Step("Clear title field then type: '{title}'")
    public void clearAndEnterTitle(String title) {
        clearAndType(titleField, title);
    }

    @Step("Type note content")
    public void enterContent(String content) {
        waitForVisible(contentField).sendKeys(content);
    }

    @Step("Clear content field then type new text")
    public void clearAndEnterContent(String content) {
        clearAndType(contentField, content);
    }

    @Step("Tap 'Save Note'")
    public void clickSave() {
        waitForClickable(saveButton).click();
    }

    /** Dismiss the validation error dialog if it is visible. */
    @Step("Dismiss validation error dialog (if present)")
    public void dismissErrorDialogIfPresent() {
        By okBtn = By.id(AppLocators.DIALOG_OK_BUTTON);
        if (isPresent(okBtn)) {
            driver.findElement(okBtn).click();
        }
    }

    // ── Getters / state ───────────────────────────────────────────────────────

    @Step("Read current title field value")
    public String getTitle() {
        return waitForVisible(titleField).getText();
    }

    @Step("Read current content field value")
    public String getContent() {
        return waitForVisible(contentField).getText();
    }

    /** @return true if the validation error dialog is currently on screen. */
    @Step("Check if validation error dialog is showing")
    public boolean isErrorDialogVisible() {
        return isPresent(By.id(AppLocators.DIALOG_OK_BUTTON));
    }

    @Step("Check if Save button is enabled")
    public boolean isSaveEnabled() {
        return saveButton.isEnabled();
    }
}
