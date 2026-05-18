package server_2026_b.server.controllers;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import server_2026_b.server.entities.RefreshToken;
import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.entities.relations.EmploymentRelation;
import server_2026_b.server.service.Persist;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.UserType;

import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@ActiveProfiles("test")
// This tells Spring to boot up your entire application on a random, open port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    // This grabs the random port Spring just opened
    @LocalServerPort
    private int port;

    @Autowired
    private Persist persist;
    Faker faker = new Faker(new Locale("en", "US"));

    private static boolean dataInit = false;

    // This runs BEFORE every single test to configure REST Assured
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Create the mock data in the temporary H2 database
        if (!dataInit){
            createStaticMockData();
        }
    }

    private void createStaticMockData() {
        persist.deleteAll(EmploymentRelation.class);
        persist.deleteAll(RefreshToken.class);

        persist.deleteAll(WorkingSite.class);
        persist.deleteAll(User.class);


        Map<String, UserType> names = Map.of(
                "testAdmin" , UserType.ADMIN
                ,"testWorker1" , UserType.EMPLOYEE
                ,"testWorker2" , UserType.EMPLOYEE
                ,"testWorker3" , UserType.EMPLOYEE
                ,"testBoss1" , UserType.EMPLOYER
                ,"testBoss2" , UserType.EMPLOYER);

        for (Map.Entry<String, UserType> name : names.entrySet()){
            User newUser = new User();
            newUser.setFirstName(faker.name().firstName());
            newUser.setLastName(faker.name().lastName());
            newUser.setPhone(faker.phoneNumber().cellPhone());
            newUser.setUserType(name.getValue());
            newUser.setUsername(name.getKey());
            newUser.setPassword(generateHashPassword(newUser.getUsername(), "1234"));
            newUser.setEmail(name.getValue().toString() + newUser.getFirstName() + "@test.com");
            persist.save(newUser);
        }

        for (int i = 1; i <= 3; i++) {
            WorkingSite workingSite = new WorkingSite();
            workingSite.setName("TestWorkingSite" + i);
            workingSite.setAddress(faker.address().city());
            workingSite.setLongitude(Double.parseDouble(faker.address().longitude()));
            workingSite.setLatitude(Double.parseDouble(faker.address().latitude()));

            persist.save(workingSite);
        }
        this.dataInit = true;
    }

    private String generateHashPassword(String username, String password) {
        return GenerateHash.hashMd5(username, password);
    }

    @Test
    public void testLoginEmployee() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        given().log().all()  // Prints the Request
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()  // Prints the Response
                .statusCode(200)
                .cookie("accessToken", notNullValue());
    }

    @Test
    public void testLoginEmployer() {
        String jsonBody = """
            {
                "username" : "testBoss1",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployer")
                .then()
                .log().all()
                .statusCode(200)
                .cookie("accessToken", notNullValue());
    }

    @Test
    public void testLoginAdmin() {
        String jsonBody = """
            {
                "username" : "testAdmin",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/login-admin")
                .then()
                .log().all()
                .statusCode(200)
                .cookie("accessToken", notNullValue());
    }

    @Test
    public void testLoginWithWrongPassword() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : "wrong_password_123"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false)) // Assert the JSON payload says it failed
                .body("errorCode", equalTo(3001));
    }

    @Test
    public void testLoginWithWrongUserType() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : "wrong_password_123"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/login-admin")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false)) // Assert the JSON payload says it failed
                .body("errorCode", equalTo(3001));
    }

    @Test
    public void testLoginEmployeeWithCorrectCredentials() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void testLoginEmployeeWithEmptyUsername() {
        String jsonBody = """
            {
                "username" : "",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLoginEmployeeWithEmptyPassword() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : ""
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLoginEmployeeWithNullUsername() {
        String jsonBody = """
            {
                "username" : null,
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLoginEmployeeWithNullPassword() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : null
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLoginEmployeeWithEmptyBody() {
        given().log().all()
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLoginEmployerWithCorrectCredentials() {
        String jsonBody = """
            {
                "username" : "testBoss1",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployer")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void testLoginEmployerWithWrongPassword() {
        String jsonBody = """
            {
                "username" : "testBoss1",
                "password" : "wrong_password"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployer")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testLoginAdminWithCorrectCredentials() {
        String jsonBody = """
            {
                "username" : "testAdmin",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/login-admin")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void testLoginAdminWithWrongPassword() {
        String jsonBody = """
            {
                "username" : "testAdmin",
                "password" : "wrong_password"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/login-admin")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testLoginEmployeeAsEmployer() {
        String jsonBody = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployer")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testLoginEmployerAsEmployee() {
        String jsonBody = """
            {
                "username" : "testBoss1",
                "password" : "1234"
            }
            """;

        given().log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testLogoutUser() {
        // 1. Perform a REAL login to get a valid token
        String loginJson = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        // We run the login request and EXTRACT the cookie it gives us
        String realValidToken = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .statusCode(200)
                .extract().cookie("accessToken"); // <--- Magic happens here!

        // 2. Now we test the logout endpoint using the real token
        given().log().all()
                .cookie("accessToken", realValidToken)
                .when()
                .post("/auth/logout")
                .then()
                .log().all()
                .statusCode(200)
                // Verify the server cleared the cookie by setting it to an empty string
                .body("success", equalTo(true))
                .cookie("accessToken", "");
    }

    @Test
    public void testLogoutWithInvalidToken() {
        given().log().all()
                .cookie("accessToken", "invalid_token_xyz")
                .when()
                .post("/auth/logout")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLogoutWithoutToken() {
        given().log().all()
                .when()
                .post("/auth/logout")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testLogoutMultipleTimes() {
        String loginJson = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        String token = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .statusCode(200)
                .extract().cookie("accessToken");

        // First logout
        given().log().all()
                .cookie("accessToken", token)
                .when()
                .post("/auth/logout")
                .then()
                .log().all()
                .statusCode(200);

        // Second logout with same token
        given().log().all()
                .cookie("accessToken", token)
                .when()
                .post("/auth/logout")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testRefreshToken() {
        String loginJson = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        // We run the login request and EXTRACT both access and refresh tokens
        var response = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .statusCode(200)
                .extract();

        String refreshToken = response.cookie("refreshToken");

        // 2. Now we test the refresh endpoint using the refresh token
        given().log().all()
                .cookie("refreshToken", refreshToken)
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    public void testRefreshTokenWithInvalidToken() {
        given().log().all()
                .cookie("refreshToken", "invalid_refresh_token")
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testRefreshTokenWithoutToken() {
        given().log().all()
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }

    @Test
    public void testRefreshTokenWithAccessToken() {
        String loginJson = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        String accessToken = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .statusCode(200)
                .extract().cookie("accessToken");

        // Try to refresh using accessToken instead of refreshToken
        given().log().all()
                .cookie("refreshToken", accessToken)
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testRefreshTokenMultipleTimes() {
        String loginJson = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        var response = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .statusCode(200)
                .extract();

        String refreshToken = response.cookie("refreshToken");

        // Refresh first time
        given()
                .cookie("refreshToken", refreshToken)
                .when()
                .post("/auth/refresh")
                .then()
                .statusCode(200);

        // Refresh second time with same token
        given().log().all()
                .cookie("refreshToken", refreshToken)
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testRefreshTokenAfterLogout() {
        String loginJson = """
            {
                "username" : "testWorker1",
                "password" : "1234"
            }
            """;

        var response = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/auth/loginEmployee")
                .then()
                .statusCode(200)
                .extract();

        String accessToken = response.cookie("accessToken");
        String refreshToken = response.cookie("refreshToken");

        // Logout first
        given()
                .cookie("accessToken", accessToken)
                .when()
                .post("/auth/logout")
                .then()
                .statusCode(200);

        // Try to refresh with the refresh token after logout
        given().log().all()
                .cookie("refreshToken", refreshToken)
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testRefreshTokenWithEmptyString() {
        given().log().all()
                .cookie("refreshToken", "")
                .when()
                .post("/auth/refresh")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(false));
    }
}