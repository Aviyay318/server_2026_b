# Test Suite Documentation

## Overview
This document describes the comprehensive test suite for the Server 2026 B project. The tests are organized by controller and cover both successful and error scenarios for all endpoints.

## Test Organization

### Project Structure
```
src/test/java/server_2026_b/server/controllers/
├── AuthControllerTest.java         - Authentication endpoints
├── AdminControllerTest.java        - Admin dashboard endpoints
├── CrudEmployeeControllerTest.java - Employee CRUD operations
├── ShiftControllerTest.java        - Shift management endpoints
└── WorkDayControllerTest.java      - Work day tracking endpoints
```

## Controllers and Endpoints

### 1. AuthController (`/auth`)
**Tests:** `AuthControllerTest.java`

#### Endpoints:
- `POST /auth/loginEmployee` - Employee login
- `POST /auth/loginEmployer` - Employer login
- `POST /auth/login-admin` - Admin login
- `POST /auth/logout` - User logout
- `POST /auth/refresh` - Refresh access token

#### Test Cases:

**Login Tests:**
- ✅ `testLoginEmployee` - Valid employee login
- ✅ `testLoginEmployer` - Valid employer login
- ✅ `testLoginAdmin` - Valid admin login
- ❌ `testLoginWithWrongPassword` - Wrong password
- ❌ `testLoginWithWrongUserType` - Trying to login with wrong endpoint
- ✅ `testLoginEmployeeWithCorrectCredentials` - Employee with correct credentials
- ❌ `testLoginEmployeeWithEmptyUsername` - Empty username
- ❌ `testLoginEmployeeWithEmptyPassword` - Empty password
- ❌ `testLoginEmployeeWithNullUsername` - Null username
- ❌ `testLoginEmployeeWithNullPassword` - Null password
- ❌ `testLoginEmployeeWithEmptyBody` - Empty JSON body
- ✅ `testLoginEmployerWithCorrectCredentials` - Employer with correct credentials
- ❌ `testLoginEmployerWithWrongPassword` - Employer with wrong password
- ✅ `testLoginAdminWithCorrectCredentials` - Admin with correct credentials
- ❌ `testLoginAdminWithWrongPassword` - Admin with wrong password
- ❌ `testLoginEmployeeAsEmployer` - Employee trying to login as employer
- ❌ `testLoginEmployerAsEmployee` - Employer trying to login as employee

**Logout Tests:**
- ✅ `testLogoutUser` - Valid logout
- ❌ `testLogoutWithInvalidToken` - Invalid token
- ❌ `testLogoutWithoutToken` - Missing token
- ✅ `testLogoutMultipleTimes` - Logging out multiple times with same token

**Refresh Token Tests:**
- ✅ `testRefreshToken` - Valid token refresh
- ❌ `testRefreshTokenWithInvalidToken` - Invalid refresh token
- ❌ `testRefreshTokenWithoutToken` - Missing token
- ❌ `testRefreshTokenWithAccessToken` - Using access token instead of refresh token
- ✅ `testRefreshTokenMultipleTimes` - Refreshing multiple times
- ❌ `testRefreshTokenAfterLogout` - Attempting refresh after logout
- ❌ `testRefreshTokenWithEmptyString` - Empty token string

---

### 2. AdminController (`/admin`)
**Tests:** `AdminControllerTest.java`

#### Endpoints:
- `GET /admin/general-info` - Get all users (Admin only)
- `GET /admin/employers-list` - Get all employers (Admin only)
- `GET /admin/employer-worker` - Get employees by employer ID (Admin only)
- `GET /admin/realtime-info` - Get active users (Admin only)

#### Test Cases:

**General Info Tests:**
- ✅ `testGeneralInfoWithValidAdminToken` - Valid admin access
- ❌ `testGeneralInfoWithInvalidToken` - Invalid token
- ❌ `testGeneralInfoWithoutToken` - Missing token
- ❌ `testGeneralInfoWithNonAdminToken` - Non-admin user (employee)

