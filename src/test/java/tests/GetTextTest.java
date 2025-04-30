package tests;

import io.qameta.allure.Link;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetTextTest {

    @Link(name = "Ссылка на апи", url = "https://playground.learnqa.ru")
    @Test
    public void testGetText() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
}
