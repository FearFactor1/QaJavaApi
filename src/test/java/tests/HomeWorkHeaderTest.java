package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWorkHeaderTest {

    @Test
    public void testHomeWorkHeader() {
        Response responseGetHeader = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        assertTrue(
                responseGetHeader.headers().hasHeaderWithName("x-secret-homework-header"),
                "Пропущен заголовок - x-secret-homework-header"
        );

        String actualValue = responseGetHeader.header("x-secret-homework-header");
        assertEquals("Some secret value", actualValue, "Не верное значение заголовка");
    }
}