**Employers List Tests:**
- ✅ `testEmployersListWithValidAdminToken` - Valid admin access
- ❌ `testEmployersListWithInvalidToken` - Invalid token
- ❌ `testEmployersListWithoutToken` - Missing token
- ❌ `testEmployersListWithNonAdminToken` - Non-admin user

**Employer Worker Tests:**
- ✅ `testEmployerWorkerWithValidAdminToken` - Valid employer ID
- ❌ `testEmployerWorkerWithInvalidEmployerId` - Non-existent employer
- ❌ `testEmployerWorkerWithoutEmployerId` - Missing required parameter
- ❌ `testEmployerWorkerWithInvalidToken` - Invalid token
- ❌ `testEmployerWorkerWithNonAdminToken` - Non-admin user

**Realtime Info Tests:**
- ✅ `testRealtimeInfoWithValidAdminToken` - Valid admin access
- ❌ `testRealtimeInfoWithInvalidToken` - Invalid token
- ❌ `testRealtimeInfoWithoutToken` - Missing token
- ❌ `testRealtimeInfoWithNonAdminToken` - Non-admin user

---

### 3. CrudEmployeeController (`/crud-employee`)
**Tests:** `CrudEmployeeControllerTest.java`

#### Endpoints:
- `GET /crud-employee/get-all-active` - Get all active employees
- `POST /crud-employee/create-employee` - Create new employee
- `DELETE /crud-employee/delete-employee` - Delete employee

#### Test Cases:

**Get All Active Tests:**
- ✅ `testGetAllActiveWithValidEmployerToken` - Employer access
- ✅ `testGetAllActiveWithValidEmployeeToken` - Employee access
- ❌ `testGetAllActiveWithInvalidToken` - Invalid token
- ❌ `testGetAllActiveWithoutToken` - Missing token

**Create Employee Tests:**
- ✅ `testCreateEmployeeWithValidData` - Valid employee creation
- ❌ `testCreateEmployeeWithMissingUsername` - Missing username
- ❌ `testCreateEmployeeWithMissingEmail` - Missing email
- ❌ `testCreateEmployeeWithInvalidToken` - Invalid token
- ❌ `testCreateEmployeeWithoutToken` - Missing token
- ❌ `testCreateEmployeeWithEmptyBody` - Empty JSON body
- ❌ `testCreateEmployeeWithEmployeeToken` - Employee trying to create employee

**Delete Employee Tests:**
- ✅ `testDeleteEmployeeWithValidEmployerId` - Valid employee ID
- ❌ `testDeleteEmployeeWithInvalidEmployeeId` - Non-existent employee
- ❌ `testDeleteEmployeeWithoutEmployeeId` - Missing required parameter
- ❌ `testDeleteEmployeeWithInvalidToken` - Invalid token
- ❌ `testDeleteEmployeeWithoutToken` - Missing token
- ❌ `testDeleteEmployeeWithEmployeeToken` - Employee trying to delete

---

### 4. ShiftController (`/shifts`)
**Tests:** `ShiftControllerTest.java`

#### Endpoints:
- `GET /shifts/get-all` - Get shifts for date range

#### Test Cases:

**Get All Shifts Tests:**
- ✅ `testGetAllShiftsWithValidDates` - Valid date range (Employee)
- ✅ `testGetAllShiftsWithValidDatesEmployer` - Valid date range (Employer)
- ❌ `testGetAllShiftsWithoutFromDate` - Missing from date
- ❌ `testGetAllShiftsWithoutToDate` - Missing to date
- ❌ `testGetAllShiftsWithoutBothDates` - Missing both dates
- ❌ `testGetAllShiftsWithInvalidDateFormat` - Invalid date format
- ❌ `testGetAllShiftsWithReversedDates` - To date before from date
- ❌ `testGetAllShiftsWithInvalidToken` - Invalid token
- ❌ `testGetAllShiftsWithoutToken` - Missing token
- ✅ `testGetAllShiftsWithSameDates` - Same from and to date
- ✅ `testGetAllShiftsWithLargeDateRange` - Large date range (1 year)

