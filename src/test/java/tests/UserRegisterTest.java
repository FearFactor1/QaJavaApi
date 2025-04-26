package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegisterTest extends BaseTestCase {

    private ApiCoreRequests apiCoreRequests;

    @BeforeEach
    public void setUp() {
        apiCoreRequests = new ApiCoreRequests();
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Step("Создание пользователя с некорректным email")
    public void testCreateUserWithInvalidEmail() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov.com");
        userData.put("password", "1234");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");
        userData.put("username", "learnqa");

        Response response = apiCoreRequests.createUser(userData);

        System.out.println("Response Body: " + response.asString());

        response.then().assertThat().statusCode(400);

        String actualResponse = response.asString();
        String expectedMessage = "Invalid email format";

        assertTrue(actualResponse.contains(expectedMessage),
                "Ожидаемое сообщение не найдено в ответе: " + actualResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "firstName", "lastName", "password"})
    @Step("Создание пользователя без указания одного из полей {0}")
    public void testCreateUserWithoutField(String fieldToOmit) {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");
        userData.put("username", "learnqa");

        userData.remove(fieldToOmit);

        Response response = apiCoreRequests.createUser(userData);

        String actualResponse = response.asString();
        System.out.println("Response Body: " + actualResponse);

        response.then().assertThat().statusCode(400);

        String expectedMessage = "The following required params are missed:";

        assertTrue(actualResponse.contains(expectedMessage),
                "Ожидаемое сообщение не найдено в ответе: " + actualResponse);
    }

    @Test
    @Step("Создание пользователя с очень коротким именем")
    public void testCreateUserWithShortName() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        userData.put("firstName", "l");
        userData.put("lastName", "learnqa");
        userData.put("username", "learnqa");

        Response response = apiCoreRequests.createUser(userData);

        String actualResponse = response.asString();
        System.out.println("Response Body: " + actualResponse);

        response.then().assertThat().statusCode(400);

        String expectedMessage = "The value of 'firstName' field is too short";

        assertTrue(actualResponse.contains(expectedMessage),
                "Ожидаемое сообщение не найдено в ответе: " + actualResponse);
    }

    @Test
    @Step("Создание пользователя с очень длинным именем")
    public void testCreateUserWithLongName () {
        String longName = new String(new char[251]).replace('\0', 'A');
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        userData.put("firstName", longName);
        userData.put("lastName", "learnqa");
        userData.put("username", "learnqa");

        Response response = apiCoreRequests.createUser(userData);

        String actualResponse = response.asString();
        System.out.println("Response Body: " + actualResponse);

        response.then().assertThat().statusCode(400);

        assertTrue(actualResponse.contains("The value of 'firstName' field is too long"),
                "Ожидаемое сообщение не найдено в ответе: " + actualResponse);
    }
}