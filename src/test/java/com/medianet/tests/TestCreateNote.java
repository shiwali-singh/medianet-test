package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCreateNote extends BaseTest {

    @Test
    @Description("Create a new text note and verify content")
    public void testCreateTextNote() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String title = "Test Note";
        String content = "This is a test note content.";

        homePage.clickAddNote();
        notePage.enterTitle(title);
        notePage.enterContent(content);
        notePage.clickSave();

        // Verify back on home page and note is displayed
        Assert.assertTrue(homePage.isNoteDisplayed(title), "Note should be displayed in the list");

        // Open the note and verify content
        homePage.selectNote(title);
        Assert.assertEquals(notePage.getTitle(), title, "Title should match");
        Assert.assertEquals(notePage.getContent(), content, "Content should match");
    }
}