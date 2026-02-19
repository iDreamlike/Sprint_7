package util;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Requests {
    public Requests(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    public Response post(String path, String jsonBody) {
        return given().contentType(ContentType.JSON).body(jsonBody).post(path);
    }

    public void delete(String path) {
        given().delete(path);
    }
}
