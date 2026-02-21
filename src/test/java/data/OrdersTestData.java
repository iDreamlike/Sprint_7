package data;

import dto.OrderBodyDto;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static constants.Colors.*;

public class OrdersTestData {
    private static final OrderBodyDto orderData = new OrderBodyDto();

    private static Stream<Arguments> ordersTestData() {
        return Stream.of(
                Arguments.of(orderData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ без выбора цвета")
                        .color(null)
                        .build()),
                Arguments.of(orderData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ c выбором цвета BLACK")
                        .color(List.of(BLACK))
                        .build()),
                Arguments.of(orderData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ c выбором цвета GREY")
                        .color(List.of(GREY))
                        .build()),
                Arguments.of(orderData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ c выбором цвета BLACK и GREY")
                        .color(List.of(BLACK, GREY))
                        .build())
        );
    }

}
