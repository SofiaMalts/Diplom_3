package helpers.api.methods;

import helpers.api.response.UserResponse;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CleanUpRequests {
    private static final String baseUrl = "https://stellarburgers.nomoreparties.site";
    private static final String userEndpoint = baseUrl + "/api/auth/user";
    private static final String loginEndpoint = baseUrl + "/api/auth/login";

    private static final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));

    public static void deleteRequest(String uri, String token) {
        given()
                .config(config)
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .delete(uri);
    }

    public static Response postRequest(String uri, Object body) {
        return given()
                .config(config)
                .header("Content-type", "application/json")
                .body(body)
                .post(uri);
    }

    public static UserResponse responseToObject(Response response) {

        return response.body().as(UserResponse.class);
    }

    public static String getUserTokenFromResponse(Response response) {
        UserResponse responseAsObject = responseToObject(response);
        String tokenString = responseAsObject.getAccessToken().replace("Bearer ", "");
        return tokenString;
    }

    public static void deleteUser(Object body) {
        Response response = postRequest(loginEndpoint, body);
        //System.out.println(response.statusCode());
        if (response.statusCode() == 200) {
            String token = getUserTokenFromResponse(response);
            deleteRequest(userEndpoint, token);
        }
    }
}
