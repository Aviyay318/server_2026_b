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

import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Persist persist;

    private static boolean dataInit = false;
    private static String adminToken = null;
    private static String employeeToken = null;
    private static String employerToken = null;

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
        User admin = createUser("testAdmin", "Admin", "User", UserType.ADMIN);
        User employee = createUser("testEmployee", "Employee", "User", UserType.EMPLOYEE);
        User employer = createUser("testEmployer", "Employer", "User", UserType.EMPLOYER);

        persist.save(admin);
        persist.save(employee);
        persist.save(employer);

        // Get tokens for testing
        adminToken = loginAndGetToken("testAdmin", "/auth/login-admin");
        employeeToken = loginAndGetToken("testEmployee", "/auth/loginEmployee");
        employerToken = loginAndGetToken("testEmployer", "/auth/loginEmployer");
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

    // Tests for /admin/general-info
    @Test
    public void testGeneralInfoWithValidAdminToken() {
        given().log().all()
                .cookie("accessToken", adminToken)
                .when()
                .get("/admin/general-info")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGeneralInfoWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", "invalid_token_xyz")
                .when()
                .get("/admin/general-info")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testGeneralInfoWithoutToken() {
        given().log().all()
                .when()
                .get("/admin/general-info")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testGeneralInfoWithNonAdminToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/admin/general-info")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /admin/employers-list
    @Test
    public void testEmployersListWithValidAdminToken() {
        given().log().all()
                .cookie("accessToken", adminToken)
                .when()
                .get("/admin/employers-list")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEmployersListWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", "invalid_token_xyz")
                .when()
                .get("/admin/employers-list")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testEmployersListWithoutToken() {
        given().log().all()
                .when()
                .get("/admin/employers-list")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testEmployersListWithNonAdminToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/admin/employers-list")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /admin/employer-worker
    @Test
    public void testEmployerWorkerWithValidAdminToken() {
        given().log().all()
                .cookie("accessToken", adminToken)
                .queryParam("employerId", 1L)
                .when()
                .get("/admin/employer-worker")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEmployerWorkerWithInvalidEmployerId() {
        given().log().all()
                .cookie("accessToken", adminToken)
                .queryParam("employerId", 99999L)
                .when()
                .get("/admin/employer-worker")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testEmployerWorkerWithoutEmployerId() {
        given().log().all()
                .cookie("accessToken", adminToken)
                .when()
                .get("/admin/employer-worker")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testEmployerWorkerWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", "invalid_token_xyz")
                .queryParam("employerId", 1L)
                .when()
                .get("/admin/employer-worker")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testEmployerWorkerWithNonAdminToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("employerId", 1L)
                .when()
                .get("/admin/employer-worker")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /admin/realtime-info
    @Test
    public void testRealtimeInfoWithValidAdminToken() {
        given().log().all()
                .cookie("accessToken", adminToken)
                .when()
                .get("/admin/realtime-info")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testRealtimeInfoWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", "invalid_token_xyz")
                .when()
                .get("/admin/realtime-info")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testRealtimeInfoWithoutToken() {
        given().log().all()
                .when()
                .get("/admin/realtime-info")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testRealtimeInfoWithNonAdminToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/admin/realtime-info")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }
}

