package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

public class UserAgentTest {

    @ParameterizedTest()
    @ValueSource(strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
    })
    public void verifyUserAgent(String userAgent) {
        String expectedDevice;
        String expectedBrowser;
        String expectedPlatform;

        switch (userAgent) {
            case "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30":
                expectedDevice = "Android";
                expectedBrowser = "Safari";
                expectedPlatform = "mobile";
                break;

            case "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1":
                expectedDevice = "iOS";
                expectedBrowser = "Chrome";
                expectedPlatform = "mobile";
                break;

            case "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)":
                expectedDevice = "Unknown";
                expectedBrowser = "Unknown";
                expectedPlatform = "web";
                break;

            case "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0":
                expectedDevice = "Windows";
                expectedBrowser = "Edge";
                expectedPlatform = "web";
                break;

            case "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1":
                expectedDevice = "iOS";
                expectedBrowser = "Safari";
                expectedPlatform = "mobile";
                break;

            default:
                expectedDevice = "Unknown";
                expectedBrowser = "Unknown";
                expectedPlatform = "Unknown";
        }

        Response response = RestAssured.given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check");

        String device = response.jsonPath().getString("device");
        String browser = response.jsonPath().getString("browser");
        String platform = response.jsonPath().getString("platform");

        List<String> incorrectParams = new ArrayList<>();
        if (!device.equals(expectedDevice)) {
            incorrectParams.add("device");
        }
        if (!browser.equals(expectedBrowser)) {
            incorrectParams.add("browser");
        }
        if (!platform.equals(expectedPlatform)) {
            incorrectParams.add("platform");
        }

        if (!incorrectParams.isEmpty()) {
            System.out.printf("User Agent: %s, Incorrect parameters: %s%n", userAgent, String.join(", ",
                    incorrectParams));
        } else {
            System.out.printf("User Agent: %s is correct.%n", userAgent);
        }
    }
}