---

### 5. WorkDayController (`/work`)
**Tests:** `WorkDayControllerTest.java`

#### Endpoints:
- `POST /work/enter` - Clock in
- `POST /work/exit` - Clock out
- `GET /work/status` - Get last work status
- `GET /work/list` - Get all work records
- `GET /work/total-hours` - Get total work hours in month
- `GET /work/sites` - Get all work sites (No auth required)
- `GET /work/get-all-absence-reasons` - Get absence reasons
- `POST /work/report-absence` - Report absence

#### Test Cases:

**Enter Tests:**
- ✅ `testEnterWithValidData` - Valid clock in
- ❌ `testEnterWithoutSiteId` - Missing site ID
- ❌ `testEnterWithoutLocation` - Missing location
- ❌ `testEnterWithoutStartTime` - Missing start time
- ❌ `testEnterWithInvalidToken` - Invalid token
- ❌ `testEnterWithoutToken` - Missing token
- ❌ `testEnterWithEmptyBody` - Empty JSON body

**Exit Tests:**
- ✅ `testExitWithValidData` - Valid clock out
- ❌ `testExitWithoutSiteId` - Missing site ID
- ❌ `testExitWithInvalidToken` - Invalid token
- ❌ `testExitWithoutToken` - Missing token

**Status Tests:**
- ✅ `testStatusWithValidToken` - Valid status check
- ❌ `testStatusWithInvalidToken` - Invalid token
- ❌ `testStatusWithoutToken` - Missing token

**List Tests:**
- ✅ `testListWithValidToken` - Valid list access
- ❌ `testListWithInvalidToken` - Invalid token
- ❌ `testListWithoutToken` - Missing token

**Total Hours Tests:**
- ✅ `testTotalHoursWithValidMonth` - Valid month (May)
- ✅ `testTotalHoursWithValidMonthJanuary` - January
- ✅ `testTotalHoursWithValidMonthDecember` - December
- ❌ `testTotalHoursWithoutMonth` - Missing month parameter
- ❌ `testTotalHoursWithInvalidMonthZero` - Invalid month (0)
- ❌ `testTotalHoursWithInvalidMonthThirteen` - Invalid month (13)
- ❌ `testTotalHoursWithInvalidToken` - Invalid token
- ❌ `testTotalHoursWithoutToken` - Missing token

**Sites Tests:**
- ✅ `testGetAllSites` - Get all sites (No auth)
- ✅ `testGetAllSitesWithToken` - Get all sites with token

**Absence Reasons Tests:**
- ✅ `testGetAllAbsenceReasonsWithValidToken` - Valid access
- ❌ `testGetAllAbsenceReasonsWithInvalidToken` - Invalid token
- ❌ `testGetAllAbsenceReasonsWithoutToken` - Missing token

**Report Absence Tests:**
- ✅ `testReportAbsenceWithValidReason` - Valid absence reason
- ❌ `testReportAbsenceWithoutReason` - Missing reason
- ❌ `testReportAbsenceWithInvalidReason` - Invalid reason
- ❌ `testReportAbsenceWithInvalidToken` - Invalid token
- ❌ `testReportAbsenceWithoutToken` - Missing token
- ❌ `testReportAbsenceWithEmptyBody` - Empty JSON body

---

## Running the Tests

### Run All Tests
```bash
mvn test
```

### Run Tests by Class
```bash
# Auth tests
mvn test -Dtest=AuthControllerTest

# Admin tests
mvn test -Dtest=AdminControllerTest

# CRUD Employee tests
mvn test -Dtest=CrudEmployeeControllerTest

# Shift tests
mvn test -Dtest=ShiftControllerTest

# Work Day tests
mvn test -Dtest=WorkDayControllerTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=AuthControllerTest#testLoginEmployee
```

