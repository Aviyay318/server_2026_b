package server_2026_b.server.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.UserType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkDayControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Persist persist;

    private static boolean dataInit = false;
    private static String employeeToken = null;
    private static String invalidToken = "invalid_token_xyz";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        if (!dataInit) {
            createStaticMockData();
            dataInit = true;
        }
    }

    private void createStaticMockData() {
        persist.deleteAll(User.class);
        persist.deleteAll(WorkingSite.class);
        // Create test employee
        User employee = createUser("workEmployee_workTest", "Work", "Employee", UserType.EMPLOYEE);
        persist.save(employee);

        // Get token for testing
        employeeToken = loginAndGetToken("workEmployee_workTest", "/auth/loginEmployee");
    }

    private User createUser(String username, String firstName, String lastName, UserType userType) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserType(userType);
        user.setPassword(GenerateHash.hashMd5(username, "1234"));
        user.setEmail(userType.toString() + firstName + "@test.com");
        user.setPhone("0501234567");
        return user;
    }

    private String loginAndGetToken(String username, String endpoint) {
        String loginJson = String.format("""
            {
                "username" : "%s",
                "password" : "1234"
            }
            """, username);

        return given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post(endpoint)
                .then()
                .statusCode(200)
                .extract().cookie("accessToken");
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    // Tests for /work/enter
    @Test
    public void testEnterWithValidData() {
        String enterJson = String.format("""
            {
                "siteId": 1,
                "location": "Site A",
                "startTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(enterJson)
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEnterWithoutSiteId() {
        String enterJson = String.format("""
            {
                "location": "Site A",
                "startTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(enterJson)
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEnterWithoutLocation() {
        String enterJson = String.format("""
            {
                "siteId": 1,
                "startTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(enterJson)
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEnterWithoutStartTime() {
        String enterJson = """
            {
                "siteId": 1,
                "location": "Site A"
            }
            """;

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(enterJson)
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEnterWithInvalidToken() {
        String enterJson = String.format("""
            {
                "siteId": 1,
                "location": "Site A",
                "startTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", invalidToken)
                .contentType("application/json")
                .body(enterJson)
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testEnterWithoutToken() {
        String enterJson = String.format("""
            {
                "siteId": 1,
                "location": "Site A",
                "startTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .contentType("application/json")
                .body(enterJson)
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testEnterWithEmptyBody() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/work/enter")
                .then()
                .log().all()
                .statusCode(200);
    }

    // Tests for /work/exit
    @Test
    public void testExitWithValidData() {
        String exitJson = String.format("""
            {
                "siteId": 1,
                "location": "Site A",
                "endTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(exitJson)
                .when()
                .post("/work/exit")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testExitWithoutSiteId() {
        String exitJson = String.format("""
            {
                "location": "Site A",
                "endTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(exitJson)
                .when()
                .post("/work/exit")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testExitWithInvalidToken() {
        String exitJson = String.format("""
            {
                "siteId": 1,
                "location": "Site A",
                "endTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .cookie("accessToken", invalidToken)
                .contentType("application/json")
                .body(exitJson)
                .when()
                .post("/work/exit")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testExitWithoutToken() {
        String exitJson = String.format("""
            {
                "siteId": 1,
                "location": "Site A",
                "endTime": "%s"
            }
            """, getCurrentDateTime());

        given().log().all()
                .contentType("application/json")
                .body(exitJson)
                .when()
                .post("/work/exit")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /work/status
    @Test
    public void testStatusWithValidToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/work/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testStatusWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", invalidToken)
                .when()
                .get("/work/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testStatusWithoutToken() {
        given().log().all()
                .when()
                .get("/work/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /work/list
    @Test
    public void testListWithValidToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/work/list")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testListWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", invalidToken)
                .when()
                .get("/work/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testListWithoutToken() {
        given().log().all()
                .when()
                .get("/work/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /work/total-hours
    @Test
    public void testTotalHoursWithValidMonth() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("month", 5)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testTotalHoursWithValidMonthJanuary() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("month", 1)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testTotalHoursWithValidMonthDecember() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("month", 12)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testTotalHoursWithoutMonth() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testTotalHoursWithInvalidMonthZero() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("month", 0)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testTotalHoursWithInvalidMonthThirteen() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("month", 13)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testTotalHoursWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", invalidToken)
                .queryParam("month", 5)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testTotalHoursWithoutToken() {
        given().log().all()
                .queryParam("month", 5)
                .when()
                .get("/work/total-hours")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /work/sites (No authentication required)
    @Test
    public void testGetAllSites() {
        given().log().all()
                .when()
                .get("/work/sites")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllSitesWithToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/work/sites")
                .then()
                .log().all()
                .statusCode(200);
    }

    // Tests for /work/get-all-absence-reasons
    @Test
    public void testGetAllAbsenceReasonsWithValidToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/work/get-all-absence-reasons")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllAbsenceReasonsWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", invalidToken)
                .when()
                .get("/work/get-all-absence-reasons")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testGetAllAbsenceReasonsWithoutToken() {
        given().log().all()
                .when()
                .get("/work/get-all-absence-reasons")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /work/report-absence
    @Test
    public void testReportAbsenceWithValidReason() {
        String reportJson = """
            {
                "reason": "SICKNESS"
            }
            """;

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(reportJson)
                .when()
                .post("/work/report-absence")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testReportAbsenceWithoutReason() {
        String reportJson = """
            {
            }
            """;

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(reportJson)
                .when()
                .post("/work/report-absence")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testReportAbsenceWithInvalidReason() {
        String reportJson = """
            {
                "reason": "INVALID_REASON"
            }
            """;

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(reportJson)
                .when()
                .post("/work/report-absence")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testReportAbsenceWithInvalidToken() {
        String reportJson = """
            {
                "reason": "SICK"
            }
            """;

        given().log().all()
                .cookie("accessToken", invalidToken)
                .contentType("application/json")
                .body(reportJson)
                .when()
                .post("/work/report-absence")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testReportAbsenceWithoutToken() {
        String reportJson = """
            {
                "reason": "SICK"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(reportJson)
                .when()
                .post("/work/report-absence")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testReportAbsenceWithEmptyBody() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/work/report-absence")
                .then()
                .log().all()
                .statusCode(200);
    }
}

