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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrudEmployeeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Persist persist;

    private static boolean dataInit = false;
    private static String employerToken = null;
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
        // Create test users
        User employer = createUser("testEmployer", "Employer", "User", UserType.EMPLOYER);
        User employee = createUser("testEmployee", "Employee", "User", UserType.EMPLOYEE);
        User admin = createUser("testAdmin", "Admin", "User", UserType.ADMIN);

        persist.save(employer);
        persist.save(employee);
        persist.save(admin);

        // Get tokens for testing
        employerToken = loginAndGetToken("testEmployer", "/auth/loginEmployer");
        employeeToken = loginAndGetToken("testEmployee", "/auth/loginEmployee");
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

    // Tests for /crud-employee/get-all-active
    @Test
    public void testGetAllActiveWithValidEmployerToken() {
        given().log().all()
                .cookie("accessToken", employerToken)
                .when()
                .get("/crud-employee/get-all-active")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllActiveWithValidEmployeeToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .when()
                .get("/crud-employee/get-all-active")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testGetAllActiveWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", invalidToken)
                .when()
                .get("/crud-employee/get-all-active")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testGetAllActiveWithoutToken() {
        given().log().all()
                .when()
                .get("/crud-employee/get-all-active")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /crud-employee/create-employee
    @Test
    public void testCreateEmployeeWithValidData() {
        String createEmployeeJson = """
            {
                "username": "newEmployee",
                "firstName": "New",
                "lastName": "Employee",
                "password": "securePass123",
                "phone": "0501234567",
                "email": "newemployee@test.com"
            }
            """;

        given().log().all()
                .cookie("accessToken", employerToken)
                .contentType("application/json")
                .body(createEmployeeJson)
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testCreateEmployeeWithMissingUsername() {
        String createEmployeeJson = """
            {
                "firstName": "New",
                "lastName": "Employee",
                "password": "securePass123",
                "phone": "0501234567",
                "email": "newemployee@test.com"
            }
            """;

        given().log().all()
                .cookie("accessToken", employerToken)
                .contentType("application/json")
                .body(createEmployeeJson)
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testCreateEmployeeWithMissingEmail() {
        String createEmployeeJson = """
            {
                "username": "newEmployee",
                "firstName": "New",
                "lastName": "Employee",
                "password": "securePass123",
                "phone": "0501234567"
            }
            """;

        given().log().all()
                .cookie("accessToken", employerToken)
                .contentType("application/json")
                .body(createEmployeeJson)
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testCreateEmployeeWithInvalidToken() {
        String createEmployeeJson = """
            {
                "username": "newEmployee",
                "firstName": "New",
                "lastName": "Employee",
                "password": "securePass123",
                "phone": "0501234567",
                "email": "newemployee@test.com"
            }
            """;

        given().log().all()
                .cookie("accessToken", invalidToken)
                .contentType("application/json")
                .body(createEmployeeJson)
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testCreateEmployeeWithoutToken() {
        String createEmployeeJson = """
            {
                "username": "newEmployee",
                "firstName": "New",
                "lastName": "Employee",
                "password": "securePass123",
                "phone": "0501234567",
                "email": "newemployee@test.com"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(createEmployeeJson)
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testCreateEmployeeWithEmptyBody() {
        given().log().all()
                .cookie("accessToken", employerToken)
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testCreateEmployeeWithEmployeeToken() {
        String createEmployeeJson = """
            {
                "username": "newEmployee",
                "firstName": "New",
                "lastName": "Employee",
                "password": "securePass123",
                "phone": "0501234567",
                "email": "newemployee@test.com"
            }
            """;

        given().log().all()
                .cookie("accessToken", employeeToken)
                .contentType("application/json")
                .body(createEmployeeJson)
                .when()
                .post("/crud-employee/create-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    // Tests for /crud-employee/delete-employee
    @Test
    public void testDeleteEmployeeWithValidEmployerId() {
        given().log().all()
                .cookie("accessToken", employerToken)
                .queryParam("employeeId", 1L)
                .when()
                .delete("/crud-employee/delete-employee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testDeleteEmployeeWithInvalidEmployeeId() {
        given().log().all()
                .cookie("accessToken", employerToken)
                .queryParam("employeeId", 99999L)
                .when()
                .delete("/crud-employee/delete-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testDeleteEmployeeWithoutEmployeeId() {
        given().log().all()
                .cookie("accessToken", employerToken)
                .when()
                .delete("/crud-employee/delete-employee")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void testDeleteEmployeeWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", invalidToken)
                .queryParam("employeeId", 1L)
                .when()
                .delete("/crud-employee/delete-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testDeleteEmployeeWithoutToken() {
        given().log().all()
                .queryParam("employeeId", 1L)
                .when()
                .delete("/crud-employee/delete-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testDeleteEmployeeWithEmployeeToken() {
        given().log().all()
                .cookie("accessToken", employeeToken)
                .queryParam("employeeId", 1L)
                .when()
                .delete("/crud-employee/delete-employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }
}

