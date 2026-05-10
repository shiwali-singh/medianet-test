package com.medianet.utils;

/**
 * Central repository of every element ID and text label used across page objects.
 *
 * IDs were extracted from the APK using:
 *   aapt dump xmltree test_app.apk res/layout/<name>.xml
 *   aapt dump resources test_app.apk | grep "spec resource"
 *
 * Package: com.example.myapp
 * Min SDK: 21 | Target SDK: 33 | Compiled SDK: 34
 */
public final class AppLocators {

    private static final String PKG = AppConfig.getAppPackage(); // com.example.myapp

    private AppLocators() {}

    // ── Home / Notes-list screen  (fragment_notes_list.xml + item_note.xml) ──

    /** FloatingActionButton — opens the "Create New Note" type-picker dialog. */
    public static final String HOME_FAB            = PKG + ":id/addNoteFab";

    /** RecyclerView that holds all note cards. */
    public static final String HOME_NOTES_LIST     = PKG + ":id/notesRecyclerView";

    // Per-card TextViews (item_note.xml)
    /** "Note" or "Todo List" label on each card. */
    public static final String HOME_NOTE_TYPE      = PKG + ":id/noteTypeTextView";
    /** The note title shown on each card. */
    public static final String HOME_NOTE_NAME      = PKG + ":id/noteNameTextView";
    /** First line of content shown on each card. */
    public static final String HOME_NOTE_PREVIEW   = PKG + ":id/noteContentPreviewTextView";
    /** Date stamp shown on each card. */
    public static final String HOME_NOTE_DATE      = PKG + ":id/noteDateTextView";

    // ── Type-picker AlertDialog (appears when FAB is tapped) ──────────────────
    /** Dialog title text — "Create New Note". */
    public static final String DIALOG_TITLE_TEXT   = "Create New Note";
    /** First list item in the dialog — navigates to NoteFragment. */
    public static final String DIALOG_OPT_TEXT     = "New Text Note";
    /** Second list item in the dialog — navigates to TodoNoteFragment. */
    public static final String DIALOG_OPT_TODO     = "New Todo List";

    // ── Note editor screen  (fragment_note.xml) ───────────────────────────────
    /** Note title field — EditText, always editable. */
    public static final String NOTE_TITLE          = PKG + ":id/noteNameEditText";
    /** Note body field — EditText, always editable. */
    public static final String NOTE_CONTENT        = PKG + ":id/noteContentEditText";
    /** "Save Note" button at bottom. Saves and navigates back to the list. */
    public static final String NOTE_SAVE           = PKG + ":id/saveNoteButton";

    // ── Todo editor screen  (fragment_todo_note.xml) ─────────────────────────
    /** Todo list title — EditText next to the save button. */
    public static final String TODO_TITLE          = PKG + ":id/todoNoteNameEditText";
    /**
     * "Save List" button next to the title.
     * Saves the list (title + all items) and navigates back to the home screen.
     */
    public static final String TODO_SAVE           = PKG + ":id/saveTodoNoteButton";
    /** RecyclerView of checklist items. */
    public static final String TODO_ITEMS_LIST     = PKG + ":id/todoItemsRecyclerView";
    /** Text input at the bottom for typing a new item. */
    public static final String TODO_ITEM_INPUT     = PKG + ":id/newTodoItemEditText";
    /** "Add" button that appends the typed item to the RecyclerView. */
    public static final String TODO_ADD_ITEM       = PKG + ":id/addTodoItemButton";

    // Per-item views  (item_todo.xml)
    /** CheckBox on each todo row. */
    public static final String TODO_ITEM_CHECKBOX  = PKG + ":id/todoCheckBox";
    /** TextView showing the item text on each todo row. */
    public static final String TODO_ITEM_TEXT      = PKG + ":id/todoTextView";

    // ── Validation / error dialog  (custom_dialog.xml) ───────────────────────
    /**
     * "Ok" button in the custom dialog.
     * Shown when the user tries to save with empty required fields.
     */
    public static final String DIALOG_OK_BUTTON    = PKG + ":id/dialog_button";

    // ── Overflow / options menu  (notes_list_menu.xml) ────────────────────────
    public static final String MENU_SHOW_TEXT      = "Show Text Notes Only";
    public static final String MENU_SHOW_TODO      = "Show Todo Lists Only";
    public static final String MENU_SHOW_ALL       = "Show All Notes";
    public static final String MENU_SORT_TITLE     = "Sort by Title";
}
