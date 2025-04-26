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

    public static Response getUserData(int userId, String header, String cookie) {
        return RestAssured.given()
                .baseUri("https://playground.learnqa.ru")
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("/ajax/api/user/" + userId);
    }
}