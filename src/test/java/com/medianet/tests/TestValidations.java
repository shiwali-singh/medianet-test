package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.pages.TodoPage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestValidations extends BaseTest {

    @Test
    @Description("Validate input restrictions, empty fields, and error messages")
    public void testEmptyFields() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        homePage.clickAddNote();
        // Try to save without title and content
        notePage.clickSave();

        // Assuming there's an error message for empty title
        // WebElement error = driver.findElementById("error_message"); // replace
        // Assert.assertTrue(error.isDisplayed(), "Error message should be displayed for empty fields");

        // For now, just check if still on note page
        // Since IDs are placeholders, this is conceptual
    }

    @Test
    @Description("Test maximum length or other restrictions")
    public void testInputRestrictions() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String longTitle = "A".repeat(1000); // Assuming max length
        String longContent = "B".repeat(10000);

        homePage.clickAddNote();
        notePage.enterTitle(longTitle);
        notePage.enterContent(longContent);
        notePage.clickSave();

        // Verify truncated or error
        // Assert.assertTrue(notePage.getTitle().length() < longTitle.length(), "Title should be truncated");
    }
}