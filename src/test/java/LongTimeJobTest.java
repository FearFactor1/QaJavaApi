import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongTimeJobTest {

    @Test
    public void testLongTimeJob() throws InterruptedException {
        JsonPath createResponse = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        int seconds = createResponse.getInt("seconds");
        String token = createResponse.getString("token");

        JsonPath statusBefore = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        Assertions.assertEquals(
                "Job is NOT ready",
                statusBefore.getString("status"),
                "Ожидался статус 'Job is NOT ready'"
        );

        Thread.sleep(seconds * 1000L);

        JsonPath statusAfter = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        Assertions.assertEquals(
                "Job is ready",
                statusAfter.getString("status"),
                "Ожидался статус 'Job is ready'"
        );

        Assertions.assertNotNull(
                statusAfter.getString("result"),
                "В ответе должно быть поле 'result'"
        );
    }
}