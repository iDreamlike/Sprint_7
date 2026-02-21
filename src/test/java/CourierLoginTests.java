import dto.CourierBodyDto;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import util.Requests;

import static constants.ErrorMessages.*;
import static config.RestAssuredConfig.configRestAssured;
import static constants.Urls.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CourierLoginTests {
    private Requests requests;
    private Response response;
    private CourierBodyDto courierJsonBody = new CourierBodyDto();

    @BeforeEach
    @Step("Создание тестовых данных. BeforeEach")
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
    @Step("Удаление тестовых данных. AfterEach")
    void tearDown() {
        requests.delete(COURIER_ENDPOINT, courierJsonBody);
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Позитивные тесты")
    @DisplayName("Логин курьера")
    void courierLoginTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody);
        response.then().assertThat().statusCode(200);
        assertNotNull(response.getBody().jsonPath().getString("id"), "Запрос не успешен");
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Негативные тесты")
    @DisplayName("Логин курьера без поля Login")
    void courierLoginWithoutLoginTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().login(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_LOGIN_NULL_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Негативные тесты")
    @Disabled("Ждем фикса бага с пустым паролем")
    @DisplayName("Логин курьера без поля Password")
    void courierLoginWithoutPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().password(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_LOGIN_NULL_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Негативные тесты")
    @Disabled("Ждем фикса бага с пустым паролем")
    @DisplayName("Логин курьера без полей Login и Password")
    void courierLoginWithoutLoginAndPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().login(null).password(null).build());
        response.then().assertThat().statusCode(400);
        assertEquals(ERROR_COURIER_LOGIN_NULL_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Негативные тесты")
    @DisplayName("Логин курьера с несуществующим Login")
    void courierLoginWithNotExistsLoginTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().login("Нет такого логина").build());
        response.then().assertThat().statusCode(404);
        assertEquals(ERROR_COURIER_LOGIN_WRONG_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Негативные тесты")
    @DisplayName("Логин курьера с несуществующим Password")
    void courierLoginWithNotExistsPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder().password("Нет такого пароля").build());
        response.then().assertThat().statusCode(404);
        assertEquals(ERROR_COURIER_LOGIN_WRONG_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }

    @Test
    @Feature("Ручка Логина курьера")
    @Story("Негативные тесты")
    @DisplayName("Логин курьера с несуществующими Login и Password")
    void courierLoginWithNotExistsLoginAndPasswordTest() {
        response = requests.post(LOGIN_ENDPOINT , courierJsonBody.toBuilder()
                .login("Нет такого логина")
                .password("Нет такого пароля").build());
        response.then().assertThat().statusCode(404);
        assertEquals(ERROR_COURIER_LOGIN_WRONG_CREDENTIALS, response.getBody().jsonPath().getString("message"));
    }
}
