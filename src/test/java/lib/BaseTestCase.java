package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {

    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name " + name);
        return headers.getValue(name);
    }

    protected String getCookie(Response Response, String name) {
        Map<String, String> cookies = Response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn't have header with name " + name);
        return cookies.get(name);
    }

    protected int getIntFromJason(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }

    protected int getLengthString(String name) {
        return name.length();
    }
}
