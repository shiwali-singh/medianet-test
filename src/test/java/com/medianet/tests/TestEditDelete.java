package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestEditDelete extends BaseTest {

    @Test
    @Description("Edit and delete existing notes and to-do list items")
    public void testEditAndDeleteNote() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String originalTitle = "Original Note";
        String originalContent = "Original content";
        String newTitle = "Edited Note";
        String newContent = "Edited content";

        // Create note
        homePage.clickAddNote();
        notePage.enterTitle(originalTitle);
        notePage.enterContent(originalContent);
        notePage.clickSave();

        // Edit note
        homePage.selectNote(originalTitle);
        notePage.clickEdit();
        notePage.enterTitle(newTitle);
        notePage.enterContent(newContent);
        notePage.clickSave();

        // Verify edited
        Assert.assertTrue(homePage.isNoteDisplayed(newTitle), "Edited note should be displayed");
        homePage.selectNote(newTitle);
        Assert.assertEquals(notePage.getTitle(), newTitle);
        Assert.assertEquals(notePage.getContent(), newContent);

        // Delete note
        notePage.clickDelete();
        Assert.assertFalse(homePage.isNoteDisplayed(newTitle), "Note should be deleted");
    }

    @Test
    @Description("Edit and delete to-do items")
    public void testEditAndDeleteTodoItem() {
        HomePage homePage = new HomePage(driver);
        TodoPage todoPage = new TodoPage(driver);

        String title = "Test Todo Edit";
        String item1 = "Item 1";
        String item2 = "Item 2";

        // Create todo
        homePage.clickAddTodo();
        todoPage.enterTitle(title);
        todoPage.addItem(item1);
        todoPage.addItem(item2);
        todoPage.clickSave();

        // Edit item
        homePage.selectNote(title);
        todoPage.editItem(0, "Edited Item 1");

        Assert.assertEquals(todoPage.getItemText(0), "Edited Item 1");

        // Delete item
        todoPage.deleteItem(1);
        Assert.assertNotEquals(todoPage.getItemText(1), item2, "Second item should be deleted");
    }
}