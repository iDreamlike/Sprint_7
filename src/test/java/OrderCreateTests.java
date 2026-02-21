import dto.Orders;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.Requests;

import static config.RestAssuredConfig.configRestAssured;
import static constants.Urls.APP_URL;
import static constants.Urls.ORDERS_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderCreateTests {
    private Requests requests;
    private Response response;

    @BeforeEach
    void setUp() {
        requests = new Requests(APP_URL);
        configRestAssured();
    }


    @ParameterizedTest
    @MethodSource("data.OrdersData#ordersTestData")
    @Description("Создание заказа")
    void orderWithColor(Orders ordersJsonBody) {
        response = requests.post(ORDERS_ENDPOINT, ordersJsonBody);
        response.then().assertThat().statusCode(201);
        assertNotNull(response.getBody().jsonPath().getString("track"), "Заказ не создан");
    }
}
