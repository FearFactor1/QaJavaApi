import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {

    @Test
    public void testLongRedirect() {
        String longUrl = "https://playground.learnqa.ru/api/long_redirect";

        while (true) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(longUrl)
                    .andReturn();

            if (response.getStatusCode() == 200) {
                System.out.println("Финальный URL с кодом 200: " + longUrl);
                break;
            } else {
                longUrl = response.getHeader("Location");
                System.out.println("Редирект на: " + longUrl);
            }
        }
    }
}