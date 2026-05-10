package com.medianet.tests;

import com.medianet.pages.HomePage;
import com.medianet.pages.NotePage;
import com.medianet.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 1 — Create a new text note and verify content is saved and displayed.
 *
 * Flow
 * ─────
 * 1. Tap FAB  →  "Create New Note" dialog appears
 * 2. Choose "New Text Note"  →  fragment_note opens
 * 3. Fill noteNameEditText (title) + noteContentEditText (body)
 * 4. Tap saveNoteButton  →  navigates back to notes list
 * 5. Assert the noteNameTextView card is visible
 * 6. Re-open the note and assert field values match
 */
@Epic("Notes App")
@Feature("Text Note Management")
public class TestCreateNote extends BaseTest {

    @Test
    @Story("Create note")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create a text note with title + body, save, then re-open to " +
                 "confirm the exact title and content are persisted.")
    public void createTextNoteAndVerifyContent() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String title   = "Meeting Notes";
        String content = "Discuss Q3 roadmap and assign action items.";

        // Create
        homePage.clickAddNote();
        notePage.enterTitle(title);
        notePage.enterContent(content);
        notePage.clickSave();

        // Verify card on home list
        Assert.assertTrue(
            homePage.isNoteDisplayed(title),
            "Note '" + title + "' should be visible on the home list after saving");

        // Re-open and verify stored values
        homePage.selectNote(title);

        Assert.assertEquals(
            notePage.getTitle(), title,
            "Stored title must match the entered title");

        Assert.assertEquals(
            notePage.getContent(), content,
            "Stored content must match the entered content");
    }

    @Test
    @Story("Create note")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create a note supplying only a title (empty body) and verify it " +
                 "appears in the list — the body field is optional.")
    public void createNoteWithTitleOnly() {
        HomePage homePage = new HomePage(driver);
        NotePage notePage = new NotePage(driver);

        String title = "Quick Reminder";

        homePage.clickAddNote();
        notePage.enterTitle(title);
        // intentionally leave content empty
        notePage.clickSave();

        Assert.assertTrue(
            homePage.isNoteDisplayed(title),
            "A note with a title but no body should still appear in the list");
    }
}