### Run with Coverage
```bash
mvn test jacoco:report
# Report will be in: target/site/jacoco/index.html
```

---

## Test Data Setup

Each test class includes a static data initialization method that:
1. Creates test users with different user types (ADMIN, EMPLOYEE, EMPLOYER)
2. Obtains authentication tokens for each user type
3. Sets up test data once per test class (not per test method)

### Default Test Users:
- **Admin**: username `testAdmin`, password `1234`
- **Employee**: username `testWorker1`/`shiftEmployee`/`workEmployee`, password `1234`
- **Employer**: username `testBoss1`/`testEmployer`/`shiftEmployer`, password `1234`

---

## Error Codes Reference

The tests verify against these error codes defined in `Errors.java`:

| Code | Constant | Description |
|------|----------|-------------|
| 2000 | ERROR_INVALID_TOKEN | Invalid authentication token |
| 3000 | ERROR_EMPTY_FIELD | Required field is empty |
| 3001 | ERROR_WRONG_CREDENTIALS | Incorrect username or password |
| 3002 | ERROR_UPDATE_TOKEN_FAILED | Token refresh failed |
| 4000 | ERROR_EMPLOYEE_ALREADY_WORKING | Employee already clocked in |
| 4001 | ERROR_EMPLOYEE_NOT_WORKING | Employee not clocked in |
| 4002 | ERROR_INVALID_ABSENCE_REASON | Invalid absence reason |
| 5001 | ERROR_FETCHING_SITES | Error fetching work sites |
| 5002 | ERROR_SITE_NOT_FOUND | Work site not found |
| 5003 | ERROR_TWO_LOCATIONS_AT_ONCE | Multiple clock-in attempts |
| 5004 | ERROR_NO_LOCATION_OR_SITE_PROVIDED | Missing location or site ID |
| 6001 | ERROR_EMPLOYEE_ALREADY_EXISTS | Employee username already exists |
| 6002 | ERROR_EMPLOYEE_NOT_FOUND | Employee not found |
| 6003 | ERROR_NOT_YOUR_EMPLOYEE | Permission denied - not your employee |
| 6004 | ERROR_CANNOT_DELETE_EMPLOYER | Cannot delete employer user |

---

## Test Patterns Used

### 1. Login and Extract Token
```java
String token = given()
    .contentType("application/json")
    .body(loginJson)
    .when()
    .post("/auth/loginEmployee")
    .then()
    .statusCode(200)
    .extract().cookie("accessToken");
```

### 2. Verify Success Response
```java
given().log().all()
    .cookie("accessToken", token)
    .when()
    .post("/some/endpoint")
    .then()
    .log().all()
    .statusCode(200)
    .body("success", equalTo(true));
```

### 3. Verify Error Response
```java
given().log().all()
    .when()
    .post("/auth/loginEmployee")
    .then()
    .log().all()
    .statusCode(200)
    .body("success", equalTo(false))
    .body("errorCode", equalTo(3001));
```

---

## Best Practices

1. **Always use `.log().all()`** - Helps with debugging failed tests
2. **Test both success and failure cases** - Each endpoint should have positive and negative tests
3. **Verify authentication** - Test with valid token, invalid token, and no token
4. **Check required parameters** - Test with and without required query/path parameters
5. **Use meaningful test names** - Method names clearly describe what is being tested

---

## Maintenance

When adding new endpoints:
1. Create appropriate test cases in the corresponding controller test class
2. Include tests for both valid and invalid inputs
3. Test authentication requirements
4. Update this documentation
5. Run full test suite to ensure no regressions

---

## Contact & Support

For issues or questions about the test suite, refer to the corresponding controller implementation and the test file comments.

