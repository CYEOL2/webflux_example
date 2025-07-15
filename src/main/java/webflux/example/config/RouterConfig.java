package webflux.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import webflux.example.handler.ApiHandler;
import webflux.example.handler.SSEHandler;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> apiRouter(ApiHandler apiHandler) {
        return RouterFunctions.route(
                    RequestPredicates.POST("/api/user").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), apiHandler :: createUser
                )
                .andRoute(RequestPredicates.GET("/api/users"), apiHandler :: getAllUsers)
                .andRoute(RequestPredicates.GET("/api/user/{userId}"), apiHandler :: getUser)
                .andRoute(RequestPredicates.PUT("/api/user/{userId}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), apiHandler :: updateUser)
                .andRoute(RequestPredicates.DELETE("/api/user/{userId}"), apiHandler :: deleteUser)
                .andRoute(RequestPredicates.POST("/api/order").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), apiHandler :: createOrder)
                .andRoute(RequestPredicates.GET("/api/order/{orderId}/orderDetails"), apiHandler :: getOrderDetails)
                .andRoute(RequestPredicates.PATCH("/api/order/{orderId}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), apiHandler :: updateOrder)
                ;
    }

    @Bean
    public RouterFunction<ServerResponse> sseRoutes(SSEHandler sseHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/sse/{userId}").and(RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)),sseHandler::stream)
                ;
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public RouterFunction<ServerResponse> fallbackRouter() {
        return RouterFunctions.route(RequestPredicates.all(), request ->
                ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue("잘못된 요청 URL입니다.")
        );
    }

}
