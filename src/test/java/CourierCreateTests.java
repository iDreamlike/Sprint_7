import dto.CourierLogin;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Requests;

import static constants.ErrorMessages.*;
import static config.RestAssuredConfig.configRestAssured;
import static constants.Urls.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierCreateTests {
    private Requests requests;
    private Response response;
    private CourierLogin courierJsonBody = new CourierLogin();

    @BeforeEach
    void setUp() {
        requests = new Requests(APP_URL);
        configRestAssured();
        courierJsonBody = courierJsonBody.toBuilder()
                .login("RandomUser123")
                .password("1234")
                .firstName("Вася")
                .build();
    }

    @AfterEach
    void tearDown() {
        requests.delete(COURIER_ENDPOINT, courierJsonBody.toBuilder().firstName(null).build());
    }

    @Test
    @Description("Создание курьера")
    void courierCreateTest() {
        response = requests.post(COURIER_ENDPOINT, courierJsonBody);
        response.then().assertThat().statusCode(201);
        assertTrue(response.jsonPath().getBoolean("ok"), "Запрос не успешен");
    }

    @Test
    @Description("Попытка создания уже существующего курьера")
    void courierCreateAlreadyExistsTest() {
        requests.post(COURIER_ENDPOINT, courierJsonBody);
        response = requests.post(COURIER_ENDPOINT, courierJsonBody);
        response.then().assertThat().statusCode(409);
        assertEquals(ERROR_COURIER_CREATE_DUPLICATE, response.jsonPath().getString("message"));
    }

    @Test
    @Description("Попытка создания курьера без обязательных полей login и password")
    void courierCreateWithoutLoginAndPasswordTest() {
        response = requests.post(COURIER_ENDPOINT, courierJsonBody.toBuilder().login(null).password(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_CREATE_NULL_CREDENTIALS, response.jsonPath().getString("message"));
    }

    @Test
    @Description("Попытка создания курьера без обязательного поля login")
    void courierCreateWithoutLoginTest() {
        response = requests.post(COURIER_ENDPOINT, courierJsonBody.toBuilder().login(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_CREATE_NULL_CREDENTIALS, response.jsonPath().getString("message"));
    }

    @Test
    @Description("Попытка создания курьера без обязательного поля password")
    void courierCreateWithoutPasswordTest() {
        response = requests.post(COURIER_ENDPOINT, courierJsonBody.toBuilder().password(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_CREATE_NULL_CREDENTIALS, response.jsonPath().getString("message"));
    }
}
