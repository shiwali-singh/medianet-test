# Notes App – Appium Automation Suite

End-to-end Appium test suite for the Notes Android app, written in **Java 11**,
following the **Page Object Model (POM)** pattern, with **TestNG** as the test
runner and **Allure** for HTML reporting.

---

## Project structure

```
notes-app-automation/
├── pom.xml                                 Maven build & dependency descriptor
├── testng.xml                              Test suite definition
├── src/test/
│   ├── resources/
│   │   ├── appium.properties               Device / app capabilities (edit this)
│   │   └── allure.properties               Allure results directory
│   └── java/com/medianet/
│       ├── utils/
│       │   ├── AppConfig.java              Reads appium.properties
│       │   ├── AppLocators.java            All element resource IDs in one place
│       │   ├── DriverManager.java          Thread-local AndroidDriver factory
│       │   ├── BaseTest.java               @BeforeMethod / @AfterMethod lifecycle
│       │   ├── TestListener.java           Allure screenshot on failure
│       │   └── ScreenshotUtil.java         Screenshot capture helper
│       ├── pages/
│       │   ├── BasePage.java               Shared wait/find helpers
│       │   ├── HomePage.java               Note list screen
│       │   ├── NotePage.java               Note editor/viewer
│       │   └── TodoPage.java               Todo editor/viewer
│       └── tests/
│           ├── TestCreateNote.java         Scenario 1
│           ├── TestCreateTodo.java         Scenario 2
│           ├── TestMarkTodoCompleted.java  Scenario 3
│           ├── TestEditDelete.java         Scenario 4
│           ├── TestValidations.java        Scenario 5
│           └── TestAdditional.java         Scenarios 6–9
```

---

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Java JDK | 11+ | `java -version` |
| Apache Maven | 3.8+ | `mvn -version` |
| Android SDK / ADB | latest | `adb version` |
| Appium Server | **2.x** | `appium --version` |
| UiAutomator2 driver | latest | `appium driver install uiautomator2` |
| Android Emulator or device | API 26+ | `adb devices` |

### Install Appium 2 and the UiAutomator2 driver

```bash
npm install -g appium
appium driver install uiautomator2
```

---

## Setup

### 1 — Place the APK

Copy the Notes app APK into the project root (or any convenient path):

```
notes-app-automation/
└── apps/
    └── notes-app.apk          ← put the APK here
```

### 2 — Edit `appium.properties`

Open `src/test/resources/appium.properties` and update every value to match your
environment:

```properties
appium.url=http://127.0.0.1:4723   # Appium 2.x (no /wd/hub suffix)
device.name=emulator-5554          # from: adb devices
platform.version=11.0
app.path=apps/notes-app.apk        # relative to project root, or absolute

# Find these with:  aapt dump badging apps/notes-app.apk | grep -E "package|launchable"
app.package=com.example.notesapp
app.activity=com.example.notesapp.MainActivity
```

### 3 — Update element IDs in `AppLocators.java`

All resource IDs live in a single class:
`src/test/java/com/medianet/utils/AppLocators.java`

Find the real IDs using one of these methods:

```bash
# Option A: Appium Inspector (GUI)
#   Start Appium server, open Appium Inspector, attach to the running app.

# Option B: UI Automator dump
adb shell uiautomator dump /sdcard/ui.xml
adb pull /sdcard/ui.xml
# Open ui.xml and search for resource-id attributes.

# Option C: Android Studio Layout Inspector
#   Run app on device/emulator, open Layout Inspector in Android Studio.
```

Replace every placeholder like `com.example.notesapp:id/fab` with the actual value.

---

## Running the tests

### Start Appium server (in a separate terminal)

```bash
appium
```

### Start emulator (if not already running)

```bash
emulator -avd <your_avd_name>
# or use Android Studio's Device Manager
```

### Execute the full suite

```bash
mvn test
```

### Execute a single test class

```bash
mvn test -Dtest=TestCreateNote
```

### Execute a single test method

```bash
mvn test -Dtest=TestCreateNote#createTextNoteAndVerifyContent
```

---

## Generating the Allure report

After `mvn test`, raw results are in `target/allure-results/`.

```bash
# Generate HTML report
mvn allure:report

# Open the report
# Windows
start target\site\allure-maven-plugin\index.html

# macOS / Linux
open target/site/allure-maven-plugin/index.html
```

Or serve it live:

```bash
mvn allure:serve
```

The report includes:
- Pass / fail / skip counts per scenario
- Allure `@Step` breadcrumbs for every page action
- A screenshot attachment on every failed test

---

## Test scenarios

| # | Class | Tests | Description |
|---|-------|-------|-------------|
| 1 | `TestCreateNote` | 2 | Create text note; verify title & content are persisted |
| 2 | `TestCreateTodo` | 2 | Create todo list with multiple items; verify each item |
| 3 | `TestMarkTodoCompleted` | 2 | Mark items checked; verify checked/unchecked states; toggle |
| 4 | `TestEditDelete` | 4 | Edit note, delete note, edit todo item, delete todo item |
| 5 | `TestValidations` | 4 | Empty fields, whitespace titles, no items, long input |
| 6–9 | `TestAdditional` | 4 | Multiple notes listed; mixed notes+todos; all items completed; count decreases after delete |

**Total: 18 tests**

---

## Design decisions

### Page Object Model
Each screen has its own class (`HomePage`, `NotePage`, `TodoPage`). Tests never
touch Selenium/Appium APIs directly — all interactions go through the page layer,
making test code read like plain English.

### Centralised locators
`AppLocators.java` is the single source of truth for every resource ID. Updating
an ID after an app change requires editing one line, not hunting across multiple
test files.

### Thread-local driver
`DriverManager` stores the `AndroidDriver` in a `ThreadLocal`, so the suite can
be parallelised later (increase `thread-count` in `testng.xml` and add more
emulators) without any code changes.

### Full test independence
`@BeforeMethod` creates a fresh driver (reinstalls the app unless `no.reset=true`).
`@AfterMethod` quits the driver. Tests share no state and can run in any order.

### Allure reporting
`@Epic`, `@Feature`, `@Story`, `@Severity`, `@Description` annotations on every
test class/method give the Allure HTML report rich navigation and filtering.
`@Step` on every page method provides a drill-down breadcrumb trail. Failed tests
automatically attach a screenshot.

---

## Troubleshooting

| Symptom | Fix |
|---------|-----|
| `SessionNotCreatedException` | Check `app.package` / `app.activity` in `appium.properties` |
| `NoSuchElementException` | Update resource IDs in `AppLocators.java` to match the real APK |
| `Connection refused` | Ensure `appium` is running and `appium.url` is correct |
| Tests fail immediately | Run `adb devices` — confirm the device/emulator is listed |
| Appium 1.x URL error | For Appium 1.x, set `appium.url=http://127.0.0.1:4723/wd/hub` |
