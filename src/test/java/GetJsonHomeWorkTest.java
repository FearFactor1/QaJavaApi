import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class GetJsonHomeWorkTest {

    @Test
    public void testGetJsonHomeWork() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();

        JsonPath jsonPath = response.jsonPath();
        String secondMessage = jsonPath.getString("messages[1]");
        System.out.println("Второе сообщение: " + secondMessage);
    }
}