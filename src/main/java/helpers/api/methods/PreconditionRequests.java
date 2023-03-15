package helpers.api.methods;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class PreconditionRequests {
    private static final String baseUrl = "https://stellarburgers.nomoreparties.site";
    private static final String createUserEndpoint = baseUrl + "/api/auth/register";

    public static void createNewUser(Object body) {
        given()
                .config(config)
                .header("Content-type", "application/json")
                .body(body)
                .post(createUserEndpoint);
    }
}
