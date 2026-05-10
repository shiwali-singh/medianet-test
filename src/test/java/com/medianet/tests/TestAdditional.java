package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestAdditional extends BaseTest {

    @Test
    @Description("Additional scenario: Create multiple notes and verify they are listed")
    public void testMultipleNotes() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String[] titles = {"Note 1", "Note 2", "Note 3"};
        String content = "Content";

        for (String title : titles) {
            homePage.clickAddNote();
            notePage.enterTitle(title);
            notePage.enterContent(content);
            notePage.clickSave();
        }

        // Verify all notes are displayed
        for (String title : titles) {
            Assert.assertTrue(homePage.isNoteDisplayed(title), "Note " + title + " should be displayed");
        }
    }
}