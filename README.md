# Notes App Automation

This project contains automated tests for the Notes App using Appium and Java with Page Object Model.

## Prerequisites

- Java 11 or higher
- Maven
- Android SDK
- Appium Server
- Android Emulator or Device
- APK file of the Notes App

## Setup

1. Clone the repository.
2. Place the APK file in the project root or update the path in `DriverManager.java`.
3. Update the package name, activity, and element IDs in the page classes to match the actual app.
4. Start Appium Server: `appium`
5. Start Android Emulator or connect device.
6. Run tests: `mvn clean test`

## Reporting

- Allure reports: `mvn allure:serve` after running tests.

## Test Scenarios

1. Create a new text note and verify content.
2. Create a new to-do list with multiple items.
3. Mark to-do items as completed.
4. Edit and delete notes and to-do items.
5. Validate input restrictions and error messages.

## Structure

- `src/test/java/com/medianet/pages/` - Page Object classes
- `src/test/java/com/medianet/tests/` - Test classes
- `src/test/java/com/medianet/utils/` - Utility classes