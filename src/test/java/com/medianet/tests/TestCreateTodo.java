package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 2 — Create a new to-do list with multiple checklist items.
 *
 * Flow
 * ─────
 * 1. Tap FAB  →  dialog  →  choose "New Todo List"
 * 2. Fill todoNoteNameEditText (title)
 * 3. For each item: type in newTodoItemEditText → tap addTodoItemButton
 * 4. Tap saveTodoNoteButton ("Save List")  →  back to home
 * 5. Assert the list title card is visible on home
 * 6. Re-open  →  assert all item texts and count match
 */
@Epic("Notes App")
@Feature("Todo List Management")
public class TestCreateTodo extends BaseTest {

    @Test
    @Story("Create todo")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a todo list with three items; save; re-open; verify " +
                 "item count and each item's text.")
    public void createTodoListWithMultipleItems() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String   title = "Grocery Shopping";
        String[] items = { "Milk", "Bread", "Eggs" };

        // Create
        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        for (String item : items) {
            todoPage.addItem(item);
        }
        todoPage.clickSave();

        // Verify title on home list
        Assert.assertTrue(
            homePage.isNoteDisplayed(title),
            "Todo list '" + title + "' should appear on the home screen");

        // Re-open and verify items
        homePage.selectNote(title);

        Assert.assertEquals(
            todoPage.getItemCount(), items.length,
            "Todo list should contain exactly " + items.length + " items");

        for (int i = 0; i < items.length; i++) {
            Assert.assertEquals(
                todoPage.getItemText(i), items[i],
                "Item at position " + i + " should read '" + items[i] + "'");
        }
    }

    @Test
    @Story("Create todo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create a todo list with a single item and confirm both the list " +
                 "title and the one item are saved correctly.")
    public void createTodoListWithOneItem() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Single Task";
        String item  = "Call the bank";

        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        todoPage.addItem(item);
        todoPage.clickSave();

        Assert.assertTrue(
            homePage.isNoteDisplayed(title),
            "Single-item todo list title should appear on the home screen");

        homePage.selectNote(title);

        Assert.assertEquals(
            todoPage.getItemCount(), 1,
            "Todo list should have exactly 1 item");

        Assert.assertEquals(
            todoPage.getItemText(0), item,
            "The single item's text should match");
    }
}
