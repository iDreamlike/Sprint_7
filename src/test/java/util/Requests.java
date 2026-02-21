package util;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static constants.Urls.LOGIN_ENDPOINT;
import static io.restassured.RestAssured.given;

public class Requests {
    public Requests(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    @Step("POST-запрос на эндпоинт {0}")
    public Response post(String endpoint, Object jsonBody) {
        return given().contentType(ContentType.JSON).body(jsonBody).post(endpoint);
    }

    @Step("DELETE-запрос на эндпоинт {0}")
    public void delete(String endpoint, Object jsonBody) {
        Response loginResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonBody)
                        .post(LOGIN_ENDPOINT);
        String id = loginResponse.getBody().jsonPath().getString("id");
        if (loginResponse.statusCode() == 200 && id != null) {
            given().delete(endpoint + "/" + id);
        }
    }

    @Step("GET-запрос на эндпоинт {0}")
    public Response get(String endpoint) {
        return given().contentType(ContentType.JSON).get(endpoint);
    }
}
