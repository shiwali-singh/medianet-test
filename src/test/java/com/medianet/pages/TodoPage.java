package com.medianet.pages;

import com.medianet.utils.AppLocators;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Page Object for the Todo (checklist) editor screen.
 *
 * Layout: fragment_todo_note.xml  +  item_todo.xml
 * ─────────────────────────────────────────────────
 * ┌─────────────────────────────────────┐
 * │  [todoNoteNameEditText] [Save List] │  ← title + saveTodoNoteButton (top row)
 * │  ──────────────────────────────── │
 * │  ☐ Item text                       │  ← todoCheckBox + todoTextView (item_todo)
 * │  ☐ Item text                       │
 * │  ···                               │
 * │  [new item input      ] [Add]      │  ← newTodoItemEditText + addTodoItemButton
 * └─────────────────────────────────────┘
 *
 * Key design notes
 * ─────────────────
 * • "Save List" (saveTodoNoteButton) saves the entire list (title + all current items)
 *   and navigates back to the home screen.
 * • Items are added via the bottom input + "Add" button.
 * • No per-item edit or delete buttons — items are deleted by swiping left
 *   (swipe-to-delete ItemTouchHelper on the RecyclerView).
 * • Checkboxes toggle the checked/unchecked state; the 'checked' content-description
 *   attribute is used to verify state.
 * • An error dialog (dialog_button) appears when saving without a title.
 */
public class TodoPage extends BasePage {

    @AndroidFindBy(id = AppLocators.TODO_TITLE)
    private WebElement titleField;

    @AndroidFindBy(id = AppLocators.TODO_SAVE)
    private WebElement saveButton;

    @AndroidFindBy(id = AppLocators.TODO_ITEM_INPUT)
    private WebElement itemInputField;

    @AndroidFindBy(id = AppLocators.TODO_ADD_ITEM)
    private WebElement addItemButton;

    public TodoPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    @Step("Enter todo list title: '{title}'")
    public void enterTitle(String title) {
        waitForVisible(titleField).sendKeys(title);
    }

    @Step("Clear title and type: '{title}'")
    public void clearAndEnterTitle(String title) {
        clearAndType(titleField, title);
    }

    @Step("Add checklist item: '{itemText}'")
    public void addItem(String itemText) {
        WebElement input = waitForVisible(itemInputField);
        input.clear();
        input.sendKeys(itemText);
        waitForClickable(addItemButton).click();
    }

    @Step("Tap 'Save List'")
    public void clickSave() {
        waitForClickable(saveButton).click();
    }

    @Step("Tap checkbox for item at index {index}")
    public void markItemCompleted(int index) {
        findByIndex(By.id(AppLocators.TODO_ITEM_CHECKBOX), index).click();
    }

    /**
     * Deletes the item at the given index by swiping its row from right to left.
     * The RecyclerView has a swipe-to-delete ItemTouchHelper attached.
     */
    @Step("Swipe-delete item at index {index}")
    public void deleteItemBySwipe(int index) {
        WebElement item = findByIndex(By.id(AppLocators.TODO_ITEM_TEXT), index);

        int startX = item.getLocation().getX() + item.getSize().getWidth() - 10;
        int startY = item.getLocation().getY() + item.getSize().getHeight() / 2;
        int endX   = item.getLocation().getX() + 10;

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

    /** Dismiss the validation error dialog if it is visible. */
    @Step("Dismiss validation error dialog (if present)")
    public void dismissErrorDialogIfPresent() {
        By okBtn = By.id(AppLocators.DIALOG_OK_BUTTON);
        if (isPresent(okBtn)) {
            driver.findElement(okBtn).click();
        }
    }

    // ── Assertions / getters ──────────────────────────────────────────────────

    @Step("Get text of checklist item at index {index}")
    public String getItemText(int index) {
        return findByIndex(By.id(AppLocators.TODO_ITEM_TEXT), index).getText();
    }

    /**
     * Returns true if the CheckBox at the given index is in the checked state.
     *
     * Appium exposes the CompoundButton's checked state via the 'checked' attribute
     * on Android (value is the string "true" or "false").
     */
    @Step("Check whether item at index {index} is checked")
    public boolean isItemChecked(int index) {
        WebElement checkbox = findByIndex(By.id(AppLocators.TODO_ITEM_CHECKBOX), index);
        String checked = checkbox.getAttribute("checked");
        return "true".equalsIgnoreCase(checked);
    }

    @Step("Get total number of checklist items")
    public int getItemCount() {
        return findAll(By.id(AppLocators.TODO_ITEM_TEXT)).size();
    }

    @Step("Get number of checked items")
    public long getCheckedItemCount() {
        return findAll(By.id(AppLocators.TODO_ITEM_CHECKBOX)).stream()
            .filter(cb -> "true".equalsIgnoreCase(cb.getAttribute("checked")))
            .count();
    }

    @Step("Check if validation error dialog is showing")
    public boolean isErrorDialogVisible() {
        return isPresent(By.id(AppLocators.DIALOG_OK_BUTTON));
    }
}
