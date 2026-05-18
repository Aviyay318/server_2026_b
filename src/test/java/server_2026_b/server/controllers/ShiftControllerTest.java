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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShiftControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Persist persist;

    private static boolean dataInit = false;
    private static String employeeToken = null;
    private static String employerToken = null;
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
        // Create test users
        User employee = createUser("shiftEmployee_shiftTest", "Employee", "User", UserType.EMPLOYEE);
        User employer = createUser("shiftEmployer_shiftTest", "Employer", "User", UserType.EMPLOYER);

        persist.save(employee);
        persist.save(employer);

        // Get tokens for testing
        employeeToken = loginAndGetToken("shiftEmployee_shiftTest", "/auth/loginEmployee");
        employerToken = loginAndGetToken("shiftEmployer_shiftTest", "/auth/loginEmployer");
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

    // Helper method to get date strings
    private String getDateString(int daysOffset) {
        LocalDate date = LocalDate.now().plusDays(daysOffset);
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    // Tests for /shifts/get-all
    @Test
    public void testGetAllShiftsWithValidDates() {
        String fromDate = getDateString(0);
        String toDate = getDateString(7);

        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllShiftsWithValidDatesEmployer() {
        String fromDate = getDateString(0);
        String toDate = getDateString(7);

        given().log().all()
                .cookie("accessToken", employerToken)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllShiftsWithoutFromDate() {
        String toDate = getDateString(7);

        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testGetAllShiftsWithoutToDate() {
        String fromDate = getDateString(0);

        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("fromDate", fromDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testGetAllShiftsWithoutBothDates() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testGetAllShiftsWithInvalidDateFormat() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("fromDate", "invalid-date")
                .queryParam("toDate", "also-invalid")
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllShiftsWithReversedDates() {
        String fromDate = getDateString(7);
        String toDate = getDateString(0);

        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllShiftsWithInvalidToken() {
        String fromDate = getDateString(0);
        String toDate = getDateString(7);

        given().log().all()
                .cookie("accessToken", invalidToken)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testGetAllShiftsWithoutToken() {
        String fromDate = getDateString(0);
        String toDate = getDateString(7);

        given().log().all()
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testGetAllShiftsWithSameDates() {
        String date = getDateString(0);

        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("fromDate", date)
                .queryParam("toDate", date)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllShiftsWithLargeDateRange() {
        String fromDate = getDateString(-365);
        String toDate = getDateString(365);

        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .when()
                .get("/shifts/get-all")
                .then()
                .log().all()
                .statusCode(200);
    }
}

