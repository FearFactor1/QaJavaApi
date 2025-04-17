package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class HomeWorkCookieTest {

    @Test
    public void testHomeWorkCookie() {
        Response responseGetCookie = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String cookieName  = "HomeWork";
        String cookieValue = responseGetCookie.getCookie(cookieName);

        assertThat("Cookie '" + cookieName + "' отсутствует", cookieValue, notNullValue());
        assertThat("Неверное значение cookie '" + cookieName + "'",
                cookieValue, equalTo("hw_value"));
    }
}