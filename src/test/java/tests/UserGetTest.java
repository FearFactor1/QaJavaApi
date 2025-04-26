package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private String token;

    @BeforeEach
    public void setUp() {
        Response authResponse = ApiCoreRequests.authorizeUser("vinkotov@example.com", "1234");
        token = authResponse.jsonPath().getString("token");
    }

    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/ajax/api/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    public void testAuthorizedUserGetDifferentUserData() {
        Response authResponse = ApiCoreRequests.authorizeUser("vinkotov@example.com", "1234");
        String header = this.getHeader(authResponse, "x-csrf-token");
        String cookie = this.getCookie(authResponse, "auth_sid");

        Response userResponse = ApiCoreRequests.getUserData(3, header, cookie);

        System.out.println("Response (Diff User Data): " + userResponse.asString());

        Assertions.assertJsonHasNotField(userResponse, "firstName");
        Assertions.assertJsonHasNotField(userResponse, "lastName");
        Assertions.assertJsonHasNotField(userResponse, "email");
    }
}