package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 5 — Input validations, empty fields, and error feedback.
 *
 * From the APK:
 *   custom_dialog.xml has a single "Ok" button (id: dialog_button) and appears
 *   when the user tries to save a note or todo list without a title.
 *   The app package is com.example.myapp.
 */
@Epic("Notes App")
@Feature("Input Validation")
public class TestValidations extends BaseTest {

    // ── Note: empty title ─────────────────────────────────────────────────────

    @Test
    @Story("Empty field validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tap Save on a new note without entering any title or content. " +
                 "Expect the validation dialog (dialog_button) to appear, preventing save.")
    public void saveEmptyNoteShouldShowValidationDialog() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        homePage.clickAddNote();
        // Tap Save immediately — no title, no content
        notePage.clickSave();

        Assert.assertTrue(
            notePage.isErrorDialogVisible(),
            "The validation dialog should appear when trying to save an empty note");

        // Dismiss and verify we are still on the note editor (not navigated back)
        notePage.dismissErrorDialogIfPresent();
        Assert.assertFalse(
            homePage.isNoteDisplayed(""),
            "No empty-title note should be created");
    }

    @Test
    @Story("Empty field validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Enter only whitespace in the title field and tap Save. " +
                 "The app should reject the whitespace-only title.")
    public void saveWhitespaceTitleShouldBeRejected() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        homePage.clickAddNote();
        notePage.enterTitle("   ");   // whitespace only
        notePage.enterContent("Some body content.");
        notePage.clickSave();

        // Either a validation dialog appears or the note is simply not created
        boolean dialogShown = notePage.isErrorDialogVisible();
        boolean noteListed  = homePage.isNoteDisplayed("   ");

        Assert.assertTrue(
            dialogShown || !noteListed,
            "A whitespace-only title should be rejected: dialog shown=" + dialogShown +
            ", note listed=" + noteListed);

        if (dialogShown) notePage.dismissErrorDialogIfPresent();
    }

    // ── Todo: empty title ─────────────────────────────────────────────────────

    @Test
    @Story("Empty field validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add items to a todo list but tap 'Save List' without giving the " +
                 "list a title.  Expect the validation dialog to appear.")
    public void saveTodoWithNoTitleShouldShowValidationDialog() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        homePage.clickAddTodo();
        // Add an item but leave the title field empty
        todoPage.addItem("Some task");
        todoPage.clickSave();

        Assert.assertTrue(
            todoPage.isErrorDialogVisible(),
            "Saving a todo list without a title should show the validation dialog");

        todoPage.dismissErrorDialogIfPresent();
    }

    // ── Todo: no items ────────────────────────────────────────────────────────

    @Test
    @Story("Empty field validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Create a todo list with a title but zero items and tap Save. " +
                 "The app should either warn the user or prevent saving.")
    public void saveTodoWithNoItemsShouldBeRejectedOrWarn() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Empty Checklist";

        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        // No items added
        todoPage.clickSave();

        boolean dialogShown   = todoPage.isErrorDialogVisible();
        boolean todoNotListed = !homePage.isNoteDisplayed(title);

        Assert.assertTrue(
            dialogShown || todoNotListed,
            "A todo list with no items should either show a dialog or not appear in the list");

        if (dialogShown) todoPage.dismissErrorDialogIfPresent();
    }

    // ── Note: very long input ─────────────────────────────────────────────────

    @Test
    @Story("Input restrictions")
    @Severity(SeverityLevel.MINOR)
    @Description("Enter a very long title (500 chars) and verify the app handles it " +
                 "without crashing — either saving it or truncating it gracefully.")
    public void veryLongTitleHandledGracefully() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String longTitle = "A".repeat(500);

        homePage.clickAddNote();
        notePage.enterTitle(longTitle);
        notePage.enterContent("Normal body.");
        notePage.clickSave();

        // Dismiss any validation dialog that might appear
        notePage.dismissErrorDialogIfPresent();

        // The app must not crash.  If the note was saved, at least one card should exist.
        // We don't enforce the exact title representation as that depends on the app's
        // max-length policy.
        boolean savedFull      = homePage.isNoteDisplayed(longTitle);
        boolean atLeastOneNote = homePage.getNoteCount() >= 1;

        Assert.assertTrue(
            savedFull || atLeastOneNote || !homePage.isListEmpty(),
            "The app should not crash when given a very long title");
    }
}
