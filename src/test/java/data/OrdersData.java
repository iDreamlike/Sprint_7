package data;

import dto.Orders;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static constants.Color.*;

public class OrdersData {
    private static Orders ordersData = new Orders();

    private static Stream<Arguments> ordersTestData() {
        return Stream.of(
                Arguments.of(ordersData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ без выбора цвета")
                        .color(null)
                        .build()),
                Arguments.of(ordersData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ c выбором цвета BLACK")
                        .color(List.of(BLACK))
                        .build()),
                Arguments.of(ordersData.toBuilder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .metroStation("Ладожская")
                        .phone("+71112223344")
                        .rentTime(2)
                        .deliveryDate("12.12.2026")
                        .comment("Заказ c выбором цвета GREY")
                        .color(List.of(GREY))
                        .build()),
                Arguments.of(ordersData.toBuilder()
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
