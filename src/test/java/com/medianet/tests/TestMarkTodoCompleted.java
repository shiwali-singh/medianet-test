package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 3 — Mark to-do list items as completed and validate their checked state.
 *
 * The 'checked' attribute on each android.widget.CheckBox (todoCheckBox) is
 * read via WebElement.getAttribute("checked") — "true" means checked.
 */
@Epic("Notes App")
@Feature("Todo List Management")
public class TestMarkTodoCompleted extends BaseTest {

    @Test
    @Story("Mark items completed")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a three-item todo. Mark items 0 and 2. Verify items 0 and 2 " +
                 "are checked, item 1 remains unchecked, and the total checked count is 2.")
    public void markSelectedItemsAndVerifyCheckedState() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String   title = "Home Chores";
        String[] items = { "Vacuum", "Do laundry", "Wash dishes" };

        // Create
        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        for (String item : items) {
            todoPage.addItem(item);
        }
        todoPage.clickSave();

        // Open and mark items 0 and 2
        homePage.selectNote(title);
        todoPage.markItemCompleted(0);
        todoPage.markItemCompleted(2);

        // Verify checked / unchecked states
        Assert.assertTrue(
            todoPage.isItemChecked(0),
            "Item 0 ('Vacuum') should be checked after tapping its checkbox");

        Assert.assertFalse(
            todoPage.isItemChecked(1),
            "Item 1 ('Do laundry') should remain unchecked");

        Assert.assertTrue(
            todoPage.isItemChecked(2),
            "Item 2 ('Wash dishes') should be checked after tapping its checkbox");

        Assert.assertEquals(
            todoPage.getCheckedItemCount(), 2L,
            "Exactly 2 of the 3 items should be in the checked state");
    }

    @Test
    @Story("Mark items completed")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tap a checkbox once (checked) then tap it again (unchecked). " +
                 "Confirm the toggle works correctly in both directions.")
    public void toggleItemCheckUncheck() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Toggle Test";

        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        todoPage.addItem("Take a walk");
        todoPage.clickSave();

        homePage.selectNote(title);

        // First tap → checked
        todoPage.markItemCompleted(0);
        Assert.assertTrue(
            todoPage.isItemChecked(0),
            "Item should be checked after the first tap");

        // Second tap → unchecked
        todoPage.markItemCompleted(0);
        Assert.assertFalse(
            todoPage.isItemChecked(0),
            "Item should be unchecked after the second tap (toggle)");
    }

    @Test
    @Story("Mark items completed")
    @Severity(SeverityLevel.MINOR)
    @Description("Mark all items in a list as completed and verify the total checked " +
                 "count equals the total item count.")
    public void markAllItemsCompleted() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String   title = "Fully Completed";
        String[] items = { "Step 1", "Step 2", "Step 3" };

        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        for (String item : items) {
            todoPage.addItem(item);
        }
        todoPage.clickSave();

        homePage.selectNote(title);

        for (int i = 0; i < items.length; i++) {
            todoPage.markItemCompleted(i);
        }

        Assert.assertEquals(
            todoPage.getCheckedItemCount(), (long) items.length,
            "All " + items.length + " items should be checked");
    }
}
