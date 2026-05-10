package com.medianet.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class NotePage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.notesapp:id/note_title")  
    private WebElement noteTitleField;

    @AndroidFindBy(id = "com.example.notesapp:id/note_content")  
    private WebElement noteContentField;

    @AndroidFindBy(id = "com.example.notesapp:id/save_button")  
    private WebElement saveButton;

    @AndroidFindBy(id = "com.example.notesapp:id/edit_button")  
    private WebElement editButton;

    @AndroidFindBy(id = "com.example.notesapp:id/delete_button")  
    private WebElement deleteButton;

    public NotePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void enterTitle(String title) {
        wait.until(ExpectedConditions.visibilityOf(noteTitleField)).sendKeys(title);
    }

    public void enterContent(String content) {
        wait.until(ExpectedConditions.visibilityOf(noteContentField)).sendKeys(content);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public void clickEdit() {
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }

    public void clickDelete() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }

    public String getTitle() {
        return noteTitleField.getText();
    }

    public String getContent() {
        return noteContentField.getText();
    }
}