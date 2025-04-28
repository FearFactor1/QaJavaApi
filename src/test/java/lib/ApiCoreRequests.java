package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Создание пользователя с параметрами: {params}")
    public Response createUser(Map<String, String> params) {
        return RestAssured
                .given()
                .body(params)
                .post("https://playground.learnqa.ru/api/user/");
    }

    public static Response authorizeUser(String email, String password) {
        return RestAssured.given()
                .baseUri("https://playground.learnqa.ru")
                .contentType("application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}")
                .post("/api/user/login");
    }

    @Step("Получить данные пользователя по ID")
    public static Response getUserData(int userId, String header, String cookie) {
        return RestAssured.given()
                .baseUri("https://playground.learnqa.ru")
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("/ajax/api/user/" + userId);
    }

    public String registerUser(Map<String, String> userData) {
        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println("Request Body: " + userData);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        Assertions.assertResponseStatusCode(response, 200);
        return response.jsonPath().getString("id");
    }

    public String loginUser(Map<String, String> userData) {
        Response response = RestAssured
                .given()
                .body(Map.of("email", userData.get("email"), "password", userData.get("password")))
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Assertions.assertResponseStatusCode(response, 200); // Должен вернуть 200 OK
        return getCookie(response, "auth_sid"); // Возвращаем значение куки
    }

    public Response editUserDataWithoutAuth(String userId, String newName) {
        return RestAssured
                .given()
                .body(Map.of("firstName", newName))
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    public Response editUserDataWithAuth(String userId, String newName, String authCookie) {
        return RestAssured
                .given()
                .cookie("auth_sid", authCookie)
                .body(Map.of("firstName", newName))
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    public Response editUserEmail(String userId, String newEmail, String authCookie) {
        return RestAssured
                .given()
                .cookie("auth_sid", authCookie)
                .body(Map.of("email", newEmail))
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    public Response editUserFirstName(String userId, String newFirstName, String authCookie) {
        return RestAssured
                .given()
                .cookie("auth_sid", authCookie)
                .body(Map.of("firstName", newFirstName))
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    public String registerAndLoginUser(Map<String, String> userData) {
        RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/");

        Response response = RestAssured
                .given()
                .body(Map.of("email", userData.get("email"), "password", userData.get("password")))
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        return getCookie(response, "auth_sid");
    }

    @Step("Удалить пользователя по ID")
    public Response deleteUser(int userId, String cookie) {
        return RestAssured
                .given()
                .baseUri("https://playground.learnqa.ru/api")
                .cookie("auth_sid", cookie)
                .when()
                .delete("/user/" + userId)
                .andReturn();
    }

    public String getCookie(Response response, String cookieName) {
        return response.getCookie(cookieName);
    }
}