package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests api = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid",  this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.asserJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    public void testEditWithoutAuthorization() {
        Response responseEditUser = api.editUserDataWithoutAuth("1", "Changed Name");
        Assertions.assertResponseStatusCode(responseEditUser, 400);
    }

    @Test
    public void testEditWithAnotherUserAuth() {
        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        Map<String, String> userData2 = DataGenerator.getRegistrationData();

        System.out.println("User1 Data: " + userData1);
        System.out.println("User2 Data: " + userData2);

        String authCookieUser1 = api.registerAndLoginUser(userData1);

        String authCookieUser2 = api.registerAndLoginUser(userData2);

        Response responseEditUser = api.editUserDataWithAuth(userData1.get("id"), "Changed Name",
                authCookieUser2);
        Assertions.assertResponseStatusCode(responseEditUser, 404);
    }

    @Test
    public void testEditEmailWithoutAtSymbol() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        ApiCoreRequests api = new ApiCoreRequests();
        String authCookie = api.registerAndLoginUser(userData);

        Response responseEditUser = api.editUserEmail("1", "invalidemail.com", authCookie);
        Assertions.assertResponseStatusCode(responseEditUser, 400);
    }

    @Test
    public void testEditFirstNameToShortValue() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String authCookie = api.registerAndLoginUser(userData);

        Response responseEditUser = api.editUserFirstName("1", "A", authCookie);
        Assertions.assertResponseStatusCode(responseEditUser, 400);
    }
}