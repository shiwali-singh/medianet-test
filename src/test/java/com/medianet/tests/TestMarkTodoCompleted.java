package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestMarkTodoCompleted extends BaseTest {

    @Test
    @Description("Mark to-do list items as completed and validate checked state")
    public void testMarkItemsCompleted() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Test Todo Complete";
        String[] items = {"Item 1", "Item 2"};

        // Create todo
        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        for (String item : items) {
            todoPage.addItem(item);
        }
        todoPage.clickSave();

        // Open and mark first item completed
        homePage.selectNote(title);
        todoPage.markItemCompleted(0);

        Assert.assertTrue(todoPage.isItemChecked(0), "First item should be checked");
        Assert.assertFalse(todoPage.isItemChecked(1), "Second item should not be checked");
    }
}