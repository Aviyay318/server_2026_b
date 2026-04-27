# 🚀 Backend Server - Class Project 2026

Welcome to the backend repository. 
Please read these notes carefully before starting your first task to ensure our environment stays consistent.

## ⚙️ Server Configuration

**Local Port**: The server runs on http://localhost:8085.

**Base Directory**: Tomcat is configured to use `${user.home}/ashcollege`. This folder will automatically be created in your User directory (Windows/Mac) to store temporary work data and logs.

**File Uploads**: Maximum file size is set to 50MB. If you are testing upload endpoints, ensure your test files do not exceed this limit.

## 📝 Logging System (logback-spring.xml)

There is a use of centralized logging, here is the concept:

**Console (STDOUT)**: Real-time logs appear in your IntelliJ terminal with timestamps and log levels.

**File Logging**: Logs are automatically saved to `${user.home}/logs/ashcollege.log`.

- for example (on Windows): `"C:\Users\ShaiGivaty\logs\ashcollege.log"`

**Rolling Policy**: The system keeps a history for 30 days. Every day, the log file is archived (e.g., ashcollege-2026-04-24.log) so the main file doesn't become too large to open.

**Quiet Hibernate**: We have set org.hibernate to ERROR level to keep the console clean from unnecessary query noise unless something actually breaks.

## 🛡️ Error Handling (GlobalExceptionHandler)

We use a centralized `@ControllerAdvice` to handle errors.

**Current Logic**: If a user tries to access a URL that does not exist (NoHandlerFoundException), the server will not crash or show a 404 page; it will automatically redirect the user to the /home route.

**Team Rule**: Do not write custom error-to-page logic in your controllers. Add new exception handlers to the GlobalExceptionHandler class instead.

## 🛠️ Development Workflow

To keep the project organized, follow this golden rules:

**The Hibernate Rule**

We are using XML Mapping (objects.hbm.xml) alongside Java objects.

***CRITICAL***: If you add, rename, or delete a field in a Model/Entity (Java class), you MUST update the corresponding entry in `src/main/resources/objects.hbm.xml`. If you forget this, the database will not recognize your changes.

## 🌿 Git Strategy

**Main Branch**: Never push directly to master!

**Features**: Create a branch for every task: `feature/your-task-name`.

**Merge**: Open a Pull Request and wait for a review before merging into the main codebase.