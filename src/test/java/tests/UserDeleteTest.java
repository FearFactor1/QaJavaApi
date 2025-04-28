package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testDeleteUserAsUnprivilegedUser() {
        Map<String, String> authData = Map.of(
                "email", "vinkotov@example.com",
                "password", "1234"
        );

        String cookie = apiCoreRequests.loginUser(authData);
        int userIdToDelete = 2;

        Response deleteResponse = apiCoreRequests.deleteUser(userIdToDelete, cookie);

        Assertions.assertEquals(400, deleteResponse.getStatusCode());
    }

    @Test
    public void testDeleteUserSuccessfully() {
        Map<String, String> newUserData = DataGenerator.getRegistrationData();

        String userId = apiCoreRequests.registerUser(newUserData);

        String cookie = apiCoreRequests.loginUser(newUserData);
        Assertions.assertNotNull(cookie, "Куки не были получены, авторизация не удалась.");

        Response deleteResponse = apiCoreRequests.deleteUser(Integer.parseInt(userId), cookie);
        Assertions.assertEquals(200, deleteResponse.getStatusCode(),
                "Ошибка при удалении пользователя.");

        Response getUserResponse = apiCoreRequests.getUserData(Integer.parseInt(userId), "", cookie);
        Assertions.assertTrue(getUserResponse.getBody().asString().contains("User not found"),
                "Пользователь не был удален, он все еще существует.");
    }

    @Test
    public void testDeleteUserAsAnotherUser() {
        Map<String, String> authDataUser1 = Map.of(
                "email", "vinkotov@example.com",
                "password", "1234"
        );

        String cookieUser1 = apiCoreRequests.loginUser(authDataUser1);
        int userIdToDelete = 3;

        Response deleteResponse = apiCoreRequests.deleteUser(userIdToDelete, cookieUser1);

        Assertions.assertEquals(400, deleteResponse.getStatusCode());
    }
}