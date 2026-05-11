package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 4 — Edit and delete existing notes and to-do list items.
 *
 * App behaviours confirmed from APK inspection
 * ─────────────────────────────────────────────
 * • Notes: No separate "Edit" button — noteNameEditText and noteContentEditText are
 *   always editable.  To edit: open note → clear field → type new text → Save.
 * • Notes: No "Delete" button inside the note fragment.  Delete via swipe-left on
 *   the note card on the home screen (swipe-to-delete ItemTouchHelper).
 * • Todo items: No per-item edit button — items are deleted by swiping their row.
 *   New items can be added after re-opening an existing todo list.
 */
@Epic("Notes App")
@Feature("Edit and Delete")
public class TestEditDelete extends BaseTest {

    // ── Note: Edit ────────────────────────────────────────────────────────────

    @Test
    @Story("Edit note")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a note; re-open it; overwrite title and content; save; " +
                 "verify the updated values are reflected in the list and editor.")
    public void editNoteAndVerifyUpdatedContent() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String origTitle   = "Draft Note";
        String origContent = "Initial draft content.";
        String newTitle    = "Final Note";
        String newContent  = "Revised and approved content.";

        // Create
        homePage.clickAddNote();
        notePage.enterTitle(origTitle);
        notePage.enterContent(origContent);
        notePage.clickSave();

        // Re-open and edit (both fields are EditTexts — no separate Edit button)
        homePage.selectNote(origTitle);
        notePage.clearAndEnterTitle(newTitle);
        notePage.clearAndEnterContent(newContent);
        notePage.clickSave();

        // Verify updated title on home list
        Assert.assertTrue(
            homePage.isNoteDisplayed(newTitle),
            "Updated title '" + newTitle + "' should appear in the home list");

        Assert.assertFalse(
            homePage.isNoteDisplayed(origTitle),
            "Old title '" + origTitle + "' must no longer appear in the list");

        // Verify stored values
        homePage.selectNote(newTitle);
        Assert.assertEquals(notePage.getTitle(),   newTitle,   "Title should reflect the edit");
        Assert.assertEquals(notePage.getContent(), newContent, "Content should reflect the edit");
    }

    // ── Note: Delete ──────────────────────────────────────────────────────────

    @Test
    @Story("Delete note")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a note; swipe its card left on the home screen to delete it; " +
                 "verify the card is removed from the list.")
    public void deleteNoteBySwipeAndVerifyRemoval() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String title = "Temporary Note";

        homePage.clickAddNote();
        notePage.enterTitle(title);
        notePage.enterContent("Will be deleted shortly.");
        notePage.clickSave();

        Assert.assertTrue(
            homePage.isNoteDisplayed(title),
            "Note must be present in the list before the delete swipe");

        // Swipe left to delete
        homePage.deleteNoteBySwipe(title);

        // Short wait for the animation to complete
        try { Thread.sleep(600); } catch (InterruptedException ignored) {}

        Assert.assertFalse(
            homePage.isNoteDisplayed(title),
            "Note card should not be present after swipe-delete");
    }

    // ── Todo: Delete item ─────────────────────────────────────────────────────

    @Test
    @Story("Delete todo list")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create a todo list; swipe its card left on the home screen to delete it; " +
                 "verify the card is removed from the home list.")
    public void deleteTodoItemBySwipeAndVerify() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Delete Todo Test";
        String item1 = "Task Alpha";
        String item2 = "Task Beta";

        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        todoPage.addItem(item1);
        todoPage.addItem(item2);
        todoPage.clickSave();

        Assert.assertTrue(
            homePage.isNoteDisplayed(title),
            "Todo list must be present in the home list before delete");

        // Swipe the todo card left on the home screen to delete it
        homePage.deleteNoteBySwipe(title);
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}

        Assert.assertFalse(
            homePage.isNoteDisplayed(title),
            "Todo list card should not be present after swipe-delete");
    }

    // ── Todo: Add item to existing list ──────────────────────────────────────

    @Test
    @Story("Edit todo list")
    @Severity(SeverityLevel.NORMAL)
    @Description("Re-open an existing todo list and add a new item; verify the new " +
                 "item is appended and the count increases by one.")
    public void addItemToExistingTodoList() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title    = "Growing List";
        String item1    = "Original task";
        String newItem  = "Newly added task";

        // Create with one item
        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        todoPage.addItem(item1);
        todoPage.clickSave();

        // Re-open and add another item
        homePage.selectNote(title);
        int before = todoPage.getItemCount();
        todoPage.addItem(newItem);
        todoPage.clickSave();

        homePage.selectNote(title);

        Assert.assertEquals(
            todoPage.getItemCount(), before + 1,
            "Item count should be one more than before after adding a new item");

        Assert.assertEquals(
            todoPage.getItemText(before), newItem,
            "The last item should be the newly added one");
    }
}
