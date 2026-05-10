package com.medianet.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class HomePage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.notesapp:id/add_note_button") // replace with actual ID
    private WebElement addNoteButton;

    @AndroidFindBy(id = "com.example.notesapp:id/add_todo_button") // replace with actual ID
    private WebElement addTodoButton;

    @AndroidFindBy(id = "com.example.notesapp:id/notes_list") // replace with actual ID
    private WebElement notesList;

    public HomePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickAddNote() {
        wait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();
    }

    public void clickAddTodo() {
        wait.until(ExpectedConditions.elementToBeClickable(addTodoButton)).click();
    }

    public boolean isNoteDisplayed(String noteTitle) {
        // Assuming notes are in a list, check if title is present
        return driver.getPageSource().contains(noteTitle);
    }

    public void selectNote(String noteTitle) {
        // Click on the note in the list
        WebElement note = driver.findElementByXPath("//android.widget.TextView[@text='" + noteTitle + "']");
        note.click();
    }
}