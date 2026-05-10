package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCreateTodo extends BaseTest {

    @Test
    @Description("Create a new to-do list with multiple items")
    public void testCreateTodoList() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Test Todo";
        String[] items = {"Item 1", "Item 2", "Item 3"};

        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        for (String item : items) {
            todoPage.addItem(item);
        }
        todoPage.clickSave();

        Assert.assertTrue(homePage.isNoteDisplayed(title), "Todo should be displayed in the list");

        // Verify items
        homePage.selectNote(title);
        for (int i = 0; i < items.length; i++) {
            Assert.assertEquals(todoPage.getItemText(i), items[i], "Item text should match");
        }
    }
}