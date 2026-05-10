package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Additional scenarios beyond the five mandatory ones.
 *
 *  6.  Multiple notes all appear in the home list.
 *  7.  A text note and a todo list coexist in the same session.
 *  8.  Overflow menu filter — "Show Text Notes Only" hides todo cards.
 *  9.  Note count decreases by exactly one after a swipe-delete.
 */
@Epic("Notes App")
@Feature("Additional Scenarios")
public class TestAdditional extends BaseTest {

    // ── Scenario 6 ────────────────────────────────────────────────────────────

    @Test
    @Story("Multiple notes")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create three separate text notes and confirm each one's card " +
                 "appears individually in the home list.")
    public void multipleNotesAllAppearInList() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String[] titles = { "Note Alpha", "Note Beta", "Note Gamma" };

        for (String title : titles) {
            homePage.clickAddNote();
            notePage.enterTitle(title);
            notePage.enterContent("Content for " + title);
            notePage.clickSave();
        }

        for (String title : titles) {
            Assert.assertTrue(
                homePage.isNoteDisplayed(title),
                "Note '" + title + "' should be visible in the home list");
        }

        Assert.assertTrue(
            homePage.getNoteCount() >= titles.length,
            "Home list must contain at least " + titles.length + " cards");
    }

    // ── Scenario 7 ────────────────────────────────────────────────────────────

    @Test
    @Story("Mixed notes and todos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create one text note and one todo list in the same session. " +
                 "Verify both appear on the home screen simultaneously.")
    public void noteAndTodoCoexistInList() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String noteTitle = "My Text Note";
        String todoTitle = "My Todo List";

        // Create text note
        homePage.clickAddNote();
        notePage.enterTitle(noteTitle);
        notePage.enterContent("Some body text.");
        notePage.clickSave();

        // Create todo
        homePage.clickAddTodo();
        todoPage.enterTitle(todoTitle);
        todoPage.addItem("Task A");
        todoPage.addItem("Task B");
        todoPage.clickSave();

        Assert.assertTrue(
            homePage.isNoteDisplayed(noteTitle),
            "Text note should be visible alongside the todo list");

        Assert.assertTrue(
            homePage.isNoteDisplayed(todoTitle),
            "Todo list should be visible alongside the text note");
    }

    // ── Scenario 8 ────────────────────────────────────────────────────────────

    @Test
    @Story("Overflow menu filter")
    @Severity(SeverityLevel.MINOR)
    @Description("Create a text note and a todo list; apply the 'Show Text Notes Only' " +
                 "filter from the overflow menu; verify the todo list card disappears " +
                 "and the text note card remains.")
    public void filterShowsOnlyTextNotes() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String noteTitle = "Filter Note";
        String todoTitle = "Filter Todo";

        homePage.clickAddNote();
        notePage.enterTitle(noteTitle);
        notePage.clickSave();

        homePage.clickAddTodo();
        todoPage.enterTitle(todoTitle);
        todoPage.addItem("A task");
        todoPage.clickSave();

        // Tap the overflow menu icon (3-dot) then choose "Show Text Notes Only"
        org.openqa.selenium.By overflow = org.openqa.selenium.By.xpath(
            "//android.widget.ImageView[@content-desc='More options']");
        driver.findElement(overflow).click();

        org.openqa.selenium.By filterOption = org.openqa.selenium.By.xpath(
            "//*[@text='" + com.medianet.utils.AppLocators.MENU_SHOW_TEXT + "']");
        driver.findElement(filterOption).click();

        Thread.sleep(400); // brief wait for list to update

        Assert.assertTrue(
            homePage.isNoteDisplayed(noteTitle),
            "The text note should still be visible after filtering");

        Assert.assertFalse(
            homePage.isNoteDisplayed(todoTitle),
            "The todo list card should be hidden when 'Show Text Notes Only' is active");
    }

    // ── Scenario 9 ────────────────────────────────────────────────────────────

    @Test
    @Story("Delete note")
    @Severity(SeverityLevel.NORMAL)
    @Description("Seed the list with two notes; swipe-delete the first; confirm the " +
                 "count dropped by one and the second note is still visible.")
    public void noteCountDecreasesAfterSwipeDelete() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String[] titles = { "Note X", "Note Y" };

        for (String t : titles) {
            homePage.clickAddNote();
            notePage.enterTitle(t);
            notePage.clickSave();
        }

        int before = homePage.getNoteCount();

        homePage.deleteNoteBySwipe(titles[0]);
        Thread.sleep(600);

        int after = homePage.getNoteCount();

        Assert.assertEquals(
            after, before - 1,
            "Note count should be exactly one less after swipe-delete");

        Assert.assertFalse(
            homePage.isNoteDisplayed(titles[0]),
            "Deleted note '" + titles[0] + "' must not appear in the list");

        Assert.assertTrue(
            homePage.isNoteDisplayed(titles[1]),
            "Non-deleted note '" + titles[1] + "' must still appear in the list");
    }
}
