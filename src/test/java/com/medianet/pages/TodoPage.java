package com.medianet.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class TodoPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.notesapp:id/todo_title") // replace
    private WebElement todoTitleField;

    @AndroidFindBy(id = "com.example.notesapp:id/add_item_button") // replace
    private WebElement addItemButton;

    @AndroidFindBy(id = "com.example.notesapp:id/item_input") // replace
    private WebElement itemInputField;

    @AndroidFindBy(id = "com.example.notesapp:id/save_todo_button") // replace
    private WebElement saveButton;

    @AndroidFindBy(id = "com.example.notesapp:id/todo_items_list") // replace
    private WebElement itemsList;

    public TodoPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void enterTitle(String title) {
        wait.until(ExpectedConditions.visibilityOf(todoTitleField)).sendKeys(title);
    }

    public void addItem(String item) {
        wait.until(ExpectedConditions.visibilityOf(itemInputField)).sendKeys(item);
        addItemButton.click();
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public void markItemCompleted(int index) {
        List<WebElement> checkboxes = driver.findElementsById("com.example.notesapp:id/item_checkbox"); // replace
        if (index < checkboxes.size()) {
            checkboxes.get(index).click();
        }
    }

    public boolean isItemChecked(int index) {
        List<WebElement> checkboxes = driver.findElementsById("com.example.notesapp:id/item_checkbox");
        if (index < checkboxes.size()) {
            return checkboxes.get(index).isSelected();
        }
        return false;
    }

    public String getItemText(int index) {
        List<WebElement> items = driver.findElementsById("com.example.notesapp:id/item_text"); // replace
        if (index < items.size()) {
            return items.get(index).getText();
        }
        return "";
    }

    public void editItem(int index, String newText) {
        // Assuming edit functionality
        List<WebElement> editButtons = driver.findElementsById("com.example.notesapp:id/edit_item_button"); // replace
        if (index < editButtons.size()) {
            editButtons.get(index).click();
            // Then enter new text, etc.
        }
    }

    public void deleteItem(int index) {
        List<WebElement> deleteButtons = driver.findElementsById("com.example.notesapp:id/delete_item_button"); // replace
        if (index < deleteButtons.size()) {
            deleteButtons.get(index).click();
        }
    }
}