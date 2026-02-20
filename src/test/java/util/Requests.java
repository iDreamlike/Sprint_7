package util;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static config.UrlConfig.LOGIN_ENDPOINT;
import static io.restassured.RestAssured.given;

public class Requests {
    public Requests(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    public Response post(String endpoint, Object jsonBody) {
        return given().contentType(ContentType.JSON).body(jsonBody).post(endpoint);
    }

    public void delete(String endpoint, Object jsonBody) {
        Response loginResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonBody)
                        .post(LOGIN_ENDPOINT);
        if (loginResponse.statusCode() == 200) {
            given().delete(endpoint + "/" + loginResponse.getBody().jsonPath().getString("id"));
        }
    }
}
