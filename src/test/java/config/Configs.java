package config;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

public class Configs {
    public static void configRestAssured() {
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
        RestAssured.filters(new RequestLoggingFilter(LogDetail.METHOD));
        RestAssured.filters(new RequestLoggingFilter(LogDetail.URI));
        RestAssured.filters(new RequestLoggingFilter(LogDetail.BODY));
        RestAssured.filters(new ResponseLoggingFilter(LogDetail.BODY));
    }
}
