import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierCreateTests {
    @BeforeEach
    void setUp() {
        RestAssured.filters((request, response, ctx) -> {

            System.out.println("\n" + "⏺".repeat(40));
            System.out.println("📋 Время: " + java.time.LocalTime.now());
            String method = request.getMethod();
            String uri = request.getURI();
            String path = uri.replace(RestAssured.baseURI, "");
            System.out.println("\uD83D\uDD37 " + method + " " + path);
            if (request.getBody() != null) {
                System.out.println("📦 Тело: " + request.getBody().toString());
            }
            Response res = ctx.next(request, response);
            System.out.println("📊 Ответ: " + res.statusCode() + " " + res.statusLine());
            System.out.println("⏺".repeat(40) + "\n");
            return res;
        });

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new RequestLoggingFilter(LogDetail.METHOD));
        RestAssured.filters(new RequestLoggingFilter(LogDetail.URI));
        RestAssured.filters(new RequestLoggingFilter(LogDetail.BODY));
        RestAssured.filters(new ResponseLoggingFilter(LogDetail.BODY));

    }

    @AfterEach
    void tearDown() {
        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"RandomUser123\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.jsonPath().getInt("id");
            given()
                    .delete("/api/v1/courier/" + courierId)
                    .then()
                    .statusCode(200);
        } else {
            System.out.println("Логин не удался. Код: " + loginResponse.statusCode());
        }
    }

    @Test
    @Description("Создание курьера")
    void courierCreateTest() {
        Response createResponse =
            given()
                    .header("Content-type", "application/json")
                    .body("{\"login\": \"RandomUser123\", \"password\": \"1234\", \"firstName\": \"Вася\"}")
            .when()
                    .post("/api/v1/courier");
        createResponse.then().assertThat().statusCode(201);
        boolean ok = createResponse.jsonPath().getBoolean("ok");
        assertTrue(ok, "Запрос не успешен");
    }

    @Test
    @Description("Попытка создания уже существующего курьера")
    void courierCreateAlreadyExistsTest() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"RandomUser123\", \"password\": \"1234\", \"firstName\": \"Вася\"}")
                .when()
                .post("/api/v1/courier");
        Response createResponse =
            given()
                    .header("Content-type", "application/json")
                    .body("{\"login\": \"RandomUser123\", \"password\": \"1234\", \"firstName\": \"Вася\"}")
                    .when()
                    .post("/api/v1/courier");
        createResponse.then().assertThat().statusCode(409);
        String errorText = createResponse.jsonPath().getString("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", errorText);
    }

    @Test
    @Description("Попытка создания курьера без обязательных полей login и password")
    void courierCreateWithoutLoginAndPasswordTest() {
        given()
                .header("Content-type", "application/json")
                .body("{\"firstName\": \"Вася\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(400);
    }

    @Test
    @Description("Попытка создания курьера без обязательного поля login")
    void courierCreateWithoutLoginTest() {
        given()
                .header("Content-type", "application/json")
                .body("{\"password\": \"1234\", \"firstName\": \"Вася\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(400);
    }

    @Test
    @Description("Попытка создания курьера без обязательного поля password")
    void courierCreateWithoutPasswordTest() {
        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"RandomUser123\", \"firstName\": \"Вася\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(400);
    }

}
