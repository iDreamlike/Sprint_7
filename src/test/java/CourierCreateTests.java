import config.Configs;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Requests;

import static config.Configs.configRestAssured;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierCreateTests {
    private Requests requests;
    private Response response;

    @BeforeEach
    void setUp() {
        requests = new Requests("https://qa-scooter.praktikum-services.ru");
        configRestAssured();
    }

    @AfterEach
    void tearDown() {
        response = requests.post("/api/v1/courier/login", "{\"login\": \"RandomUser123\"," +
                " \"password\": \"1234\"}");
        response.then().assertThat().statusCode(200);
        requests.delete("/api/v1/courier/" + response.jsonPath().getInt("id"));
    }

    @Test
    @Description("Создание курьера")
    void courierCreateTest() {
        response = requests.post("/api/v1/courier",
                "{\"login\": \"RandomUser123\", \"password\": \"1234\", \"firstName\": \"Вася\"}");
        response.then().assertThat().statusCode(201);
        assertTrue(response.jsonPath().getBoolean("ok"), "Запрос не успешен");
    }

    @Test
    @Description("Попытка создания уже существующего курьера")
    void courierCreateAlreadyExistsTest() {
        requests.post("/api/v1/courier",
                "{\"login\": \"RandomUser123\", \"password\": \"1234\", \"firstName\": \"Вася\"}");
        response = requests.post("/api/v1/courier",
                "{\"login\": \"RandomUser123\", \"password\": \"1234\", \"firstName\": \"Вася\"}");
        response.then().assertThat().statusCode(409);
        assertEquals("Этот логин уже используется. Попробуйте другой.",
                response.jsonPath().getString("message"));
    }

    @Test
    @Description("Попытка создания курьера без обязательных полей login и password")
    void courierCreateWithoutLoginAndPasswordTest() {
        response = requests.post("/api/v1/courier",
                "{\"firstName\": \"Вася\"}");
        response.then().assertThat().statusCode(400);
    }

    @Test
    @Description("Попытка создания курьера без обязательного поля login")
    void courierCreateWithoutLoginTest() {
        response = requests.post("/api/v1/courier",
                "{\"password\": \"1234\", \"firstName\": \"Вася\"}");
        response.then().assertThat().statusCode(400);
    }

    @Test
    @Description("Попытка создания курьера без обязательного поля password")
    void courierCreateWithoutPasswordTest() {
        response = requests.post("/api/v1/courier",
                "{\"login\": \"RandomUser123\", \"firstName\": \"Вася\"}");
        response.then().assertThat().statusCode(400);
    }
}
