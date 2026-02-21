import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.Requests;

import static constants.ErrorMessages.*;
import static config.RestAssuredConfig.configRestAssured;
import static constants.Urls.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CourierLoginTests {
    private Requests requests;
    private Response response;
    private dto.CourierLogin courierJsonBody = new dto.CourierLogin();

    @BeforeEach
    void setUp() {
        requests = new Requests(APP_URL);
        configRestAssured();
        courierJsonBody = courierJsonBody.toBuilder()
                .login("RandomUser123")
                .password("1234")
                .build();
        response = requests.post(COURIER_ENDPOINT, courierJsonBody);
    }

    @AfterEach
    void tearDown() {
        requests.delete(COURIER_ENDPOINT, courierJsonBody);
    }

    @Test
    @Description("Логин курьера")
    void courierLoginTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody);
        response.then().assertThat().statusCode(200);
        assertNotNull(response.getBody().jsonPath().getString("id"), "Запрос не успешен");
    }

    @Test
    @Description("Логин курьера без поля Login")
    void courierLoginWithoutLoginTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().login(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_LOGIN_NULL_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Disabled("Ждем фикса бага с пустым паролем")
    @Description("Логин курьера без поля Password")
    void courierLoginWithoutPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().password(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_LOGIN_NULL_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Disabled("Ждем фикса бага с пустым паролем")
    @Description("Логин курьера без полей Login и Password")
    void courierLoginWithoutLoginAndPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().login(null).password(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_LOGIN_NULL_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Description("Логин курьера с несуществующим Login")
    void courierLoginWithNotExistsLoginTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().login("Нет такого логина").build());
        response.then().assertThat().statusCode(404);
        assertEquals(ERROR_COURIER_LOGIN_WRONG_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Description("Логин курьера с несуществующим Password")
    void courierLoginWithNotExistsPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().password("Нет такого пароля").build());
        response.then().assertThat().statusCode(404);
        assertEquals(ERROR_COURIER_LOGIN_WRONG_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Description("Логин курьера с несуществующими Login и Password")
    void courierLoginWithNotExistsLoginAndPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder()
                .login("Нет такого логина")
                .password("Нет такого пароля").build());
        response.then().assertThat().statusCode(404);
        assertEquals(ERROR_COURIER_LOGIN_WRONG_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }
}
