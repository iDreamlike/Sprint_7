import dto.OrderBodyDto;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.Requests;

import static config.RestAssuredConfig.configRestAssured;
import static constants.Urls.APP_URL;
import static constants.Urls.ORDERS_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrdersTests {
    private Requests requests;
    private Response response;

    @BeforeEach
    @Step("Создание тестовых данных. BeforeEach")
    void setUp() {
        requests = new Requests(APP_URL);
        configRestAssured();
    }

    @ParameterizedTest
    @MethodSource("data.OrdersTestData#ordersTestData")
    @Feature("Ручка Создания заказа")
    @Story("Позитивные тесты")
    @DisplayName("Создание заказа с разными цветами")
    void orderWithColor(OrderBodyDto orderJsonBody) {
        response = requests.post(ORDERS_ENDPOINT, orderJsonBody);
        response.then().assertThat().statusCode(201);
        assertNotNull(response.getBody().jsonPath().getString("track"), "Заказ не создан");
    }

    @Test
    @Feature("Ручка Получения списка заказов")
    @Story("Позитивные тесты")
    @DisplayName("Получение списка заказов")
    void ordersGetList() {
        response = requests.get(ORDERS_ENDPOINT);
        response.then().assertThat().statusCode(200);
        assertNotNull(response.getBody().jsonPath().getList("orders"), "Список заказов не получен");
    }
}
